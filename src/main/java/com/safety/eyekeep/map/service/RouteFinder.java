package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.domain.LinkList;
import com.safety.eyekeep.map.dto.NodeToNode;
import com.safety.eyekeep.map.dto.RoadNetworkLinkDTO;
import com.safety.eyekeep.map.dto.RoadNetworkNodeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteFinder {
    private final RepositoryService repositoryService;
    private static final double EARTH_RADIUS_KM = 6371000.0; // 지구 반지름 (킬로미터 단위)
    private final Map<RoadNetworkNodeDTO, Map<RoadNetworkNodeDTO, RoadNetworkLinkDTO>> nodeLinkMap = new HashMap<>();

    public List<RoadNetworkNodeDTO> findPath(RoadNetworkNodeDTO start, RoadNetworkNodeDTO goal) {
        List<NodeToNode> graph = setMap(start, goal);
        buildNodeLinkMap(graph);
        Set<RoadNetworkNodeDTO> closedSet = new HashSet<>();
        Map<RoadNetworkNodeDTO, RoadNetworkNodeDTO> cameFrom = new HashMap<>();
        Map<RoadNetworkNodeDTO, Double> gScore = new HashMap<>();
        Map<RoadNetworkNodeDTO, Double> fScore = new HashMap<>();
        PriorityQueue<RoadNetworkNodeDTO> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(node -> fScore.getOrDefault(node, Double.POSITIVE_INFINITY)));

        gScore.put(start, 0.0);
        fScore.put(start, calculateDistance(start.getLatitude(), start.getLongitude(), goal.getLatitude(), goal.getLongitude()));

        openSet.add(start);

        while (!openSet.isEmpty()) {
            RoadNetworkNodeDTO current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            closedSet.add(current);

            for (NodeToNode node : graph) {
                RoadNetworkNodeDTO neighbor = node.getToNode().equals(current) ? node.getFromNode() : null;
                if (neighbor == null) {
                    continue;
                }

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + Double.parseDouble(node.getEdge().getLength());

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + calculateDistance(neighbor.getLatitude(), neighbor.getLongitude(), goal.getLatitude(), goal.getLongitude()));
            }
        }

        return null; // 경로를 찾을 수 없을 때
    }

    // 경로를 재구성하며 중간 노드를 추가하는 메서드
    private List<RoadNetworkNodeDTO> reconstructPath(Map<RoadNetworkNodeDTO, RoadNetworkNodeDTO> cameFrom, RoadNetworkNodeDTO current) {
        List<RoadNetworkNodeDTO> totalPath = new ArrayList<>();
        // 경로 재구성을 위한 스택
        Stack<RoadNetworkNodeDTO> pathStack = new Stack<>();
        pathStack.push(current);

        // 기존 경로를 스택에 넣기
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            pathStack.push(current);
        }

        // 스택에서 경로를 꺼내면서 중간 노드 추가
        RoadNetworkNodeDTO previousNode = null;
        while (!pathStack.isEmpty()) {
            current = pathStack.pop();

            // 중간 노드를 추가할 필요가 있는지 확인
            if (previousNode != null) {
                RoadNetworkLinkDTO link = getLinksBetweenNodes(previousNode, current);
                if (link == null) {
                    previousNode = current;
                    continue;
                }
                List<LinkList> coordinates = link.getLink();
                if (coordinates.size() > 2) { // 중간 노드가 있으면
                    for (int i = 1; i < coordinates.size() - 1; i++) { // 첫 번째와 마지막 링크를 제외
                        RoadNetworkNodeDTO intermediateNode = new RoadNetworkNodeDTO();
                        intermediateNode.setNodeId(link.getLinkId());
                        intermediateNode.setLatitude(coordinates.get(i).getLatitude());
                        intermediateNode.setLongitude(coordinates.get(i).getLongitude());
                        totalPath.add(intermediateNode);
                    }
                }
            }

            totalPath.add(current);
            previousNode = current;
        }

        return totalPath;
    }

    // 그래프를 설정하는 메서드에서 맵을 채우기
    private void buildNodeLinkMap(List<NodeToNode> graph) {
        for (NodeToNode nodeToNode : graph) {
            RoadNetworkNodeDTO fromNode = nodeToNode.getFromNode();
            RoadNetworkNodeDTO toNode = nodeToNode.getToNode();
            RoadNetworkLinkDTO link = nodeToNode.getEdge();

            nodeLinkMap.putIfAbsent(fromNode, new HashMap<>());
            nodeLinkMap.get(fromNode).put(toNode, link);
        }
    }

    // 두 노드 사이의 링크를 가져오는 메서드
    private RoadNetworkLinkDTO getLinksBetweenNodes(RoadNetworkNodeDTO fromNode, RoadNetworkNodeDTO toNode) {
        if (nodeLinkMap.containsKey(fromNode) && nodeLinkMap.get(fromNode).containsKey(toNode)) {
           return nodeLinkMap.get(fromNode).get(toNode);
        }
        return null;
    }

    public List<NodeToNode> setMap(RoadNetworkNodeDTO startNode, RoadNetworkNodeDTO goalNode) {
        double startLatitude = startNode.getLatitude();
        double startLongitude = startNode.getLongitude();
        double goalLatitude = goalNode.getLatitude();
        double goalLongitude = goalNode.getLongitude();

        // 원의 중심 계산 (출발지와 도착지의 중점)
        double centerLatitude = (startLatitude + goalLatitude) / 2;
        double centerLongitude = (startLongitude + goalLongitude) / 2;

        // 원의 반지름 계산 (출발지와 도착지 사이의 거리의 3/4)
        double radius = calculateDistance(startLatitude, startLongitude, goalLatitude, goalLongitude) * 3 / 4;

        // JPA 메서드를 호출하여 조건에 맞는 튜플 가져오기
        List<RoadNetworkNodeDTO> roadNetworkNodeDTOs = repositoryService.findWithInRadiusAtRoadNetworkNodeRepository(centerLatitude, centerLongitude, radius);
        List<RoadNetworkLinkDTO> roadNetworkLinkDTOs = repositoryService.findWithInRadiusAtRoadNetworkLinkRepository(centerLatitude, centerLongitude, radius);

        // 그래프
        List<NodeToNode> mapGraph = new ArrayList<>();

        // 시작, 도착 리스트 설정
        NodeToNode firstMap = new NodeToNode();
        NodeToNode lastMap = new NodeToNode();
        firstMap.setFromNode(startNode);
        lastMap.setToNode(goalNode);

        double fromstartNode = Double.MAX_VALUE;
        double toLastNode = Double.MAX_VALUE;
        RoadNetworkNodeDTO nextNode = null;
        RoadNetworkNodeDTO prevNode = null;
        for (RoadNetworkNodeDTO nodeDTO : roadNetworkNodeDTOs) {
            double checkDistanceFromStart = calculateDistance(startLatitude, startLongitude, nodeDTO.getLatitude(), nodeDTO.getLongitude());
            double checkDistanceToLast = calculateDistance(nodeDTO.getLatitude(), nodeDTO.getLongitude(), goalLatitude, goalLongitude);
            if (checkDistanceFromStart < fromstartNode) {
                fromstartNode = checkDistanceFromStart;
                nextNode = nodeDTO;
            }
            if (checkDistanceToLast < toLastNode) {
                toLastNode = checkDistanceToLast;
                prevNode = nodeDTO;
            }
        }
        firstMap.setToNode(nextNode);
        lastMap.setFromNode(prevNode);

        // 시작, 도착 링크 설정
        RoadNetworkLinkDTO firstLink = createLink(startNode, nextNode, "0");
        RoadNetworkLinkDTO lastLink = createLink(prevNode, goalNode, "-1");

        firstMap.setEdge(firstLink);
        lastMap.setEdge(lastLink);

        mapGraph.add(firstMap);
        mapGraph.add(lastMap);

        for (RoadNetworkLinkDTO link : roadNetworkLinkDTOs) {
            RoadNetworkNodeDTO fromNode = null;
            RoadNetworkNodeDTO toNode = null;
            for (RoadNetworkNodeDTO node : roadNetworkNodeDTOs) {
                if (node.getNodeId().equals(link.getStartNode())) {
                    fromNode = node;
                }
                if (node.getNodeId().equals(link.getEndNode())) {
                    toNode = node;
                }
            }

            // 링크가 유효하고, 시작 및 끝 노드가 존재할 때만 추가
            if (fromNode != null && toNode != null) {
                NodeToNode fromNodeToNode = new NodeToNode();
                fromNodeToNode.setFromNode(fromNode);
                fromNodeToNode.setToNode(toNode);
                fromNodeToNode.setEdge(link);
                mapGraph.add(fromNodeToNode);
            }
        }

        List<NodeToNode> reverseGraph = new ArrayList<>();

        // 원래 리스트를 순회하면서 reverseMap을 생성하고 새 리스트에 추가
        for (NodeToNode map : mapGraph) {
            NodeToNode reverseMap = reverseMap(map);
            reverseGraph.add(reverseMap);
        }

        // 모든 reverseMap을 원래 리스트에 추가
        mapGraph.addAll(reverseGraph);

        return mapGraph;
    }

    // 두 점 사이의 유클리드 거리 계산
    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        return Math.sqrt(Math.pow(latitude2 - latitude1, 2) + Math.pow(longitude2 - longitude1, 2));
    }

    private static double calculateLength(double latitude1, double longitude1, double latitude2, double longitude2) {
        // 위도와 경도를 라디안으로 변환
        double lat1Rad = Math.toRadians(latitude1);
        double lon1Rad = Math.toRadians(longitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double lon2Rad = Math.toRadians(longitude2);

        // 위도 및 경도의 차이 계산
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine 공식 계산
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        return EARTH_RADIUS_KM * c;
    }

    private static RoadNetworkLinkDTO createLink(RoadNetworkNodeDTO startNode, RoadNetworkNodeDTO endNode, String linkId) {
        RoadNetworkLinkDTO link = new RoadNetworkLinkDTO();
        link.setLinkId(linkId);
        link.setStartNode(startNode.getNodeId());
        link.setEndNode(endNode.getNodeId());

        List<LinkList> LinkList = new ArrayList<>();

        LinkList startCoordinate = new LinkList();
        startCoordinate.setLatitude(startNode.getLatitude());
        startCoordinate.setLongitude(startNode.getLongitude());

        LinkList endCoordinate = new LinkList();
        endCoordinate.setLatitude(endNode.getLatitude());
        endCoordinate.setLongitude(endNode.getLongitude());

        LinkList.add(startCoordinate);
        LinkList.add(endCoordinate);

        link.setLink(LinkList);

        double firstEdgeLength = calculateLength(startCoordinate.getLatitude(), startCoordinate.getLongitude(), endCoordinate.getLatitude(), endCoordinate.getLongitude());
        link.setLength(String.valueOf(firstEdgeLength));

        return link;
    }

    private static NodeToNode reverseMap(NodeToNode map) {
        NodeToNode reverseMap = new NodeToNode();
        reverseMap.setToNode(map.getFromNode());
        reverseMap.setFromNode(map.getToNode());

        RoadNetworkLinkDTO reverseLink = new RoadNetworkLinkDTO();
        RoadNetworkLinkDTO originalLink = map.getEdge();
        reverseLink.setLinkId(originalLink.getLinkId());
        reverseLink.setStartNode(originalLink.getEndNode());
        reverseLink.setEndNode(originalLink.getStartNode());
        reverseLink.setLength(originalLink.getLength());

        // 리스트의 순서를 반대로 변경
        List<LinkList> linkList = originalLink.getLink();
        List<LinkList> reversedList = linkList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.reverse(list);
                            return list;
                        }
                ));
        reverseLink.setLink(reversedList);

        reverseMap.setEdge(reverseLink);
        return reverseMap;
    }
}
