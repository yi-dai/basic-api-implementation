package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RsEvent {
    private String eventName;
    private String keyWord;
    private String user;

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

    public String getUser() {
        return user;
    }

    public void setUser(String userString) {
        //ObjectMapper objectMapper = new ObjectMapper();
        //User user = objectMapper.readValue(userString,User.class);
        this.user = userString;
    }

    public RsEvent(String eventName, String keyWord, String user){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }
}
