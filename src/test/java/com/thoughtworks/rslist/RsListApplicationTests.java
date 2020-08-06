package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEventDB;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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
    @Autowired
    VoteRepository voteRepository;

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
        Integer id = userEntity.getId();
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
        Integer id = userEntity.getId();
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
        String userID = String.valueOf(userEntity.getId());
        RsEventDB rsEventDB = new RsEventDB("股票", "经济", userID);
        String ursEventDBString = objectMapper.writeValueAsString(rsEventDB);
        mockMvc.perform(post("/db/rs/event").content(ursEventDBString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals(1,rsEventList.size());
        assertEquals("股票", rsEventList.get(0).getEventName());
        assertEquals("经济", rsEventList.get(0).getKeyWord());
    }

    @Test
    void shouldDeleteAllRsEventWhenDeleteUser() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        Integer intUserID = userEntity.getId();
        String userID = String.valueOf(intUserID);
        RsEventDB rsEventDB = new RsEventDB("股票", "经济", userID);
        String ursEventDBString = objectMapper.writeValueAsString(rsEventDB);
        mockMvc.perform(post("/db/rs/event").content(ursEventDBString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        RsEventDB rsEventDB1 = new RsEventDB("期货", "经济", userID);
        String ursEventDBString1 = objectMapper.writeValueAsString(rsEventDB);
        mockMvc.perform(post("/db/rs/event").content(ursEventDBString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/db/user/" + intUserID))
                .andExpect(status().isOk());
        List<UserEntity> userListNew = userRepository.findAll();
        assertEquals(0,userListNew.size());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals(0,rsEventList.size());
    }

    @Test
    void shouldUpdateRsEvent() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsEventID = rsEventEntity.getId();
        RsEventEntity newRsEventEntity = RsEventEntity.builder()
                .eventName("期货")
                .keyWord("经济")
                .userId(userID)
                .build();
        String rsEventEntityString = objectMapper.writeValueAsString(newRsEventEntity);
        mockMvc.perform(patch("/db/rs/"+rsEventID).content(rsEventEntityString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals("期货", rsEventList.get(0).getEventName());
    }

    @Test
    void shouldUpdateRsEventWithDifferentUser() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsEventID = rsEventEntity.getId();
        String newUserID = String.valueOf(userEntity.getId() + 1);
        RsEventEntity newRsEventEntity = RsEventEntity.builder()
                .eventName("期货")
                .keyWord("经济")
                .userId(newUserID)
                .build();
        String rsEventEntityString = objectMapper.writeValueAsString(newRsEventEntity);
        mockMvc.perform(patch("/db/rs/"+rsEventID).content(rsEventEntityString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateRsEventWithoutEventName() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsEventID = rsEventEntity.getId();
        RsEventEntity newRsEventEntity = RsEventEntity.builder()
                .eventName(null)
                .keyWord("经济")
                .userId(userID)
                .build();
        String rsEventEntityString = objectMapper.writeValueAsString(newRsEventEntity);
        mockMvc.perform(patch("/db/rs/"+rsEventID).content(rsEventEntityString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals("股票", rsEventList.get(0).getEventName());
    }

    @Test
    void shouldUpdateRsEventWithoutUser() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsEventID = rsEventEntity.getId();
        RsEventEntity newRsEventEntity = RsEventEntity.builder()
                .eventName(null)
                .keyWord("经济")
                .userId(null)
                .build();
        String rsEventEntityString = objectMapper.writeValueAsString(newRsEventEntity);
        mockMvc.perform(patch("/db/rs/"+rsEventID).content(rsEventEntityString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        assertEquals("股票", rsEventList.get(0).getEventName());
    }

    @Test
    void shouldVoteToOneRsEvent() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        //String rsEventID = String.valueOf(rsEventEntity.getId());
        int rsID = rsEventEntity.getId();

        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(4)
                .userId(userID)
                .localDateTime(LocalDateTime.now())
                .rsID(String.valueOf(rsID))
                .build();
        String voteEntityString = objectMapper.writeValueAsString(voteEntity);

        mockMvc.perform(post("/vote").content(voteEntityString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<VoteEntity> voteList = voteRepository.findAll();
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(4,voteList.get(0).getVoteNum());
        assertEquals(6,userList.get(0).getVoteNumLeft());
        assertEquals(4,rsEventList.get(0).getVoteNum());
    }

}
