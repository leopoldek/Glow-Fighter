package com.slurpy.glowfighter.utils.tasks;

import com.badlogic.gdx.Gdx;

public class Task {
	
	private final KeyFrame[] frames;
	private final float[] times;
	private final float[] cTimes;
	
	private float timer = 0f;
	private boolean paused = false;
	
	private int keyFrame = 0;
	
	Task(KeyFrame[] frames, float[] times){
		this.frames = frames;
		this.times = times;
		cTimes = new float[times.length];
		
		float length = 0f;
		for(int i = 0; i < times.length; i++){
			length += times[i];
			cTimes[i] = length;
		}
		frames[0].start();
	}
	
	public void update(){
		if(paused)return;
		timer += Gdx.graphics.getDeltaTime();
		
		while(timer >= cTimes[keyFrame]){
			frames[keyFrame].end();
			keyFrame++;
			if(keyFrame == frames.length)return;
			frames[keyFrame].start();
		}
		
		float progress = getProgress();
		float frameProgress = (keyFrame == 0 ? timer : timer - cTimes[keyFrame - 1]) / times[keyFrame];
		frames[keyFrame].act(progress, frameProgress);
		return;
	}
	
	public void setKeyFrame(int frame){
		
	}
	
	public void setTime(){
		
	}
	
	public float getTime(){
		return timer;
	}
	
	public float getProgress(){
		return timer / cTimes[cTimes.length - 1];
	}
	
	public void pause(){
		paused = true;
	}
	
	public void resume(){
		paused = false;
	}
	
	public float getLength(){
		return cTimes[cTimes.length - 1];
	}
	
	public boolean isDone(){
		return timer > cTimes[cTimes.length - 1];
	}
}
