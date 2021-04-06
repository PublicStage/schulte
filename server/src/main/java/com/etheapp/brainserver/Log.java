package com.etheapp.brainserver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
    static Logger logger;
    static SimpleDateFormat logTime;
    static StringBuilder sb;
    static java.util.Formatter formatter;
    static StringBuilder argsBuilder;

    static {
        init();
    }

    public static String timeString(long time) {
        return logTime.format(time);
    }

    synchronized private static void init() {
        logger = Logger.getLogger("Brain");
        logTime = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        sb = new StringBuilder();
        argsBuilder = new StringBuilder();
        formatter = new java.util.Formatter(argsBuilder);
    }

    private static String createFileName() {
        return "logs/brain." + new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date()) + ".log";
    }

    synchronized public static void initLogFile() throws IOException {
        FileHandler fh = new FileHandler(createFileName());
        logger.addHandler(fh);
        fh.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                sb.setLength(0);
                return sb.append(logTime.format(record.getMillis())).append(" ").append(record.getLevel()).append(" ").append(record.getMessage()).append("\n").toString();
            }
        });

    }

    synchronized public static void info(String message) {
        logger.info(message);
    }

    synchronized public static void info(String message, Object... args) {
        argsBuilder.setLength(0);
        formatter.format(message, args);
        logger.info(argsBuilder.toString());
    }

}
