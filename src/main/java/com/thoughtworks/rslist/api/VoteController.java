package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class VoteController {
    private final VoteRepository voteRepository;
    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;

    public VoteController(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/vote")
    public ResponseEntity voteForOneEvent(@RequestBody VoteEntity voteEntity){
        int userID = Integer.valueOf(voteEntity.getUserId());
        String rsID = voteEntity.getRsID();
        int rsEventIDInt = Integer.valueOf(rsID);
        if(rsEventRepository.existsById(rsEventIDInt) && userRepository.existsById(userID)){
            RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventIDInt).get();
            UserEntity userEntity = userRepository.findById(userID).get();
            int userVoteLeft = userEntity.getVoteNumLeft();
            int voteNum = voteEntity.getVoteNum();
            if(userVoteLeft >= voteNum){
                voteEntity.setRsID(rsID);
                userEntity.setVoteNumLeft(userVoteLeft - voteNum);
                rsEventEntity.setVoteNum(voteNum);
                rsEventRepository.save(rsEventEntity);
                userRepository.save(userEntity);
                voteRepository.save(voteEntity);
                return ResponseEntity.created(null).build();
            }else{
                rsEventRepository.save(rsEventEntity);
                userRepository.save(userEntity);
                voteRepository.save(voteEntity);
                return ResponseEntity.badRequest().build();
            }

        }
        return ResponseEntity.ok(null);


    }
}
