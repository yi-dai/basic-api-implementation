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
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(status().isCreated());
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

    @Test
    void getAllUsers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user1 = new User("Alibaba", "female", 18, "a@b.c", "11234567890");
        String userJSON1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/user").content(userJSON1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        User user2 = new User("Tom", "female", 18, "a@b.c", "11234567890");
        String userJSON2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/user").content(userJSON2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/list"))
                .andExpect(jsonPath("$[0].name", is("Alibaba")))
                .andExpect(status().isOk());
    }

    @Test
    void phoneShouldBe11Digital2() throws Exception {
        User user = new User("Alibaba", "male", 21, "a@b.c", "1123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJSON = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("invalid user")))
                .andExpect(status().isBadRequest());
    }
}
