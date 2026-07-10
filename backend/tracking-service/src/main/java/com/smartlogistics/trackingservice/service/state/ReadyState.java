package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class ReadyState implements TripState {

    @Override
    public void assign(Trip trip) {
        throw new InvalidTripStateException("Cannot re-assign a READY trip.");
    }

    @Override
    public void ready(Trip trip) {
        // Idempotent
        trip.setStatus("READY");
    }

    @Override
    public void start(Trip trip) {
        trip.setStatus("STARTED");
    }

    @Override
    public void pause(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to PAUSED from READY.");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to RESUMED from READY.");
    }

    @Override
    public void complete(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to COMPLETED from READY.");
    }

    @Override
    public void cancel(Trip trip) {
        trip.setStatus("CANCELLED");
    }
}
