package com.example.electronic_journal.admin.editing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Student;

import org.jetbrains.annotations.NotNull;

public class StudentEditingDialogFragment extends DialogFragment {
    private long studentId;

    private AlertDialog dialog;
    private StudentEditingViewModel studentEditingViewModel;

    private EditText studentName;
    private EditText studentSecondName;
    private EditText studentLogin;
    private EditText studentPassword;
    private CheckBox generateCheckBox;
    private Group group;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_editing, null);

        studentId = getArguments().getLong("studentId");
        studentEditingViewModel = new ViewModelProvider(this).get(StudentEditingViewModel.class);
        studentEditingViewModel.downloadStudentByIdWithLogin(studentId);

        setupAutoGenerate(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Измените данные студента")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            studentEditingViewModel.editStudent(studentId, studentName.getText().toString(),
                    studentSecondName.getText().toString(), group, studentLogin.getText().toString(),
                    studentPassword.getText().toString(), "ROLE_STUDENT");

            studentEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    if (answer instanceof Result.Success) {
                        Toast.makeText(getContext(), "Успешное изменение данных студента", Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", 1);
                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_editing_to_admin_actions, bundle);
                    } else {
                        String error = ((Result.Error) answer).getError();
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                        studentEditingViewModel.setAnswer(new MutableLiveData<>());
                    }
                }
            });
        });
    }

    private void setupAutoGenerate(View root) {
        generateCheckBox = root.findViewById(R.id.studentEditingGenerate);
        studentName = root.findViewById(R.id.studentNameEditing);
        studentSecondName = root.findViewById(R.id.studentSecondNameEditing);
        studentLogin = root.findViewById(R.id.studentLoginEditing);
        studentPassword = root.findViewById(R.id.studentPasswordEditing);

        studentEditingViewModel.getStudent().observe(this, student -> {
            if (student != null) {
                if (student instanceof Result.Success) {
                    Student studentData = ((Result.Success<Student>) student).getData();

                    studentName.setText(studentData.getFirstName());
                    studentSecondName.setText(studentData.getSecondName());
                    studentLogin.setText(studentData.getUsername());
                    //studentPassword.setText(studentData.getPassword());
                    group = studentData.getGroup();
                    generateCheckBox.setEnabled(true);
                }
            }
        });

        TextWatcher afterNameAndSecondNameChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentEditingViewModel.studentEditingDataChanged(studentName.getText().toString(),
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
                studentEditingViewModel.studentEditingDataChanged(studentName.getText().toString(),
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

        studentEditingViewModel.getStudentFormState().observe(this, studentFormState -> {
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

                    setEmptyLoginAndPassword();
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
        studentLogin.setText(studentName.getText().toString().toLowerCase() +
                studentSecondName.getText().toString().toLowerCase() + studentId);
        studentPassword.setText("123456" + studentId);
    }

    private void setEmptyLoginAndPassword() {
        studentLogin.setText("");
        studentPassword.setText("");
    }
}
