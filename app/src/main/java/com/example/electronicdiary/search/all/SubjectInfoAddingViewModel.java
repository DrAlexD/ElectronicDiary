package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class SubjectInfoAddingViewModel extends ViewModel {
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<Subject> subject = new MutableLiveData<>();
    private final MutableLiveData<Result<Professor>> professor = new MutableLiveData<>();
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();

    public LiveData<Group> getGroup() {
        return group;
    }

    public LiveData<Subject> getSubject() {
        return subject;
    }

    public LiveData<Result<Professor>> getProfessor() {
        return professor;
    }

    public LiveData<Semester> getSemester() {
        return semester;
    }

    public void downloadEntities(long groupId, long semesterId, long subjectId, long professorId) {
        Repository.getInstance().getGroupById(groupId, group);
        Repository.getInstance().getSemesterById(semesterId, semester);
        Repository.getInstance().getSubjectById(subjectId, subject);
        Repository.getInstance().getProfessorById(professorId, professor);
    }

    public void addSubjectInfo(Group group, Subject subject, long lecturerId, Professor seminarian,
                               Semester semester, boolean isExam, boolean isDifferentiatedCredit) {
        Repository.getInstance().addSubjectInfo(new SubjectInfo(group, subject, lecturerId, seminarian,
                semester, isExam, isDifferentiatedCredit));
    }
}