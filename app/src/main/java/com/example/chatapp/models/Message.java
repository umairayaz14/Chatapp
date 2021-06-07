package com.example.chatapp.models;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    String id;
    String text;
    User user;
    Date createdAt;
    public  Message()
    {

    }

    public Message(String text, User user)
    {
        this.text = text;
        this.user = user;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

/*
    public Map<String, Object> hashMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("text", text);
        hashMap.put("user", user);
        hashMap.put("createdAt", createdAt);

        return hashMap;
    }
*/

}

