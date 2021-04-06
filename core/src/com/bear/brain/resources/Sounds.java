package com.bear.brain.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;

public enum Sounds {
    CLICK("click.mp3", 0),
    FINISH("finish.mp3", 0);

    public static boolean ON = true;

    private String name;
    private Sound sound;
    private List<Sound> sounds;
    private int priority = 1;
    private boolean oneSound = false;
    private int count = 1;


    Sounds(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    Sounds(String name, int priority, int count) {
        this.name = name;
        this.priority = priority;
        this.count = count;
    }

    Sounds(String name, int priority, boolean oneSound) {
        this.name = name;
        this.priority = priority;
        this.oneSound = oneSound;
    }

    public static void addToAssetManager(AssetManager manager) {
        for (Sounds type : values()) {
            if (type.count == 1) {
                manager.load("sounds/" + type.name, Sound.class);
            } else {
                for (int id = 1; id <= type.count; id++) {
                    manager.load("sounds/" + type.name + id + ".mp3", Sound.class);
                }
            }

        }
    }

    public static void loadFromAssetManager(AssetManager manager) {
        for (Sounds type : values()) {
            if (type.count == 1) {
                type.sound = manager.get("sounds/" + type.name, Sound.class);
            } else {
                type.sounds = new ArrayList<>();
                for (int id = 1; id <= type.count; id++) {
                    type.sounds.add(manager.get("sounds/" + type.name + id + ".mp3", Sound.class));
                }
            }

        }
    }

    public void play() {
        play(1);
    }

    public void play(float volume) {
        if (ON) {
            if (count > 1) {
                sound = sounds.get((int) (Math.random() * count));
            }
            if (oneSound) {
                sound.stop();
            }
            sound.play(volume);
        }
    }
}