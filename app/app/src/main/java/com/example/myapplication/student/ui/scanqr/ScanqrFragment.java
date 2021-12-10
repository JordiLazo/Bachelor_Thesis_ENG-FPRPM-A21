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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanqrViewModel =
                new ViewModelProvider(this).get(ScanqrViewModel.class);

        binding = StudentFragmentScanqrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.actionScanQr.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity().getApplication(), qrscannerStudent.class));
        });

        textView = root.findViewById(R.id.qrtext);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}