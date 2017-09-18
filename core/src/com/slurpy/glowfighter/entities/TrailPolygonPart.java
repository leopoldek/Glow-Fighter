package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class TrailPolygonPart implements Part {
	
	public PolygonPart part;
	public float stayTime;
	public float length;
	public Color color;
	
	private Array<Vector3> pastPos;//Change to linked queue later.
	private Array<Float> alphas;
	
	private Vector3 last;
	
	private Vector2 tempPos = new Vector2();
	
	public TrailPolygonPart(Vector2[] points, float width, Color color, float stayTime, float length){
		part = new PolygonPart(points, width, new Color(color));
		this.stayTime = stayTime;
		this.length = length;
		this.color = color;
		pastPos = new Array<>();
		alphas = new Array<>();
	}
	
	@Override
	public void draw(Vector2 pos, float rot) {
		if(last == null){
			last = new Vector3(pos, rot);
			return;
		}
		
		if(Vector2.dst(last.x, last.y, pos.x, pos.y) > length){
			last.set(pos, rot);
			pastPos.add(new Vector3(pos, rot));
			alphas.add(1.0f);
		}
		
		for(int i = 0; i < pastPos.size; i++){
			Vector3 past = pastPos.get(i);
			part.color.a = alphas.get(i);
			part.draw(tempPos.set(past.x, past.y), past.z);
			alphas.set(i, alphas.get(i) - (Gdx.graphics.getDeltaTime() / stayTime));
			if(alphas.get(i) <= 0){
				pastPos.removeIndex(i);
				alphas.removeIndex(i);
				i--;
			}
		}
		
		part.color.set(color);
		part.draw(pos, rot);
	}
}
