package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initalList();
  private List<RsEvent> initalList(){
    List<RsEvent> list = new ArrayList<>();
    User user = new User("Alibaba", "female",19,"a@c.v", "11234567890");
    list.add(new RsEvent("第一条事件","一类",user));
    list.add(new RsEvent("第二条事件","二类", user));
    list.add(new RsEvent("第三条事件","未分类", user));
    return list;

  }

  @GetMapping("/rs/list")
  @JsonView(RsEvent.publicView.class)
  public ResponseEntity<List<RsEvent>> getRsList(@RequestParam(required = false) Integer start,
                                                @RequestParam(required = false) Integer end){
    if (start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @GetMapping("/rs/{index}")
  @JsonView(RsEvent.publicView.class)
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent1) throws JsonProcessingException {
    User user = rsEvent1.getUser();
    rsList.add(rsEvent1);
    UserController.userRegister(user);
    Integer index = rsList.size() - 1;
    String headValue = index.toString();
    return ResponseEntity.created(null).header("index", headValue).build();
  }

  @PostMapping("/rs/{index}")
  public ResponseEntity updateOneRsEvent(@PathVariable int index, @RequestBody String rsEventString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventString,RsEvent.class);
    RsEvent rsEventNeedBeUpdated = rsList.get(index - 1);
    String keyWord = rsEvent.getKeyWord();
    String eventName = rsEvent.getEventName();
    Integer indexInteger = index;
    String headValue = indexInteger.toString();
    if (keyWord == null){
      if (!rsEventNeedBeUpdated.getEventName().equals(rsEvent.getEventName())){
        rsEventNeedBeUpdated.setEventName(rsEvent.getEventName().toString());
      }
      return ResponseEntity.created(null).header("index", headValue).build();
    } else if (eventName == null){
      if (!rsEventNeedBeUpdated.getKeyWord().equals(rsEvent.getKeyWord())){
        rsEventNeedBeUpdated.setKeyWord(rsEvent.getKeyWord().toString());
      }
      return ResponseEntity.created(null).header("index", headValue).build();
    } else {
      if (!rsEventNeedBeUpdated.getEventName().equals(rsEvent.getEventName())){
        rsEventNeedBeUpdated.setEventName(rsEvent.getEventName().toString());
      }
      if (!rsEventNeedBeUpdated.getKeyWord().equals(rsEvent.getKeyWord())){
        rsEventNeedBeUpdated.setKeyWord(rsEvent.getKeyWord().toString());
      }
    }
    return ResponseEntity.created(null).header("index", headValue).build();
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
    return ResponseEntity.ok(null);
  }
}
