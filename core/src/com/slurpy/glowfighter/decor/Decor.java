package com.slurpy.glowfighter.decor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;

public abstract class Decor {
	
	protected Part part;
	protected Color color;
	
	public final Vector2 pos;
	public float rot;
	
	private boolean deleted;
	
	public Decor(DecorDef def){
		pos = new Vector2(def.pos);
		rot = def.rot;
		part = def.part;
		color = def.color;
	}
	
	public abstract void update();
	
	public void draw(){
		if(part.visible)part.draw(pos, rot, color);
	}
	
	public void delete(){
		deleted = true;
	}
	
	public boolean isDeleted(){
		return deleted;
	}
}
