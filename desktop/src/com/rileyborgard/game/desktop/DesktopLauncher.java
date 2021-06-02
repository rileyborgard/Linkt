package com.rileyborgard.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rileyborgard.game.Constants;
import com.rileyborgard.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
        config.foregroundFPS = 60;
		new LwjglApplication(new Main(), config);
	}
}
