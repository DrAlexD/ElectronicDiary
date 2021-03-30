package com.example.electronicdiary;

public class GroupInfo {
    private final Group group;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final boolean isExam;
    private boolean isQueueAllowed;

    public GroupInfo(Group group, int subjectId, int lecturerId, int seminarianId, int semesterId, boolean isExam) {
        this.group = group;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isExam = isExam;
    }

    public GroupInfo(Group group, int subjectId, int lecturerId, int seminarianId, int semesterId, boolean isExam, boolean isQueueAllowed) {
        this.group = group;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isExam = isExam;
        this.isQueueAllowed = isQueueAllowed;
    }

    public Group getGroup() {
        return group;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public int getSeminarianId() {
        return seminarianId;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public boolean isExam() {
        return isExam;
    }

    public boolean isQueueAllowed() {
        return isQueueAllowed;
    }

    public void setQueueAllowed(boolean queueAllowed) {
        isQueueAllowed = queueAllowed;
    }
}
