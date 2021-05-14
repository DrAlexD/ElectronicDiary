package com.example.electronicdiary.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.JwtResponse;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.User;

import java.util.List;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<JwtResponse>> response = new MutableLiveData<>();
    private final MutableLiveData<List<Semester>> semesters = new MutableLiveData<>();
    private final MutableLiveData<Result<User>> user = new MutableLiveData<>();

    public LiveData<List<Semester>> getSemesters() {
        return semesters;
    }

    LiveData<Result<JwtResponse>> getResponse() {
        return response;
    }

    LiveData<Result<User>> getUser() {
        return user;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public void login(String username, String password) {
        Repository.getInstance().login(username, password, response);
    }

    public void getLoggedUser(JwtResponse jwtResponse) {
        Repository.getInstance().getLoggedUser(jwtResponse, user);
    }

    public void loginDataChanged(String username, String password) {
        loginFormState.setValue(new LoginFormState(username, password));
    }

    public void downloadSemesters() {
        Repository.getInstance().getSemesters(semesters);
    }
}