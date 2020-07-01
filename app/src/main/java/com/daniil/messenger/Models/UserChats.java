package com.daniil.messenger.Models;

public class UserChats {
    private String groupUID;

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public UserChats(String groupUID) {
        this.groupUID = groupUID;
    }
}
