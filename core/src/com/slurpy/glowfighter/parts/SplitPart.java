package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SplitPart extends Part{
	
	public Part first, second;
	public Color firstColor, secondColor;
	
	public SplitPart(Part first, Part second, Color firstColor, Color secondColor){
		this.first = first;
		this.second = second;
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	
	public SplitPart(Part first, Part second, Color firstColor){
		this(first, second, firstColor, null);
	}
	
	public SplitPart(Part first, Part second){
		this(first, second, null, null);
	}
	
	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		first.draw(pos, rot, firstColor == null ? color : firstColor);
		second.draw(pos, rot, secondColor == null ? color : secondColor);
	}

	@Override
	public SplitPart clone() {
		return new SplitPart(first.clone(), second.clone(), firstColor.cpy(), secondColor.cpy());
	}
}
