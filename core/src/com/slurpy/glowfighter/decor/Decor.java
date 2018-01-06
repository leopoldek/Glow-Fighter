package com.slurpy.glowfighter.decor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;

public abstract class Decor {
	
	protected Part[] parts;
	protected Color[] colors;
	
	public final Vector2 pos;
	public float rot;
	
	private boolean deleted;
	
	public Decor(DecorDef def){
		pos = new Vector2(def.pos);
		rot = def.rot;
		parts = def.parts;
		colors = def.colors;
	}
	
	public abstract void update();
	
	public void draw(){
		for(int i = 0; i < parts.length; i++){
			Part part = parts[i];
			if(part.visible)
				part.draw(pos, rot, colors[i]);
		}
	}
	
	public void delete(){
		deleted = true;
	}
	
	public boolean isDeleted(){
		return deleted;
	}
}
