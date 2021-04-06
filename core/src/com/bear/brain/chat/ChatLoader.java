package com.bear.brain.chat;

import com.bear.brain.AnswerListener;
import com.bear.brain.ChatMessage;
import com.bear.brain.ChatPage;
import com.bear.brain.RequestChat;
import com.bear.brain.Texts;
import com.bear.brain.events.ChatAdd;
import com.bear.brain.events.RequestChatEvent;
import com.bear.brain.net.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatLoader extends AnswerListener<ChatPage> {

    int maxIdOnServer = 0;
    int zeroId = 0;
    int dates = 0;

    private Calendar calendar = Calendar.getInstance();

    public ChatLoader() {
        EventBus.getDefault().register(this);
    }

    List<ChatMessage> list = new ArrayList<>();

    public ChatMessage get(int position) {
        return list.get(position - zeroId);
    }

    public int size() {
        return list.size();
    }

    public int getLastId() {
        return zeroId + list.size() - 1 - dates;
    }

    public int getZeroId() {
        return zeroId;
    }

    public int getMax() {
        return maxIdOnServer + dates;
    }

    public int getMin() {
        return 0;
    }

    public void loadFirst() {
        HttpRequest.request(new RequestChat(Texts.getLanguage(), -1, true), this);
    }

    public void loadNext() {
        HttpRequest.request(new RequestChat(Texts.getLanguage(), getLastId(), true), this);
    }

    public void loadPrev() {
        HttpRequest.request(new RequestChat(Texts.getLanguage(), zeroId, false), this);
    }

    @Subscribe
    public void onMessageEvent(RequestChatEvent event) {
        loadNext();
    }


    @Override
    public void ok(ChatPage answer) {
        maxIdOnServer = answer.getChatSize();
        if (answer.getList().size() > 0) {
            if (list.size() == 0) {
                zeroId = answer.getFrom();
                addToEnd(answer.getList());
            } else if (answer.getFrom() == getLastId() + 1) {
                addToEnd(answer.getList());
            } else if (answer.getFrom() + answer.getList().size() == zeroId) {
                addToStart(answer.getList());
                zeroId = answer.getFrom();
            }
        }
    }

    private void addToStart(List<ChatMessage> add) {

        for (int id = add.size() - 1; id >= 0; id--) {
            ChatMessage message = add.get(id);
            ChatMessage first = list.get(0);
            if (notDate(first) && diffDate(first.getTime(), message.getTime())) {
                list.add(0, createDateMessage(first.getTime()));
                dates++;
            }
            list.add(0, message);
        }
    }

    private void addToEnd(List<ChatMessage> add) {

        for (ChatMessage message : add) {
            if (list.size() > 0) {
                ChatMessage last = list.get(list.size() - 1);
                if (notDate(last) && diffDate(last.getTime(), message.getTime())) {
                    list.add(createDateMessage(message.getTime()));
                    dates++;
                }
            }
            list.add(message);
        }

        EventBus.getDefault().post(new ChatAdd());
    }

    private boolean notDate(ChatMessage message) {
        return message.getName() != null;
    }

    public boolean isMessage(int position) {
        return notDate(get(position));
    }

    private ChatMessage createDateMessage(long time) {
        return new ChatMessage(time, null, null);
    }

    private boolean diffDate(long time, long time1) {
        calendar.setTimeInMillis(time);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(time1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        return day != day1;
    }

    @Override
    public void cancel() {

    }

}
