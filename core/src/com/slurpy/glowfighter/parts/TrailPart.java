package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TrailPart extends Part{
	
	protected Part part;
	protected float stayTime;
	protected float length;//Squared length
	
	private Array<Vector2> pastPos;//Change to linked queue later.
	private Array<Float> pastRot;
	private Array<Float> alphas;
	
	private Vector2 lastPos;
	
	private Color tempColor = new Color();
	
	public TrailPart(Part part, float stayTime, float length){
		this.part = part;
		this.stayTime = stayTime;
		this.length = length * length;
		pastPos = new Array<>();
		pastRot = new Array<>();
		alphas = new Array<>();
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		if(lastPos == null){
			lastPos = new Vector2(pos);
			part.draw(pos, rot, color);
			return;
		}
		
		if(lastPos.dst2(pos) > length){
			lastPos.set(pos);
			pastPos.add(new Vector2(pos));
			pastRot.add(rot);
			alphas.add(1.0f);
		}
		
		tempColor.set(color);
		for(int i = 0; i < pastPos.size; i++){
			tempColor.a = alphas.get(i) * color.a;
			part.draw(pastPos.get(i), pastRot.get(i), tempColor);
			alphas.set(i, alphas.get(i) - (Gdx.graphics.getDeltaTime() / stayTime));
			if(alphas.get(i) <= 0){
				pastPos.removeIndex(i);
				pastRot.removeIndex(i);
				alphas.removeIndex(i);
				i--;
			}
		}
		
		part.draw(pos, rot, color);
	}
}
