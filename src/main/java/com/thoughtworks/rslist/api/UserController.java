package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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






}
