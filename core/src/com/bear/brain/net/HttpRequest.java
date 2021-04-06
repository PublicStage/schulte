package com.bear.brain.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Array;
import com.bear.brain.AnswerListener;
import com.bear.brain.Brain;
import com.bear.brain.events.ServerNotAvailable;
import com.bear.brain.logic.coding.Coder;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.bear.brain.resources.Resources.TAG;

public class HttpRequest {
    public static final int TIMEOUT = 2000;
    public static final String URL = "http://mobal.top/brain";

    private static final Gson gson = new Gson();

    private static Array<Net.HttpRequest> activeRequests = new Array<>();

    private static boolean logRequests;
    private static boolean logResults;

    private static void log(Net.HttpRequest request) {
        try {
            Gdx.app.log(TAG, "CONTENT " + Coder.decode(request.getContent()));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeRequestFromActive(Net.HttpRequest request) {
        activeRequests.removeValue(request, false);
    }

    public static void stopAllRequests() {
        for (Net.HttpRequest request : activeRequests) {
            Gdx.net.cancelHttpRequest(request);
        }
        activeRequests.clear();
    }

    public static boolean isLogRequests() {
        return logRequests;
    }

    public static void setLogRequests(boolean logRequests) {
        HttpRequest.logRequests = logRequests;
    }

    public static boolean isLogResults() {
        return logResults;
    }

    public static void setLogResults(boolean logResults) {
        HttpRequest.logResults = logResults;
    }

    public static <T> Net.HttpRequest request(Object command, final AnswerListener<T> listener) {

        // TODO сервер на команду T.getCommand() должен вернуть json объект типа T
        // command json ----> answer json

        final Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        activeRequests.add(request);
        request.setUrl(URL + "/s");
        request.setTimeOut(TIMEOUT);
        try {
            request.setContent(Coder.encode(command));
            if (logRequests) log(request);
            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                        Brain.serverState.setWork(true);
                        String str = httpResponse.getResultAsString();
                        //Gdx.app.log(TAG, str);
                        try {
                            str = Coder.decode(str);
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (logResults) Gdx.app.log(TAG, str);
                        final T obj = new Gson().fromJson(str, listener.getType());
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (obj != null) {
                                    listener.ok(obj);
                                } else {
                                    listener.cancel();
                                }
                            }
                        });
                    } else {
                        Brain.serverState.setWork(false);
                        EventBus.getDefault().post(new ServerNotAvailable());
                        listener.cancel();
                    }
                    removeRequestFromActive(request);
                }

                @Override
                public void failed(Throwable t) {
                    Brain.serverState.setWork(false);
                    EventBus.getDefault().post(new ServerNotAvailable());
                    listener.cancel();
                    removeRequestFromActive(request);
                }

                @Override
                public void cancelled() {
                    Brain.serverState.setWork(false);
                    EventBus.getDefault().post(new ServerNotAvailable());
                    listener.cancel();
                    removeRequestFromActive(request);
                }
            });
        } catch (Exception ex) {
            Gdx.app.log(TAG, "HttpResponseListener Exception: " + ex.toString());
            removeRequestFromActive(request);
        }
        return request;
    }
}
