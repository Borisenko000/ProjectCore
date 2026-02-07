package edu.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.module.UserRequestDto;
import edu.spring.module.User;
import edu.spring.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUser_ReturnsUser_WhenFound() throws Exception {
        User user = new User("Polina", "123456");
        when(userService.getUserByEmail("Polina")).thenReturn(user);
        mockMvc.perform(get("/api/v1/user/Polina")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("Polina"));
    }

    @Test
    void postUser_WithEmptyPassword() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("Polina", " ", " ");
        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postUser_WithShortPassword() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("Polina", "123", "123");
        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
