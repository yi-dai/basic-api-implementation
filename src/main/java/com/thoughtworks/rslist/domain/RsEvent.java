package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class RsEvent {
    private String eventName;
    private String keyWord;
    private @Valid User user;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public RsEvent(){
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent(String eventName, String keyWord, User user){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }
}
