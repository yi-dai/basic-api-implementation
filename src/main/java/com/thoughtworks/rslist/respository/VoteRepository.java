package com.thoughtworks.rslist.respository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();
}
