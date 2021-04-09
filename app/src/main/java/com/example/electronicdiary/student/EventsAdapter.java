package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.ModuleInfo;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import java.util.ArrayList;
import java.util.HashMap;

class EventsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;

    private final ArrayList<Integer> modules;
    private final HashMap<Integer, ModuleInfo> moduleInfoByModules;
    private final HashMap<Integer, StudentPerformanceInModule> studentPerformanceByModules;
    private final HashMap<Integer, ArrayList<Event>> eventsByModules;
    private final HashMap<Integer, ArrayList<StudentEvent>> studentEventsByModules;

    EventsAdapter(Context context, ArrayList<Integer> modules, HashMap<Integer, ModuleInfo> moduleInfoByModules,
                  HashMap<Integer, StudentPerformanceInModule> studentPerformanceByModules,
                  HashMap<Integer, ArrayList<Event>> eventsByModules,
                  HashMap<Integer, ArrayList<StudentEvent>> studentEventsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modules = modules;
        this.moduleInfoByModules = moduleInfoByModules;
        this.studentPerformanceByModules = studentPerformanceByModules;
        this.eventsByModules = eventsByModules;
        this.studentEventsByModules = studentEventsByModules;
    }

    @Override
    public int getGroupCount() {
        return modules.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return eventsByModules.get(modules.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return modules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return eventsByModules.get(modules.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String moduleTitle = "Модуль " + modules.get(groupPosition);
        ModuleInfo moduleInfo = moduleInfoByModules.get(modules.get(groupPosition));
        StudentPerformanceInModule studentPerformanceInModule = studentPerformanceByModules.get(modules.get(groupPosition));

        if (view == null) {
            view = inflater.inflate(R.layout.holder_module_performance, null);
        }

        TextView moduleTitleWithPointsView = view.findViewById(R.id.moduleTitleWithPoints);
        moduleTitleWithPointsView.setText(moduleTitle + " (" + moduleInfo.getMinPoints() +
                "-" + moduleInfo.getMaxPoints() + ")");
        moduleTitleWithPointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > moduleInfo.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        TextView earnedModulePointsView = view.findViewById(R.id.earnedModulePoints);
        earnedModulePointsView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
        earnedModulePointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > moduleInfo.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        Event event = eventsByModules.get(modules.get(groupPosition)).get(childPosition);
        ArrayList<StudentEvent> studentEvents = studentEventsByModules.get(modules.get(groupPosition));

        int lastAttempt = 0;
        StudentEvent studentEvent = null;
        for (int i = 0; i < studentEvents.size(); i++) {
            if (event.getId() == studentEvents.get(i).getEventId() && studentEvents.get(i).getAttemptNumber() > lastAttempt) {
                studentEvent = studentEvents.get(i);
                lastAttempt = studentEvents.get(i).getAttemptNumber();
            }
        }

        if (view == null) {
            view = inflater.inflate(R.layout.holder_event_performance, null);
        }

        TextView eventTitleView = view.findViewById(R.id.eventTitle);
        TextView attemptNumberView = view.findViewById(R.id.attemptNumber);
        TextView allPointsView = view.findViewById(R.id.allPoints);
        TextView finishDateView = view.findViewById(R.id.finishDate);

        if (lastAttempt == 0) {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText("");
            allPointsView.setText("Нет данных");
            finishDateView.setText("");
        } else if (!studentEvent.isAttended()) {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText("");
            allPointsView.setText("Не посещено");
            finishDateView.setText("");
        } else {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText(String.valueOf(studentEvent.getAttemptNumber()));
            if (studentEvent.getEarnedPoints() == -1 && studentEvent.getBonusPoints() == -1) {
                allPointsView.setText("-");
            } else if (studentEvent.getEarnedPoints() == -1) {
                allPointsView.setText(String.valueOf(studentEvent.getBonusPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            } else if (studentEvent.getBonusPoints() == -1) {
                allPointsView.setText(String.valueOf(studentEvent.getEarnedPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            } else {
                allPointsView.setText(String.valueOf(studentEvent.getEarnedPoints() + studentEvent.getBonusPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            }

            if (studentEvent.getFinishDate() != null) {
                finishDateView.setText(studentEvent.getFinishDate().getDate());
                finishDateView.setTextColor(inflater.getContext().getColor(studentEvent.getFinishDate().after(event.getDeadlineDate())
                        ? R.color.red : R.color.green));
            } else
                finishDateView.setText("-");

            eventTitleView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ? R.color.green : R.color.red));
            attemptNumberView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ? R.color.green : R.color.red));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

