package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Entity;

public abstract class Gun {
	
	public final float maxTime;
	protected float timer;
	
	public Gun(float time){
		maxTime = time;
		timer = time;
	}
	
	public abstract void start(Entity entity);
	public abstract void update(boolean shoot, Vector2 pos, float rot);
	public abstract void end();
	
	public void tick(float delta){
		timer -= delta;
		if(timer < 0)timer = 0;
	}
	
	public float getTimeLeft(){
		return timer;
	}
	
	public abstract String getName();
}
