package com.example.electronicdiary.search.all;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Subject;

import org.jetbrains.annotations.NotNull;

public class SubjectInfoAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private SubjectInfoAddingViewModel subjectInfoAddingViewModel;
    private View root;
    private LiveData<Result<Professor>> professorLiveData;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_subject_info_adding, null);

        subjectInfoAddingViewModel = new ViewModelProvider(this).get(SubjectInfoAddingViewModel.class);

        CheckBox isLecturer = root.findViewById(R.id.subjectInfoIsLecturerAdding);
        CheckBox isSeminarian = root.findViewById(R.id.subjectInfoIsSeminarianAdding);
        CheckBox isExam = root.findViewById(R.id.subjectInfoIsExamAdding);
        CheckBox isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditAdding);

        isLecturer.setOnClickListener(view -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isLecturer.isChecked() || isSeminarian.isChecked());
        });

        isSeminarian.setOnClickListener(view -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isLecturer.isChecked() || isSeminarian.isChecked());
        });

        isExam.setOnClickListener(view -> {
            if (isExam.isChecked() && isDifferentiatedCredit.isChecked()) {
                isDifferentiatedCredit.setChecked(false);
            }
        });

        isDifferentiatedCredit.setOnClickListener(view -> {
            if (isExam.isChecked() && isDifferentiatedCredit.isChecked()) {
                isExam.setChecked(false);
            }
        });

        subjectInfoAddingViewModel.downloadEntities(getArguments().getLong("groupId"),
                getArguments().getLong("semesterId"), getArguments().getLong("subjectId"),
                getArguments().getLong("professorId"));

        LiveData<Group> groupLiveData = subjectInfoAddingViewModel.getGroup();
        LiveData<Semester> semesterLiveData = Transformations.switchMap(groupLiveData,
                g -> subjectInfoAddingViewModel.getSemester());
        LiveData<Subject> subjectLiveData = Transformations.switchMap(semesterLiveData,
                s -> subjectInfoAddingViewModel.getSubject());
        professorLiveData = Transformations.switchMap(subjectLiveData,
                p -> subjectInfoAddingViewModel.getProfessor());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Укажите доп. информацию")
                .setPositiveButton("Подтвердить", null).create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        CheckBox isLecturer = root.findViewById(R.id.subjectInfoIsLecturerAdding);
        CheckBox isSeminarian = root.findViewById(R.id.subjectInfoIsSeminarianAdding);
        CheckBox isExam = root.findViewById(R.id.subjectInfoIsExamAdding);
        CheckBox isDifferentiatedCredit = root.findViewById(R.id.subjectInfoIsDifferentiatedCreditAdding);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            {
                LiveData<Boolean> answerLiveData = Transformations.switchMap(professorLiveData,
                        professor -> {
                            if (professor instanceof Result.Success) {
                                Professor professorData = ((Result.Success<Professor>) professor).getData();
                                Group group = subjectInfoAddingViewModel.getGroup().getValue();
                                Subject subject = subjectInfoAddingViewModel.getSubject().getValue();
                                Semester semester = subjectInfoAddingViewModel.getSemester().getValue();
                                subjectInfoAddingViewModel.addSubjectInfo(group,
                                        subject, isLecturer.isChecked() ? getArguments().getLong("professorId") : null,
                                        isSeminarian.isChecked() ? professorData : null,
                                        semester, isExam.isChecked(), isDifferentiatedCredit.isChecked());
                                return subjectInfoAddingViewModel.getAnswer();
                            } else
                                return null;
                        });

                answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null)
                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_subject_info_adding_to_profile);
                });
            }
        });
    }
}
