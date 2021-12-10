package com.example.myapplication.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.StudentActivityMainBinding;
import com.example.myapplication.student.model.CurrentStudent;
import com.example.myapplication.student.model.Student;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityStudent extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private StudentActivityMainBinding binding;
    private TextView textStudent;
    private View headerView;
    private Student studentOnline;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = StudentActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.studentAppBarMain.toolbar);

        DrawerLayout drawer = binding.studentDrawerLayout;
        NavigationView navigationView = binding.studentNavView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.student_nav_scanqr,
                R.id.student_nav_latestattendance, R.id.student_nav_history, R.id.student_nav_settings,R.id.student_nav_logout)
                .setOpenableLayout(drawer)
                .build();
        //navigationView.findViewById(R.id.)
        NavController navController = Navigation.findNavController(this, R.id.student_nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        this.headerView = navigationView.getHeaderView(0);
        this.textStudent = headerView.findViewById(R.id.student_viaidView);
        this.databaseReference = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        /*databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    studentOnline = s.getValue(Student.class);
                    if(studentOnline.getStatus().equals("online")){
                        textStudent.setText(studentOnline.getViaID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/
        textStudent.setText(CurrentStudent.getCurrentViaID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.student_nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(MenuItem item) {
        startActivity(new Intent(MainActivityStudent.this, LoginActivity.class));
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