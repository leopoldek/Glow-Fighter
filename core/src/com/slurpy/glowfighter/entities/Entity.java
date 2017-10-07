package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.parts.Part;

public abstract class Entity {
	
	protected Part[] parts;
	protected Color[] colors;
	
	public final Body body;
	public final Category category;
	
	private boolean deleted = false;
	
	public Entity(EntityDef entityDef){
		parts = entityDef.parts;
		colors = entityDef.colors;
		category = entityDef.category;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(entityDef.pos);
		bodyDef.angle = entityDef.rot;
		bodyDef.fixedRotation = true;
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.active = true;
		bodyDef.bullet = entityDef.bullet;
		
		body = Core.physics.createEntityBody(this, bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.set(entityDef.polygon);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		fixDef.shape = shape;
		fixDef.filter.categoryBits = entityDef.category.categoryBits;
		fixDef.filter.maskBits = entityDef.category.maskBits;
		fixDef.filter.groupIndex = (short) -entityDef.team.ordinal();
		
		body.createFixture(fixDef);
		
		shape.dispose();
	}
	
	public abstract void update();
	
	public void draw(){
		for(int i = 0; i < parts.length; i++){
			Part part = parts[i];
			if(part.visible)
				part.draw(body.getPosition(), body.getAngle(), colors[i]);
		}
	}
	
	public abstract void hit(Entity other);
	
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
