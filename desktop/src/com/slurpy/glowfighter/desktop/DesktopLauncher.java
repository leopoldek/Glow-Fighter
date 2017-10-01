package com.slurpy.glowfighter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slurpy.glowfighter.Core;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Glow Fighter";
		//config.addIcon(path, fileType);
		
		config.vSyncEnabled = false;
		config.foregroundFPS = 120;
		config.backgroundFPS = 120;
		//config.backgroundFPS = -1;
		//config.samples = 8;//DOES NOT AFFECT FBO
		//config.useGL30 = true;//Don't use! Crashes due to shader errors. Fix maybe?
		
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		//config.fullscreen = true
		
		new LwjglApplication(new Core(), config);
	}
}
