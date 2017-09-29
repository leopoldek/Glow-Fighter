package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.slurpy.glowfighter.Core;

public abstract class Entity {
	
	protected Body body;
	
	protected Part[] parts;
	
	public Entity(BodyDef bodyDef, Part[] parts){
		this.parts = parts;
		
		body = Core.physics.registerBody(this, bodyDef);
	}
	
	public abstract void update();
	
	public void postUpdate(){}
	
	public void draw(){
		for(Part part : parts){
			part.draw(body.getPosition(), body.getAngle());
		}
	}
	
	public void hit(Entity other){}
}
