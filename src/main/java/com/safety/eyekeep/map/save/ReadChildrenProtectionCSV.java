package com.safety.eyekeep.map.save;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.safety.eyekeep.map.dto.ChildrenProtectionDTO;
import com.safety.eyekeep.map.service.NaverGeocoding;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadChildrenProtectionCSV {
    private final RepositoryService repositoryService;
    private final NaverGeocoding naverGeocoding;

    public String saveChildrenProtectioncsv() {
        String csvFilePath = "/Users/dadigom/PublicDataAPI/ChildrenProtectionZone.csv"; // CSV 파일 경로

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFilePath), "EUC-KR"))) {
            csvReader.readNext(); // 첫 번째 행을 건너뜁니다.
            List<ChildrenProtectionDTO> childrenProtectionDTOs = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {

                String name = line[1];
                String address = line[2];

                ChildrenProtectionDTO childrenProtectionDTO = new ChildrenProtectionDTO();
                childrenProtectionDTO.setName(name);
                String[] coordinates = naverGeocoding.convertAddressToCoordinates(address);

                if (coordinates == null) {
                    childrenProtectionDTO.setLatitude(null);
                    childrenProtectionDTO.setLongitude(null);
                    childrenProtectionDTOs.add(childrenProtectionDTO);
                    continue;
                }

                childrenProtectionDTO.setLatitude(coordinates[0]);
                childrenProtectionDTO.setLongitude(coordinates[1]);

                childrenProtectionDTOs.add(childrenProtectionDTO);
            }
            repositoryService.saveAllChildrenProtectRepository(childrenProtectionDTOs);
        } catch (IOException | CsvValidationException e) {
            return e.getMessage();
        }

        return "Success";
    }
}
