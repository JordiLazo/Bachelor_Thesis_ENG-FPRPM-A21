package com.example.myapplication.student.ui.latestattendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.StudentFragmentLatestattendanceBinding;
import com.example.myapplication.student.model.CurrentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LatestAttendanceFragment extends Fragment {

    private LatestAttendanceViewModel latestAttendanceFragment;
    private StudentFragmentLatestattendanceBinding binding;
    private static TextView room;
    private static TextView day;
    private static TextView course;
    private DatabaseReference dataBase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        String currentviaID = CurrentStudent.getCurrentViaID();
        latestAttendanceFragment = new ViewModelProvider(this).get(LatestAttendanceViewModel.class);
        binding = StudentFragmentLatestattendanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        room = root.findViewById(R.id.room_data);
        day = root.findViewById(R.id.day_data);
        course = root.findViewById(R.id.course_data);

        dataBase.child("students").child(currentviaID).child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Map<String,String> map = (Map<String, String>) questionSnapshot.getValue();
                    String course = (String) map.get("course");
                    String room = (String) map.get("room");
                    String date = (String) map.get("date");
                    LatestAttendanceFragment.room.setText(room);
                    LatestAttendanceFragment.day.setText(date);
                    LatestAttendanceFragment.course.setText(course);
                }
                /*
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot propertySnapshot : questionSnapshot.getChildren()) {
                        String room = propertySnapshot.getKey();
                        String hour = (String) propertySnapshot.getValue();
                        LatestAttendanceFragment.room.setText(room);
                        LatestAttendanceFragment.date.setText(hour);
                    }
                }
                 */
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
