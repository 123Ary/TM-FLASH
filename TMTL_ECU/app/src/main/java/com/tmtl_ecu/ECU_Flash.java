package com.tmtl_ecu;


import android.Manifest;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.OpenableColumns;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.SearchView;
        import android.widget.TextView;

        import com.chaquo.python.PyObject;
        import com.chaquo.python.Python;
        import com.chaquo.python.android.AndroidPlatform;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.StringTokenizer;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;


public class ECU_Flash extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    int flag=0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static  int PICK_FILE_REQUEST ;
    Button browse,download,reflash,logout,hextobin;
    String RTSstr="";
    int RTS=0;
    String dowloadresponse=null;
    String actualfilename="";
    private Uri FilePath;
    FileOutputStream fos;
    private ProgressBar spinner;
    int progress=0;
    int i=2;
    int uploadStatus=0;
    TextView t,textviewDownload,textviewhexbin;
    String pathh="";
    upload u = new upload();
    File f = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int DEFAULT_SCOPED_STORAGE=1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.image, null);

        actionBar.setCustomView(v);*/
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        verifyStoragePermissions(this);
        browse = (Button)findViewById(R.id.button1);
        browse.setOnClickListener(this::onClick);

        t=(TextView)findViewById(R.id.textview);

        t.setText("PROGRESS   :0");
        textviewDownload=(TextView)findViewById(R.id.textviewDownload);
        textviewhexbin=(TextView)findViewById(R.id.textviewhexbin);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        t.setVisibility(View.INVISIBLE);
        textviewhexbin.setVisibility(View.INVISIBLE);
        download=(Button)findViewById(R.id.button2);

        download.setOnClickListener(this::onClick);
        hextobin=(Button)findViewById(R.id.button5);
        hextobin.setVisibility(View.INVISIBLE);
        //  hextobin.setOnClickListener(this::onClick);
        logout=(Button)findViewById(R.id.logout);

        logout.setOnClickListener(this::onClick);
    }

    //  @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        System.out.println("external storage :"+Environment.getExternalStorageDirectory());
        int writePerm =   ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPerm = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);

        Uri uri =data.getData();


        String path= uri.getPath();

        Cursor returnCursor =
                getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

        returnCursor.moveToFirst();



        System.out.println("path is :"+returnCursor.getString(nameIndex));
        System.out.println("path is :"+uri);
       /* String[] filenametemp=path.split(File.separator);
        for(int j=0;j<filenametemp.length;j++)
        {
            actualfilename=filenametemp[j];
        }*/

        actualfilename=returnCursor.getString(nameIndex);
        System.out.println("file name from folder :"+actualfilename);
        if(resultCode == Activity.RESULT_OK){
            System.out.println("requestCode ok :"+requestCode);
            PICK_FILE_REQUEST=requestCode;
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                pathh = folder.getPath();


                if(flag==0)
                {
                    t.setVisibility(View.INVISIBLE);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //  AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("Download Started" )
                                    //   .setNeutralButton("ok", null).show();
                                    textviewDownload.setVisibility(View.VISIBLE);
                                    textviewDownload.setText("Please Wait..");
                                    hextobin.setVisibility(View.VISIBLE);
                                    textviewhexbin.setVisibility(View.VISIBLE);
                                }

                            });

                            String filename=  u.hextobin(pathh + "/" + actualfilename);
                            dowloadresponse= u.DownloadFiles(filename);
                            System.out.println("Download statusssssssssssssssssss:"+dowloadresponse);
                            if(dowloadresponse.contentEquals("Download Complete"))
                            {
                                System.out.println("Download statusssssssssssssssssss:"+dowloadresponse);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        //  t.setText("Download Completed");
                                        t.setVisibility(View.INVISIBLE);
                                        actualfilename=filename;
                                        AlertDialog dialog = new AlertDialog.Builder(ECU_Flash.this).setTitle("Download Completed" )
                                                .setNeutralButton("ok", null).show();
                                        textviewDownload.setVisibility(View.INVISIBLE);
                                        hextobin.setVisibility(View.INVISIBLE);
                                        textviewhexbin.setVisibility(View.INVISIBLE);
                                    }

                                });


                            }
                        }
                    } ).start();

                    spinner.setVisibility(View.INVISIBLE);

                }
                if(flag==8)
                {

                    t.setVisibility(View.INVISIBLE);

                    if(!Python.isStarted())
                    {
                        Python.start(new AndroidPlatform(this));
                    }

                    String hexname=  pathh + "/" + actualfilename;
                    String binFile=actualfilename.replace(".hex",".bin");
                    System.out.println("biiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii :"+binFile);
                    String binname=pathh +"/"+ binFile;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    String result="";
                                    textviewDownload.setVisibility(View.INVISIBLE);
                                    //    textviewDownload.setText("Please Wait..");
                                    hextobin.setVisibility(View.INVISIBLE);
                                    textviewhexbin.setVisibility(View.INVISIBLE);
                                    StringTokenizer tokens = new StringTokenizer(actualfilename, ".");
                                    String first = tokens.nextToken();
                                    //  String second = tokens.nextToken();
                                    String binFileName = first + ".bin";

                                    String hexFile=pathh + "/" + actualfilename;
                                    String bin=pathh + "/" + binFileName;

                                    Python py=Python.getInstance();
                                    PyObject pyObj=py.getModule("conversion");
                                    PyObject obj=    pyObj.callAttr("main",hexname,binname);
                                    result=obj.toString();
                                    if(result.contentEquals("success")) {
                                        AlertDialog dialog = new AlertDialog.Builder(ECU_Flash.this).setTitle("Successfully Converted HEX to BIN")
                                                .setNeutralButton("ok", null).show();
                                    }
                                    //   GenerateFiles gf=new  GenerateFiles();
                                    //  File ff=new File(hexFile);
                                    /*Python py=Python.getInstance();
                                    PyObject pyObj=py.getModule("conversion");
                                    PyObject obj=    pyObj.callAttr("main",hexname,binname);
                                     result=obj.toString();
                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("Successfully Converted HEX to BIN" )
                                            .setNeutralButton("ok", null).show();
                                    //   }
                                  /*  else {
                                       // Python py = Python.getInstance();
                                      //  PyObject pyObj = py.getModule("conversion");

                                     //   PyObject obj = pyObj.callAttr("main", hexname, binname);
                                        result="success";
                                        if(result.contentEquals("success"))
                                        {
                                            System.out.println("Download statusssssssssssssssssss:"+dowloadresponse);
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    //  t.setText("Download Completed");
                                                    t.setVisibility(View.INVISIBLE);

                                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("Successfully Converted HEX to BIN" )
                                                            .setNeutralButton("ok", null).show();
                                                    textviewDownload.setVisibility(View.INVISIBLE);
                                                    hextobin.setVisibility(View.INVISIBLE);
                                                    textviewhexbin.setVisibility(View.INVISIBLE);
                                                }

                                            });


                                        }
                                    }*/

                                }

                            });




                        }
                    } ).start();

                    spinner.setVisibility(View.INVISIBLE);

                }
