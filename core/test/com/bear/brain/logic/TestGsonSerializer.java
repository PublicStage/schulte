package com.bear.brain.logic;

import com.bear.brain.GameResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import junit.framework.TestCase;

import java.lang.reflect.Type;

public class TestGsonSerializer extends TestCase {
    public void testGson() {
        TimeLog timeLog = new TimeLog();
        timeLog.addGameResult(3, 1000);
        timeLog.addGameResult(3, 2000);
        timeLog.addGameResult(4, 3000);


        Gson gson = new GsonBuilder()
                /*.setPrettyPrinting()*/
                .registerTypeAdapter(TimeLog.class, new TimeLogSerializer())
                .registerTypeAdapter(ResultInterval.class, new ResultIntervalSerializer())
                .registerTypeAdapter(GameResult.class, new GameResultSerializer())
                .create();
        String json = gson.toJson(timeLog);
        System.out.println(json);

    }

    public static class TimeLogSerializer implements JsonSerializer<TimeLog> {
        @Override
        public JsonElement serialize(TimeLog src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            for (int level : src.logByLevel.keySet()) {
                result.add(String.valueOf(level), context.serialize(src.logByLevel.get(level)));
            }
            return result;
        }
    }

    public static class ResultIntervalSerializer implements JsonSerializer<ResultInterval> {
        @Override
        public JsonElement serialize(ResultInterval src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray result = new JsonArray();
            for (GameResult res : src.array) {
                result.add(context.serialize(res));
            }
            return result;
        }
    }

    public static class GameResultSerializer implements JsonSerializer<GameResult> {

        @Override
        public JsonElement serialize(GameResult src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            //result.addProperty(String.valueOf(src.dateTime), src.duration);
            result.addProperty("date", src.getDateTime());
            result.addProperty("result", src.getDateTime());
            //result.addProperty(String.valueOf(src.dateTime), src.duration);
            return result;
        }
    }
}
