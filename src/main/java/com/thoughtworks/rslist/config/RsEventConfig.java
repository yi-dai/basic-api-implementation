package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.respository.RsEventRepository;
import com.thoughtworks.rslist.respository.UserRepository;
import com.thoughtworks.rslist.respository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RsEventConfig {
    @Bean
    public RsEventService rsEventService(UserRepository userRepository, RsEventRepository rsEventRepository,
                                         VoteRepository voteRepository){
        return new RsEventService(userRepository,rsEventRepository,voteRepository);

    }
}