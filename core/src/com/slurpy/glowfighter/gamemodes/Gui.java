package com.slurpy.glowfighter.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class Gui {
	
	protected int width, height;
	
	public abstract void draw();
	
	public final void resize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	protected final void drawIndicator(float angle, float radius, Color color){
		//TODO Draw indicator
		throw new UnsupportedOperationException();
	}
	
	protected class Position{
		
		float x;
		float y;
		Anchor horizontal;
		Anchor vertical;

		private final Vector2 pos = new Vector2();

		Position(float x, float y, Anchor horizontal, Anchor vertical){
			this.x = x;
			this.y = y;
			this.horizontal = horizontal;
			this.vertical = vertical;
		}

		Vector2 getPosition(){
			if (horizontal == Anchor.start) {
				pos.x = x;
			} else if (horizontal == Anchor.center) {
				pos.x = x + width / 2;
			} else {
				pos.x = width - x;
			}

			if (vertical == Anchor.start) {
				pos.y = y;
			} else if (vertical == Anchor.center) {
				pos.y = y + height / 2;
			} else {
				pos.y = height - y;
			}
			return pos;
		}
	}
	
	protected enum Anchor{
		start, center, end
	}
}
