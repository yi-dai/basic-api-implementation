package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> users = new ArrayList<>();

    @PostMapping("/user")
    public void userRegister(@RequestBody @Valid User user){
        users.add(user);
    }


}
