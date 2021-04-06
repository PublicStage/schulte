package com.etheapp.brainserver.logic.chat;

import com.bear.brain.ChatMessage;
import com.bear.brain.ChatPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Chat {

    private ArrayList<ChatMessage> list = new ArrayList<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addMessage(ChatMessage message) {
        lock.writeLock().lock();
        try {
            list.add(message);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getSize() {
        return list.size();
    }

    public ChatPage getPage(int from, int count, boolean forward) {
        ChatPage page;
        int fromId = forward ? from : Math.max(from - count + 1, 0);
        lock.readLock().lock();
        try {
            int toId = forward ? Math.min(from + count, list.size()) - 1 : from;
            page = new ChatPage(getSize(), fromId, list.subList(fromId, toId + 1));
        } finally {
            lock.readLock().unlock();
        }
        return page;
    }

    public void addTest() {
        for (int i = 0; i < 100; i++) {
            addMessage(new ChatMessage(System.currentTimeMillis(), "Name", "Message" + i));
        }
    }

    public ChatPage getLastPage(int pageSize) {
        ChatPage page;
        lock.readLock().lock();
        try {
            int fromId = Math.max(getSize() - pageSize, 0);
            page = new ChatPage(getSize(), fromId, list.subList(fromId, getSize()));
        } finally {
            lock.readLock().unlock();
        }
        return page;
    }

    public void load(List<ChatMessage> list) {
        lock.writeLock().lock();
        try {
            this.list.clear();
            this.list.addAll(list);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
