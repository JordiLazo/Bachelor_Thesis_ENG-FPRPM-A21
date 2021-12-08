package com.example.myapplication.ui.latestattendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLatestattendanceBinding;
import com.example.myapplication.model.CurrentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LatestAttendanceFragment extends Fragment {

    private LatestAttendanceViewModel latestAttendanceFragment;
    private FragmentLatestattendanceBinding binding;
    public static TextView room;
    public static TextView date;
    private DatabaseReference dataBase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        String currentviaID = CurrentStudent.getCurrentViaID();
        latestAttendanceFragment = new ViewModelProvider(this).get(LatestAttendanceViewModel.class);
        binding = FragmentLatestattendanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        room = root.findViewById(R.id.lastattendanceroom);
        date = root.findViewById(R.id.lastattendancedate);

        dataBase.child("students").child(currentviaID).child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot propertySnapshot : questionSnapshot.getChildren()) {
                        String room = propertySnapshot.getKey();
                        String hour = (String) propertySnapshot.getValue();
                        String ans1 = questionSnapshot.child("ans1").getValue(String.class);
                        LatestAttendanceFragment.room.setText(room);
                        LatestAttendanceFragment.date.setText(hour);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
