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

}
