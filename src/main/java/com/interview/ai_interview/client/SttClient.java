package com.interview.ai_interview.client;

import com.interview.ai_interview.exceptions.SttServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import java.io.File;
import java.util.Map;

@Service
@Slf4j
public class SttClient {

    @Value("${stt.url}")
    private String sttUrl;

    private final RestTemplate restTemplate;

    public SttClient() {
        this.restTemplate = new RestTemplate();
    }

    public String transcribe(File audioFile) {

        try {
            log.info("Sending audio to STT service...");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            sttUrl,
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );

            // ===============================
            // Handle non 2xx response
            // ===============================
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new SttServiceException(
                        "STT responded with status: " + response.getStatusCode()
                );
            }

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("text")) {
                throw new SttServiceException("Invalid STT response format");
            }

            String transcript = responseBody.get("text").toString();

            log.info("STT transcription success");

            return transcript;

        } catch (HttpClientErrorException.Forbidden e) {
            throw new SttServiceException("STT service forbidden (invalid API key or auth)");

        } catch (HttpClientErrorException.BadRequest e) {
            throw new SttServiceException("Bad request to STT service");

        } catch (ResourceAccessException e) {
            throw new SttServiceException("STT service unreachable or timeout");

        } catch (RestClientException e) {
            throw new SttServiceException("STT service communication error");

        } catch (Exception e) {
            throw new SttServiceException("Unexpected STT error: " + e.getMessage());
        }
    }
}