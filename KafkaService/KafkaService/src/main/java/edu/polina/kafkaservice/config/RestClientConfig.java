package edu.polina.kafkaservice.config;

import edu.polina.kafkaservice.external.WireMockWelcomeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${external.wire-mock.welcome-url}")
    private String BASE_URL;

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }


}
