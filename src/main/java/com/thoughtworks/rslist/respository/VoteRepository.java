package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity,Integer> {

    List<VoteEntity> findAll();
    List<VoteEntity> findAllByLocalDateTimeBetween(LocalDateTime star,LocalDateTime end);
}
