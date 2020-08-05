package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup(){
        UserController.users.clear();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("Alibaba", "female", 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1, UserController.users.size());
    }

    @Test
    void nameShouldNotNull() throws Exception {
        User user = new User(null, "female", 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nameShouldNotEmpty() throws Exception {
        User user = new User("", "female", 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nameShouldNotLongerThan8() throws Exception {
        User user = new User("Alibababa", "female", 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genderShouldNotNull() throws Exception {
        User user = new User("Alibaba", null, 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genderShouldNotEmpty() throws Exception {
        User user = new User("Alibaba", "", 18, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldNotLess18() throws Exception {
        User user = new User("Alibaba", "male", 17, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldNotLarge100() throws Exception {
        User user = new User("Alibaba", "male", 101, "a@b.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emailShouldValid() throws Exception {
        User user = new User("Alibaba", "male", 21, "ab.c", "11234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phoneShouldStartWith1() throws Exception {
        User user = new User("Alibaba", "male", 21, "a@b.c", "21234567890");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phoneShouldBe11Digital() throws Exception {
        User user = new User("Alibaba", "male", 21, "a@b.c", "1123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
