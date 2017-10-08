package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.slurpy.glowfighter.utils.Constants;

public class AssetManager implements Disposable{
	
	private static AssetManager singleton;
	
	public static AssetManager getAssetManager(){
		if(singleton == null)singleton = new AssetManager();
		return singleton;
	}
	
	private final com.badlogic.gdx.assets.AssetManager assets;
	
	private AssetManager(){
		assets = new com.badlogic.gdx.assets.AssetManager();
		//assets.load("WhiteCircle.png", Texture.class);
		assets.load(Constants.FONT_FILE, BitmapFont.class);
		assets.finishLoading();
	}
	
	public <T> T get(String name, Class<T> type){
		return assets.get(name, type);
	}

	@Override
	public void dispose() {
		assets.dispose();
	}
}
