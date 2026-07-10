package com.smartlogistics.shared.util;

import com.smartlogistics.shared.dto.Coordinates;
import com.smartlogistics.shared.dto.GeoPoint;

public class GeoUtils {

    public static boolean isWithinRadius(double originLat, double originLon, double targetLat, double targetLon, double radiusKm) {
        return DistanceCalculator.calculateDistance(originLat, originLon, targetLat, targetLon) <= radiusKm;
    }

    public static GeoPoint toGeoPoint(Coordinates coords) {
        if (coords == null) return null;
        return new GeoPoint(coords.latitude(), coords.longitude());
    }

    public static Coordinates toCoordinates(GeoPoint point) {
        if (point == null) return null;
        return new Coordinates(point.latitude(), point.longitude());
    }
}
