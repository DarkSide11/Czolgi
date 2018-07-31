package com.czolgi.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.czolgi.game.Czolgi;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	 config.title="Czolgi";
	 config.useGL30 = false;
	 config.width = 800;
	 config.height = 600;
		new LwjglApplication(new Czolgi(), config);
	}
}
