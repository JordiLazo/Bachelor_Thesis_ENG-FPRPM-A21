package com.example.myapplication.professor.ui.currentattendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentAttendanceViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CurrentAttendanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
