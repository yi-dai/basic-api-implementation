package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.RsEventDB;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.resposiry.RsEventRepository;
import com.thoughtworks.rslist.resposiry.UserRepository;
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
    @Autowired
    RsEventRepository rsEventRepository;

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
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        Integer id = userEntity.getID();
        mockMvc.perform(get("/db/user/"+id))
                .andExpect(jsonPath("$.name", is("Tom")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserWhenGivenId() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        Integer id = userEntity.getID();
        mockMvc.perform(delete("/db/user/"+id))
                .andExpect(status().isOk());
        List<UserEntity> userListNew = userRepository.findAll();
        assertEquals(0,userListNew.size());
    }

    @Test
    void shouldAddRsEvent() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getID());
        RsEventDB rsEventDB = new RsEventDB("股票", "经济", userID);
        String ursEventDBString = objectMapper.writeValueAsString(rsEventDB);
        mockMvc.perform(post("/db/rs/event").content(ursEventDBString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals(1,rsEventList.size());
        assertEquals("股票", rsEventList.get(0).getEventName());
        assertEquals("经济", rsEventList.get(0).getKeyWord());
    }
}
