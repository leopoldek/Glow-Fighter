package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class PolygonPart extends Part{
	
	public Vector2[] points;
	public float width;
	
	private Vector2[] pointsPos;
	
	public PolygonPart(Vector2[] points, float width){
		this.points = points;
		this.width = width;
		
		pointsPos = new Vector2[points.length];
		for(int i = 0; i < pointsPos.length; i++){
			pointsPos[i] = new Vector2();
		}
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		for(int i = 0; i < points.length; i++){
			pointsPos[i].set(points[i]);
			pointsPos[i].rotateRad(rot);
			pointsPos[i].add(pos);
		}
		Core.graphics.drawPolygon(pointsPos, width, color);
	}
}
