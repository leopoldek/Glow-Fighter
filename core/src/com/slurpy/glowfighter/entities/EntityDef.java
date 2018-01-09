package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.parts.Part;

public class EntityDef {
	public Part part;
	public Color color;
	public Vector2[] polygon;
	public final Vector2 pos = new Vector2();
	public float rot = 0;
	public Category category = Category.ENTITY;
	public Team team = Team.NEUTRAL;
	public boolean bullet = false;
	public BodyType bodyType = BodyType.DynamicBody;
	public boolean sensor = false;
	public float density = 0f;
	
	public void setNoCollisionForce(){
		density = 1e30f;
	}
}
