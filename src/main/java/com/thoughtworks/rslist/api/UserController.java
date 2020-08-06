package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.resposiry.UserRepository;
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

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private final UserRepository userRepository;

    @GetMapping("/user/list")
    public ResponseEntity<List<User>> getUsersList(){
        return ResponseEntity.ok(users);
    }



    @GetMapping("/db/user/{id}")
    public ResponseEntity<UserEntity> getUsersFromDB(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            return ResponseEntity.ok(null);
        }else{
            UserEntity userEntity = userRepository.findById(id).get();
            return ResponseEntity.ok(userEntity);
        }
    }


    @DeleteMapping("/db/user/{id}")
    public ResponseEntity deleteUsersFromDB(@PathVariable Integer id){
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return ResponseEntity.ok(null);
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



    @PostMapping("/db/user")
    public ResponseEntity userRegisterInDB(@RequestBody @Valid User user){
        UserEntity userEntity = UserEntity.builder()
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .vote((user.getVote()))
                .build();
        userRepository.save(userEntity);

        return ResponseEntity.created(null).build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex){
        CommonError commonError = new CommonError();
        commonError.setErrorMessage("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }






}
