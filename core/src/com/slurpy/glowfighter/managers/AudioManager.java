package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class AudioManager {
	
	private static AudioManager singleton;
	
	public static AudioManager getAudioManager(){
		if(singleton == null)singleton = new AudioManager();
		return singleton;
	}
	
	public Array<Sound2D> sounds = new Array<>(false, 32);
	
	public void update(){
		Vector2 cameraPos = Core.graphics.getCameraPosition();
		Vector2 cameraVel = Core.graphics.getCameraVelocity();
		for(Sound2D sound : sounds){
			
		}
	}
	
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
	
	public Sound2D playSound2D(SoundAsset soundAsset, Vector2 pos, Vector2 vel, float volume, float range, boolean loop){
		Sound sound = Core.assets.getSound(soundAsset);
		Sound2D sound2D;
		
		Vector2 cameraPos = Core.graphics.getCameraPosition();
		float distX = pos.x - cameraPos.x;
		float distY = pos.y - cameraPos.y;
		
		Vector2 cameraVel = Core.graphics.getCameraVelocity();
		float source = Vector2.dot(distX, distY, vel.x, vel.y);
		float reciever = Vector2.dot(distX, distY, cameraVel.x, cameraVel.y);
		
		float realVolume = MathUtils.clamp(volume * (Vector2.len(distX, distY) / range), 0, 1);
		float realPitch = MathUtils.clamp(reciever / source, 0.5f, 2f);
		float realPan = MathUtils.clamp(distY / range, -1, 1);
		
		if(loop){
			sound2D = new Sound2D(sound, sound.loop(realVolume, realPitch, realPan), pos.x, pos.y, vel.x, vel.y, volume, range);
		}else{
			sound2D = new Sound2D(sound, sound.play(realVolume, realPitch, realPan), pos.x, pos.y, vel.x, vel.y, volume, range);
		}
		sounds.add(sound2D);
		return sound2D;
	}
	
	public Music getMusic(MusicAsset musicAsset){
		return Core.assets.getMusic(musicAsset);
	}
	
	public class Sound2D{
		private final Sound sound;
		private final long id;
		
		public float x;
		public float y;
		public float velX;
		public float velY;
		public float volume;
		public float range;
		private Sound2D(Sound sound, long id, float x, float y, float velX, float velY, float volume, float range) {
			this.sound = sound;
			this.id = id;
			this.x = x;
			this.y = y;
			this.velX = velX;
			this.velY = velY;
			this.volume = volume;
			this.range = range;
		}
		
		public void stop(){
			sound.stop(id);
			sounds.removeValue(this, true);
		}
		
		public void resume(){
			sound.resume(id);
			sounds.add(this);
		}
	}
}
