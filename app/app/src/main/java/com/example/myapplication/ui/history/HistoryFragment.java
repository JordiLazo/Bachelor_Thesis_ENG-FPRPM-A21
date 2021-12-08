package com.example.myapplication.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHistoryBinding;
import com.example.myapplication.model.CurrentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FragmentHistoryBinding binding;
    private Spinner spinner;
    private DatabaseReference dataBase;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference("students");
        this.arrayList = new ArrayList<>();
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        spinner = root.findViewById(R.id.spinnercourse);
        showDataSpinner(root);

        return root;
    }

    private void showDataSpinner(View root) {
        adapter = new ArrayAdapter<>(root.getContext(),R.layout.style_spinner,arrayList);
        String currentStudent = CurrentStudent.getCurrentViaID();
        dataBase.child(currentStudent).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    arrayList.add(item.getValue().toString());
                }
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}