/*
                    GenerateFiles g = new GenerateFiles();
                     f = new File(pathh + "/" + actualfilename);
                    int file_size = Integer.parseInt(String.valueOf(f.length()));
                    try {
                        String status=g.splitFile(f);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                if(flag==1) {





                    GenerateFiles g = new GenerateFiles();
                    File f = new File(pathh + "/" + actualfilename);
                    int file_size = Integer.parseInt(String.valueOf(f.length()));
                    try {
                        g.splitFile(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // int  file_size = Integer.parseInt(String.valueOf(f.length()));
                    int code = u.startflash(file_size);
                    spinner.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (code == 200) {
                                t.setVisibility(View.INVISIBLE);

                                uploadStatus = u.uploadFile(pathh + "/File1.bin");

                                if (uploadStatus == 200) {


                                    while (true) {

                                        RTSstr = u.getRTS();


                                        System.out.println("valueeeeeeeeeeeeeeeeeeeeee:" + RTSstr);
                                        if ((!RTSstr.contentEquals("NO RESPONSE")) && (!RTSstr.contentEquals("null")) && (!RTSstr.contentEquals(""))) {
                                            if ((RTSstr != null) | (RTSstr != "") | (RTSstr.contentEquals("")) | (RTSstr.isEmpty())) {

                                                String[] split = RTSstr.split(" ");
                                                String progressTemp = split[0];

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        t.setVisibility(View.VISIBLE);
                                                        t.setText(progressTemp + "%");

                                                    }
                                                });
                                                String rtstemp = split[1];
                                                RTS = Integer.parseInt(String.valueOf(rtstemp));

                                                progress = Integer.parseInt(String.valueOf(progressTemp));
                                                if (RTS == 1) {
                                                    if (i <= 12) {

                                                        String filename = "/File" + i + ".bin";
                                                        u.uploadFile(pathh + filename);
                                                        i = i + 1;
                                                    }


                                                }
                                                if (progress >= 100) {

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            t.setVisibility(View.VISIBLE);
                                                            t.setText("Finished");
                                                            spinner.setVisibility(View.INVISIBLE);

                                                        }
                                                    });

                                                   /* for(int i=1;i<=12;i++)
                                                    {
                                                        try {


                                                        String filename = "/File" + i + ".bin";
                                                        File f=new File(pathh + filename);
                                                        if(f.exists()){
                                                           f.delete();
                                                        }
                                                            }
                                                            catch(Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                    }*/
                                                    break;
                                                }

                                            }
                                        }
                                      /*  try {
                                           Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }*/

                                    }


                                }

                            }
                        }
                    }).start();
                    //    u.uploadFile(pathh+"/File1.bin");


                }


            }
        }
    }

    private void setProgress(String progressTemp) {
        t.setText("PROGRESS   :"+progressTemp);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;

        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button1)
        {

            flag=1;
            Intent intent = new Intent();
            //sets the select file to all types of files
            intent.setType("*/*");
            //allows to select data and return it
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
            //   startFlash();
        }

        if(v.getId()==R.id.button2)
        {
            // flag=0;
            flag=8;

            Intent intent = new Intent();
            //sets the select file to all types of files
            intent.setType("*/*");
            //allows to select data and return it
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
        }


        if(v.getId()==R.id.logout)
        {
            startActivity(new Intent(ECU_Flash.this, MainActivity.class));
        }

      /*  if(v.getId()==R.id.button5)
        {
          flag=3;
            Intent intent = new Intent();



            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);


        }*/


    }

    private void convertFiles(String s) {
        actualfilename=s+".bin";


        GenerateFiles g = new GenerateFiles();
        f = new File(pathh + "/" + actualfilename);
        int file_size = Integer.parseInt(String.valueOf(f.length()));
        try {
            String status=g.splitFile(f);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startFlash() {
        if(flag==1) {





               /*     GenerateFiles g = new GenerateFiles();
                    File f = new File(pathh + "/" + actualfilename);
                    int file_size = Integer.parseInt(String.valueOf(f.length()));
                    try {
                        g.splitFile(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
            int file_size = Integer.parseInt(String.valueOf(f.length()));
            int code = u.startflash(file_size);
            spinner.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (code == 200) {
                        t.setVisibility(View.INVISIBLE);

                        uploadStatus = u.uploadFile(pathh + "/File1.bin");

                        if (uploadStatus == 200) {


                            while (true) {

                                RTSstr = u.getRTS();


                                System.out.println("valueeeeeeeeeeeeeeeeeeeeee:" + RTSstr);
                                if ((!RTSstr.contentEquals("NO RESPONSE")) && (!RTSstr.contentEquals("null")) && (!RTSstr.contentEquals(""))) {
                                    if ((RTSstr != null) | (RTSstr != "") | (RTSstr.contentEquals("")) | (RTSstr.isEmpty())) {

                                        String[] split = RTSstr.split(" ");
                                        String progressTemp = split[0];

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                t.setVisibility(View.VISIBLE);
                                                t.setText(progressTemp + "%");

                                            }
                                        });
                                        String rtstemp = split[1];
                                        RTS = Integer.parseInt(String.valueOf(rtstemp));

                                        progress = Integer.parseInt(String.valueOf(progressTemp));
                                        if (RTS == 1) {
                                            if (i <= 12) {

                                                String filename = "/File" + i + ".bin";
                                                u.uploadFile(pathh + filename);
                                                i = i + 1;
                                            }


                                        }
                                        if (progress >= 100) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    t.setVisibility(View.VISIBLE);
                                                    t.setText("Finished");
                                                    spinner.setVisibility(View.INVISIBLE);

                                                }
                                            });

                                                   /* for(int i=1;i<=12;i++)
                                                    {
                                                        try {


                                                        String filename = "/File" + i + ".bin";
                                                        File f=new File(pathh + filename);
                                                        if(f.exists()){
                                                           f.delete();
                                                        }
                                                            }
                                                            catch(Exception e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                    }*/
                                            break;
                                        }

                                    }
                                }
                                      /*  try {
                                           Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }*/

                            }


                        }

                    }
                }
            }).start();
            //    u.uploadFile(pathh+"/File1.bin");


        }
    }

    public void addOperation(View view) {
        startActivity(new Intent(ECU_Flash.this, MainActivity.class));
    }

   /* public void reflash() {

        new Thread(new Runnable() {
      //      @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void run() {

            int rtsreflash;


     upload u=new upload();
                if (Build.VERSION.SDK_INT >= 31) {
                    u.reflash();
                }
                try {
            while(true)
            {
                String status = null;
                if (Build.VERSION.SDK_INT >= 31) {
                    status = u.getRTS();
                }
                if ((!status.contentEquals("NO RESPONSE")) && (!status.contentEquals("null")) && (!status.contentEquals(""))) {
                    if ((status != null) | (status != "") | (status.contentEquals("")) | (status.isEmpty())) {

                        String[] split = status.split(" ");
                        String reflashProgressTemp= split[0];

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t.setVisibility(View.VISIBLE);
                                t.setText(reflashProgressTemp + "%");

                            }
                        });
                        String rtstemp = split[1];
                        rtsreflash = Integer.parseInt(String.valueOf(rtstemp));
                        if(rtsreflash>=100)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    t.setVisibility(View.VISIBLE);
                                    t.setText("Finished");
                                    spinner.setVisibility(View.INVISIBLE);

                                }
                            });
                            break;
                        }
                     //   Thread.sleep(500);
                    }
                }
            }
//InterruptedException
        } catch ( Exception e) {
            e.printStackTrace();
        }
            }
        }).start();

    }*/


}