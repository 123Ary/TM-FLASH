package com.dts;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
public class LandingPage extends AppCompatActivity {
    Button login,userlogin;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static  int PICK_FILE_REQUEST ;
    EditText email,password;
    //@RequiresApi(api = Build.VERSION_CODES.S)
  //  @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this::onClick);
        userlogin = (Button)findViewById(R.id.loginuser);
        userlogin.setOnClickListener(this::onClick);
        Intent intent = new Intent();

        int version = Build.VERSION.SDK_INT;

        if(version>=30) {

            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_CODE);
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);


            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_CODE);
        }
    }
    private void onClick(View v) {
        if(v.getId()==R.id.login)
        {
            System.out.println("navigate to admin page");
            startActivity(new Intent(LandingPage.this, Login.class));
        }
        if(v.getId()==R.id.loginuser)
        {

            startActivity(new Intent(LandingPage.this, LoginUser.class));
        }

    }
}