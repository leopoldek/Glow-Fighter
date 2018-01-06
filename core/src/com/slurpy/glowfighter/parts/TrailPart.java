package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TrailPart extends Part{
	
	public Part part;
	public float stayTime;
	public float length;
	
	private Array<Vector2> pastPos;
	private Array<Float> pastRot;//TODO Change to linked queue later or FloatArray
	private Array<Float> alphas;
	private Array<Part> parts;
	
	private Vector2 lastPos;
	
	private final Color tempColor = new Color();
	
	public TrailPart(Part part, float stayTime, float length){
		this.part = part;
		this.stayTime = stayTime;
		this.length = length;
		pastPos = new Array<>();
		pastRot = new Array<>();
		alphas = new Array<>();
		parts = new Array<>();
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		if(lastPos == null){
			lastPos = new Vector2(pos);
			part.draw(pos, rot, color);
			return;
		}
		
		if(lastPos.dst2(pos) > length * length){
			lastPos.set(pos);
			pastPos.add(new Vector2(pos));
			pastRot.add(rot);
			alphas.add(1.0f);
			parts.add(part.clone());
		}
		
		tempColor.set(color);
		for(int i = 0; i < pastPos.size; i++){
			tempColor.a = alphas.get(i) * color.a;
			parts.get(i).draw(pastPos.get(i), pastRot.get(i), tempColor);
			alphas.set(i, alphas.get(i) - (Gdx.graphics.getDeltaTime() / stayTime));
			if(alphas.get(i) <= 0){
				pastPos.removeIndex(i);
				pastRot.removeIndex(i);
				alphas.removeIndex(i);
				parts.removeIndex(i);
				i--;
			}
		}
		
		part.draw(pos, rot, color);
	}

	@Override
	public TrailPart clone() {
		return new TrailPart(part.clone(), stayTime, length);
	}
}
