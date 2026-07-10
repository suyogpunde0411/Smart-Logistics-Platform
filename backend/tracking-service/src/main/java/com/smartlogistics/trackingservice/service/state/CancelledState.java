package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class CancelledState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void pause(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void complete(Trip trip) {
        throw new InvalidTripStateException("Trip is cancelled.");
    }

    @Override
    public void cancel(Trip trip) {
        // Idempotent
        trip.setStatus("CANCELLED");
    }
}
