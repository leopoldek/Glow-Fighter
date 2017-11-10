package com.slurpy.glowfighter.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.Util;

public class Slider{
	
	public static final float width = 8f;
	public static final float sliderWidth = 12f;
	
	public final Position position;
	public final float length;
	
	public float sliderPosition = 1f;
	private boolean isMoving = false;
	
	public Slider(Position position, float length) {
		this.position = position;
		this.length = length;
	}
	
	public boolean sliderPressed(int x, int y){
		Vector2 pos = position.getPosition();
		Vector2 center = pos.cpy().add(length / 2, 0);
		if(!Util.isInsideRect(center, new Vector2(x, y), length + 2*sliderWidth, 2*sliderWidth))return false;
		sliderPosition = (x - pos.x) / length;
		sliderPosition = MathUtils.clamp(sliderPosition, 0f, 1f);
		isMoving = true;
		return true;
	}
	
	public boolean sliderDragged(int newX, int newY){
		if(!isMoving)return false;
		Vector2 pos = position.getPosition();
		sliderPosition = (newX - pos.x) / length;
		sliderPosition = MathUtils.clamp(sliderPosition, 0f, 1f);
		return true;
	}
	
	public boolean sliderReleased(){
		boolean released = isMoving;
		isMoving = false;
		return released;
	}
	
	public void draw(){
		Vector2 start = position.getPosition();
		Vector2 end = new Vector2(start).add(length, 0);
		Core.graphics.drawLine(start, end, width, Color.WHITE);
		
		end.set(start).add(length * sliderPosition, 0);
		Core.graphics.drawCircle(end, sliderWidth, Color.GRAY.cpy().lerp(Color.RED, sliderPosition));
	}
}
