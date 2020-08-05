package com.thoughtworks.rslist.domain;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RsEvent {



    public interface publicView{};
    public interface privateView extends publicView{};
    @NotNull
    @JsonView(privateView.class)
    private @Valid User user;

    @NotNull
    @Size(min = 1)
    @JsonView(publicView.class)
    private String eventName;
    @NotNull
    @Size(min = 1)
    @JsonView(publicView.class)
    private String keyWord;


    public RsEvent(String eventName, String keyWord, User user){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }
}
