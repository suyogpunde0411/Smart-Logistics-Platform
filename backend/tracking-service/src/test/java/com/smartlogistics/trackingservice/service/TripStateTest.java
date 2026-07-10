package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;
import com.smartlogistics.trackingservice.service.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TripStateTest {

    private Trip trip;

    @BeforeEach
    public void setup() {
        trip = new Trip();
        trip.setStatus("WAITING");
    }

    @Test
    public void testWaitingStateTransitions() {
        TripState state = getTripState(trip.getStatus());
        assertTrue(state instanceof WaitingState);

        // Valid transition
        state.assign(trip);
        assertEquals("ASSIGNED", trip.getStatus());

        // Reset and test invalid transition
        trip.setStatus("WAITING");
        state = getTripState(trip.getStatus());
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).ready(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).start(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).pause(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).resume(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).complete(trip));
    }

    @Test
    public void testAssignedStateTransitions() {
        trip.setStatus("ASSIGNED");
        TripState state = getTripState(trip.getStatus());
        assertTrue(state instanceof AssignedState);

        // Valid transition to READY
        state.ready(trip);
        assertEquals("READY", trip.getStatus());

        // Cancel transition
        trip.setStatus("ASSIGNED");
        getTripState(trip.getStatus()).cancel(trip);
        assertEquals("CANCELLED", trip.getStatus());

        // Invalid transitions
        trip.setStatus("ASSIGNED");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).start(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).complete(trip));
    }

    @Test
    public void testReadyStateTransitions() {
        trip.setStatus("READY");
        TripState state = getTripState(trip.getStatus());
        assertTrue(state instanceof ReadyState);

        // Valid transition to STARTED
        state.start(trip);
        assertEquals("STARTED", trip.getStatus());

        // Cancel
        trip.setStatus("READY");
        getTripState(trip.getStatus()).cancel(trip);
        assertEquals("CANCELLED", trip.getStatus());

        // Invalid
        trip.setStatus("READY");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).assign(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).complete(trip));
    }

    @Test
    public void testStartedStateTransitions() {
        trip.setStatus("STARTED");
        TripState state = getTripState(trip.getStatus());
        assertTrue(state instanceof StartedState);

        // Pause
        state.pause(trip);
        assertEquals("PAUSED", trip.getStatus());

        // Complete
        trip.setStatus("STARTED");
        getTripState(trip.getStatus()).complete(trip);
        assertEquals("COMPLETED", trip.getStatus());

        // Cancel
        trip.setStatus("STARTED");
        getTripState(trip.getStatus()).cancel(trip);
        assertEquals("CANCELLED", trip.getStatus());

        // Invalid
        trip.setStatus("STARTED");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).ready(trip));
    }

    @Test
    public void testPausedStateTransitions() {
        trip.setStatus("PAUSED");
        TripState state = getTripState(trip.getStatus());
        assertTrue(state instanceof PausedState);

        // Resume
        state.resume(trip);
        assertEquals("RESUMED", trip.getStatus());

        // Cancel
        trip.setStatus("PAUSED");
        getTripState(trip.getStatus()).cancel(trip);
        assertEquals("CANCELLED", trip.getStatus());

        // Invalid
        trip.setStatus("PAUSED");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).complete(trip));
    }

    @Test
    public void testTerminalStates() {
        trip.setStatus("COMPLETED");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).start(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).cancel(trip));

        trip.setStatus("CANCELLED");
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).start(trip));
        assertThrows(InvalidTripStateException.class, () -> getTripState(trip.getStatus()).complete(trip));
    }

    private TripState getTripState(String status) {
        return switch (status.toUpperCase()) {
            case "WAITING" -> new WaitingState();
            case "ASSIGNED" -> new AssignedState();
            case "READY" -> new ReadyState();
            case "STARTED" -> new StartedState();
            case "IN_PROGRESS" -> new InProgressState();
            case "PAUSED" -> new PausedState();
            case "RESUMED" -> new ResumedState();
            case "COMPLETED" -> new CompletedState();
            case "CANCELLED" -> new CancelledState();
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}
