package com.example.electronicdiary.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private SubjectsWithGroupsAdapter subjectsWithGroupsAdapter;
    //private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        TextView userName = (TextView) root.findViewById(R.id.user_name_text);
        userName.setText(sharedPreferences.getString("username", ""));

        //TODO поиск предметов и групп у преподавателя
        HashMap<String, ArrayList<String>> subjectsWithGroups = new HashMap<>();

        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Алгебра");

        ArrayList<ArrayList<String>> groups = new ArrayList<>();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("ИУ9-11");
        groups1.add("ИУ9-21");
        groups1.add("ИУ9-31");
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("ИУ9-41");
        groups2.add("ИУ9-51");
        groups2.add("ИУ9-61");
        ArrayList<String> groups3 = new ArrayList<>();
        groups3.add("ИУ9-11");
        groups3.add("ИУ9-31");
        groups3.add("ИУ9-51");
        groups.add(groups1);
        groups.add(groups2);
        groups.add(groups3);

        for (int i = 0; i < subjects.size(); i++) {
            subjectsWithGroups.put(subjects.get(i), groups.get(i));
        }

        subjectsWithGroupsAdapter = new SubjectsWithGroupsAdapter(getContext(), subjects, subjectsWithGroups);

        final ExpandableListView expandableListView = root.findViewById(R.id.subjectsWithGroupsList);
        expandableListView.setAdapter(subjectsWithGroupsAdapter);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("subject", subjects.get(groupPosition));
            bundle.putString("group", subjectsWithGroups.get(subjects.get(groupPosition)).get(childPosition));
            Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
            return true;
        });

        /*profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }
}