package com.ptithcm.qlthuoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptithcm.qlthuoc.Entity.AppUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Registration extends AppCompatActivity {
    EditText txtUsername,txtPassword, txtHoTen;
    TextView txtLogin;
    Button btnRegistration, btnSelectPhoto;
    ImageView avatar;
    byte[] file;
    DbContext dbContext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final int REQUEST_UPLOAD_FILE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
        setConfig();
        setControl();
        setEvent();
    }

    private void setConfig() {
        sharedPreferences = getSharedPreferences("my_data.xml", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbContext = new DbContext(this);
    }

    private void setEvent() {
        btnRegistration.setOnClickListener(view -> {
            String username = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String hoten = txtHoTen.getText().toString().trim();
            byte[] avt = file;
            String role = "USER";
            try (SQLiteDatabase db = dbContext.getWritableDatabase())
            {
                ContentValues ct = new ContentValues();
                ct.put("username", username);
                ct.put("password", password);
                ct.put("hoten",hoten);
                ct.put("avatar", avt);
                ct.put("role", role);
                db.insert("AppUser",null, ct);
                Toast.makeText(this, "Dang ky thanh cong", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            }catch (Exception e){
                Toast.makeText(this, "Dang ky that bai "+e.getMessage()+"", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        txtLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        btnSelectPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(this, UploadFile.class);
            startActivityForResult(intent, REQUEST_UPLOAD_FILE);
        });
    }


    private void setControl() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtHoTen = findViewById(R.id.txtHoTen);
        btnRegistration = findViewById(R.id.btnRegistration);
        txtLogin = findViewById(R.id.txtLogin);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        avatar = findViewById(R.id.avatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_UPLOAD_FILE)
            {
                file = data.getByteArrayExtra("byte");
                Uri uri = data.getData();
                avatar.setImageURI(uri);
            }
        }
    }
}