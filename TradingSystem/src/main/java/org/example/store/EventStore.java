package org.example.store;

import org.example.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventStore {
    private final List<Event> eventLog = new ArrayList<>();
    public void append(Event event){
        eventLog.add(event);
    }

    public List<Event> getAllEvents(){
        return Collections.unmodifiableList(eventLog);
    }
}
