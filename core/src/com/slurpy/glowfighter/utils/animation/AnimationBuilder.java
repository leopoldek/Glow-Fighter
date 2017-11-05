package com.slurpy.glowfighter.utils.animation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class AnimationBuilder {
	
	private Array<KeyFrame> frames = new Array<>(KeyFrame.class);
	private FloatArray times = new FloatArray();
	
	public void addKeyFrame(KeyFrame frame, float time){
		frames.add(frame);
		times.add(time);
	}
	
	public Animation build(){
		return new Animation(frames.toArray(), times.toArray());
	}
}
