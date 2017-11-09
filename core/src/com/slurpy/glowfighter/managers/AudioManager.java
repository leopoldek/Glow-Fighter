package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;
import com.slurpy.glowfighter.utils.SoundType;

public class AudioManager {
	
	private float masterVolume;
	private final float[] volumes;
	
	public AudioManager(){
		masterVolume = 1f;
		volumes = new float[SoundType.values().length];
		for(int i = 0; i < volumes.length; i++){
			volumes[i] = 1f;
		}
	}
	
	public float getMasterVolume(){
		return masterVolume;
	}
	
	public void setMasterVolume(float volume){
		masterVolume = volume;
		for(MusicAsset musicAsset : MusicAsset.values()){
			Core.assets.getMusic(musicAsset).setVolume(getActualVolume(musicAsset.type));
		}
	}
	
	public float getVolume(SoundType type){
		return volumes[type.ordinal()];
	}
	
	public void setVolume(SoundType type, float volume){
		volumes[type.ordinal()] = volume;
		for(MusicAsset musicAsset : MusicAsset.values()){
			if(musicAsset.type == type){
				Core.assets.getMusic(musicAsset).setVolume(getActualVolume(type));
			}
		}
	}
	
	public float getActualVolume(SoundType type){
		return masterVolume * volumes[type.ordinal()];
	}
	
	public void playSound(SoundAsset soundAsset){
		Sound sound = Core.assets.getSound(soundAsset);
		sound.play(getActualVolume(soundAsset.type));
	}
	
	public void playSound(SoundAsset soundAsset, float volume){
		Sound sound = Core.assets.getSound(soundAsset);
		sound.play(volume);
	}
	
	public void playSound(SoundAsset soundAsset, float volume, float pitch, float pan){
		Sound sound = Core.assets.getSound(soundAsset);
		sound.play(volume, pitch, pan);
	}
	
	public Sound getSound(SoundAsset soundAsset){
		return Core.assets.getSound(soundAsset);
	}
	
	public void playSound2D(SoundAsset soundAsset, Vector2 pos, float range){
		playSound2D(soundAsset, pos, getActualVolume(soundAsset.type), range);
	}
	
	public void playSound2D(SoundAsset soundAsset, Vector2 pos, float volume, float range){
		Vector2 cameraPos = Core.graphics.getCameraPosition();
		float distX = pos.x - cameraPos.x;
		float distY = pos.y - cameraPos.y;
		
		float realVolume = Math.max(volume * ((range - Vector2.len(distX, distY)) / range), 0f);
		float realPan = MathUtils.clamp(distX / range, -1, 1);
		
		Core.assets.getSound(soundAsset).play(realVolume, 1, realPan);
	}
	
	public Music getMusic(MusicAsset musicAsset){
		return Core.assets.getMusic(musicAsset);
	}
	
	public void stopAll(){
		for(SoundAsset soundAsset : SoundAsset.values()){
			Core.assets.getSound(soundAsset).stop();
		}
		for(MusicAsset musicAsset : MusicAsset.values()){
			Core.assets.getMusic(musicAsset).stop();
		}
	}
}
