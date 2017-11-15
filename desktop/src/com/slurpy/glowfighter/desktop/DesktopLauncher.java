package com.slurpy.glowfighter.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slurpy.glowfighter.Core;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Glow Fighter";
		config.addIcon("icons/128x128.png", FileType.Internal);
		config.addIcon("icons/32x32.png", FileType.Internal);
		config.addIcon("icons/16x16.png", FileType.Internal);
		
		//Gdx.app.getPreferences("");
		
		config.vSyncEnabled = true;
		//config.vSyncEnabled = false;  
		//config.foregroundFPS = 120;	
		//config.foregroundFPS = 0;
		//config.backgroundFPS = 120;
		config.backgroundFPS = -1;
		//config.samples = 8;//DOES NOT AFFECT FBO
		//config.useGL30 = true;//Don't use! Crashes due to shader errors. Fix maybe?
		
		config.width = 1000;
		config.height = 700;
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.fullscreen = false;
		
		new LwjglApplication(new Core(), config);
	}
}
