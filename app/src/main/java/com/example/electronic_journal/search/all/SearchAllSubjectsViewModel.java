package com.example.electronic_journal.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Subject;

import java.util.List;

public class SearchAllSubjectsViewModel extends ViewModel {
    private final MutableLiveData<List<Subject>> allSubjects = new MutableLiveData<>();
    private final MutableLiveData<List<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public LiveData<List<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public void downloadAllSubjects() {
        Repository.getInstance().getAllSubjects(allSubjects);
    }

    public void downloadAvailableSubjects(long professorId, long semesterId) {
        Repository.getInstance().getAvailableSubjects(professorId, semesterId, availableSubjects);
    }

    public void deleteSubject(long subjectId) {
        Repository.getInstance().deleteSubject(subjectId, answer);
    }
}