package com.bear.brain.widgets;

import com.badlogic.gdx.utils.StringBuilder;
import com.bear.lib.LabelLayer;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TimeLabel extends LabelLayer {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdfDayMonth = new SimpleDateFormat("d MMMM");
    static long DAY = TimeUnit.DAYS.toMillis(1);
    static long HOUR = TimeUnit.HOURS.toMillis(1);
    static long MINUTE = TimeUnit.MINUTES.toMillis(1);
    static long SECOND = TimeUnit.SECONDS.toMillis(1);

    StringBuilder buf = new StringBuilder();


    public TimeLabel(LabelStyle style) {
        super(null, style);
    }

    public void setTime_mm_ss_SS(long val) {
        buf.setLength(0);
        append(buf, 2, (val / MINUTE));
        val %= MINUTE;
        buf.append(':');
        append(buf, 2, (val / SECOND));
        buf.append('.');
        val %= SECOND;
        append(buf, 2, (val / 10));
        setText(buf);
        pack();
    }

    public void setTime_hh_mm(long val) {
        setText(sdf.format(val));
        pack();
    }

    public void setTime_dd_M(long val) {
        setText(sdfDayMonth.format(val));
        pack();
    }


    public void setTime_m_ss_SS(long val) {
        buf.setLength(0);
        append(buf, 1, (val / MINUTE));
        val %= MINUTE;
        buf.append(':');
        append(buf, 2, (val / SECOND));
        buf.append('.');
        val %= SECOND;
        append(buf, 2, (val / 10));
        setText(buf);
        pack();
    }

    public void setTime_ss_SS(long val) {
        buf.setLength(0);
        append(buf, 1, (val / SECOND));
        buf.append('.');
        val %= SECOND;
        append(buf, 2, (val / 10));
        setText(buf);
        pack();
    }


    public void setTime_ss_S(long val) {
        buf.setLength(0);
        append(buf, 1, (val / SECOND));
        buf.append('.');
        val %= SECOND;
        append(buf, 1, (val / 100));
        setText(buf);
        pack();
    }

    /**
     * Append a right-aligned and zero-padded numeric value to a `StringBuilder`.
     */
    private void append(StringBuilder tgt, int dgt, long val) {
        if (dgt > 1) {
            int pad = (dgt - 1);
            for (long xa = val; xa > 9 && pad > 0; xa /= 10) {
                pad--;
            }
            for (int xa = 0; xa < pad; xa++) {
                tgt.append('0');
            }
        }
        tgt.append(val);
    }
}
