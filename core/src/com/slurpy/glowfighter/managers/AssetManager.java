package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.slurpy.glowfighter.utils.SoundType;

public class AssetManager implements Disposable{
	
	private final com.badlogic.gdx.assets.AssetManager assets;
	
	private final ObjectMap<EffectAsset, ParticleEffectPool> effectPools = new ObjectMap<>();
	
	public AssetManager(){
		assets = new com.badlogic.gdx.assets.AssetManager();
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
		for(TextureAsset texture : TextureAsset.values()){
			assets.load(texture.file, Texture.class);
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
	
	public Texture getTexture(TextureAsset texture){
		return assets.get(texture.file, Texture.class);
	}

	@Override
	public void dispose() {
		assets.dispose();
	}
	
	public enum FontAsset{
		CatV("CatV_6x12_9.fnt");
		
		public final String file;
		public static final Class<BitmapFont> clazz = BitmapFont.class;

		private FontAsset(String file) {
			this.file = "fonts/" + file;
		}
	}
	
	public enum EffectAsset{
		Explosion("Missile Death.effect");
		
		public final String file;
		public static final Class<ParticleEffect> clazz = ParticleEffect.class;

		private EffectAsset(String file) {
			this.file = "effects/" + file;
		}
	}
	
	public enum MusicAsset{
		BackgroundTechno("Retro Techno Music.mp3", SoundType.music);
		
		public final String file;
		public final SoundType type;
		public static final Class<MusicAsset> clazz = MusicAsset.class;
		
		private MusicAsset(String file, SoundType type){
			this.file = "songs/" + file;
			this.type = type;
		}
	}
	
	public enum SoundAsset{
		Shoot("Shoot.wav", SoundType.effect), Hit("Hit.wav", SoundType.effect),
		PlayerDie("Player Die.wav", SoundType.effect), Pulse("Pulse.wav", SoundType.effect),
		Activated("Activated.wav", SoundType.effect);
		
		public final String file;
		public final SoundType type;
		public static final Class<MusicAsset> clazz = MusicAsset.class;
		
		private SoundAsset(String file, SoundType type){
			this.file = "sounds/" + file;
			this.type = type;
		}
	}
	
	public enum TextureAsset{
		WhitePixel("WhitePixel.png");
		
		public final String file;
		public static final Class<MusicAsset> clazz = MusicAsset.class;
		
		private TextureAsset(String file){
			this.file = "textures/" + file;
		}
	}
}
