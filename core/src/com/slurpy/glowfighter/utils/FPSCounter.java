package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.Gdx;

public class FPSCounter {
	
	private final float timer;
	
	private float accumulator = 0;
	
	public FPSCounter(float timer){
		this.timer = timer;
	}
	
	public void update(float delta){
		if(accumulator > timer){
			System.out.println(Gdx.graphics.getFramesPerSecond());
			accumulator = 0;
		}else
			accumulator += delta;
	}
}
