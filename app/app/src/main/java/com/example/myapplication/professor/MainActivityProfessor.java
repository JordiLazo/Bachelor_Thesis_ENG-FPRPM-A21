package com.example.myapplication.professor;

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
import com.example.myapplication.databinding.ProfessorActivityMainBinding;
import com.example.myapplication.professor.model.CurrentProfessor;
import com.example.myapplication.professor.model.Professor;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityProfessor extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ProfessorActivityMainBinding binding;
    private TextView textProfessor;
    private View headerView;
    private Professor professorOnline;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ProfessorActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.professorAppBarMain.professorToolbar);
        DrawerLayout drawer = binding.professorDrawerLayout;
        NavigationView navigationView = binding.professorNavView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.professor_nav_check_current_attendance, R.id.professor_nav_history, R.id.professor_nav_settings,R.id.professor_nav_logout)
                .setOpenableLayout(drawer)
                .build();
        //navigationView.findViewById(R.id.)
        NavController navController = Navigation.findNavController(this, R.id.professor_nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        this.headerView = navigationView.getHeaderView(0);
        this.textProfessor = headerView.findViewById(R.id.professor_viaidView);
        this.databaseReference = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        this.textProfessor.setText(CurrentProfessor.getCurrentViaID());

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.professor_nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void professorlogout(MenuItem item) {
        startActivity(new Intent(MainActivityProfessor.this, LoginActivity.class));
        databaseReference.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    String currentUser = CurrentProfessor.getCurrentViaID();
                    professorOnline = s.getValue(Professor.class);
                    if(currentUser.equals(professorOnline.getViaID())){
                        databaseReference.child("professors").child(currentUser).child("status").setValue("offline");
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