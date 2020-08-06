package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.resposiry.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void shouldAddUser() throws Exception {
        User user = new User("Tom","male",19,"a@b.c","12345678910");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/db/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(1,userList.size());
        assertEquals("Tom", userList.get(0).getName());
        assertEquals("male", userList.get(0).getGender());
    }

    @Test
    void shouldAddUser1() throws Exception {
        User user = new User("Tom","male",19,"a@b.c","12345678910");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/db/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        User user1 = new User("Bob","male",20,"a@b.c","12345678910");
        String userString1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/db/user").content(userString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(2,userList.size());
        assertEquals("Bob", userList.get(1).getName());
        assertEquals("male", userList.get(1).getGender());
    }

    @Test
    void contextLoads() {
    }



}
