package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
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
  public void updateOneRsEvent(@PathVariable int index, @RequestBody String rsEventString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventString,RsEvent.class);
    RsEvent rsEventNeedBeUpdated = rsList.get(index - 1);

    String keyWord = rsEvent.getKeyWord();
    String eventName = rsEvent.getEventName();



    if (keyWord == null){
      if (!rsEventNeedBeUpdated.getEventName().equals(rsEvent.getEventName())){
        rsEventNeedBeUpdated.setEventName(rsEvent.getEventName().toString());
      }
      return;
    } else if (eventName == null){
      if (!rsEventNeedBeUpdated.getKeyWord().equals(rsEvent.getKeyWord())){
        rsEventNeedBeUpdated.setKeyWord(rsEvent.getKeyWord().toString());
      }
      return;
    } else {
      if (!rsEventNeedBeUpdated.getEventName().equals(rsEvent.getEventName())){
        rsEventNeedBeUpdated.setEventName(rsEvent.getEventName().toString());
      }
      if (!rsEventNeedBeUpdated.getKeyWord().equals(rsEvent.getKeyWord())){
        rsEventNeedBeUpdated.setKeyWord(rsEvent.getKeyWord().toString());
      }
    }





  }

  @DeleteMapping("/rs/{index}")
  public void deleteOneRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
  }
}
