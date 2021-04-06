package com.etheapp.brainserver;

import javax.servlet.http.HttpServletRequest;

public class Commands {

    public static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    public static void writeLogin(String uuid) {
        try {
            LoginSaveQueue.addItem(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeResult(String request) {
        //AddResultQueue.addItem(request);
    }
}
