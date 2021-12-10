package com.example.myapplication.professor.ui.historycourses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ProfessorFragmentHistoryCourseBinding;
import com.example.myapplication.databinding.StudentFragmentHistoryCourseBinding;
import com.example.myapplication.student.model.CurrentStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HistoryCourses extends Fragment {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ProfessorFragmentHistoryCourseBinding binding;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference dataBase;
    private String currentStundent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = ProfessorFragmentHistoryCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.arrayList = new ArrayList<>();
        this.currentStundent = CurrentStudent.getCurrentViaID();
        this.arrayAdapter = new ArrayAdapter<String>(root.getContext(),R.layout.style_list,arrayList);
        this.listView = root.findViewById(R.id.historyList);
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        dataBase.child("students").child(currentStundent).child("attendance").limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    //for (DataSnapshot propertySnapshot : questionSnapshot.getChildren()) {
                        Map<String,String> map = (Map<String, String>) questionSnapshot.getValue();
                        String course = (String) map.get("course");
                        String room = (String) map.get("room");
                        String date = (String) map.get("date");
                        //String room = propertySnapshot.getKey();
                        //String hour = (String) propertySnapshot.getValue();
                        arrayList.add(room+"\n"+course+"\n"+date);
                    //}
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}