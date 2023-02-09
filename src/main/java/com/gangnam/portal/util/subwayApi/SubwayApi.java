package com.gangnam.portal.util.subwayApi;

import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.SubwayDTO;
import com.gangnam.portal.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SubwayApi {
    @Autowired
    private final RestTemplate restTemplate;

    @Value("${spring.etc.subway.apiUri}")
    private String apiUri;

    @Value("${spring.etc.subway.secretKey}")
    private String secretKey;


    public List<SubwayDTO.Realtimearrivallist> subwayInfo() {
        try {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(apiUri +
                            "/" + secretKey +
                            "/json" +
                            "/realtimeStationArrival" +
                            "/0" +
                            "/7" +
                            "/강남" )
                    .build();

            System.out.println(uri.toUriString());

            ResponseEntity<SubwayDTO> response = restTemplate.exchange(
                    uri.toUriString(),
                    HttpMethod.GET,
                    null,
                    SubwayDTO.class);

            return Objects.requireNonNull(response.getBody()).getRealtimearrivallist();
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.SUBWAY_INFO_FAIL);
        }
    }


//    public List<EtcDTO.SubwayInfoData> subwayInfo() throws IOException {
//        /*URL*/
//        String urlBuilder = apiUri +
//                "/" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8) + /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
//                "/" + URLEncoder.encode("json", StandardCharsets.UTF_8) + /*요청파일타입 (xml,xmlf,xls,json) */
//                "/" + URLEncoder.encode("realtimeStationArrival", StandardCharsets.UTF_8) + /*서비스명 (대소문자 구분 필수입니다.)*/
//                "/" + URLEncoder.encode("0", StandardCharsets.UTF_8) + /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
//                "/" + URLEncoder.encode("7", StandardCharsets.UTF_8) + /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
//                // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.
//
//                // 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.
//                "/" + URLEncoder.encode("강남", StandardCharsets.UTF_8); /* 서비스별 추가 요청인자들*/
//
//        URL url = new URL(urlBuilder);
//        System.out.println(url.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
//        BufferedReader rd;
//
//        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            throw new IOException();
//        }
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//
//        String result = sb.toString();
//
//        System.out.println(result);
//
//        JsonParser parser = new JsonParser();
//        JsonElement element = parser.parse(result);
//
//        JsonArray jsonArray = (JsonArray) element.getAsJsonObject().get("realtimeArrivalList");
//
//        List<EtcDTO.SubwayInfoData> subwayInfoList = new ArrayList<>();
//
//        for (int i=0; i<jsonArray.size(); i++) {
//            String direction = jsonArray.get(i).getAsJsonObject().get("trainLineNm").getAsString().split(" - ")[1];
//
//            String currentPosition = jsonArray.get(i).getAsJsonObject().get("arvlMsg3").getAsString();
//            String time = jsonArray.get(i).getAsJsonObject().get("arvlMsg2").getAsString();
//
//            if (time.contains("번째 전역")) time = currentPosition + "(" + time.split("]번째 전역")[0].substring(1) + " 전)";
//
//            EtcDTO.SubwayInfoData subwayInfo = EtcDTO.SubwayInfoData.builder()
//                        .direction(direction)
//                        .time(time)
//                        .currentPosition(currentPosition)
//                    .build();
//
//            subwayInfoList.add(subwayInfo);
//        }
//
//        subwayInfoList.sort(Comparator.comparing(EtcDTO.SubwayInfoData::getDirection));
//
//        return subwayInfoList;
//    }
}