package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class AudioManager {
	
	public void playSound(SoundAsset soundAsset){
		Sound sound = Core.assets.getSound(soundAsset);
		sound.play();
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
