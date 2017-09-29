package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class PolygonPart implements Part {
	
	public Vector2[] points;
	public float width;
	public Color color;
	
	private Vector2[] pointsPos;
	
	public PolygonPart(Vector2[] points, float width, Color color) {
		this.points = points;
		this.width = width;
		this.color = color;
		
		pointsPos = new Vector2[points.length];
		for(int i = 0; i < pointsPos.length; i++){
			pointsPos[i] = new Vector2();
		}
	}
	
	@Override
	public void draw(Vector2 pos, float rot) {
		for(int i = 0; i < points.length; i++){
			pointsPos[i].set(points[i]);
			pointsPos[i].rotateRad(rot);
			pointsPos[i].add(pos);
		}
		Core.graphics.drawPolygon(pointsPos, width, color);
	}
}
