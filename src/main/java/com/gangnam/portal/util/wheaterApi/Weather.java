package com.gangnam.portal.util.wheaterApi;

import com.gangnam.portal.dto.EtcDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class Weather {
    private final Integer[] BASE_TIME = {2, 5, 8, 11, 14, 17, 20, 23};
    private final String[] BASE_TIME_STRING = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

    @Value("${spring.etc.weather.apiUri}")
    private  String apiUri;

    @Value("${spring.etc.weather.secretKey}")
    private  String secretKey;

    public EtcDTO.WeatherInfo lookUpWeather(EtcDTO.RegionCodeDTO regionCodeDTO) throws IOException, NullPointerException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH00");

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

        Date today = new Date();

        String nx = regionCodeDTO.getX().toString();	//위도
        String ny = regionCodeDTO.getY().toString();	//경도
        String pageNo = "1";
        String numberOfRows = "300";
        String type = "JSON";	//타입 xml, json 등등 ..

        Integer baseDate = Integer.parseInt(dateFormat.format(today));
//        Integer baseDate = 20230130;
        String baseTime = null;

        Integer currentHour = Integer.parseInt(hourFormat.format(new Date()));
//        Integer currentHour = 0;

        if (currentHour <= BASE_TIME[0]) {
            baseDate--;
            baseTime = BASE_TIME_STRING[7];
        } else {
            for (int i=1; i<BASE_TIME.length; i++) {
                if (currentHour <= BASE_TIME[i]) {
                    baseTime = BASE_TIME_STRING[i-1];
                    break;
                }
            }

            if (baseTime == null) baseTime = BASE_TIME_STRING[7];
        }

        System.out.println(nx + " " + ny + " " + baseDate + " " + baseTime + " " + timeFormat.format(new Date()));

        String urlBuilder = apiUri + "?" + URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8) + "=" + secretKey +
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(pageNo, StandardCharsets.UTF_8) + //페이지번호
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(numberOfRows, StandardCharsets.UTF_8) + //한페이지 표시할 갯수
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(nx, StandardCharsets.UTF_8) + //경도
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(ny, StandardCharsets.UTF_8) + //위도
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(baseDate), StandardCharsets.UTF_8) + /* 조회하고싶은 날짜*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(baseTime, StandardCharsets.UTF_8) + /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(type, StandardCharsets.UTF_8);    /* 타입 */

        URL url = new URL(urlBuilder);
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

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

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

                weatherInfo = EtcDTO.WeatherInfo.builder()
                            .date(baseDate.toString().substring(0,4) + "-" + baseDate.toString().substring(4,6) + "-" + baseDate.toString().substring(6,8))
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
