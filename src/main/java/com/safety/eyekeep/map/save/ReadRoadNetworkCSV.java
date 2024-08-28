package com.safety.eyekeep.map.save;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.safety.eyekeep.map.domain.LinkList;
import com.safety.eyekeep.map.dto.RoadNetworkLinkDTO;
import com.safety.eyekeep.map.dto.RoadNetworkNodeDTO;
import com.safety.eyekeep.map.service.CoordinateUtil;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReadRoadNetworkCSV {
    private final RepositoryService repositoryService;
    private final CoordinateUtil coordinateUtil = new CoordinateUtil();

    public String saveRoadNetworkcsv() {
        String csvFilePath = "/Users/dadigom/PublicDataAPI/SeoulRoadNetwork.csv"; // CSV 파일 경로

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFilePath), "EUC-KR"))) {
            csvReader.readNext(); // 첫 번째 행을 건너뜁니다.
            List<RoadNetworkNodeDTO> nodeDTOs = new ArrayList<>();
            List<RoadNetworkLinkDTO> linkDTOs = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if(Objects.equals(line[0], "NODE")) {
                    String point = line[1];
                    double[] coordinates = coordinateUtil.parsePoint(point);
                    String nodeId = line[2];

                    RoadNetworkNodeDTO node = new RoadNetworkNodeDTO();
                    node.setLatitude(coordinates[1]);
                    node.setLongitude(coordinates[0]);
                    node.setNodeId(nodeId);
                    nodeDTOs.add(node);
                    if(nodeDTOs.size() > 10000) {
                        repositoryService.saveAllRoadNetworkNodeRepository(nodeDTOs);
                        nodeDTOs.clear();
                    }
                }

                else if(Objects.equals(line[0], "LINK")) {
                    String lineString = line[4];
                    List<LinkList> link = coordinateUtil.parseLineString(lineString);
                    String linkId = line[5];
                    String startNode = line[7];
                    String endNode = line[8];
                    String length = line[9];

                    RoadNetworkLinkDTO edge = new RoadNetworkLinkDTO();
                    edge.setLink(link);
                    edge.setLinkId(linkId);
                    edge.setStartNode(startNode);
                    edge.setEndNode(endNode);
                    edge.setLength(length);
                    linkDTOs.add(edge);
                    if(linkDTOs.size() > 10000) {
                        repositoryService.saveAllRoadNetworkLinkRepository(linkDTOs);
                        linkDTOs.clear();
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            return e.getMessage();
        }

        return "Success";
    }
}
