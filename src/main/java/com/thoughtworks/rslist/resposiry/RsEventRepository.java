package com.thoughtworks.rslist.resposiry;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventEntity, Integer> {

    List<RsEventEntity> findAll();
}