package com.smartlogistics.trackingservice.constants;

public class TrackingConstants {
    // Consumer Topics
    public static final String TOPIC_BID_ACCEPTED = "bid.accepted";
    public static final String TOPIC_SHIPMENT_CANCELLED = "shipment.cancelled";
    public static final String TOPIC_TRUCK_AVAILABILITY_CHANGED = "truck.availability.changed";

    // Producer Topics
    public static final String TOPIC_TRIP_CREATED = "trip.created";
    public static final String TOPIC_TRIP_STARTED = "trip.started";
    public static final String TOPIC_TRIP_PAUSED = "trip.paused";
    public static final String TOPIC_TRIP_RESUMED = "trip.resumed";
    public static final String TOPIC_TRIP_COMPLETED = "trip.completed";
    public static final String TOPIC_TRIP_CANCELLED = "trip.cancelled";
    public static final String TOPIC_GPS_UPDATED = "gps.updated";
    public static final String TOPIC_ETA_UPDATED = "eta.updated";
}
