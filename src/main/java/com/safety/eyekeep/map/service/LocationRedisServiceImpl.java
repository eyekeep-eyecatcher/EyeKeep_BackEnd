package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.dto.ChildLocationDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationRedisServiceImpl implements LocationRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ListOperations<String, Object> listOperations;

    public LocationRedisServiceImpl(RedisTemplate<String, Object> redisTemplate1) {
        this.redisTemplate = redisTemplate1;
        this.listOperations = redisTemplate.opsForList();
    }

    @Override
    public void setLocation(String key, ChildLocationDTO location, Duration duration) {
        listOperations.rightPush(key, location);
        // TTL 설정
        redisTemplate.expire(key, duration);
    }

    @Override
    public void addLocation(String key, ChildLocationDTO location) {
        listOperations.rightPush(key, location);
    }

    @Override
    public List<ChildLocationDTO> getAllLocations(String key) {
        List<Object> lawList = listOperations.range(key, 0, -1);
        List<ChildLocationDTO> childLocationDTOs = new ArrayList<>();

        if(lawList == null || lawList.isEmpty()) {
            return null;
        }

        for (Object object : lawList) {
            childLocationDTOs.add((ChildLocationDTO) object);
        }
        return childLocationDTOs;
    }

    @Override
    public ChildLocationDTO getLastLocation(String key) {
        Object location = listOperations.index(key, -1);
        return location instanceof ChildLocationDTO ? (ChildLocationDTO) location : null;
    }

    @Override
    public boolean checkLocationExists(String key) { return Boolean.TRUE.equals(redisTemplate.hasKey(key)); }
}