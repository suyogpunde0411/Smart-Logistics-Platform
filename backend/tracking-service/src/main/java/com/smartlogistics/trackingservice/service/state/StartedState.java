package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class StartedState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Cannot assign a trip that has already started.");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Cannot transition back to READY after trip has started.");
    }

    @Override
    public void start(Trip trip) {
        // Idempotent
        trip.setStatus("STARTED");
    }

    @Override
    public void pause(Trip trip) {
        trip.setStatus("PAUSED");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to RESUMED from STARTED.");
    }

    @Override
    public void complete(Trip trip) {
        trip.setStatus("COMPLETED");
    }

    @Override
    public void cancel(Trip trip) {
        trip.setStatus("CANCELLED");
    }
}
