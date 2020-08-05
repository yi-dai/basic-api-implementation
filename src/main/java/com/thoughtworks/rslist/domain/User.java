package com.thoughtworks.rslist.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class User {

    @Size(min = 1, max = 8)
    @NotNull
    private String name;
    @Size(min = 1)
    @NotNull
    private String gender;
    @Max(100)
    @Min(18)
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    private int vote = 10;


    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
