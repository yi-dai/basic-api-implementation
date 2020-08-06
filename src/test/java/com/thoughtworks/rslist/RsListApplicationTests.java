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
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void shouldGetUserWhenGivenId() throws Exception {
        User user = new User("Tom","male",19,"a@b.c","12345678910");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/db/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> userList = userRepository.findAll();
        UserEntity userEntity = userList.get(0);
        Integer id = userEntity.getId();

        mockMvc.perform(get("/db/user/"+id))
                .andExpect(jsonPath("$.name", is("Tom")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserWhenGivenId() throws Exception {
        User user = new User("Tom","male",19,"a@b.c","12345678910");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/db/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> userList = userRepository.findAll();
        UserEntity userEntity = userList.get(0);
        Integer id = userEntity.getId();
        mockMvc.perform(delete("/db/user/"+id))
                .andExpect(status().isOk());
        List<UserEntity> userListNew = userRepository.findAll();
        assertEquals(0,userListNew.size());

    }





}
