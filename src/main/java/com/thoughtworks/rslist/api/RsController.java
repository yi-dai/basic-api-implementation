package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    if(start < 0 || end > rsList.size() - 1){
      throw new InvalidIndexException(("invalid request param"));
    }
    return ResponseEntity.ok(rsList.subList(start - 1, end));
  }

  @GetMapping("/rs/{index}")
  @JsonView(RsEvent.publicView.class)
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index){
    if(index < 0 || index > rsList.size() - 1){
      throw new InvalidIndexException(("invalid index"));
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent1) throws JsonProcessingException {
    User user = rsEvent1.getUser();
    rsList.add(rsEvent1);
    UserController.addUser(user);
    Integer index = rsList.size() - 1;
    String headValue = index.toString();
    return ResponseEntity.created(null).header("index", headValue).build();
  }

  @PutMapping("/rs/{index}")
  public ResponseEntity updateOneRsEvent(@PathVariable int index, @RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
    RsEvent rsEventNeedBeUpdated = rsList.get(index - 1);
    if (!rsEventNeedBeUpdated.getEventName().equals(rsEvent.getEventName())){
      rsEventNeedBeUpdated.setEventName(rsEvent.getEventName());
    }
    if (!rsEventNeedBeUpdated.getKeyWord().equals(rsEvent.getKeyWord())){
      rsEventNeedBeUpdated.setKeyWord(rsEvent.getKeyWord());
    }
    Integer indexInteger = index;
    String headValue = indexInteger.toString();
    return ResponseEntity.created(null).header("index", headValue).build();
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
    return ResponseEntity.ok(null);
  }

  @ExceptionHandler({InvalidIndexException.class, MethodArgumentNotValidException.class})
  public ResponseEntity exceptionHandler(Exception ex){
    String errorMessage;
    CommonError commonError = new CommonError();
    if(ex instanceof MethodArgumentNotValidException){
      errorMessage = "invalid param";
    } else{
      errorMessage = ex.getMessage();
    }
    commonError.setErrorMessage(errorMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);

  }
}
