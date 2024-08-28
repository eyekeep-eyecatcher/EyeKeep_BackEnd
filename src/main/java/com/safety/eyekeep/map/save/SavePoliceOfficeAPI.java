package com.safety.eyekeep.map.save;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.safety.eyekeep.map.dto.PoliceOfficeDTO;
import com.safety.eyekeep.map.service.NaverGeocoding;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SavePoliceOfficeAPI {

    @Value("${PublicDataAPI.SERVICE_KEY}")
    private String serviceKey;

    private final RepositoryService repositoryService;
    private final NaverGeocoding naverGeocoding;

    public String savePoliceOffice() throws IOException {

        // 치안센터 저장.
        /*URL*/
        String urlBuilder_1 = "https://api.odcloud.kr/api/15076962/v1/uddi:496cadf8-cb37-478a-81b4-efbbe881c819" + "?"
                + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                "&" + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지 번호*/
                "&" + URLEncoder.encode("perPage", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("853", StandardCharsets.UTF_8); /*한 페이지 결과 수*/
        JsonArray dataArray_1 = getJsonElements(urlBuilder_1);
        if (dataArray_1 == null) return "Http request failed";


        // 데이터를 추출하여 리스트에 추가
        List<PoliceOfficeDTO> policeOfficeDTOs_1 = new ArrayList<>();
        for (int i = 0; i < dataArray_1.size(); i++) {
            JsonObject dataObject = dataArray_1.get(i).getAsJsonObject();
            // 서울 지역만 수집.
            if(!Objects.equals(dataObject.get("시도청").getAsString(), "서울청")){
                continue;
            }
            String officeName = dataObject.get("치안센터명").getAsString();

            PoliceOfficeDTO policeOfficeDTO = new PoliceOfficeDTO();
            policeOfficeDTO.setOfficeName(officeName);

            String address = dataObject.get("주소").getAsString();
            String[] coordinates = naverGeocoding.convertAddressToCoordinates(address);
            if (coordinates == null) {
                policeOfficeDTO.setLatitude(null);
                policeOfficeDTO.setLongitude(null);
                policeOfficeDTOs_1.add(policeOfficeDTO);
                continue;
            }

            policeOfficeDTO.setLatitude(coordinates[0]);
            policeOfficeDTO.setLongitude(coordinates[1]);

            policeOfficeDTOs_1.add(policeOfficeDTO);
        }
        repositoryService.saveAllPoliceOfficeRepository(policeOfficeDTOs_1);

        // 지구대, 파출소 저장.
        String urlBuilder_2 = "https://api.odcloud.kr/api/15054711/v1/uddi:9097ad1f-3471-42c6-a390-d85b5121816a" + "?"
                + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                "&" + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지 번호*/
                "&" + URLEncoder.encode("perPage", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("2051", StandardCharsets.UTF_8); /*한 페이지 결과 수*/
        JsonArray dataArray_2 = getJsonElements(urlBuilder_2);
        if (dataArray_2 == null) return "Http request failed";


        // 데이터를 추출하여 리스트에 추가
        List<PoliceOfficeDTO> policeOfficeDTOs_2 = new ArrayList<>();
        for (int i = 0; i < dataArray_2.size(); i++) {
            JsonObject dataObject = dataArray_2.get(i).getAsJsonObject();
            // 서울 지역만 수집.
            if(!Objects.equals(dataObject.get("시도청").getAsString(), "서울청")){
                continue;
            }
            String officeName = dataObject.get("관서명").getAsString() + " " + dataObject.get("구분").getAsString();
            PoliceOfficeDTO policeOfficeDTO = new PoliceOfficeDTO();
            policeOfficeDTO.setOfficeName(officeName);

            String address = dataObject.get("주소").getAsString();
            String[] coordinates = naverGeocoding.convertAddressToCoordinates(address);
            if (coordinates == null) {
                policeOfficeDTO.setLatitude(null);
                policeOfficeDTO.setLongitude(null);
                policeOfficeDTOs_2.add(policeOfficeDTO);
                continue;
            }

            policeOfficeDTO.setLatitude(coordinates[0]);
            policeOfficeDTO.setLongitude(coordinates[1]);

            policeOfficeDTOs_2.add(policeOfficeDTO);
        }
        repositoryService.saveAllPoliceOfficeRepository(policeOfficeDTOs_2);

        // 경찰서 저장.
        String urlBuilder_3 = "https://api.odcloud.kr/api/15124966/v1/uddi:345a2432-5fee-4c49-a353-80b62496a43b" + "?"
                + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                "&" + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지 번호*/
                "&" + URLEncoder.encode("perPage", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("259", StandardCharsets.UTF_8); /*한 페이지 결과 수*/
        JsonArray dataArray_3 = getJsonElements(urlBuilder_3);
        if (dataArray_3 == null) return "Http request failed";


        // 데이터를 추출하여 리스트에 추가
        List<PoliceOfficeDTO> policeOfficeDTOs_3 = new ArrayList<>();
        for (int i = 0; i < dataArray_3.size(); i++) {
            JsonObject dataObject = dataArray_3.get(i).getAsJsonObject();
            // 서울 지역만 수집.
            if(!Objects.equals(dataObject.get("시도경찰청").getAsString(), "서울특별시경찰청")){
                continue;
            }
            String officeName = dataObject.get("경찰서명칭").getAsString();
            PoliceOfficeDTO policeOfficeDTO = new PoliceOfficeDTO();
            policeOfficeDTO.setOfficeName(officeName);

            String address = dataObject.get("경찰서주소").getAsString();
            String[] coordinates = naverGeocoding.convertAddressToCoordinates(address);
            if (coordinates == null) {
                policeOfficeDTO.setLatitude(null);
                policeOfficeDTO.setLongitude(null);
                policeOfficeDTOs_3.add(policeOfficeDTO);
                continue;
            }

            policeOfficeDTO.setLatitude(coordinates[0]);
            policeOfficeDTO.setLongitude(coordinates[1]);

            policeOfficeDTOs_3.add(policeOfficeDTO);
        }
        repositoryService.saveAllPoliceOfficeRepository(policeOfficeDTOs_3);

        return "Success";
    }

    private static JsonArray getJsonElements(String urlBuilder) throws IOException {
        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
        BufferedReader rd;
        if(conn.getResponseCode() == 200) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else{
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // JSON 파싱
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        return jsonObject.getAsJsonArray("data");
    }
}
