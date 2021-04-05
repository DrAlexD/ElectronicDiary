package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Repository;

import java.util.Date;

public class EventEditingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public MutableLiveData<Event> getEvent() {
        return event;
    }

    public void eventEditingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints));
    }

    public void downloadEventById(int eventId) {
        this.event.setValue(Repository.getInstance().getEventById(eventId));
    }

    public void editEvent(int eventId, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().editEvent(eventId, startDate, deadlineDate, minPoints, maxPoints);
    }

    public void deleteEvent(int eventId) {
        Repository.getInstance().deleteEvent(eventId);
    }
}