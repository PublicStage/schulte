package com.bear.brain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatPage {
    @SerializedName("chatSize")
    int chatSize;
    @SerializedName("from")
    int from;
    @SerializedName("list")
    List<ChatMessage> list;

    public ChatPage(int chatSize, int from, List<ChatMessage> list) {
        this.chatSize = chatSize;
        this.from = from;
        this.list = list;
    }

    public int getChatSize() {
        return chatSize;
    }

    public List<ChatMessage> getList() {
        return list;
    }

    public int getFrom() {
        return from;
    }
}
