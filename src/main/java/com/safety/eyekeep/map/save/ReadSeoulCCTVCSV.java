package com.safety.eyekeep.map.save;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.safety.eyekeep.map.dto.CCTVDTO;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ReadSeoulCCTVCSV {
    private final RepositoryService repositoryService;

    public String saveSeoulCCTVcsv() {
        String csvFilePath = "/Users/dadigom/PublicDataAPI/SeoulCCTV.csv"; // CSV 파일 경로

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<CCTVDTO> cctvDTOs = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String address; // 주소
                if(!line[2].isEmpty()){
                    address = line[2];
                }
                else{
                    address = line[9];
                }
                String latitude = line[4]; // 위도
                String longitude = line[5]; // 경도

                // 중복 좌표 저장 x.
                boolean flag = false;
                for(CCTVDTO cctvDTO : cctvDTOs){
                    if(cctvDTO.getLatitude().equals(latitude) && cctvDTO.getLongitude().equals(longitude)){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    continue;
                }

                // 위도나 경도가 없을 경우 저장 x.
                if(Objects.equals(latitude, "") || Objects.equals(longitude, "")){
                    continue;
                }

                CCTVDTO cctvDTO = new CCTVDTO();
                cctvDTO.setAddress(address);
                cctvDTO.setLatitude(latitude);
                cctvDTO.setLongitude(longitude);

                cctvDTOs.add(cctvDTO);
            }
            repositoryService.saveAllCCTVRepository(cctvDTOs);
        } catch (IOException | CsvValidationException e) {
            return e.getMessage();
        }

        return "Success";
    }
}
