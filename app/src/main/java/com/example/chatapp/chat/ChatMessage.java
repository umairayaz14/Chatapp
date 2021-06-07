package com.example.chatapp.chat;

import com.example.chatapp.models.Message;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatMessage implements IMessage {
    String id;
    String text;
    ChatUser user;
    Date createdAt;

    public ChatMessage(Message message)
    {
        this.id = message.getId();
        this.text = message.getText();
        this.user = new ChatUser(message.getUser());
        this.createdAt = message.getCreatedAt();
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
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

