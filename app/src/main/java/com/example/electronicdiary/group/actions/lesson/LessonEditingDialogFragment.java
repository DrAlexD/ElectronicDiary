package com.example.electronicdiary.group.actions.lesson;

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

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Lesson;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private long lessonId;
    private LessonEditingViewModel lessonEditingViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_lesson_editing, null);

        lessonId = getArguments().getLong("lessonId");

        lessonEditingViewModel = new ViewModelProvider(this).get(LessonEditingViewModel.class);
        lessonEditingViewModel.downloadLessonById(lessonId);

        EditText dateAndTime = root.findViewById(R.id.lessonDateAndTimeEditing);
        CheckBox isLecture = root.findViewById(R.id.lessonIsLectureEditing);
        EditText pointsPerVisit = root.findViewById(R.id.lessonPointsPerVisitEditing);
        lessonEditingViewModel.getLesson().observe(this, lesson -> {
            if (lesson != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                dateAndTime.setText(dateFormat.format(lesson.getDateAndTime()));
                isLecture.setChecked(lesson.isLecture());
                pointsPerVisit.setText(String.valueOf(lesson.getPointsPerVisit()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                lessonEditingViewModel.lessonEditingDataChanged(dateAndTime.getText().toString(), pointsPerVisit.getText().toString());
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
        dateAndTime.addTextChangedListener(afterTextChangedListener);
        pointsPerVisit.addTextChangedListener(afterTextChangedListener);

        lessonEditingViewModel.getLessonFormState().observe(this, lessonFormState -> {
            if (lessonFormState == null) {
                return;
            }

            dateAndTime.setError(lessonFormState.getDateAndTimeError() != null ?
                    getString(lessonFormState.getDateAndTimeError()) : null);

            pointsPerVisit.setError(lessonFormState.getPointsPerVisitError() != null ?
                    getString(lessonFormState.getPointsPerVisitError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(lessonFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные занятия")
                .setPositiveButton("Подтвердить", null)
                .setNegativeButton("Отменить", (dialog, id) -> dismiss())
                .setNeutralButton("Удалить", null).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText dateAndTime = root.findViewById(R.id.lessonDateAndTimeEditing);
        CheckBox isLecture = root.findViewById(R.id.lessonIsLectureEditing);
        EditText pointsPerVisit = root.findViewById(R.id.lessonPointsPerVisitEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date dateAndTimeR = dateFormat.parse(dateAndTime.getText().toString());

                Lesson lesson = lessonEditingViewModel.getLesson().getValue();
                lessonEditingViewModel.editLesson(lessonId, lesson.getModule(), dateAndTimeR, isLecture.isChecked(),
                        Integer.parseInt(pointsPerVisit.getText().toString()));

                lessonEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", lesson.getModule().getModuleNumber() - 1);
                        bundle.putLong("subjectInfoId", lesson.getModule().getSubjectInfo().getId());
                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_editing_to_group_performance, bundle);
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(view -> {
            lessonEditingViewModel.deleteLesson(lessonId);

            Lesson lesson = lessonEditingViewModel.getLesson().getValue();
            lessonEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", lesson.getModule().getModuleNumber() - 1);
                    bundle.putLong("subjectInfoId", lesson.getModule().getSubjectInfo().getId());
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_lesson_editing_to_group_performance, bundle);
                }
            });
        });
    }
}
