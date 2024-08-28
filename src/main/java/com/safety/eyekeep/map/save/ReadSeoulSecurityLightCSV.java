package com.safety.eyekeep.map.save;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.safety.eyekeep.map.dto.SecurityLightDTO;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ReadSeoulSecurityLightCSV {

    private final RepositoryService repositoryService;

    public String saveSeoulSecurityLightCSV() {
        String csvFilePath = "/Users/dadigom/PublicDataAPI/SeoulSecurityLight.csv"; // CSV 파일 경로

        Set<String> validLocations = new HashSet<>();
        validLocations.add("도봉구청");
        validLocations.add("서울특별시 서대문구청");
        validLocations.add("서울특별시 용산구청");
        validLocations.add("서울특별시 은평구청");
        validLocations.add("서울특별시 성동구청");
        validLocations.add("서울특별시 동작구청");

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<SecurityLightDTO> securityLightDTOs = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if(!validLocations.contains(line[10])){
                    continue;
                }
                String address; // 주소
                if(!line[4].isEmpty()){
                    address = line[4];
                }
                else{
                    address = line[3];
                }
                String latitude = line[5]; // 위도
                String longitude = line[6]; // 경도

                // 중복 좌표 저장 x.
                boolean flag = false;
                for(SecurityLightDTO securityLightDTO : securityLightDTOs){
                    if(securityLightDTO.getLatitude().equals(latitude) && securityLightDTO.getLongitude().equals(longitude)){
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


                SecurityLightDTO securityLightDTO = new SecurityLightDTO();
                securityLightDTO.setAddress(address);
                securityLightDTO.setLatitude(latitude);
                securityLightDTO.setLongitude(longitude);

                securityLightDTOs.add(securityLightDTO);
            }
            repositoryService.saveAllSecurityLightRepository(securityLightDTOs);
        } catch (IOException | CsvValidationException e) {
            return e.getMessage();
        }

        return "Success";
    }
}
