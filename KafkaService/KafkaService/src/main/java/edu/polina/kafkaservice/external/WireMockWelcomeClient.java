package edu.polina.kafkaservice.external;

import edu.polina.kafkaservice.api.WelcomeRequestDto;
import edu.polina.kafkaservice.api.WelcomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@RequiredArgsConstructor
public  class WireMockWelcomeClient {

    private final RestClient restClient;

    public WelcomeResponseDto createWelcomeResponse(WelcomeRequestDto requestDto) {
        return restClient.post()
                .uri("/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .body(WelcomeResponseDto.class);
    }
}
