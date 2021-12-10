package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.professor.MainActivityProfessor;
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

        this.viaID = findViewById(R.id.viaid_text);
        this.password = findViewById(R.id.password_text);
        this.buttonLogIn = findViewById(R.id.buttonLogIn);
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String user_viaid = viaID.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                if(user_password.isEmpty() || user_viaid.isEmpty()){
                    Toast.makeText(getApplication(), "Introduce credentials", Toast.LENGTH_SHORT).show();
                }else{
                    dataBase.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(user_viaid)) {
                                String getpassword = snapshot.child(user_viaid).child("password").getValue(String.class);
                                if (getpassword.equals(user_password)) {
                                    Toast.makeText(getApplication(), "Login successfully\n", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplication(), "Login successfully\n", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplication(), MainActivityProfessor.class));
                                    CurrentStudent.setCurrentViaID(user_viaid);
                                    dataBase.child("professors").child(user_viaid).child("status").setValue("online");
                                    flag =true;
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
                                    Toast.makeText(getApplication(), "Login successfully\n", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplication(), MainActivityStudent.class));
                                    CurrentStudent.setCurrentViaID(user_viaid);
                                    dataBase.child("scheduleManager").child(user_viaid).child("status").setValue("online");
                                    flag = true;
                                }
                            }if(!flag) {
                                Toast.makeText(getApplication(), "Log in failed by NETSCALER AAA", Toast.LENGTH_SHORT).show();
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
}