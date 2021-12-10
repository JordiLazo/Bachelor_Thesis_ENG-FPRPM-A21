package com.example.myapplication.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.example.myapplication.model.CurrentStudent;
import com.example.myapplication.ui.scanqr.ScanqrFragment;
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

public class qrscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

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

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    @Override
    public void handleResult(Result rawResult) {
        String nameRoom = rawResult.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String finalDate = dateFormat.format(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        String finalHour = timeFormat.format(time);
        map1.put(ROOM,nameRoom);
        map2.put(DATE,finalDate);
        map3.put(HOUR,finalHour);
        databaseReference.child("students").child(currentStudent).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    arrayList.add(item.getValue().toString());
                }
                int index = (int) (Math.random() * arrayList.size());
                String randomcourse = arrayList.get(index);
                map4.put(COURSE,randomcourse);
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
                                ScanqrFragment.textView.setText("Registered successfully in the classroom");
                                onBackPressed();
                            }
                        });
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                });

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