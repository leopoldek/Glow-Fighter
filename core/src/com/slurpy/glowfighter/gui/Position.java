package com.slurpy.glowfighter.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public final class Position{
	
	public float rx;
	public float ry;
	public float x;
	public float y;

	private final Vector2 pos = new Vector2();

	public Position(float rx, float ry, float x, float y){
		this.rx = rx;
		this.ry = ry;
		this.x = x;
		this.y = y;
	}

	public Vector2 getPosition(){
		return pos.set(rx * Gdx.graphics.getWidth(), ry * Gdx.graphics.getHeight()).add(x, y);
	}
}
