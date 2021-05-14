package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SemesterFormState;
import com.example.electronicdiary.data_classes.Semester;

public class SemesterEditingViewModel extends ViewModel {
    private final MutableLiveData<SemesterFormState> semesterFormState = new MutableLiveData<>();
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();

    LiveData<SemesterFormState> getSemesterFormState() {
        return semesterFormState;
    }

    public LiveData<Semester> getSemester() {
        return semester;
    }

    public void semesterEditingDataChanged(String semesterYear) {
        semesterFormState.setValue(new SemesterFormState(semesterYear));
    }

    public void downloadSemesterById(long semesterId) {
        Repository.getInstance().getSemesterById(semesterId, semester);
    }

    public void editSemester(long semesterId, int semesterYear, boolean isFirstHalf) {
        Repository.getInstance().editSemester(semesterId, new Semester(semesterYear, isFirstHalf));
    }
}