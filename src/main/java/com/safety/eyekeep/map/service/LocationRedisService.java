package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.dto.ChildLocationDTO;

import java.time.Duration;
import java.util.List;

public interface LocationRedisService {
    void setLocation(String key, ChildLocationDTO location, Duration duration);
    void addLocation(String key, ChildLocationDTO location);
    List<ChildLocationDTO> getAllLocations(String key);  // 모든 위치 정보를 가져오는 메서드
    ChildLocationDTO getLastLocation(String key);     // 마지막에 추가한 위치 정보를 가져오는 메서드
    boolean checkLocationExists(String key);
}
