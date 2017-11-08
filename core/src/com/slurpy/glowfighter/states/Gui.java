package com.slurpy.glowfighter.states;

import com.badlogic.gdx.math.Vector2;

public abstract class Gui {
	
	private int width, height;
	
	public abstract void draw();
	
	public final void resize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	protected final float getWidth(){
		return width;
	}
	
	protected final float getHeight(){
		return height;
	}
	
	protected final class Position{
		
		protected float rx;
		protected float ry;
		protected float x;
		protected float y;

		private final Vector2 pos = new Vector2();

		protected Position(float rx, float ry, float x, float y){
			this.rx = rx;
			this.ry = ry;
			this.x = x;
			this.y = y;
		}

		protected Vector2 getPosition(){
			return pos.set(rx * width, ry * height).add(x, y);
		}
	}
}
