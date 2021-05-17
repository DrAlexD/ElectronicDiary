package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.GroupFormState;
import com.example.electronicdiary.data_classes.Group;

public class GroupEditingViewModel extends ViewModel {
    private final MutableLiveData<GroupFormState> groupFormState = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<GroupFormState> getGroupFormState() {
        return groupFormState;
    }

    public LiveData<Group> getGroup() {
        return group;
    }

    public void groupEditingDataChanged(String groupTitle) {
        groupFormState.setValue(new GroupFormState(groupTitle));
    }

    public void downloadGroupById(long groupId) {
        Repository.getInstance().getGroupById(groupId, group);
    }

    public void editGroup(long groupId, String groupTitle) {
        Repository.getInstance().editGroup(groupId, new Group(groupTitle), answer);
    }
}