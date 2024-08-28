package com.safety.eyekeep.map.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class NaverGeocoding {
    @Value("${NCP.ID}")
    private String ID;
    @Value("${NCP.KEY}")
    private String KEY;

    private static final Logger logger = LoggerFactory.getLogger(NaverGeocoding.class);

    public String[] convertAddressToCoordinates(String address) throws IOException {
        /*URL*/
        String urlBuilder = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode" + "?"
                + URLEncoder.encode("query", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + /*주소*/
                "&" + URLEncoder.encode("count", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8); /*결과 수*/
        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
        // X-Client_Naver 헤더 추가
        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ID);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", KEY);
        BufferedReader rd;
        if(conn.getResponseCode() == 200) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else{
            logger.error("HTTP Error: " + conn.getResponseCode());
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
        String status = jsonObject.getAsJsonPrimitive("status").getAsString();
        if(!Objects.equals(status, "OK")) {
            logger.error("API Status Error: " + status);
            return null;
        }
        JsonArray addressArray = (JsonArray) jsonObject.get("addresses");

        if(addressArray.size() <= 0) {
            logger.error("Addresses array is empty or null.");
            return null;
        }
        // 첫 번째 주소 객체 가져오기
        JsonObject addressObject = (JsonObject) addressArray.get(0);


        // "x"와 "y" 값 추출
        String longitude = addressObject.get("x").getAsString();
        String latitude = addressObject.get("y").getAsString();

        return new String[]{latitude, longitude};
    }

}
