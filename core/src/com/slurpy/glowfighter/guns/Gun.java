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
	
	public String getName(){
		String name = this.getClass().getSimpleName();
		StringBuilder builder = new StringBuilder();
		builder.append(name.charAt(0));
		for(int i = 1; i < name.length(); i++){
			char letter = name.charAt(i);
			if(Character.isUpperCase(letter))builder.append(' ');
			builder.append(letter);
		}
		return builder.toString();
	}
}
