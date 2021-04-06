package com.etheapp.brainserver.ranger;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    public long userPercent = 0;

    public List<Result> list = new ArrayList<>();

    public int getPercent(long result) {
        for (Result res : list) {
            if (result < res.time || (result == res.time && userPercent == res.percent)) {
                return res.percent;
            }
        }
        return 0;
    }
}
