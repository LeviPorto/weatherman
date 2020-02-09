package com.levi.weatherman.client;

import com.levi.weatherman.config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;

@Component
public class NasaRestTemplate {

    private final String NASA_BASE_URL = "https://api.nasa.gov/";

    private final String API_KEY_PARAMETER = "api_key";
    private final String FEED_TYPE_PARAMETER = "feedtype";
    private final String VER_PARAMETER = "api_key";

    @Value("${nasa-api.api-version}")
    public String VER_PARAMETER_VALUE;

    @Value("${nasa-api.api-feed-type}")
    public String FEED_TYPE_PARAMETER_VALUE;

    @Value("${nasa-api.api-key}")
    public String API_KEY_PARAMETER_VALUE;

    public static final String INSIGHT_WEATHER_URL = "insight_weather/";

    private RestTemplate restTemplate;
    private RestTemplateConfig restTemplateConfig;

    public NasaRestTemplate(RestTemplate restTemplate, RestTemplateConfig restTemplateConfig) {
        this.restTemplate = restTemplate;
        this.restTemplateConfig = restTemplateConfig;
    }

    public UriComponentsBuilder constructNasaBaseURL(String URL) {
        return UriComponentsBuilder.fromHttpUrl(NASA_BASE_URL + URL)
                .queryParam(API_KEY_PARAMETER, API_KEY_PARAMETER_VALUE)
                .queryParam(FEED_TYPE_PARAMETER, FEED_TYPE_PARAMETER_VALUE)
                .queryParam(VER_PARAMETER, VER_PARAMETER_VALUE);
    }

    @SuppressWarnings("rawtypes")
    public HttpEntity<LinkedHashMap> getSolsInformationFromNasa() {
        return restTemplate.exchange(constructNasaBaseURL(INSIGHT_WEATHER_URL).toUriString()
                , HttpMethod.GET, restTemplateConfig.getDefaultHttpEntity(), LinkedHashMap.class);
    }

}
