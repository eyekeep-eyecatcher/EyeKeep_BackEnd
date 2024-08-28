package com.safety.eyekeep.map.save;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.safety.eyekeep.map.dto.ChildrenGuardHouseDTO;
import com.safety.eyekeep.map.service.CoordinateTransform;
import com.safety.eyekeep.map.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveChildrenGuardHouseAPI {
    @Value("${OpenAPI.SERVICE_KEY}")
    private String serviceKey;

    private final RepositoryService repositoryService;

    public String saveChildGuardHouse() throws IOException {
        for(int pageNo = 1; pageNo < 11; pageNo++) {
            /*URL*/
            String urlBuilder = "https://safemap.go.kr/openApiService/data/getChildrenguardhouseData.do" + "?"
                    + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                    "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                    "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(pageNo), StandardCharsets.UTF_8) + /*페이지 번호*/
                    "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*데이터 타입*/
                    "&" + URLEncoder.encode("Fclty_Cd", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("504040", StandardCharsets.UTF_8);
            URL url = new URL(urlBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
            BufferedReader rd;
            if(conn.getResponseCode() == 200) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else{
                log.error(String.valueOf(conn.getResponseCode()));
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
            JsonObject responseBody = jsonObject.getAsJsonObject("response").getAsJsonObject("body");
            JsonArray itemsArray = responseBody.getAsJsonArray("items");


            // 데이터를 추출하여 리스트에 추가
            List<ChildrenGuardHouseDTO> childrenGuardHouseDTOs = new ArrayList<>();
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                String cityCode = itemObject.get("CTPRVN_CD").getAsString();
                if(!Objects.equals(cityCode, "11")) {
                    continue;
                }

                String name = itemObject.get("FCLTY_NM").getAsString();
                double x = itemObject.get("X").getAsDouble();
                double y = itemObject.get("Y").getAsDouble();

                String[] coordinate = CoordinateTransform.fromEPSG3857ToEPSG4326(x, y);
                String longitude = coordinate[0];
                String latitude = coordinate[1];

                // 중복 좌표 저장 x.
                boolean flag = false;
                for(ChildrenGuardHouseDTO childrenGuardHouseDTO : childrenGuardHouseDTOs){
                    if(childrenGuardHouseDTO.getLatitude().equals(latitude) && childrenGuardHouseDTO.getLongitude().equals(longitude)){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    continue;
                }


                ChildrenGuardHouseDTO childrenGuardHouseDTO = new ChildrenGuardHouseDTO();
                childrenGuardHouseDTO.setName(name);
                childrenGuardHouseDTO.setLatitude(latitude);
                childrenGuardHouseDTO.setLongitude(longitude);

                childrenGuardHouseDTOs.add(childrenGuardHouseDTO);
            }
            repositoryService.saveAllChildrenGuardHouseRepository(childrenGuardHouseDTOs);
        }
        return "Success";
    }
}
