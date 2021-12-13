package com.example.myapplication.schedulemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.SmActivityMainBinding;
import com.example.myapplication.schedulemanager.model.CurrentScheduleManager;
import com.example.myapplication.schedulemanager.model.ScheduleManager;
import com.example.myapplication.student.model.CurrentStudent;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityScheduleManager extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SmActivityMainBinding binding;
    private TextView textStudent;
    private View headerView;
    private ScheduleManager scheduleManagerOnline;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = SmActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.smAppBarMain.smToolbar);

        DrawerLayout drawer = binding.smDrawerLayout;
        NavigationView navigationView = binding.smNavView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.sm_nav_scanqr,R.id.sm_nav_history, R.id.sm_nav_settings,R.id.sm_nav_logout)
                .setOpenableLayout(drawer)
                .build();
        //navigationView.findViewById(R.id.)
        NavController navController = Navigation.findNavController(this, R.id.sm_nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        this.headerView = navigationView.getHeaderView(0);
        this.textStudent = headerView.findViewById(R.id.sm_viaidView);
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
        NavController navController = Navigation.findNavController(this, R.id.sm_nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void smlogout(MenuItem item) {
        startActivity(new Intent(MainActivityScheduleManager.this, LoginActivity.class));
        databaseReference.child("scheduleManager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    String currentUser = CurrentScheduleManager.getCurrentViaID();
                    scheduleManagerOnline = s.getValue(ScheduleManager.class);
                    if(currentUser.equals(scheduleManagerOnline.getViaID())){
                        databaseReference.child("scheduleManager").child(currentUser).child("status").setValue("offline");
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