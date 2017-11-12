package com.slurpy.glowfighter.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class Rectangle{
	
	public final Position position;
	public float w, h;
	public Color color;
	public final float lineWidth;
	
	public Rectangle(Position position, int w, int h, Color color, float lineWidth) {
		this.position = position;
		this.w = w;
		this.h = h;
		this.color = color;
		this.lineWidth = lineWidth;
	}
	
	public void draw(){
		Vector2 pos = position.getPosition();
		Core.graphics.drawRectangle(pos.x, pos.y, w, h, lineWidth, color);
	}
	
	public boolean contains(int x, int y){
		Vector2 pos = position.getPosition();
		return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
	}
}
