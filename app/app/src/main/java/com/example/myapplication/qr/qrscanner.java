package com.example.myapplication.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.model.CurrentStudent;
import com.example.myapplication.model.Student;
import com.example.myapplication.ui.scanqr.ScanqrFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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
    private Map<String, String> map;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.map = new HashMap<String, String>();
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
        String data = rawResult.getText().toString();
        SimpleDateFormat time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateTime = time.format(date);
        map.put(data,dateTime);
        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String current_user = CurrentStudent.getCurrentViaID();
                databaseReference.child("students").child(current_user).child("attendance").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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