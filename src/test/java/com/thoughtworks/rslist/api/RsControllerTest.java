package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    @Order(1)
    void shouldGetRSList() throws Exception {
        mockMvc.perform(get("/rs/list/"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("一类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("二类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("未分类")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void shouldGetOneRSEvent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("一类")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void shouldGetRSEventBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("一类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("二类")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void shouldAddOneRsEvent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Alibaba", "female",19,"a@c.v", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        RsEvent rsEvent = new RsEvent("第四条事件", "未分类",userString);
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("一类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("二类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("未分类")))
                .andExpect(jsonPath("$[3].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWord",is("未分类")))
                .andExpect(status().isOk());
        assertEquals(1, UserController.users.size());
    }

    @Test
    @Order(5)
    void shouldUpdateOneRsEvent() throws Exception {
        RsEvent rsEvent = new RsEvent(null, "未分类",null);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/1").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("未分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("二类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("未分类")))
                .andExpect(jsonPath("$[3].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWord",is("未分类")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void shouldDeleteOneRsEvent() throws Exception {
        mockMvc.perform(delete("/rs/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("二类")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("未分类")))
                .andExpect(jsonPath("$[2].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("未分类")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void shouldAddOneRsEventWithUserAlreadyExist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Alibaba", "female",19,"a@c.v", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        RsEvent rsEvent = new RsEvent("第五条事件", "未分类",userString);
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[3].eventName",is("第五条事件")))
                .andExpect(jsonPath("$[3].keyWord",is("未分类")))
                .andExpect(status().isOk());
        assertEquals(1, UserController.users.size());
    }

    @Test
    @Order(8)
    void shouldAddOneRsEventWithUserNotExist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("A", "female",19,"a@c.v", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        RsEvent rsEvent = new RsEvent("第六条事件", "未分类",userString);
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[4].eventName",is("第六条事件")))
                .andExpect(jsonPath("$[4].keyWord",is("未分类")))
                .andExpect(status().isOk());
        assertEquals(2, UserController.users.size());
    }
}
