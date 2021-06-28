package com.example.chatapp.models;

public class ChatListModel {
    String GroupKey;
    String GroupName;
    String LastMessage;

    public ChatListModel(String groupKey, String groupName, String lastMessage) {
        GroupKey = groupKey;
        GroupName = groupName;
        LastMessage = lastMessage;
    }

    public String getGroupKey() {
        return GroupKey;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getLastMessage() {
        return LastMessage;
    }
}
