package com.bear.brain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bear.brain.Brain;

public class DesktopLauncher640x360 {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 640;
		config.width = 360;
		config.forceExit = false;
		new LwjglApplication(new Brain(), config);
	}
}
