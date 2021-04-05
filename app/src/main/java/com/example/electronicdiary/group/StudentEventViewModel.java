package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentEvent;

import java.util.Date;

public class StudentEventViewModel extends ViewModel {
    private final MutableLiveData<StudentEventFormState> studentEventFormState = new MutableLiveData<>();
    private final MutableLiveData<StudentEvent> studentEvent = new MutableLiveData<>();

    LiveData<StudentEventFormState> getStudentEventFormState() {
        return studentEventFormState;
    }

    public MutableLiveData<StudentEvent> getStudentEvent() {
        return studentEvent;
    }

    public void eventPerformanceDataChanged(String variantNumber) {
        studentEventFormState.setValue(new StudentEventFormState(variantNumber));
    }

    public void eventPerformanceDataChanged(String variantNumber, String finishDate, String earnedPoints, String bonusPoints) {
        studentEventFormState.setValue(new StudentEventFormState(variantNumber, finishDate, earnedPoints, bonusPoints));
    }

    public void downloadStudentEventById(int attemptNumber, int eventId, int studentId) {
        this.studentEvent.setValue(Repository.getInstance().getStudentEventById(attemptNumber, eventId, studentId));
    }

    public void addStudentEvent(int attemptNumber, int eventId, int studentId, int moduleNumber, int groupId, int subjectId,
                                int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber) {
        Repository.getInstance().addStudentEvent(attemptNumber, eventId, studentId, moduleNumber, groupId, subjectId,
                lecturerId, seminarianId, semesterId, isAttended, variantNumber);
    }

    public void editStudentEvent(int attemptNumber, int eventId, int studentId, boolean isAttended, int variantNumber,
                                 Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        Repository.getInstance().editStudentEvent(attemptNumber, eventId, studentId, isAttended, variantNumber,
                finishDate, earnedPoints, bonusPoints, isHaveCredit);
    }

    public void deleteStudentEvent(int attemptNumber, int eventId, int studentId) {
        Repository.getInstance().deleteStudentEvent(attemptNumber, eventId, studentId);
    }
}