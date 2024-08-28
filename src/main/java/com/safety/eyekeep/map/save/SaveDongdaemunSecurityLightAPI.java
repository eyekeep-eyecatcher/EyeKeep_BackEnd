package com.safety.eyekeep.map.save;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.safety.eyekeep.map.dto.SecurityLightDTO;
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
public class SaveDongdaemunSecurityLightAPI {

    @Value("${PublicDataAPI.SERVICE_KEY}")
    private String serviceKey;

    private final RepositoryService repositoryService;

    public String saveDongdaemunSecurityLight() throws IOException {
        /*URL*/
        String urlBuilder = "https://api.odcloud.kr/api/15128084/v1/uddi:6af7ce5f-4a0a-41ce-a0eb-fa3653c61737" + "?"
                    + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                "&" + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*페이지 번호*/
                "&" + URLEncoder.encode("perPage", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("3175", StandardCharsets.UTF_8); /*한 페이지 결과 수*/
        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
        BufferedReader rd;
        if(conn.getResponseCode() == 200) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else{
            return "Http request failed";
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
        JsonArray dataArray = jsonObject.getAsJsonArray("data");


        // 데이터를 추출하여 리스트에 추가
        List<SecurityLightDTO> securityLightDTOs = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JsonObject dataObject = dataArray.get(i).getAsJsonObject();
            String address = "서울특별시" + " " + dataObject.get("구").getAsString() + " " + dataObject.get("동").getAsString() + " " + dataObject.get("설치주소").getAsString();
            String latitude = dataObject.get("위도").getAsString();
            String longitude = dataObject.get("경도").getAsString();

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

        return "Success";
    }
}
