package com.example.chatapp.models;

public class MessageModel {

    String Sender;
    String SenderName;
    String MsgType;
    String Content;
    String DateTime;
    String MsgID;

    public MessageModel(String sender, String senderName, String msgType, String content, String dateTime, String msgID) {
        Sender = sender;
        SenderName = senderName;
        MsgType = msgType;
        Content = content;
        DateTime = dateTime;
        MsgID = msgID;
    }


    public String getSender() {
        return Sender;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getMsgType() {
        return MsgType;
    }

    public String getContent() {
        return Content;
    }

    public String getDateTime() {
        return DateTime;
    }

    public String getMsgID() {
        return MsgID;
    }
}
