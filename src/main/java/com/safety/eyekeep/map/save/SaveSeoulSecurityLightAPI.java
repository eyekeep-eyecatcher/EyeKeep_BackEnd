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
public class SaveSeoulSecurityLightAPI {

    @Value("${PublicDataAPI.SERVICE_KEY}")
    private String serviceKey;

    private final RepositoryService repositoryService;

    public String saveSeoulSecurityLight() throws IOException {
        List<String[]> District = new ArrayList<>();

        District.add(new String[]{"서울특별시 강남구청", "10210"});
        District.add(new String[]{"서울특별시 강동구청", "7534"});
        District.add(new String[]{"서울특별시 강북구청", "9338"});
        District.add(new String[]{"서울특별시 강서구청", "8942"});
        District.add(new String[]{"서울특별시 관악구청", "12931"});
        District.add(new String[]{"서울특별시 광진구청", "9229"});
        District.add(new String[]{"서울특별시 구로구청", "7122"});
        District.add(new String[]{"서울특별시 금천구청", "6043"});
        District.add(new String[]{"서울특별시 노원구청", "5870"});
        // District.add(new String[]{"도봉구청", "5918"}); // API 데이터가 csv 파일 데이터보다 많이 작음.
        // District.add(new String[]{"서울특별시 동작구청", "9199"}); // 좌표 값이 null인 것이 많음.
        District.add(new String[]{"서울특별시 마포구청", "5985"});
        // District.add(new String[]{"서울특별시 서대문구청", "9262"}); // API 데이터가 csv 파일 데이터보다 많이 작음.
        District.add(new String[]{"서울특별시 서초구청", "11071"});
        // District.add(new String[]{"서울특별시 성동구청", ""}); // 자료가 부정확함.
        District.add(new String[]{"서울특별시 성북구청", "12190"});
        // District.add(new String[]{"서울특별시 송파구청", "5578"}); // 좌표 값이 null인 것이 많음.
        District.add(new String[]{"서울특별시 양천구청", "7350"});
        District.add(new String[]{"서울특별시 영등포구", "8535"});
        // District.add(new String[]{"서울특별시 용산구청", "1068"}); // 자료가 부정확함.
        // District.add(new String[]{"서울특별시 은평구청", "9917"}); // 좌표 값이 null인 것이 많음.
        District.add(new String[]{"서울특별시 종로구청 도로과", "9699"});
        District.add(new String[]{"서울특별시 중구청 도로시설과", "5874"});
        District.add(new String[]{"서울특별시 중랑구청", "8914"});
        // 주석 처리한 구는 csv 파일을 통해 저장.



        // 리스트 출력
        for (String[] district : District) {
            List<SecurityLightDTO> securityLightDTOs = new ArrayList<>();
            for(int pageNo = 1; pageNo <= Integer.parseInt(district[1])/1000 + 1; pageNo++) {
                /*URL*/
                String urlBuilder = "http://api.data.go.kr/openapi/tn_pubr_public_scrty_lmp_api" + "?"
                        + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8) + /*Service Key*/
                        "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(pageNo), StandardCharsets.UTF_8) + /*페이지 번호*/
                        "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                        "&" + URLEncoder.encode("type", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) + /*한 페이지 결과 수*/
                        "&" + URLEncoder.encode("INSTITUTION_NM", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(district[0], StandardCharsets.UTF_8);
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
                JsonObject responseObject = jsonObject.getAsJsonObject("response");
                JsonObject headerObject = responseObject.getAsJsonObject("header");

                // resultCode 추출
                String resultCode = headerObject.get("resultCode").getAsString();
                if(!resultCode.equals("00")) {
                    return headerObject.get("resultMsg").getAsString();
                }
                JsonObject bodyObject;
                try{
                    bodyObject = responseObject.getAsJsonObject("body");
                } catch(NullPointerException e){
                    return e.getMessage();
                }
                JsonArray itemsArray = bodyObject.getAsJsonArray("items");



                // 데이터를 추출하여 리스트에 추가
                for (int i = 0; i < itemsArray.size(); i++) {
                    JsonObject dataObject = itemsArray.get(i).getAsJsonObject();
                    String address;
                    if (!Objects.equals(dataObject.get("lnmadr").getAsString(), "")) {
                        address = dataObject.get("lnmadr").getAsString();
                    } else {
                        address = dataObject.get("rdnmadr").getAsString();
                    }
                    String latitude = dataObject.get("latitude").getAsString();
                    String longitude = dataObject.get("longitude").getAsString();

                    // 중복 좌표 저장 x.
                    boolean flag = false;
                    for (SecurityLightDTO securityLightDTO : securityLightDTOs) {
                        if (securityLightDTO.getLatitude().equals(latitude) && securityLightDTO.getLongitude().equals(longitude)) {
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


                    SecurityLightDTO securityLightDTO = new SecurityLightDTO();
                    securityLightDTO.setAddress(address);
                    securityLightDTO.setLatitude(latitude);
                    securityLightDTO.setLongitude(longitude);

                    securityLightDTOs.add(securityLightDTO);

                }
            }
            repositoryService.saveAllSecurityLightRepository(securityLightDTOs);
        }


        return "Success";
    }
}
