package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class PausedState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Cannot assign a paused trip.");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to READY from PAUSED.");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Trip already started.");
    }

    @Override
    public void pause(Trip trip) {
        // Idempotent
        trip.setStatus("PAUSED");
    }

    @Override
    public void resume(Trip trip) {
        trip.setStatus("RESUMED");
    }

    @Override
    public void complete(Trip trip) {
        throw new InvalidTripStateException("Cannot complete a paused trip. Resume first.");
    }

    @Override
    public void cancel(Trip trip) {
        trip.setStatus("CANCELLED");
    }
}
