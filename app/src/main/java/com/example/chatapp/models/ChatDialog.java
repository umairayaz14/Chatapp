package com.example.chatapp.models;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatDialog implements IDialog {
    public String id;
    public String dialogPhoto;
    public String dialogName;
    @Exclude
    public ArrayList<User> users = new ArrayList<>();
    public IMessage lastMessage;
    public int unreadCount;

    public ChatDialog(Message firstMessage)
    {
        this.dialogPhoto = "";
        this.dialogName = firstMessage.text;
        this.users.add(firstMessage.user);
        this.lastMessage = firstMessage;
        this.unreadCount = 0;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {

        this.lastMessage = message;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public Map<String, Object> hashMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("dialogName", dialogName);
        hashMap.put("users", users);
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("unreadCount", unreadCount);
        return  hashMap;
    }

}
