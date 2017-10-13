package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	
	private final ObjectMap<EffectAsset, ParticleEffectPool> effectPools = new ObjectMap<>();
	
	private AssetManager(){
		assets = new com.badlogic.gdx.assets.AssetManager();
		//assets.load("WhiteCircle.png", Texture.class);
		for(FontAsset font : FontAsset.values()){
			assets.load(font.file, BitmapFont.class);
		}
		for(EffectAsset effect : EffectAsset.values()){
			assets.load(effect.file, ParticleEffect.class);
		}
		for(MusicAsset music : MusicAsset.values()){
			assets.load(music.file, Music.class);
		}
		for(SoundAsset sound : SoundAsset.values()){
			assets.load(sound.file, Sound.class);
		}
		assets.finishLoading();
	}
	
	public <T> T getAsset(String name, Class<T> type){
		return assets.get(name, type);
	}
	
	public BitmapFont getFont(FontAsset font){
		return assets.get(font.file, BitmapFont.class);
	}
	
	public PooledEffect getEffect(EffectAsset effect){
		if(!effectPools.containsKey(effect))effectPools.put(effect, new ParticleEffectPool(assets.get(effect.file, ParticleEffect.class), 5, 50));
		return effectPools.get(effect).obtain();
	}
	
	public Sound getSound(SoundAsset sound){
		return assets.get(sound.file, Sound.class);
	}
	
	public Music getMusic(MusicAsset music){
		return assets.get(music.file, Music.class);
	}

	@Override
	public void dispose() {
		assets.dispose();
	}
	
	public enum FontAsset{
		CatV("fonts/CatV_6x12_9.fnt");
		
		public final String file;
		public static final Class<BitmapFont> clazz = BitmapFont.class;

		private FontAsset(String file) {
			this.file = file;
		}
	}
	
	public enum EffectAsset{
		Explosion("effects/Missile Death.p");
		
		public final String file;
		public static final Class<ParticleEffect> clazz = ParticleEffect.class;

		private EffectAsset(String file) {
			this.file = file;
		}
	}
	
	public enum MusicAsset{
		;
		
		public final String file;
		public static final Class<MusicAsset> clazz = MusicAsset.class;
		
		private MusicAsset(String file){
			this.file = file;
		}
	}
	
	public enum SoundAsset{
		;
		
		public final String file;
		public static final Class<MusicAsset> clazz = MusicAsset.class;
		
		private SoundAsset(String file){
			this.file = file;
		}
	}
}
