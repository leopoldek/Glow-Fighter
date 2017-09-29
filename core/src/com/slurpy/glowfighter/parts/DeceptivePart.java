package com.slurpy.glowfighter.parts;

import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DeceptivePart extends Part{
	
	protected Part part;
	protected int amount;
	protected float stayTime;
	protected float range;
	protected boolean drawReal;
	
	private Array<Vector2> pastPos;//Change to linked queue later.
	private Array<Float> pastRot;
	private Array<Float> alphas;
	
	private Color tempColor = new Color();
	
	public DeceptivePart(Part part, int amount, float stayTime, float range, boolean drawReal) {
		this.part = part;
		this.amount = amount;
		this.stayTime = stayTime;
		this.range = range;
		this.drawReal = drawReal;
		pastPos = new Array<>();
		pastRot = new Array<>();
		alphas = new Array<>();
	}
	
	public void draw(Vector2 pos, float rot, Color color){
		if(pastPos.size < Math.min(random(amount), random(amount))){//TODO Could be improved
			float random = random(PI2);
			float randomRange = random(range);
			pastPos.add(new Vector2(pos).add(randomRange * cos(random), randomRange * sin(random)));
			pastRot.add(rot);
			alphas.add(1f);
		}
		
		tempColor.set(color);
		for(int i = 0; i < pastPos.size; i++){
			tempColor.a = alphas.get(i) * color.a;
			part.draw(pastPos.get(i), pastRot.get(i), tempColor);
			alphas.set(i, alphas.get(i) - (Gdx.graphics.getRawDeltaTime() / stayTime));
			
			if(alphas.get(i) <= 0){
				pastPos.removeIndex(i);
				pastRot.removeIndex(i);
				alphas.removeIndex(i);
				i--;
			}
		}
		
		if(drawReal)
			part.draw(pos, rot, color);
	}
}
