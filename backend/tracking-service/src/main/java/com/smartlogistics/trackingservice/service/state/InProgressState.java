package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class InProgressState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Cannot assign a trip that is in progress.");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Cannot set trip to READY while in progress.");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Trip has already started.");
    }

    @Override
    public void pause(Trip trip) {
        trip.setStatus("PAUSED");
    }

    @Override
    public void resume(Trip trip) {
        // Idempotent
        trip.setStatus("IN_PROGRESS");
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
