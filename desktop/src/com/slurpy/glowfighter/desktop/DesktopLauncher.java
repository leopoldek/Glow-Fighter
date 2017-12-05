package com.slurpy.glowfighter.desktop;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		DisplayMode display = LwjglApplicationConfiguration.getDesktopDisplayMode();	
		if(display.width < Constants.minWidth || display.height < Constants.minHeight){
			JOptionPane.showMessageDialog(null, "Your display is too small.\nThe game can not run.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		config.title = "Glow Fighter";
		config.addIcon("icons/128x128.png", FileType.Internal);
		config.addIcon("icons/32x32.png", FileType.Internal);
		config.addIcon("icons/16x16.png", FileType.Internal);
		
		//Gdx.app.getPreferences("");
		
		config.vSyncEnabled = true;
		//config.vSyncEnabled = false;  
		//config.foregroundFPS = 120;	
		config.foregroundFPS = 240;
		//config.backgroundFPS = 120;
		config.backgroundFPS = -1;
		//config.samples = 8;//DOES NOT AFFECT FBO
		//config.useGL30 = true;
		
		config.width = Constants.minWidth;
		config.height = Constants.minHeight;
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.fullscreen = false;
		
		new LwjglApplication(new Core(), config);
	}
}
