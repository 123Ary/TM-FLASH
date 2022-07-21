package com.dts;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity2 extends AppCompatActivity
{
    int flag=5;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static  int PICK_FILE_REQUEST ;
    Button browse,download,reflash;
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
    TextView t;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.image, null);

        actionBar.setCustomView(v);*/
        setContentView(R.layout.activity_main2);

        browse = (Button) findViewById(R.id.button1);
        reflash = (Button) findViewById(R.id.button3);

        t = (TextView) findViewById(R.id.textview);
        t.setText("PROGRESS   :0");
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        t.setVisibility(View.INVISIBLE);
        download = (Button) findViewById(R.id.button2);

   /*     browse.setOnClickListener(this);
        download.setOnClickListener(this);
        reflash.setOnClickListener(this);
*/

        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                flag = 0;
                Intent intent = new Intent();
                //sets the select file to all types of files
                intent.setType("*/*");
                //allows to select data and return it
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

            }
        });
        browse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                flag = 1;
                System.out.println("flagggg :" + flag);
                Intent intent = new Intent();
                //sets the select file to all types of files
                intent.setType("*/*");
                //allows to select data and return it
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

            }
        });
        reflash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                flag = 3;


                //  t.setText("Download Completed");
                t.setVisibility(view.VISIBLE);
                t.setText("Reflash Started");
                System.out.println("reflash started");


                upload u = new upload();
                u.reflash();
                try {
                    while (true) {
                        String status = u.getRTS();
                        if ((!status.contentEquals("NO RESPONSE")) && (!status.contentEquals("null")) && (!status.contentEquals(""))) {
                            if ((status != null) | (status != "") | (status.contentEquals("")) | (status.isEmpty())) {

                                String[] split = status.split(" ");
                                String progressValue = split[0];
                                int progressint = Integer.parseInt(String.valueOf(progressValue));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        spinner.setVisibility(View.VISIBLE);
                                        t.setVisibility(View.VISIBLE);
                                        t.setText(progressValue + "%");

                                    }
                                });
                                if (progressint >= 100) {
                                    break;
                                }
                                String rtstemp = split[1];
                                // RTS = Integer.parseInt(String.valueOf(rtstemp));
                                Thread.sleep(30000);
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


    }




    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        int writePerm =   ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPerm = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);

        Uri uri =data.getData();
        String path= uri.getPath();
        System.out.println("path is :"+path);

        String[] filenametemp=path.split(File.separator);
        for(int j=0;j<filenametemp.length;j++)
        {
            actualfilename=filenametemp[j];
        }
        System.out.println("file name from folder :"+actualfilename);
        if(resultCode == Activity.RESULT_OK){
            System.out.println("requestCode ok :"+requestCode);
            PICK_FILE_REQUEST=requestCode;
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                System.out.println("flaggggggggg :"+flag);
                String pathh = folder.getPath();
                upload u = new upload();
                spinner.setVisibility(View.VISIBLE);
                if(flag==0)
                {
                    t.setVisibility(View.VISIBLE);
                    t.setText("Please Wait...Downloading");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {


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
                                        AlertDialog dialog = new AlertDialog.Builder(MainActivity2.this).setTitle("Download Completed" )
                                                .setNeutralButton("ok", null).show();
                                    }

                                });


                            }
                        }
                    } ).start();

                    spinner.setVisibility(View.INVISIBLE);



                   flag=1;
                }
               if(flag==1) {





                    GenerateFiles g = new GenerateFiles();
                    File f = new File(pathh + "/" + actualfilename);
                    int file_size = Integer.parseInt(String.valueOf(f.length()));
                    try {
                        g.splitFile(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                                        if(RTSstr.contentEquals("NO RESPONSE"))
                                        {
                                            RTSstr = u.getRTS();
                                        }
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
                                                System.out.println("rtssssssssssssssssssssssssssssssssssssss :" + RTS);
                                                progress = Integer.parseInt(String.valueOf(progressTemp));
                                                if (RTS == 1) {
                                                    if (i <= 6) {
                                                        String filename = "/File" + i + ".bin";
                                                      int code=  u.uploadFile(pathh + filename);
                                                      if(code!=200)
                                                      {
                                                          try {
                                                              Thread.sleep(10000);
                                                          } catch (InterruptedException e) {
                                                              e.printStackTrace();
                                                          }
                                                      }
                                                        i = i + 1;
                                                    }


                                                }
                                                if (progress >= 100) {
                                                    break;
                                                }

                                            }
                                        }
                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }

                            }
                        }
                    }).start();
                    //    u.uploadFile(pathh+"/File1.bin");

                    flag=0;
                }

            }
        }
    }

    private void setProgress(String progressTemp) {
        t.setText("PROGRESS   :"+progressTemp);
    }


/*
    @Override
    public void onClick(View v) {
      //  Intent intent = new Intent();



      //  intent.setAction(Intent.ACTION_GET_CONTENT);

   //     startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);


    }*/
    public void addOperation(View view) {
        startActivity(new Intent(MainActivity2.this, Login.class));
    }




}