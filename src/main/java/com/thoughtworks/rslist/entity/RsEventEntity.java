package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rsEvent")
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer ID;
    @Column(name = "name")
    private String eventName;
    private String keyWord;
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
}
