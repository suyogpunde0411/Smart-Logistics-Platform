package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class CompletedState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Trip is already completed.");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Trip is already completed.");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Trip is already completed.");
    }

    @Override
    public void pause(Trip trip) {
        throw new InvalidTripStateException("Trip is already completed.");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Trip is already completed.");
    }

    @Override
    public void complete(Trip trip) {
        // Idempotent
        trip.setStatus("COMPLETED");
    }

    @Override
    public void cancel(Trip trip) {
        throw new InvalidTripStateException("Cannot cancel a completed trip.");
    }
}
