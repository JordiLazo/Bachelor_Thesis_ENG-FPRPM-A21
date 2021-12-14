package com.example.myapplication.schedulemanager.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SmFragmentHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryFragmentSm extends Fragment {

    private SmFragmentHistoryBinding binding;
    private Spinner spinner;
    private DatabaseReference dataBase;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private int day;
    private int month;
    private int year;
    private DatePickerDialog dialog;
    private Button button;
    private EditText dateFrom;
    private EditText dateUntil;
    private Calendar calendar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataBase = FirebaseDatabase.getInstance("https://eng-fprpm-a21-82b48-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        this.arrayList = new ArrayList<>();
        this.binding = SmFragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.calendar = Calendar.getInstance();
        this.dateFrom = root.findViewById(R.id.sm_date_from);
        this.dateUntil = root.findViewById(R.id.sm_date_until);
        this.spinner = root.findViewById(R.id.spinnercourse);
        this.button = root.findViewById(R.id.sm_search_button);
        showDataSpinner(root);
        showCalendarFrom(root);
        showCalendarUntil(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_history_to_history_classroom);
            }
        });
    }

    private void showCalendarFrom(View root) {
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                dialog = new DatePickerDialog(root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        dateFrom.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                }, year,month,day);
                dialog.show();
            }
        });
    }

    private void showCalendarUntil(View root) {
        dateUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                dialog = new DatePickerDialog(root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        dateUntil.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                }, year,month,day);
                dialog.show();
            }
        });
    }

    private void showDataSpinner(View root) {
        adapter = new ArrayAdapter<>(root.getContext(),R.layout.style_spinner,arrayList);
        dataBase.child("classroom").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    arrayList.add(item.getKey().toString());
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