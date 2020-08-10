package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

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
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    void shouldVoteToOneRsEvent() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .voteNumLeft(10)
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsID = rsEventEntity.getId();


        Vote vote = new Vote(4,userID,null);
        String voteString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/"+rsID).content(voteString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());


        List<VoteEntity> voteList = voteRepository.findAll();
        List<RsEventEntity> rsEventList = rsEventRepository.findAll();
        List<UserEntity> userList = userRepository.findAll();
        assertEquals(4,voteList.get(0).getVoteNum());
        assertEquals(6,userList.get(0).getVoteNumLeft());
        assertEquals(4,rsEventList.get(0).getVoteNum());
    }

    @Test
    void shouldReturnVoteBetweenTime() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .voteNumLeft(10)
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsID = rsEventEntity.getId();
        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(2)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2018, 2, 3, 0, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity);
        VoteEntity voteEntity1 = VoteEntity.builder()
                .voteNum(1)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2019, 2, 3, 12, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity1);
        VoteEntity voteEntity2 = VoteEntity.builder()
                .voteNum(1)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2012, 2, 3, 12, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity2);
        List<VoteEntity> voteEntities = voteRepository.findAll();
        mockMvc.perform(get("/db/rs/vote?startTime=" + LocalDateTime.of(2019, 1, 1, 0, 0, 0).toString()
                + "&endTime=" + LocalDateTime.of(2020, 1, 1, 0, 0, 0).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].voteNum", is(1)));
    }

    @Test
    void shouldReturn2VotesBetweenTime() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("Tom")
                .gender("male")
                .age(19)
                .email("a@b.c")
                .phone("12345678910")
                .voteNumLeft(10)
                .build();
        userEntity = userRepository.save(userEntity);
        String userID = String.valueOf(userEntity.getId());

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股票")
                .keyWord("经济")
                .userId(userID)
                .build();
        rsEventEntity = rsEventRepository.save(rsEventEntity);
        int rsID = rsEventEntity.getId();
        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(2)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2019, 2, 3, 0, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity);
        VoteEntity voteEntity1 = VoteEntity.builder()
                .voteNum(1)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2019, 2, 3, 12, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity1);
        VoteEntity voteEntity2 = VoteEntity.builder()
                .voteNum(1)
                .userID(userID)
                .localDateTime(LocalDateTime.of(2012, 2, 3, 12, 0, 0))
                .rsID(String.valueOf(rsID))
                .build();
        voteRepository.save(voteEntity2);
        List<VoteEntity> voteEntities = voteRepository.findAll();
        mockMvc.perform(get("/db/rs/vote?startTime=" + LocalDateTime.of(2019, 1, 1, 0, 0, 0).toString()
                + "&endTime=" + LocalDateTime.of(2020, 1, 1, 0, 0, 0).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


}
