package com.dts;


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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class UserHome extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    int flag=0;
    int successStm=0,successEsp=0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static  int PICK_FILE_REQUEST ;
    Button button1,button2,logout;
    int responseCodeStm=0,responseCodeEsp=0;
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
        setContentView(R.layout.activity_user_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        verifyStoragePermissions(this);
        button1 = (Button)findViewById(R.id.buttonstm);
        button1.setOnClickListener(this::onClick);


        button2=(Button)findViewById(R.id.buttonesp);

        button2.setOnClickListener(this::onClick);

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

                if(flag==1)
                {

                    System.out.println("file name from folderrrrrrrrrrrrrr :"+actualfilename);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {


                                            successStm=  u.stmflashing();



                                            int  uploadStatus = u.uploadFilestm(pathh + "/"+actualfilename);
                                            if(uploadStatus==200)
                                            {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        AlertDialog dialog = new AlertDialog.Builder(UserHome.this).setTitle("Finished" )
                                                                .setNeutralButton("ok", null).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).start();


                                }

                            });


                        }
                    } ).start();


                }
                if(flag==2)
                {

                    System.out.println("file name from folderrrrrrrrrrrrrr :"+actualfilename);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            successEsp=  u.espflashing();



                                            int  uploadStatus = u.updateFileota(pathh + "/"+actualfilename);
                                            if(uploadStatus==200)
                                            {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        AlertDialog dialog = new AlertDialog.Builder(UserHome.this).setTitle("Finished" )
                                                                .setNeutralButton("ok", null).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).start();


                                }

                            });


                        }
                    } ).start();


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
        if(v.getId()==R.id.buttonstm)
        {
            flag=1;
            Intent intent = new Intent();
            //sets the select file to all types of files
            intent.setType("*/*");
            //allows to select data and return it
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
        }

        if(v.getId()==R.id.buttonesp)
        {
            flag=2;
            Intent intent = new Intent();
            //sets the select file to all types of files
            intent.setType("*/*");
            //allows to select data and return it
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
        }


        if(v.getId()==R.id.logout)
        {
            startActivity(new Intent(UserHome.this, LandingPage.class));
        }

      /*  if(v.getId()==R.id.button5)
        {
          flag=3;
            Intent intent = new Intent();



            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);


        }*/


    }

    private int esp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        responseCodeStm=  u.espflashing();
                    }

                });


            }
        } ).start();

        return responseCodeEsp;
    }

    private int stm() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        responseCodeStm=  u.stmflashing();
                    }

                });


            }
        } ).start();
        return responseCodeStm;
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
        startActivity(new Intent(UserHome.this, Login.class));
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