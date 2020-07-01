package com.daniil.messenger.Models;

public class Chat {
    private String sender;
    private String message;
    private String recipient;
    private String type;
    private String groupID;

    public Chat() {
    }

    public Chat(String sender, String message, String recipient, String type, String groupID) {
        this.sender = sender;
        this.message = message;
        this.recipient = recipient;
        this.type = type;
        this.groupID = groupID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
