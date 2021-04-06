package com.bear.brain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bear.brain.Brain;

public class DesktopLauncher1100x720 {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1100;
		config.width = 720;
		config.forceExit = false;
		new LwjglApplication(new Brain(), config);
	}
}
