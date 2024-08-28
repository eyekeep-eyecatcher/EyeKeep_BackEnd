package com.safety.eyekeep.map.controller;

import com.safety.eyekeep.map.dto.RoadNetworkNodeDTO;
import com.safety.eyekeep.map.service.RouteFinder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindPathController {
    private final RouteFinder routeFinder;

    @PostMapping("/find/path")
    public List<RoadNetworkNodeDTO> findPath(@RequestBody List<RoadNetworkNodeDTO> startEndNode, HttpServletResponse response) {
        List<RoadNetworkNodeDTO> route = routeFinder.findPath(startEndNode.get(0), startEndNode.get(1));
        if (route == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        response.setStatus(200); // OK
        return route;
    }
}
