package com.slurpy.glowfighter.entities;

import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DeceptivePolygonPart extends PolygonPart{
	
	public int amount;
	public float stayTime;
	public float range;
	public boolean drawReal;
	
	private Array<Vector2> pastPos;//Change to linked queue later.
	private Array<Float> pastRot;
	private Array<Float> alphas;
	
	public DeceptivePolygonPart(Vector2[] points, float width, Color color, int amount, float stayTime, float range, boolean drawReal) {
		super(points, width, color);
		this.amount = amount;
		this.stayTime = stayTime;
		this.range = range;
		this.drawReal = drawReal;
		pastPos = new Array<>();
		pastRot = new Array<>();
		alphas = new Array<>();
	}
	
	public void draw(Vector2 pos, float rot){
		if(pastPos.size < Math.min(random(amount), random(amount))){//TODO Could be improved
			float random = random(PI2);
			float randomRange = random(range);
			pastPos.add(new Vector2(pos).add(randomRange * cos(random), randomRange * sin(random)));
			pastRot.add(rot);
			alphas.add(1f);
		}
		
		float temp = color.a;
		for(int i = 0; i < pastPos.size; i++){
			color.a = alphas.get(i);
			super.draw(pastPos.get(i), pastRot.get(i));
			alphas.set(i, alphas.get(i) - (Gdx.graphics.getRawDeltaTime() / stayTime));
			
			if(alphas.get(i) <= 0){
				pastPos.removeIndex(i);
				pastRot.removeIndex(i);
				alphas.removeIndex(i);
				i--;
			}
		}
		
		color.a = temp;
		if(drawReal)
			super.draw(pos, rot);
	}
}
