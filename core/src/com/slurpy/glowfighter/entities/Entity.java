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
	protected Color[] color;
	
	protected final Body body;
	
	public Entity(Vector2 pos, float rot, Color color, Part[] parts, Vector2[] polygon){
		this.parts = parts;
		this.color = new Color[parts.length];
		for(int i = 0; i < this.color.length; i++){
			this.color[i] = color;
		}
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos);
		bodyDef.angle = rot;
		bodyDef.fixedRotation = true;
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.linearDamping = 1f;
		bodyDef.active = true;
		
		body = Core.physics.createEntityBody(this, bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.set(polygon);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		fixDef.shape = shape;
		
		body.createFixture(fixDef);
		
		shape.dispose();
	}
	
	public abstract void update();
	
	public void draw(){
		for(int i = 0; i < parts.length; i++){
			Part part = parts[i];
			if(part.visible)
				part.draw(body.getPosition(), body.getAngle(), color[i]);
		}
	}
	
	public abstract void hit(Entity other);
}
