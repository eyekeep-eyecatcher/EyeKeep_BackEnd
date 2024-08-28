package com.safety.eyekeep.map.save;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.safety.eyekeep.map.dto.AccidentBlackSpotDTO;
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
public class SaveAccidentBlackSpotAPI {

    @Value("${PublicDataAPI.SERVICE_KEY}")
    private String serviceKey;

    private final RepositoryService repositoryService;

    public String saveAccidentBlackSpot() throws IOException {
        List<String[]> Accident = new ArrayList<>();

        Accident.add(new String[]{"무단횡단", "2627"});
        Accident.add(new String[]{"보행어린이", "1001"});
        Accident.add(new String[]{"스쿨존어린이", "391"});
        Accident.add(new String[]{"자전거", "4357"});


        // 리스트 출력
        for (String[] accident : Accident) {
            List<AccidentBlackSpotDTO> accidentBlackSpotDTOs = new ArrayList<>();
            for(int pageNo = 1; pageNo <= Integer.parseInt(accident[1])/100 + 1; pageNo++) {
                /*URL*/
                String urlBuilder = "http://api.data.go.kr/openapi/tn_pubr_public_acdnt_area_api" + "?"
                        + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                        "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(pageNo), StandardCharsets.UTF_8) + /*페이지 번호*/
                        "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("100", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                        "&" + URLEncoder.encode("type", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                        "&" + URLEncoder.encode("ACDNT_TYPE_SE", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(accident[0], StandardCharsets.UTF_8); /*사고 타입*/
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
                JsonObject responseBody = jsonObject.getAsJsonObject("response").getAsJsonObject("body");
                JsonArray itemsArray = responseBody.getAsJsonArray("items");

                // 데이터를 추출하여 리스트에 추가
                for (int i = 0; i < itemsArray.size(); i++) {
                    JsonObject dataObject = itemsArray.get(i).getAsJsonObject();
                    String locationCode = dataObject.get("lcCode").getAsString();
                    if (!locationCode.startsWith("11")) {
                        continue;
                    }
                    String accidentType = dataObject.get("acdntTypeSe").getAsString();
                    String location = dataObject.get("acdntAreaLcNm").getAsString();
                    String latitude = dataObject.get("latitude").getAsString();
                    String longitude = dataObject.get("longitude").getAsString();

                    // 중복 좌표 저장 x.
                    boolean flag = false;
                    for (AccidentBlackSpotDTO accidentBlackSpotDTO : accidentBlackSpotDTOs) {
                        if (accidentBlackSpotDTO.getLatitude().equals(latitude) && accidentBlackSpotDTO.getLongitude().equals(longitude)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        continue;
                    }

                    // 위도나 경도가 없을 경우 저장 x.
                    if (Objects.equals(latitude, "") || Objects.equals(longitude, "")) {
                        continue;
                    }


                    AccidentBlackSpotDTO accidentBlackSpotDTO = new AccidentBlackSpotDTO();
                    accidentBlackSpotDTO.setLocation(location);
                    accidentBlackSpotDTO.setLatitude(latitude);
                    accidentBlackSpotDTO.setLongitude(longitude);
                    accidentBlackSpotDTO.setAccidentType(accidentType);

                    accidentBlackSpotDTOs.add(accidentBlackSpotDTO);

                }
            }
            repositoryService.saveAllAccidentBlackSpotRepository(accidentBlackSpotDTOs);
        }


        return "Success";
    }
}
