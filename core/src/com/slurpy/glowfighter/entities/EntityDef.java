package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;

public class EntityDef {
	public Part[] parts;
	public Color[] colors;
	public Vector2[] polygon;
	public final Vector2 pos = new Vector2();
	public float rot = 0;
	public Category category = Category.ENTITY;
	public Team team = Team.NEUTRAL;
	public boolean bullet = false;
	
	public void setColor(Color color){
		colors = new Color[parts.length];
		for(int i = 0; i < colors.length; i++){
			colors[i] = color;
		}
	}
}
