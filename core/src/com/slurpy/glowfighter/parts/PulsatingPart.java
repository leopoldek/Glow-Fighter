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
	
	private float time = 1;
	private final Color tempColor = new Color();
	
	public PulsatingPart(Part part, float period, float min, float max) {
		this.part = part;
		this.period = period;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		time += Gdx.graphics.getDeltaTime() / period;
		tempColor.set(color);
		tempColor.a *= (max - min) * Math.abs(MathUtils.sin(time * MathUtils.PI)) + min;
		part.draw(pos, rot, tempColor);
	}

	@Override
	public PulsatingPart clone() {
		return new PulsatingPart(part.clone(), period, min, max);
	}
}
