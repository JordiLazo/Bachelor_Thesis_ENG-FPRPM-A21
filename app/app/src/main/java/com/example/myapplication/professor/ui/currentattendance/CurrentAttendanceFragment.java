package com.example.myapplication.professor.ui.currentattendance;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ProfessorFragmentCheckCurrentAttendanceBinding;
import com.example.myapplication.professor.model.CurrentProfessor;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CurrentAttendanceFragment extends Fragment {

    private ProfessorFragmentCheckCurrentAttendanceBinding binding;
    private PieChart pieChart;
    private static TextView textProfessorCourse;
    private DatabaseReference dataBase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProfessorFragmentCheckCurrentAttendanceBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        this.pieChart = root.findViewById(R.id.professor_piechart);
        this.textProfessorCourse = root.findViewById(R.id.professor_course);
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        loadNameCourse();
        binding.professorActionShowGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPieChartData();
            }
        });

        return root;
    }

    private void loadPieChartData() {
        dataBase.child("courses").child(CurrentProfessor.getCurrentProfessorCourse()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String activeUsers = snapshot.child("activeUsers").getValue(String.class);
                String enrolledStudents = snapshot.child("enrolledStudents").getValue(String.class);

                float studentsPresent = Float.parseFloat(activeUsers);
                float studentsEnrolled = Float.parseFloat(enrolledStudents);

                int intStudentsPresent = Integer.parseInt(activeUsers);
                int intstudentsEnrolled = Integer.parseInt(enrolledStudents);

                float percentatge = (studentsPresent/studentsEnrolled)*100;
                String showPercentatge = String.format("%.02f",percentatge);


                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(intStudentsPresent,"Attending students"));
                entries.add(new PieEntry(intstudentsEnrolled,"Registered students"));

                int[] colors = new int[] {Color.BLACK, Color.GRAY};

                PieDataSet dataSet = new PieDataSet(entries,"");
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                //data.setDrawValues(true);
                //data.setValueFormatter(new PercentFormatter(pieChart));
                data.setValueTextSize(19);
                data.setValueTextColor(Color.WHITE);
                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) Math.floor(value));
                    }
                });
                setLegend(pieChart,intstudentsEnrolled,intStudentsPresent);
                pieChart.setCenterText("AVG.\n"+showPercentatge+"%");
                pieChart.setData(data);
                pieChart.invalidate();
                pieChart.animateY(1400, Easing.EaseInOutQuad);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setEntryLabelTextSize(11);
                pieChart.setCenterTextSize(22);
                pieChart.getDescription().setEnabled(false);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setHoleRadius(75);
                pieChart.setDrawSliceText(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setLegend(PieChart pieChart, int intstudentsEnrolled, int intStudentsPresent) {
        Legend legend = pieChart.getLegend();
        LegendEntry[] test = new LegendEntry[2];
        String[] legendNames = {"Attending students: "+intStudentsPresent,"Registered students: "+intstudentsEnrolled};
        for(int i = 0; i < test.length;i++){
            LegendEntry entry = new LegendEntry();
            entry.label = legendNames[i];
            test[i] = entry;
        }
        legend.setTextSize(15);
        legend.setCustom(test);
        legend.setWordWrapEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }

    private void loadNameCourse() {
        dataBase.child("professors").child(CurrentProfessor.getCurrentViaID()).child("courseID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String course = (String) snapshot.getValue();
                textProfessorCourse.setText(course);
                CurrentProfessor.setCurrentProfessorCourse(course);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}