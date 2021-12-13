package com.example.myapplication.schedulemanager.ui.checkroom;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SmFragmentCheckRoomBinding;
import com.example.myapplication.professor.model.CurrentProfessor;
import com.example.myapplication.student.model.CurrentStudent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckRoomFragment extends Fragment {

    private SmFragmentCheckRoomBinding binding;
    private PieChart pieChart;
    private DatabaseReference dataBase;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SmFragmentCheckRoomBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        this.pieChart = root.findViewById(R.id.sm_piechart);
        this.spinner = root.findViewById(R.id.sm_spinnercourse);
        this.arrayList = new ArrayList<>();
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        showDataSpinner(root);
        binding.smActionShowGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String item = spinner.getSelectedItem().toString();
            //setupPieChart();
            loadPieChartData(item);
            }
        });

        return root;
}

    private void loadPieChartData(String item) {
        dataBase.child("classroom").child(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String activeUsers = snapshot.child("activeUsers").getValue(String.class);
                String enrolledStudents = snapshot.child("capacity").getValue(String.class);
                float users = Float.parseFloat(activeUsers);
                float enrolled = Float.parseFloat(enrolledStudents);

                int intusers = Integer.parseInt(activeUsers);
                int intenrolled = Integer.parseInt(enrolledStudents);

                float percentatge = (users/enrolled)*100;
                String showtotal = String.format("%.02f",percentatge);


                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(intusers,"Online students"));
                entries.add(new PieEntry(intenrolled,"Enrolled Students"));

                ArrayList<Integer> colors = new ArrayList<>();
                for (int color: ColorTemplate.MATERIAL_COLORS){
                    colors.add(color);
                }

                for (int color: ColorTemplate.VORDIPLOM_COLORS){
                    colors.add(color);
                }

                PieDataSet dataSet = new PieDataSet(entries,"Test");
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                //data.setValueFormatter(new PercentFormatter(pieChart));
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);
                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) Math.floor(value));
                    }
                });
                pieChart.setCenterText(showtotal);
                pieChart.setData(data);
                pieChart.invalidate();
                pieChart.animateY(1400, Easing.EaseInOutQuad);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setEntryLabelTextSize(12);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterTextSize(24);
                pieChart.getDescription().setEnabled(false);
                //String activeUsers = snapshot.child("activeUsers").getValue(String.class);
                //String enrolledStudents = snapshot.child("capacity").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDataSpinner(View root) {
        adapter = new ArrayAdapter<>(root.getContext(),R.layout.style_spinner,arrayList);
        dataBase.child("classroom").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    arrayList.add(item.getKey());
                }
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
