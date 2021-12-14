package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.professor.MainActivityProfessor;
import com.example.myapplication.professor.model.CurrentProfessor;
import com.example.myapplication.schedulemanager.MainActivityScheduleManager;
import com.example.myapplication.schedulemanager.model.CurrentScheduleManager;
import com.example.myapplication.student.MainActivityStudent;
import com.example.myapplication.student.model.CurrentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogIn;
    private EditText viaID, password;
    private DatabaseReference dataBase;
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }else{
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

            this.viaID = findViewById(R.id.viaid_text);
            this.password = findViewById(R.id.password_text);
            this.buttonLogIn = findViewById(R.id.buttonLogIn);
            this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

            buttonLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user_viaid = viaID.getText().toString().trim();
                    String user_password = password.getText().toString().trim();
                    if (user_password.isEmpty() || user_viaid.isEmpty()) {
                        Toast toast = Toast.makeText(getApplication(), "Please introduce credentials", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else {
                        dataBase.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(user_viaid)) {
                                    String getpassword = snapshot.child(user_viaid).child("password").getValue(String.class);
                                    if (getpassword.equals(user_password)) {
                                        Toast toast = Toast.makeText(getApplication(), "Login successfully", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        startActivity(new Intent(getApplication(), MainActivityStudent.class));
                                        CurrentStudent.setCurrentViaID(user_viaid);
                                        dataBase.child("students").child(user_viaid).child("status").setValue("online");
                                        flag = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        dataBase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(user_viaid)) {
                                    String getpassword = snapshot.child(user_viaid).child("password").getValue(String.class);
                                    if (getpassword.equals(user_password)) {
                                        Toast toast = Toast.makeText(getApplication(), "Login successfully", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        startActivity(new Intent(getApplication(), MainActivityProfessor.class));
                                        CurrentProfessor.setCurrentViaID(user_viaid);
                                        dataBase.child("professors").child(user_viaid).child("status").setValue("online");
                                        flag = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        dataBase.child("scheduleManager").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(user_viaid)) {
                                    String getpassword = snapshot.child(user_viaid).child("password").getValue(String.class);
                                    if (getpassword.equals(user_password)) {
                                        Toast toast = Toast.makeText(getApplication(), "Login successfully", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        startActivity(new Intent(getApplication(), MainActivityScheduleManager.class));
                                        CurrentScheduleManager.setCurrentViaID(user_viaid);
                                        dataBase.child("scheduleManager").child(user_viaid).child("status").setValue("online");
                                        flag = true;
                                    }
                                }
                                if (!flag) {
                                    Toast toast = Toast.makeText(getApplication(), "Login failed by NETSCALER AAA", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    //viaID.setText("");
                    //password.setText("");
                }
            });
        }
    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Toast toast = Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                return;
            }
        }
    }
}