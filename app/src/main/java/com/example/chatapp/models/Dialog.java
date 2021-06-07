package com.example.chatapp.models;
import com.example.chatapp.chat.ChatMessage;
import com.example.chatapp.chat.ChatUser;
import com.example.chatapp.models.User;
import com.stfalcon.chatkit.commons.models.IDialog;

import java.io.Serializable;
import java.util.ArrayList;

public class Dialog implements Serializable {
    String id;
    String dialogPhoto;
    String dialogName;
    ArrayList<User> users = new ArrayList<User>();
    Message lastMessage;
    int unreadCount;

    public Dialog()
    {

    }
    public Dialog(Message firstMessage)
    {
        this.dialogPhoto = "";
        this.dialogName = firstMessage.text;
        this.users.add(firstMessage.user);
        this.lastMessage = firstMessage;
        this.unreadCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id)
    {
       this.id = id;
    }

    public String getDialogPhoto() {
        return dialogPhoto;
    }

    public String getDialogName() {
        return dialogName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message message) {

        this.lastMessage = message;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    /*
    public Map<String, Object> hashMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("dialogName", dialogName);
        hashMap.put("users", users);
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("unreadCount", unreadCount);
        return  hashMap;
    }
   */

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Dialog)obj).id);
    }

    @Override
    public  int hashCode() {
        return this.id.hashCode();
    }
}
