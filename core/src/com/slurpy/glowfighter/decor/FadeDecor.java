package com.slurpy.glowfighter.decor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.slurpy.glowfighter.parts.Part;

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
		for(int i = 0; i < parts.length; i++){
			Part part = parts[i];
			if(part.visible){
				tempColor.set(colors[i]);
				tempColor.a *= alpha;
				part.draw(pos, rot, tempColor);
			}
		}
	}
}
