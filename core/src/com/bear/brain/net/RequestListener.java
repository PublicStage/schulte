package com.bear.brain.net;

public interface RequestListener<T> {
    void ok(T t);

    void error();
}
