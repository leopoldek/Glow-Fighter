package com.slurpy.glowfighter.decor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;

public class DecorDef {
	public Part[] parts;
	public Color[] colors;
	public final Vector2 pos = new Vector2();
	public float rot = 0;
	
	public void setColor(Color color){
		colors = new Color[parts.length];
		for(int i = 0; i < colors.length; i++){
			colors[i] = color;
		}
	}
}
