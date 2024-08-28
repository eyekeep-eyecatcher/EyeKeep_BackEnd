package com.safety.eyekeep.map.service;

public class CoordinateTransform {
    // 상수 정의
    private static final double R_MAJOR = 6378137.0; // WGS84 타원체의 반경 (반지름)

    // EPSG:3857 (Web Mercator) -> EPSG:4326 (WGS84) 변환 메서드
    public static String[] fromEPSG3857ToEPSG4326(double x, double y) {
        double longitude = radToDeg(x / R_MAJOR);
        double latitude = radToDeg(mercatorToLatitude(y));

        return new String[]{String.valueOf(longitude), String.valueOf(latitude)};
    }

    // 라디안 -> 도(degree) 변환 메서드
    private static double radToDeg(double rad) {
        return rad * (180.0 / Math.PI);
    }

    // Mercator Y 좌표를 위도로 변환하는 메서드
    private static double mercatorToLatitude(double mercatorY) {
        return Math.atan(Math.sinh(mercatorY / R_MAJOR));
    }
}
