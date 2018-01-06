package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class LinePart extends Part{
	
	public final Vector2 start;
	public final Vector2 end;
	public float width;
	
	private final Vector2 startTemp = new Vector2();
	private final Vector2 endTemp = new Vector2();
	
	public LinePart(Vector2 start, Vector2 end, float width) {
		this.start = start;
		this.end = end;
		this.width = width;
	}

	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		startTemp.set(start);
		endTemp.set(end);
		startTemp.rotateRad(rot);
		endTemp.rotateRad(rot);
		startTemp.add(pos);
		endTemp.add(pos);
		
		Core.graphics.drawLine(startTemp, endTemp, width, color);
	}

	@Override
	public LinePart clone() {
		return new LinePart(start.cpy(), end.cpy(), width);
	}
}
