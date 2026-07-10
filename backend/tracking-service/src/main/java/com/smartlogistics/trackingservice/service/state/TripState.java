package com.smartlogistics.trackingservice.service.state;

import com.smartlogistics.trackingservice.entity.Trip;

public interface TripState {
    void assign(Trip trip);
    void ready(Trip trip);
    void start(Trip trip);
    void pause(Trip trip);
    void resume(Trip trip);
    void complete(Trip trip);
    void cancel(Trip trip);
}
