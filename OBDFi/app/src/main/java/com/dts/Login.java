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

public class Login extends AppCompatActivity {
Button login;
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static  int PICK_FILE_REQUEST ;
EditText email,password;
    //@RequiresApi(api = Build.VERSION_CODES.S)
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // ActionBar actionBar = getSupportActionBar();
      //  actionBar.setDisplayShowCustomEnabled(true);

     //   LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   View v = inflator.inflate(R.layout.activity_custom_imageview, null);

     //   actionBar.setCustomView(v);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this::onClick);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setSingleLine(true);
        password.setMaxLines(1);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Intent intent = new Intent();

        int version = Build.VERSION.SDK_INT;
     




        }





    private void onClick(View view) {
        upload u=new upload();
        String emailValue=email.getText().toString();

        String passwordValue=password.getText().toString();
   
      if((emailValue.contentEquals("demo@thingsalive.com"))&&(passwordValue.contentEquals("password")))
        {

            startActivity(new Intent(Login.this, AdminHome.class));
        }
        else
        {
            AlertDialog dialog = new AlertDialog.Builder(Login.this).setTitle("Invalid Email Id or Password" )
                    .setNeutralButton("ok", null).show();
        }
    }


 //   @RequiresApi(api = Build.VERSION_CODES.S)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
    }
}