package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.parts.Part;

public abstract class Entity {
	
	public Part part;
	public final Color color;
	
	public final Body body;
	public final Category category;
	public final Team team;
	
	private boolean deleted = false;
	
	public Entity(EntityDef entityDef){
		part = entityDef.part;
		color = entityDef.color;
		category = entityDef.category;
		team = entityDef.team;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(entityDef.pos);
		bodyDef.angle = entityDef.rot;
		bodyDef.fixedRotation = true;
		bodyDef.type = entityDef.bodyType;
		bodyDef.active = true;
		bodyDef.bullet = entityDef.bullet;
		
		body = Core.physics.createEntityBody(this, bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.set(entityDef.polygon);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = entityDef.sensor;
		fixDef.density = entityDef.density;
		fixDef.filter.categoryBits = entityDef.category.categoryBits;
		fixDef.filter.maskBits = entityDef.category.maskBits;
		fixDef.filter.groupIndex = (short) -entityDef.team.ordinal();
		
		body.createFixture(fixDef);
		
		shape.dispose();
	}
	
	public abstract void update();
	
	public void draw(){
		if(part.visible)part.draw(body.getPosition(), body.getAngle(), color);
	}
	
	public void hit(Entity other){}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public float getRotation(){
		return body.getAngle();
	}
	
	public final void delete(){
		deleted = true;
	}
	
	public boolean isDeleted(){
		return deleted;
	}
}
