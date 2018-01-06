package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PulsatingPart extends Part{
	
	public Part part;
	public float period;
	public float min;
	public float max;
	
	private float time;
	private final Color tempColor = new Color();
	
	public PulsatingPart(Part part, float period, float min, float max, float offset) {
		this.part = part;
		this.period = period;
		this.min = min;
		this.max = max;
		time = offset;
	}
	
	public PulsatingPart(Part part, float period, float min, float max) {
		this(part, period, min, max, 1f);
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		time += Gdx.graphics.getDeltaTime() / period;
		tempColor.set(color);
		tempColor.a *= (max - min) * Math.abs(MathUtils.sin(time * MathUtils.PI)) + min;
		part.draw(pos, rot, tempColor);
	}
	
	public float getTime(){
		return time;
	}

	@Override
	public PulsatingPart clone() {
		return new PulsatingPart(part.clone(), period, min, max);
	}
}
