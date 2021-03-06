package com.example.electronic_journal.admin.adding;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StudentAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private StudentAddingViewModel studentAddingViewModel;

    private EditText studentName;
    private EditText studentSecondName;
    private EditText studentLogin;
    private EditText studentPassword;
    private CheckBox generateCheckBox;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_adding, null);

        studentAddingViewModel = new ViewModelProvider(this).get(StudentAddingViewModel.class);

        setupAutoGenerate(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные студента")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionCode", 0);
                    bundle.putString("studentName", studentName.getText().toString());
                    bundle.putString("studentSecondName", studentSecondName.getText().toString());
                    bundle.putString("studentLogin", studentLogin.getText().toString());
                    bundle.putString("studentPassword", studentPassword.getText().toString());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_adding_to_search_all_groups, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    private void setupAutoGenerate(View root) {
        generateCheckBox = root.findViewById(R.id.studentAddingGenerate);
        studentName = root.findViewById(R.id.studentNameAdding);
        studentSecondName = root.findViewById(R.id.studentSecondNameAdding);
        studentLogin = root.findViewById(R.id.studentLoginAdding);
        studentPassword = root.findViewById(R.id.studentPasswordAdding);

        TextWatcher afterNameAndSecondNameChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentAddingViewModel.studentAddingDataChanged(studentName.getText().toString(),
                        studentSecondName.getText().toString(), studentLogin.getText().toString(),
                        studentPassword.getText().toString(), true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        };

        TextWatcher afterLoginAndPasswordChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentAddingViewModel.studentAddingDataChanged(studentName.getText().toString(),
                        studentSecondName.getText().toString(), studentLogin.getText().toString(),
                        studentPassword.getText().toString(), false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        };

        studentName.addTextChangedListener(afterNameAndSecondNameChangedListener);
        studentSecondName.addTextChangedListener(afterNameAndSecondNameChangedListener);
        studentLogin.addTextChangedListener(afterLoginAndPasswordChangedListener);
        studentPassword.addTextChangedListener(afterLoginAndPasswordChangedListener);
        generateCheckBox.setOnClickListener(view -> {
            if (generateCheckBox.isChecked()) {
                setGeneratedLoginAndPassword();
            } else {
                setEmptyLoginAndPassword();
            }
        });

        studentAddingViewModel.getStudentFormState().observe(this, studentFormState -> {
            if (studentFormState == null) {
                return;
            }

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(studentFormState.isDataValid());
            if (studentFormState.isNameOrSecondNameChanged()) {
                studentName.setError(studentFormState.getStudentNameError() != null ?
                        getString(studentFormState.getStudentNameError()) : null);
                studentSecondName.setError(studentFormState.getStudentSecondNameError() != null ?
                        getString(studentFormState.getStudentSecondNameError()) : null);
                if (studentFormState.getStudentNameError() == null && studentFormState.getStudentSecondNameError() == null) {
                    generateCheckBox.setEnabled(true);

                    if (generateCheckBox.isChecked()) {
                        setGeneratedLoginAndPassword();
                    }
                } else {
                    generateCheckBox.setEnabled(false);

                    //setEmptyLoginAndPassword();
                }
            } else {
                studentLogin.setError(studentFormState.getStudentLoginError() != null ?
                        getString(studentFormState.getStudentLoginError()) : null);
                studentPassword.setError(studentFormState.getStudentPasswordError() != null ?
                        getString(studentFormState.getStudentPasswordError()) : null);
            }
        });
    }

    private void setGeneratedLoginAndPassword() {
        int randomNumber = new Random().nextInt(100);
        studentLogin.setText(studentName.getText().toString().toLowerCase() +
                studentSecondName.getText().toString().toLowerCase() + randomNumber);
        studentPassword.setText("123456" + randomNumber);
    }

    private void setEmptyLoginAndPassword() {
        studentLogin.setText("");
        studentPassword.setText("");
    }
}
