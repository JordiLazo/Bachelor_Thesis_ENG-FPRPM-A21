package com.example.myapplication.student.ui.scanqr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanqrViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScanqrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}