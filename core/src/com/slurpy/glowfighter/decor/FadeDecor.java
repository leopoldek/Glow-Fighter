package com.slurpy.glowfighter.decor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class FadeDecor extends Decor{
	
	private float alpha = 1f;
	private final Color tempColor = new Color();
	
	public FadeDecor(DecorDef def) {
		super(def);
	}
	
	@Override
	public void update() {
		alpha -= Gdx.graphics.getDeltaTime();
		if(alpha <= 0f)delete();
	}

	@Override
	public void draw() {
		if(part.visible){
			tempColor.set(color);
			tempColor.a *= alpha;
			part.draw(pos, rot, tempColor);
		}
	}
}
