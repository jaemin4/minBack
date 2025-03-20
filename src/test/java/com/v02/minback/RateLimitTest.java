package com.v02.minback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest(classes = MinBackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void testRateLimiting() {
        int maxRequests = 10;
        String url = "http://localhost:" + port + "/user/bank/deposit";

        // 요청 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accountNumber", 900000006);
        requestBody.put("balance", 1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        for (int i = 0; i < maxRequests; i++) {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        // 11번째 요청에서 429 응답 확인
        try {
            restTemplate.postForEntity(url, requestEntity, String.class);
            fail("Expected TooManyRequests (429) but request was successful.");
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        }
    }
}
