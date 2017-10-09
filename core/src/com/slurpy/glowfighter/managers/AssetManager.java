package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetManager implements Disposable{
	
	private static AssetManager singleton;
	
	public static AssetManager getAssetManager(){
		if(singleton == null)singleton = new AssetManager();
		return singleton;
	}
	
	private final com.badlogic.gdx.assets.AssetManager assets;
	
	private final ObjectMap<Effect, ParticleEffectPool> effectPools = new ObjectMap<>();
	
	private AssetManager(){
		assets = new com.badlogic.gdx.assets.AssetManager();
		//assets.load("WhiteCircle.png", Texture.class);
		for(Font font : Font.values()){
			assets.load(font.file, BitmapFont.class);
		}
		for(Effect effect : Effect.values()){
			assets.load(effect.file, ParticleEffect.class);
		}
		assets.finishLoading();
	}
	
	public <T> T getAsset(String name, Class<T> type){
		return assets.get(name, type);
	}
	
	public BitmapFont getFont(Font font){
		return assets.get(font.file, BitmapFont.class);
	}
	
	public PooledEffect getEffect(Effect effect){
		if(!effectPools.containsKey(effect))effectPools.put(effect, new ParticleEffectPool(assets.get(effect.file, ParticleEffect.class), 5, 50));
		return effectPools.get(effect).obtain();
	}

	@Override
	public void dispose() {
		assets.dispose();
	}
	
	public enum Font{
		CatV("fonts/CatV_6x12_9.fnt");
		
		public final String file;
		public static final Class<BitmapFont> clazz = BitmapFont.class;

		private Font(String file) {
			this.file = file;
		}
	}
	
	public enum Effect{
		Explosion("effects/Missile Death.p");
		
		public final String file;
		public static final Class<ParticleEffect> clazz = ParticleEffect.class;

		private Effect(String file) {
			this.file = file;
		}
	}
}
