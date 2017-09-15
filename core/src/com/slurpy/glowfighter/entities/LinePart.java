package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

class LinePart implements Part {
	
	private Vector2 start;
	private Vector2 end;
	private float width;
	private Color color;
	
	private Vector2 startPos = new Vector2();
	private Vector2 endPos = new Vector2();
	
	public LinePart(Vector2 start, Vector2 end, float width, Color color) {
		this.start = start;
		this.end = end;
		this.width = width;
		this.color = color;
	}

	@Override
	public void draw(Vector2 pos, float rot) {
		startPos.set(start);
		endPos.set(end);
		startPos.rotate(rot);
		endPos.rotate(rot);
		startPos.add(pos);
		endPos.add(pos);
		
		Core.graphics.drawLine(startPos, endPos, width, color);
	}

}
