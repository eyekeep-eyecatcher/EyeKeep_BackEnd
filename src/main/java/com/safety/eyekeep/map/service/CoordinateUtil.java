package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.domain.LinkList;

import java.util.ArrayList;
import java.util.List;

public class CoordinateUtil {

    public double[] parsePoint(String wktPoint) {
        // "POINT(lng lat)" 형식에서 숫자 부분을 추출
        if (wktPoint != null && wktPoint.startsWith("POINT(") && wktPoint.endsWith(")")) {
            // "POINT("와 ")" 사이의 내용을 추출
            String pointContent = wktPoint.substring(6, wktPoint.length() - 1).trim();
            String[] coordinates = pointContent.split(" ");

            if (coordinates.length == 2) {
                // 경도(lng)와 위도(lat) 값을 문자열로 반환
                String longitude = coordinates[0];
                String latitude = coordinates[1];
                return new double[]{Double.parseDouble(longitude), Double.parseDouble(latitude)};
            } else {
                throw new IllegalArgumentException("Invalid POINT format: " + wktPoint);
            }
        } else {
            throw new IllegalArgumentException("Invalid WKT POINT format: " + wktPoint);
        }
    }

    public List<LinkList> parseLineString(String wktLineString) {
        List<LinkList> latLngList = new ArrayList<>();

        // "LINESTRING" 형식에서 좌표 부분만 추출
        if (wktLineString != null && wktLineString.startsWith("LINESTRING(") && wktLineString.endsWith(")")) {
            String coordinatesPart = wktLineString.substring(11, wktLineString.length() - 1).trim();
            String[] coordinatePairs = coordinatesPart.split(",");

            for (String pair : coordinatePairs) {
                String[] coordinates = pair.trim().split(" ");
                if (coordinates.length == 2) {
                    String longitude = coordinates[0];
                    String latitude = coordinates[1];
                    LinkList linkList = new LinkList();
                    linkList.setLatitude(Double.parseDouble(latitude));
                    linkList.setLongitude(Double.parseDouble(longitude));
                    latLngList.add(linkList);
                } else {
                    throw new IllegalArgumentException("Invalid coordinate pair format: " + pair);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid WKT LINESTRING format: " + wktLineString);
        }

        return latLngList;
    }

}