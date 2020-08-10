package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEventDB;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;



public class RsEventService {
    final UserRepository userRepository;
    final RsEventRepository rsEventRepository;
    final VoteRepository voteRepository;

    public RsEventService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public boolean addRsEvent(RsEventDB rsEventDB){
        Integer userID = Integer.valueOf(rsEventDB.getUserId());
        if(userRepository.existsById(userID)){
            RsEventEntity rsEventEntity = RsEventEntity.builder()
                    .eventName(rsEventDB.getEventName())
                    .keyWord(rsEventDB.getKeyWord())
                    .userId(rsEventDB.getUserId())
                    .build();
            rsEventRepository.save(rsEventEntity);
            return true;
        }else{
            return false;
        }
    }
}
