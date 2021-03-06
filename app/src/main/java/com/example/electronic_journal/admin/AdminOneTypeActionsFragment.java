package com.example.electronic_journal.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;

import java.util.ArrayList;
import java.util.List;

public class AdminOneTypeActionsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_one_type_actions, container, false);

        int position = getArguments().getInt("position");

        if (position == 1) {
            addActions(root);
        } else if (position == 2) {
            editActions(root);
        } else {
            deleteActions(root);
        }

        return root;
    }

    private void addActions(View root) {
        List<String> actions = new ArrayList<>();
        actions.add("Добавить группу");
        actions.add("Добавить студента к группе");
        actions.add("Добавить предмет");
        actions.add("Добавить преподавателя");
        actions.add("Добавить семестр");

        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_admin_action, R.id.adminActionTitle, actions);
        final ListView listView = root.findViewById(R.id.adminActionsList);
        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Navigation.findNavController(view).navigate(R.id.action_admin_actions_to_dialog_group_adding);
            } else if (position == 1) {
                Navigation.findNavController(view).navigate(R.id.action_admin_actions_to_dialog_student_adding);
            } else if (position == 2) {
                Navigation.findNavController(view).navigate(R.id.action_admin_actions_to_dialog_subject_adding);
            } else if (position == 3) {
                Navigation.findNavController(view).navigate(R.id.action_admin_actions_to_dialog_professor_adding);
            } else if (position == 4) {
                Navigation.findNavController(view).navigate(R.id.action_admin_actions_to_dialog_semester_adding);
            }
        });
    }

    private void editActions(View root) {
        List<String> actions = new ArrayList<>();
        actions.add("Изменить информацию студента");
        actions.add("Поменять группу у студентов при смене семестра");
        actions.add("Изменить информацию преподавателя");

        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_admin_action, R.id.adminActionTitle, actions);
        final ListView listView = root.findViewById(R.id.adminActionsList);
        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putInt("actionCode", 1);
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_students, bundle);
            } else if (position == 1) {
                bundle.putInt("actionCode", 3);
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_groups, bundle);
            } else if (position == 2) {
                bundle.putInt("actionCode", 1);
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_professors, bundle);
            }
        });
    }

    private void deleteActions(View root) {
        List<String> actions = new ArrayList<>();
        actions.add("Удалить группу");
        actions.add("Удалить студента");
        actions.add("Удалить предмет");
        actions.add("Удалить преподавателя");
        actions.add("Удалить семестр");

        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_admin_action, R.id.adminActionTitle, actions);
        final ListView listView = root.findViewById(R.id.adminActionsList);
        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 2);
            if (position == 0) {
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_groups, bundle);
            } else if (position == 1) {
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_students, bundle);
            } else if (position == 2) {
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_subjects, bundle);
            } else if (position == 3) {
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_professors, bundle);
            } else if (position == 4) {
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_semesters, bundle);
            }
        });
    }
}