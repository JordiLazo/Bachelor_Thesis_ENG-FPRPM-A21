package com.example.myapplication.student.ui.logout;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.student.model.CurrentStudent;
import com.example.myapplication.student.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Logout extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Student studentOnline;

    public void logout(MenuItem item) {
        this.databaseReference = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        startActivity(new Intent(Logout.this, LoginActivity.class));
        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    String currentUser = CurrentStudent.getCurrentViaID();
                    studentOnline = s.getValue(Student.class);
                    if(currentUser.equals(studentOnline.getViaID())){
                        databaseReference.child("students").child(currentUser).child("status").setValue("offline");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        finish();
    }
}
