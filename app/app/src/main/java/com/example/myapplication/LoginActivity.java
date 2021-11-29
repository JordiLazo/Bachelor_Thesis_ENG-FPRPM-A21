package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogIn;
    private EditText viaID, password;
    private DatabaseReference dataBase;

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
                String user_viaid = viaID.getText().toString();
                String user_password = password.getText().toString();
                if(user_password.isEmpty() || user_viaid.isEmpty()){
                    Toast.makeText(getApplication(), "Introduce names", Toast.LENGTH_SHORT).show();
                }else{
                    dataBase.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(user_viaid)) {
                                String getpassword = snapshot.child(user_viaid).child("password").getValue(String.class);
                                if (getpassword.equals(user_password)) {
                                    Toast.makeText(getApplication(), "Login successfully\n", Toast.LENGTH_SHORT).show();
                                    //navController.navigate(R.id.action_logIn_to_logOut);
                                    startActivity(new Intent(getApplication(), MainActivity.class));
                                }
                                else {
                                    Toast.makeText(getApplication(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplication(), "Incorrect VIA ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}