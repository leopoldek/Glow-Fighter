package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class LinePart extends Part{
	
	public Vector2 start;
	public Vector2 end;
	public float width;
	
	private Vector2 startPos = new Vector2();
	private Vector2 endPos = new Vector2();
	
	public LinePart(Vector2 start, Vector2 end, float width) {
		this.start = start;
		this.end = end;
		this.width = width;
	}

	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		startPos.set(start);
		endPos.set(end);
		startPos.rotateRad(rot);
		endPos.rotateRad(rot);
		startPos.add(pos);
		endPos.add(pos);
		
		Core.graphics.drawLine(startPos, endPos, width, color);
	}
}