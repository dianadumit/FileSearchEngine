package org.example.event;

import org.example.model.SystemState;

public interface Event {
    void replay(SystemState state);
}
