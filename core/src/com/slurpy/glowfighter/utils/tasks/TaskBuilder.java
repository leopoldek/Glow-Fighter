package com.slurpy.glowfighter.utils.tasks;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class TaskBuilder {
	
	private Array<KeyFrame> frames = new Array<>(KeyFrame.class);
	private FloatArray times = new FloatArray();
	
	public TaskBuilder addKeyFrame(KeyFrame frame, float time){
		frames.add(frame);
		times.add(time);
		return this;
	}
	
	public TaskBuilder addKeyFrame(float time){
		frames.add((progress, frameProgress) -> {});
		times.add(time);
		return this;
	}
	
	public Task build(){
		return new Task(frames.toArray(), times.toArray());
	}
}
