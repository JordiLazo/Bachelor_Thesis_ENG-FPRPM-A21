package com.example.myapplication.professor.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ProfessorFragmentHistoryBinding;
import com.example.myapplication.professor.model.CurrentProfessor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryFragmentProfessor extends Fragment {

    private ProfessorFragmentHistoryBinding binding;
    private EditText dateFrom;
    private EditText dateUntil;
    private Calendar calendar;
    private static TextView textProfessorView;
    private int day;
    private int month;
    private int year;
    private DatePickerDialog dialog;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = ProfessorFragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.calendar = Calendar.getInstance();
        this.textProfessorView = root.findViewById(R.id.professor_textview);
        this.dateFrom = root.findViewById(R.id.professor_date_from);
        this.dateUntil = root.findViewById(R.id.professor_date_until);
        this.button = root.findViewById(R.id.professor_search_button);
        showCourseProfessor(root);
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
                navController.navigate(R.id.action_nav_history_to_history_course);
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

    private void showCourseProfessor(View root) {
        String course = CurrentProfessor.getCurrentProfessorCourse();
        textProfessorView.setText(course);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}