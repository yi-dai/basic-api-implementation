package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNumLeft;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userId")
    private List<RsEventEntity> events;

}
