package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PulsatingPart extends Part{
	
	protected Part part;
	protected float period;
	protected float min;
	protected float max;
	
	private float time = 0;
	private Color tempColor = new Color();
	
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
		tempColor.a = (max - min) * Math.abs(MathUtils.sin(time * MathUtils.PI)) + min;
		part.draw(pos, rot, tempColor);
	}
}
