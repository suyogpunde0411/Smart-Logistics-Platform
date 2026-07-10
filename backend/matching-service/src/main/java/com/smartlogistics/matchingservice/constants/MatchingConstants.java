package com.smartlogistics.matchingservice.constants;

public final class MatchingConstants {

    private MatchingConstants() {}

    // MatchRequest Statuses
    public static final String REQ_STATUS_PENDING = "PENDING";
    public static final String REQ_STATUS_COMPLETED = "COMPLETED";
    public static final String REQ_STATUS_EXPIRED = "EXPIRED";
    public static final String REQ_STATUS_CANCELLED = "CANCELLED";

    // MatchResult Statuses
    public static final String MATCH_STATUS_RECOMMENDED = "RECOMMENDED";
    public static final String MATCH_STATUS_ACCEPTED = "ACCEPTED";
    public static final String MATCH_STATUS_REJECTED = "REJECTED";
    public static final String MATCH_STATUS_EXPIRED = "EXPIRED";
    public static final String MATCH_STATUS_COMPLETED = "COMPLETED";

    // Bid Statuses
    public static final String BID_STATUS_PENDING = "PENDING";
    public static final String BID_STATUS_ACCEPTED = "ACCEPTED";
    public static final String BID_STATUS_REJECTED = "REJECTED";
    public static final String BID_STATUS_EXPIRED = "EXPIRED";
    public static final String BID_STATUS_COMPLETED = "COMPLETED";

    // Rule Codes
    public static final String RULE_PICKUP_DISTANCE = "PICKUP_DISTANCE";
    public static final String RULE_DESTINATION_SIMILARITY = "DESTINATION_SIMILARITY";
    public static final String RULE_TRUCK_CAPACITY = "TRUCK_CAPACITY";
    public static final String RULE_CARGO_COMPATIBILITY = "CARGO_COMPATIBILITY";
    public static final String RULE_AVAILABILITY = "AVAILABILITY";
    public static final String RULE_VEHICLE_TYPE = "VEHICLE_TYPE";
    public static final String RULE_DRIVER_RATING = "DRIVER_RATING";
    public static final String RULE_BUSINESS_PREFERENCE = "BUSINESS_PREFERENCE";

    // Kafka Topics
    public static final String TOPIC_TRUCK_AVAILABILITY_CHANGED = "truck.availability.changed";
    public static final String TOPIC_SHIPMENT_READY_FOR_MATCHING = "shipment.ready-for-matching";
    public static final String TOPIC_SHIPMENT_CANCELLED = "shipment.cancelled";
    public static final String TOPIC_TRUCK_DELETED = "truck.deleted";
    public static final String TOPIC_TRIP_COMPLETED = "trip.completed";

    public static final String TOPIC_MATCH_CREATED = "match.created";
    public static final String TOPIC_BID_PLACED = "bid.placed";
    public static final String TOPIC_BID_ACCEPTED = "bid.accepted";
    public static final String TOPIC_BID_REJECTED = "bid.rejected";
    public static final String TOPIC_MATCH_EXPIRED = "match.expired";
    public static final String TOPIC_MATCH_COMPLETED = "match.completed";
}
