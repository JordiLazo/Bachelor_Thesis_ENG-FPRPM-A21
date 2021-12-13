package com.example.myapplication.professor.ui.currentattendance;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ProfessorFragmentLiveAttendanceBinding;
import com.example.myapplication.professor.model.CurrentProfessor;
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

public class CurrentAttendanceFragment extends Fragment {

    private ProfessorFragmentLiveAttendanceBinding binding;
    private CurrentAttendanceViewModel currentAttendanceViewModel;
    private PieChart pieChart;
    private static TextView textProfessorCourse;
    private DatabaseReference dataBase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentAttendanceViewModel = new ViewModelProvider(this).get(CurrentAttendanceViewModel.class);
        binding = ProfessorFragmentLiveAttendanceBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        this.pieChart = root.findViewById(R.id.professor_piechart);
        this.textProfessorCourse = root.findViewById(R.id.professor_course);
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        loadNameCourse();
        binding.professorActionShowGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupPieChart();
                loadPieChartData();
            }
        });

        return root;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
    }

    private void loadPieChartData() {
        dataBase.child("courses").child(CurrentProfessor.getCurrentProfessorCourse()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String activeUsers = snapshot.child("activeUsers").getValue(String.class);
                String enrolledStudents = snapshot.child("enrolledStudents").getValue(String.class);

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

                PieDataSet dataSet = new PieDataSet(entries,"Expense category");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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