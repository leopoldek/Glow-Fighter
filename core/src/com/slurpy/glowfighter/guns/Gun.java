package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Entity;

public abstract class Gun {
	
	protected Entity entity;
	
	public Gun(Entity entity){
		this.entity = entity;
	}
	
	public abstract void start();
	public abstract void update(boolean shoot, Vector2 pos, float rot);
	public abstract void end();
}
