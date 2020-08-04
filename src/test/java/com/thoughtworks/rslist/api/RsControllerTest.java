package com.thoughtworks.rslist.api;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        String rsEventJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"未分类\"}";
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
    }

    @Test
    @Order(5)
    void shouldChangeOneRsEvent() throws Exception {
        String rsEventJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"未分类\"}";
        mockMvc.perform(post("/rs/1").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("未分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("二类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("未分类")))
                .andExpect(jsonPath("$[3].eventName",is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWord",is("未分类")))
                .andExpect(status().isOk());
    }

}
