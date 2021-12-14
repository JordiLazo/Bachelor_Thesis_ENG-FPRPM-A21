package com.example.myapplication.student.ui.scanqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.StudentFragmentScanqrBinding;
import com.example.myapplication.student.qr.qrscannerStudent;

public class ScanqrFragment extends Fragment {

    private ScanqrViewModel scanqrViewModel;
    private StudentFragmentScanqrBinding binding;
    public static TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scanqrViewModel = new ViewModelProvider(this).get(ScanqrViewModel.class);

        binding = StudentFragmentScanqrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.actionScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),qrscannerStudent.class);
                startActivity(intent);
            }
        });

        /*binding.actionScanQr.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity().getApplication(), qrscannerStudent.class));
        });*/

        textView = root.findViewById(R.id.qrtext);

        return root;
    }
/*
    private void checkPermissions(String permission, int requestCode ) {
        if(ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{permission},requestCode);
            //ActivityCompat.requestPermissions(,new String[]{permission},requestCode);
        }else{
            Toast.makeText(getContext(), "Permission already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "HOLA", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }
 */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}