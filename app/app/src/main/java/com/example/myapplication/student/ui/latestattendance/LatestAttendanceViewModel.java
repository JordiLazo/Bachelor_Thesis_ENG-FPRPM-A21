package com.example.myapplication.student.ui.latestattendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LatestAttendanceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LatestAttendanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is latest attendance");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
