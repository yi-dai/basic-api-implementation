package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RsController {
  //private List<String> rsList = Stream.of("第一条事件", "第二条事件", "第三条事件").collect(Collectors.toList());
  private List<RsEvent> rsList = initalList();
  private List<RsEvent> initalList(){
    List<RsEvent> list = new ArrayList<>();
    list.add(new RsEvent("第一条事件","一类"));
    list.add(new RsEvent("第二条事件","二类"));
    list.add(new RsEvent("第三条事件","未分类"));
    return list;

  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsList(@RequestParam(required = false) Integer start,
                                 @RequestParam(required = false) Integer end){
    if (start == null || end == null){
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index){
    return rsList.get(index - 1);
  }

  @PostMapping("/rs/event")
  public void addOneRsEvent(@RequestBody String rsEvent1) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEvent1,RsEvent.class);
    rsList.add(rsEvent);
  }

  @PostMapping("/rs/{index}")
  public void changeOneRsEvent(@PathVariable int index, @RequestBody String rsEventString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventString,RsEvent.class);
    RsEvent rsEventOld = rsList.get(index - 1);
    if (!rsEventOld.getEventName().equals(rsEvent.getEventName())){
      rsEventOld.setEventName(rsEvent.getEventName().toString());
    }
    if (!rsEventOld.getKeyWord().equals(rsEvent.getKeyWord())){
      rsEventOld.setKeyWord(rsEvent.getKeyWord().toString());
    }
  }
}
