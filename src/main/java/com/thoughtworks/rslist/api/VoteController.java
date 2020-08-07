package com.thoughtworks.rslist.api;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class VoteController {
    private final VoteRepository voteRepository;
    private final RsEventRepository rsEventRepository;
    private final UserRepository userRepository;

    public VoteController(VoteRepository voteRepository, RsEventRepository rsEventRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
    }




    @PostMapping("/vote/{rsEventIDInt}")
    public ResponseEntity voteForOneEvent(@PathVariable int rsEventIDInt, @RequestBody Vote vote){
        int userID = Integer.valueOf(vote.getUserID());
        if(rsEventRepository.existsById(rsEventIDInt) && userRepository.existsById(userID)){
            RsEventEntity rsEventEntity = rsEventRepository.findById(rsEventIDInt).get();
            UserEntity userEntity = userRepository.findById(userID).get();
            int userVoteLeft = userEntity.getVoteNumLeft();
            int voteNum = vote.getVoteNum();
            if(userVoteLeft >= voteNum){
                VoteEntity voteEntity = VoteEntity.builder()
                        .userID(vote.getUserID())
                        .voteNum(voteNum)
                        .localDateTime(LocalDateTime.now())
                        .rsID(String.valueOf(rsEventIDInt))
                        .build();
                userEntity.setVoteNumLeft(userVoteLeft - voteNum);
                rsEventEntity.setVoteNum(voteNum);
                rsEventRepository.save(rsEventEntity);
                userRepository.save(userEntity);
                voteRepository.save(voteEntity);
                return ResponseEntity.created(null).build();
            }else{
                rsEventRepository.save(rsEventEntity);
                userRepository.save(userEntity);
                return ResponseEntity.ok().build();
            }

        }
        return ResponseEntity.ok(null);

    }


    @GetMapping("/db/rs/vote")
    public ResponseEntity<List<VoteEntity>> getRsListBetween
            (@RequestParam(required = false) String startTime,
             @RequestParam(required = false) String endTime){
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return ResponseEntity.ok(voteRepository.findAllByLocalDateTimeBetween(start,end));

    }

}
