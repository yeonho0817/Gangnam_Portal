package com.gangnam.portal.util.wheaterApi;

import com.gangnam.portal.dto.EtcDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.WeatherDTO;
import com.gangnam.portal.exception.CustomException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherApi {
    private final RestTemplate restTemplate;

    @Value("${spring.etc.weather.apiUri}")
    private  String apiUri;

    @Value("${spring.etc.weather.secretKey}")
    private  String secretKey;

    @Value("${spring.etc.weather.secretKeyDecode}")
    private  String secretKeyDecode;


    private final Integer[] BASE_TIME = {2, 5, 8, 11, 14, 17, 20, 23};
    private final String[] BASE_TIME_STRING = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

    private String baseDate = null;
    private String baseTime = null;
    private final Date today = new Date();

    public List<WeatherDTO.Item> test(EtcDTO.RegionCoordinateDTO regionCoordinate) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH00");

            setBaseDateTime();
            EtcDTO.RegionCodeDTO regionCodeDTO = regionCodeInfo(regionCoordinate);

            String nx = regionCodeDTO.getX().toString();	//위도
            String ny = regionCodeDTO.getY().toString();	//경도
            String pageNo = "1";
            String numberOfRows = "300";
            String type = "json";	//타입 xml, json 등등 ..

            System.out.println(this.baseDate + " " + this.baseTime + " " + timeFormat.format(new Date()));

            UriComponents uri = UriComponentsBuilder
                        .fromHttpUrl(apiUri)
                        .queryParam("ServiceKey", URLEncoder.encode(secretKeyDecode, StandardCharsets.UTF_8))
                        .queryParam("pageNo", URLEncoder.encode(pageNo, StandardCharsets.UTF_8))
                        .queryParam("numOfRows", URLEncoder.encode(numberOfRows, StandardCharsets.UTF_8))
                        .queryParam("nx", URLEncoder.encode(nx, StandardCharsets.UTF_8))
                        .queryParam("ny", URLEncoder.encode(ny, StandardCharsets.UTF_8))
                        .queryParam("base_date", URLEncoder.encode(this.baseDate, StandardCharsets.UTF_8))
                        .queryParam("base_time", URLEncoder.encode(this.baseTime, StandardCharsets.UTF_8))
                        .queryParam("dataType", URLEncoder.encode(type, StandardCharsets.UTF_8))
                    .build();

            System.out.println(uri);

            HttpMessageConverter<String> stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            List<HttpMessageConverter<?>> httpMessageConverter = new ArrayList<>();
            httpMessageConverter.add(stringHttpMessageConverter);
            restTemplate.setMessageConverters(httpMessageConverter);


            HttpHeaders header = new HttpHeaders();
            header.set("content-type", "application/json");
            header.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(header);

            ResponseEntity<WeatherDTO> response = this.restTemplate.exchange(
                    uri.toUriString(),
                    HttpMethod.GET,
                    entity,
                    WeatherDTO.class);

            System.out.println(response);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorStatus.WEATHER_INFO_FAIL);
        }
    }

    private void setBaseDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

        int intBaseDate = Integer.parseInt(dateFormat.format(today));

        int currentHour = Integer.parseInt(hourFormat.format(new Date()));

        if (currentHour <= BASE_TIME[0]) {
            this.baseDate = String.valueOf(--intBaseDate);

            this.baseTime = BASE_TIME_STRING[7];
        } else {
            for (int i=1; i<BASE_TIME.length; i++) {
                if (currentHour <= BASE_TIME[i]) {
                    this.baseDate = dateFormat.format(today);
                    this.baseTime = BASE_TIME_STRING[i-1];
                    break;
                }
            }
            if (this.baseTime == null) this.baseTime = BASE_TIME_STRING[7];
        }
    }


    public EtcDTO.WeatherInfo lookUpWeather(EtcDTO.RegionCodeDTO regionCodeDTO) throws IOException, NullPointerException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH00");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

        setBaseDateTime();

        String nx = regionCodeDTO.getX().toString();	//위도
        String ny = regionCodeDTO.getY().toString();	//경도
        String pageNo = "1";
        String numberOfRows = "300";
        String type = "JSON";	//타입 xml, json 등등 ..

        System.out.println(this.baseDate + " " + this.baseTime + " " + timeFormat.format(new Date()));

        String urlBuilder = apiUri + "?" + URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8) + "=" + secretKey +
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(pageNo, StandardCharsets.UTF_8) + //페이지번호
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(numberOfRows, StandardCharsets.UTF_8) + //한페이지 표시할 갯수
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(nx, StandardCharsets.UTF_8) + //경도
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(ny, StandardCharsets.UTF_8) + //위도
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(this.baseDate, StandardCharsets.UTF_8) + /* 조회하고싶은 날짜*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(this.baseTime, StandardCharsets.UTF_8) + /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(type, StandardCharsets.UTF_8);    /* 타입 */


        URL url = new URL(urlBuilder);
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            throw new IOException();
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result= sb.toString();


        JsonElement element = JsonParser.parseString(result);

        JsonArray jsonArray = (JsonArray) element.getAsJsonObject().get("response")
                .getAsJsonObject().get("body")
                .getAsJsonObject().get("items")
                .getAsJsonObject().get("item");

        JsonElement weather = jsonArray.get(0);

        EtcDTO.WeatherInfo weatherInfo = null;

        for (int i=0; i<jsonArray.size(); i+=12, weather = jsonArray.get(i)) {
            if (weather.getAsJsonObject().get("category").getAsString().equals("TMN") || weather.getAsJsonObject().get("category").getAsString().equals("TMX")) {
                weather = jsonArray.get(++i);
            }

            if (weather.getAsJsonObject().get("fcstTime").getAsString().equals(timeFormat.format(today))) {
                String sky = String.valueOf(jsonArray.get(i+5).getAsJsonObject().get("fcstValue").getAsString());
                String pty = String.valueOf(jsonArray.get(i+6).getAsJsonObject().get("fcstValue").getAsString());

                System.out.println(jsonArray.get(i+5));

                weatherInfo = EtcDTO.WeatherInfo.builder()
                            .date(baseDate.substring(0,4) + "-" + baseDate.substring(4,6) + "-" + baseDate.substring(6,8))
                            .time(baseTime.substring(0, 2))
                            .isNight((Integer.parseInt(hourFormat.format(new Date())) >= 19 && Integer.parseInt(hourFormat.format(new Date())) <= 24) || (Integer.parseInt(hourFormat.format(new Date()))>=0 && Integer.parseInt(hourFormat.format(new Date())) < 6) )
                            .tmp(jsonArray.get(i).getAsJsonObject().get("fcstValue").getAsString())
                            .wsd(jsonArray.get(i+4).getAsJsonObject().get("fcstValue").getAsString())
                            .sky(sky.equals("1") ? "맑음" : sky.equals("2") ? "비" : sky.equals("3") ? "구름 많음" : sky.equals("4") ? "흐림" : null)
                            .pty(pty.equals("0") ? "없음" : pty.equals("1") ? "비" : pty.equals("2") ? "비/눈" : pty.equals("3") ? "눈" : pty.equals("4") ? "소나기" : null)
                            .pcp(jsonArray.get(i+9).getAsJsonObject().get("fcstValue").getAsString())
                            .pop(String.valueOf(jsonArray.get(i+7).getAsJsonObject().get("fcstValue").getAsString()))
                            .sno(String.valueOf(jsonArray.get(i+11).getAsJsonObject().get("fcstValue").getAsString()))
                        .build();

                break;
            }
        }
        return weatherInfo;
    }

    // 위도,경도 -> 기상청 좌표
    public EtcDTO.RegionCodeDTO regionCodeInfo(EtcDTO.RegionCoordinateDTO regionCoordinateDTO)  throws IOException {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)
        double DEGRAD = Math.PI/ 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI* 0.25 + slat2 * 0.5) / Math.tan(Math.PI* 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI* 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI* 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        EtcDTO.RegionCodeDTO rs = new EtcDTO.RegionCodeDTO();

        double ra = Math.tan(Math.PI* 0.25 + (regionCoordinateDTO.getLatitude()) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = regionCoordinateDTO.getLongitude() * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        rs.setX((int) Math.floor(ra * Math.sin(theta) + XO + 0.5));
        rs.setY((int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));

        return rs;
    }
}
