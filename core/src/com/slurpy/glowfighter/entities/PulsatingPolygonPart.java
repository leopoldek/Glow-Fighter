package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PulsatingPolygonPart extends PolygonPart{
	
	private float time = 0;
	public float period;
	public float min;
	public float max;
	
	public PulsatingPolygonPart(Vector2[] points, float width, Color color, float period, float min, float max) {
		super(points, width, color);
		this.period = period;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void draw(Vector2 pos, float rot) {
		time += Gdx.graphics.getRawDeltaTime() / period;
		color.a = (max - min) * Math.abs(MathUtils.sin(time * MathUtils.PI)) + min;
		super.draw(pos, rot);
	}
}
