package com.example.chatapp.chat;

import com.example.chatapp.chat.ChatMessage;
import com.example.chatapp.chat.ChatUser;
import com.example.chatapp.models.Dialog;
import com.example.chatapp.models.User;
import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatDialog implements IDialog<ChatMessage> {
    String id;
    String dialogPhoto;
    String dialogName;
    ArrayList<ChatUser> users = new ArrayList<ChatUser>();
    ChatMessage lastMessage;
    int unreadCount;

    public ChatDialog(Dialog dialog)
    {
        this.id = dialog.getId();
        this.dialogPhoto = dialog.getDialogPhoto();
        this.dialogName = dialog.getDialogName();

        for (User user:dialog.getUsers())
        {
            this.users.add(new ChatUser(user));
        }

        this.lastMessage = new ChatMessage(dialog.getLastMessage());
        this.unreadCount = getUnreadCount();
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
    public ArrayList<ChatUser> getUsers() {
        return users;
    }

    @Override
    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(ChatMessage message) {

        this.lastMessage = message;
    }

    @Override
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
}
