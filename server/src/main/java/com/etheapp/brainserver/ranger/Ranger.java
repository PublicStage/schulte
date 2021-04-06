package com.etheapp.brainserver.ranger;

import com.bear.brain.GameResult;
import com.etheapp.brainserver.Log;
import com.etheapp.brainserver.db.ResultRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ranger {
    public List<User> list = new ArrayList<>();
    Map<String, User> map = new HashMap<>();
    Comparator<User> comparator = new Comparator<User>() {
        @Override
        public int compare(User user2, User user1) {
            return user2.result < user1.result ? -1 : (user2.result == user1.result ? Long.compare(user2.next, user1.next) : 1);
        }
    };

    public Grid getGrid(String uuid) {
        Grid grid = new Grid();

        if (list.isEmpty()) {
            grid.list.add(new Result(100, Long.MAX_VALUE));
        } else if (list.size() < 100) {
            float delta = 100f / list.size();
            for (int i = 0; i < list.size(); i++) {
                Result result = new Result((int) (100 - i * delta), list.get(i).result);
                grid.list.add(result);
                if (list.get(i).uuid.equals(uuid)) grid.userPercent = result.percent;
            }
        }
        return grid;
    }

    public void addResultList(String uuid, ArrayList<GameResult> list) {
        if (!map.containsKey(uuid)) {
            addUser(new User(uuid));
        }
        if (addResultListToUser(list, map.get(uuid))) sort();
    }

    private void addUser(User user) {
        map.put(user.uuid, user);
        list.add(user);
    }

    private boolean addResultListToUser(ArrayList<GameResult> list, User user) {
        boolean needSort = false;
        for (GameResult result : list) {
            if (result.getDuration() < user.result) {
                if (user.result < user.next) user.next = user.result;
                user.result = result.getDuration();
                needSort = true;
            } else if (result.getDuration() < user.next) {
                user.next = result.getDuration();
                needSort = true;
            }
        }
        return needSort;
    }

    public void addResult(String uuid, long result) {
        if (map.containsKey(uuid)) {
            User user = map.get(uuid);
            if (result < user.result) {
                if (user.result < user.next) user.next = user.result;
                user.result = result;
                sort();
            } else if (result < user.next) {
                user.next = result;
                sort();
            }
        } else {
            User user = new User(uuid, result);
            map.put(uuid, user);
            list.add(user);
            sort();
        }
    }

    private void clear() {
        this.map.clear();
        this.list.clear();
    }

    private void sort() {
        list.sort(comparator);
    }

    public void load(List<ResultRecord> list) {
        clear();
        for (ResultRecord resultRecord : list) {
            addRecord(resultRecord);
        }
        sort();
        Log.info("Ranger load %d results (%d users) from DB", list.size(), map.size());
    }


    private void addRecord(ResultRecord record) {
        if (map.containsKey(record.uuid)) {
            User user = map.get(record.uuid);
            if (record.result < user.result) {
                if (user.result < user.next) user.next = user.result;
                user.result = record.result;
            } else if (record.result < user.next) {
                user.next = record.result;
            }
        } else {
            User user = new User(record.uuid, record.result);
            map.put(record.uuid, user);
            list.add(user);
        }
    }

    public User getUser(String uuid) {
        return map.get(uuid);
    }
}
