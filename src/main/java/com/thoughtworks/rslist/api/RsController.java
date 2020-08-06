package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.RsEventDB;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.resposiry.RsEventRepository;
import com.thoughtworks.rslist.resposiry.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {


  public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
    this.userRepository = userRepository;
    this.rsEventRepository = rsEventRepository;
  }
  private final UserRepository userRepository;
  private final RsEventRepository rsEventRepository;

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

  @PostMapping("/db/rs/event")
  public ResponseEntity addOneRsEventDB(@RequestBody @Valid RsEventDB rsEventDB) throws JsonProcessingException {
    Integer userID = Integer.valueOf(rsEventDB.getUserId());
    if(userRepository.existsById(userID)){
      RsEventEntity rsEventEntity = RsEventEntity.builder()
              .eventName(rsEventDB.getEventName())
              .keyWord(rsEventDB.getKeyWord())
              .userId(rsEventDB.getUserId())
              .build();
      rsEventRepository.save(rsEventEntity);
      return ResponseEntity.created(null).build();
    }else{
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/db/rs/{rsEventID}")
  public ResponseEntity updateRsEventDB(@PathVariable Integer rsEventID, @RequestBody RsEventDB rsEventDB){
    if(rsEventRepository.existsById(rsEventID)){
      RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventID).get();
      String userIDOfRsEventEntity = rsEventEntity.getUserId();
      String userID = rsEventDB.getUserId();
      if(userIDOfRsEventEntity.equals(userID)){
        if(rsEventDB.getEventName() != null){
          rsEventEntity.setEventName(rsEventDB.getEventName());
        }
        if(rsEventDB.getKeyWord() != null){
          rsEventEntity.setKeyWord(rsEventDB.getKeyWord());
        }

        rsEventRepository.save(rsEventEntity);

        return ResponseEntity.created(null).build();
      }
    }
    return ResponseEntity.badRequest().build();
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
