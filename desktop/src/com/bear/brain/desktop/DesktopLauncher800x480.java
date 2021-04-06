package com.bear.brain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bear.brain.Brain;

public class DesktopLauncher800x480 {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800;
		config.width = 480;
		config.forceExit = false;
		new LwjglApplication(new Brain(), config);
	}
}
