package com.gangnam.portal.util.holidayApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gangnam.portal.dto.EtcDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HolidayApi {

    private HashMap<String, Object> result = new HashMap<String, Object>();
    private String jsonInString = "";
    @Value("${spring.etc.holiday.apiUri}")
    private String apiUri;
    @Value("${spring.etc.holiday.secretKey}")
    private String secretKey;
    @Value("${spring.etc.holiday.secretKeyDecoding}")
    private String secretKeyDecoding;

    private final Integer NUMBER_OF_ROWS = 100;


    public List<EtcDTO.HolidayData> holidayInfo(Integer startYear) throws Exception {
        List<List<EtcDTO.HolidayData>> holidayList = new ArrayList<>();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); //타임아웃 설정 5초
        factory.setReadTimeout(5000);//타임아웃 설정 5초
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders header = new HttpHeaders();
        header.set("content-type", "application/json");
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                    .queryParam("serviceKey", secretKeyDecoding)
                    .queryParam("_type", "json")
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", NUMBER_OF_ROWS)
                    .queryParam("solYear", startYear)
                .build();

        //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
        ResponseEntity<Map> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

        ObjectMapper mapper = new ObjectMapper();
        jsonInString = mapper.writeValueAsString(response.getBody());

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonInString);

        JsonArray jsonArray = (JsonArray) element
                .getAsJsonObject().get("response")
                .getAsJsonObject().get("body")
                .getAsJsonObject().get("items")
                .getAsJsonObject().get("item");

        List<EtcDTO.HolidayData> holidayDataList = new ArrayList<>();

        for (int i=0; i<jsonArray.size(); i++) {
            JsonElement jsonHolidayData = jsonArray.get(i);

            String date = jsonHolidayData.getAsJsonObject().get("locdate").getAsString().substring(0, 4) + "-" +
                    jsonHolidayData.getAsJsonObject().get("locdate").getAsString().substring(4, 6) + "-" +
                    jsonHolidayData.getAsJsonObject().get("locdate").getAsString().substring(6, 8);

            EtcDTO.HolidayData holidayData = EtcDTO.HolidayData.builder()
                        .isHoliday(jsonHolidayData.getAsJsonObject().get("isHoliday").getAsString().equals("Y"))
                        .holidayDate(date)
                        .dateName(jsonHolidayData.getAsJsonObject().get("dateName").getAsString())

                    .build();

            holidayDataList.add(holidayData);
        }

        return holidayDataList;
    }

}
