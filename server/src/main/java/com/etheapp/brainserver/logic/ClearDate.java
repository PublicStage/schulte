package com.etheapp.brainserver.logic;

public interface ClearDate {
    long getFirstDateOrZero();

    void removeEventsBefore(long time);
}
