package com.thoughtworks.rslist.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vote")
public class VoteEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private LocalDateTime localDateTime;
    private int voteNum;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
    @Column(name = "user_id")
    @NotNull
    private String userId;


    @NotNull
    private String rsEventID;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventEntity rsEventEntity;


}
