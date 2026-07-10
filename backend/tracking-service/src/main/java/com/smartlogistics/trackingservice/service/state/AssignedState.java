package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class AssignedState implements TripState {

    @Override
    public void assign(Trip trip) {
        // Idempotent or re-assignment allowed in ASSIGNED
        trip.setStatus("ASSIGNED");
    }

    @Override
    public void ready(Trip trip) {
        trip.setStatus("READY");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to STARTED from ASSIGNED. Trip must be READY first.");
    }

    @Override
    public void pause(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to PAUSED from ASSIGNED.");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to RESUMED from ASSIGNED.");
    }

    @Override
    public void complete(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to COMPLETED from ASSIGNED.");
    }

    @Override
    public void cancel(Trip trip) {
        trip.setStatus("CANCELLED");
    }
}
