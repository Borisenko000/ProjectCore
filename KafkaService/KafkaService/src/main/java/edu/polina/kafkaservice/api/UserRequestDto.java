package edu.polina.kafkaservice.api;


public record UserRequestDto(
        Long id,
        String name,
        String password
) {
}
