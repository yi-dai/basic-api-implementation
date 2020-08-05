package com.thoughtworks.rslist.api;


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
public class UserController {
    public static List<User> users = new ArrayList<>();

    public static void addUser(User user){
        if(!users.contains(user)){
            users.add(user);
        }
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<User>> getUsersList(){
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity userRegister(@RequestBody @Valid User user){
        Integer indexNew = null;
        if(!users.contains(user)){
            users.add(user);
            indexNew = users.indexOf(user);
        }
        String headValue = indexNew.toString();
        return ResponseEntity.created(null).header("index",headValue).build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex){
        CommonError commonError = new CommonError();
        commonError.setErrorMessage("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }






}
