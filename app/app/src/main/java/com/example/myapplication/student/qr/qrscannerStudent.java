package com.example.myapplication.student.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.student.model.CurrentStudent;
import com.example.myapplication.student.ui.scanqr.ScanqrFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrscannerStudent extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private DatabaseReference databaseReference;
    private Map<String, String> map1;
    private Map<String, String> map2;
    private Map<String, String> map3;
    private Map<String, String> map4;
    private Map<String, String> finalmap;
    private ArrayList<String> arrayList;
    private String currentStudent;
    private static final String COURSE = "course";
    private static final String ROOM = "room";
    private static final String DATE = "date";
    private static final String HOUR = "hour";
    public boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentStudent = CurrentStudent.getCurrentViaID();
        this.map1 = new HashMap<String, String>();
        this.map2 = new HashMap<String,String>();
        this.map3 = new HashMap<String,String>();
        this.map4 = new HashMap<String,String>();
        this.finalmap = new HashMap<String,String>();
        this.arrayList = new ArrayList<String>();
        this.scannerView = new ZXingScannerView(this);
        this.databaseReference = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent(qrscannerStudent.this, LoginActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(qrscannerStudent.this, "Please grant permission of your camera to use the app", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    public void updateCounterClassroom(String nameRoom){
        databaseReference.child("classroom").child(nameRoom).child("activeUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String updateCounter = snapshot.getValue(String.class);
                int x = Integer.parseInt(updateCounter);
                x += 1;
                String counter = String.valueOf(x);
                databaseReference.child("classroom").child(nameRoom).child("activeUsers").setValue(counter);
            }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    public void updateCounterCourse(String randomCourse){
        databaseReference.child("courses").child(randomCourse).child("activeUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String updateCounter = snapshot.getValue(String.class);
                int x = Integer.parseInt(updateCounter);
                x += 1;
                String counter = String.valueOf(x);
                databaseReference.child("courses").child(randomCourse).child("activeUsers").setValue(counter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void handleResult(Result rawResult) {
        String nameRoom = rawResult.getText();
        databaseReference.child("classroom").child(nameRoom).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for(DataSnapshot item: snapshot.getChildren()){
                //String classroomID = item.child("classroomID").getValue().toString();
                if (snapshot.exists()) {
                    flag = true;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    String finalDate = dateFormat.format(date);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    Date time = new Date();
                    String finalHour = timeFormat.format(time);
                    map1.put(ROOM, nameRoom);
                    map2.put(DATE, finalDate);
                    map3.put(HOUR, finalHour);
                    databaseReference.child("students").child(currentStudent).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                arrayList.add(item.getValue().toString());
                            }
                            int index = (int) (Math.random() * arrayList.size());
                            String randomcourse = arrayList.get(index);
                            map4.put(COURSE, randomcourse);
                            finalmap.putAll(map1);
                            finalmap.putAll(map2);
                            finalmap.putAll(map3);
                            finalmap.putAll(map4);
                            databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child("students").child(currentStudent).child("attendance").push().setValue(finalmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ScanqrFragment.textView.setText("Registry successful.\n You are now successfully logged in for the lecture:\n"+ randomcourse);
                                            updateCounterClassroom(nameRoom);
                                            updateCounterCourse(randomcourse);
                                            onBackPressed();
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getApplication(), "Registry failed.\nUnknown classroom", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    onBackPressed();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}