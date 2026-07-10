package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;

public class WaitingState implements TripState {

    @Override
    public void assign(Trip trip) {
        trip.setStatus("ASSIGNED");
    }

    @Override
    public void ready(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to READY from WAITING. Trip must be ASSIGNED first.");
    }

    @Override
    public void start(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to STARTED from WAITING. Trip must be ASSIGNED and READY.");
    }

    @Override
    public void pause(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to PAUSED from WAITING.");
    }

    @Override
    public void resume(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to RESUMED from WAITING.");
    }

    @Override
    public void complete(Trip trip) {
        throw new InvalidTripStateException("Cannot transition to COMPLETED from WAITING.");
    }

    @Override
    public void cancel(Trip trip) {
        trip.setStatus("CANCELLED");
    }
}
