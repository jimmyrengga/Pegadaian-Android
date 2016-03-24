package com.pegadaian.aplikasi.cobalagi.service;

import com.pegadaian.aplikasi.cobalagi.domain.Cabang;
import com.pegadaian.aplikasi.cobalagi.domain.CekHarga;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by opaw on 3/18/16.
 */
public class HttpService {
    private static String HOST = "http://192.168.1.170:8080/pegadaian/";
    private static String BASE_URI =  HOST + "/api/";
    private RestTemplate restTemplate = new RestTemplate();
    private static String TAG = "HTTP SERVICE";

    public HttpService(){
        ((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(60 * 1000);

        CloseableHttpClient client = HttpUtils.getNewHttpClient();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    private HttpEntity buildHttpEntityRequest(String cookies) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookies);
        return new HttpEntity(headers);
    }

    public ResponseEntity login (String username, String password){
        String url = HOST + "/login";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);

        return restTemplate.postForEntity(url, formData, String.class);
    }

    public ResponseEntity cekHarga (String cookie){
        String url = BASE_URI + "cekharga";
        HttpEntity entity = buildHttpEntityRequest(cookie);
        return restTemplate.exchange(url, HttpMethod.GET, entity, CekHarga.class);
    }

    public ResponseEntity cariCabang (String cookie){
        String url = BASE_URI + "cabang";
        HttpEntity entity = buildHttpEntityRequest(cookie);
//        return restTemplate.exchange(url, HttpMethod.GET, entity, Cabang.class);
        return restTemplate.getForEntity(url, Cabang[].class);
    }
}
