package com.example.myapplication.schedulemanager.ui.checkroom;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.SmFragmentClassroomAttendanceBinding;
import com.example.myapplication.schedulemanager.model.CalculateSD;
import com.example.myapplication.R;
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

public class CheckRoomFragment extends Fragment {

    private SmFragmentClassroomAttendanceBinding binding;
    private PieChart pieChart;
    private DatabaseReference dataBase;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SmFragmentClassroomAttendanceBinding.inflate(inflater,container,false);
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
                float occupiedSeats = Float.parseFloat(activeUsers);
                float availableSeats = Float.parseFloat(enrolledStudents);

                int intOccupiedSeats = Integer.parseInt(activeUsers);
                int intAvailableSeats = Integer.parseInt(enrolledStudents);

                float percentatge = (occupiedSeats/availableSeats)*100;
                String showPercentatge = String.format("%.02f",percentatge);

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(intOccupiedSeats,"Occupied seats"));
                entries.add(new PieEntry(intAvailableSeats,"Available seats"));

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
                setLegend(pieChart,intAvailableSeats,showPercentatge);
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
                //String activeUsers = snapshot.child("activeUsers").getValue(String.class);
                //String enrolledStudents = snapshot.child("capacity").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setLegend(PieChart pieChart, int availableSeats, String percentatge){
        Legend legend = pieChart.getLegend();
        LegendEntry[] test = new LegendEntry[3];
        String[] legendNames = {"Avg. of occupied seats: "+percentatge,"Available seats: "+availableSeats,"Deviation within the period: "+ CalculateSD.calculateSD()};
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
