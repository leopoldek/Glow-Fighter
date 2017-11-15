package com.slurpy.glowfighter.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;
import com.slurpy.glowfighter.utils.Util;

public class Button{
	
	public final String text;//TODO Make private and non-final later and recalculate fontWidth/Height when setter called.
	public final Position position;
	public int w, h;
	public final float fontW, fontH;
	public Color color;
	public final float size;
	public float lineWidth;
	
	public Button(String text, Position pos, int w, int h, Color color, float size, float lineWidth) {
		this.text = text;
		this.position = pos;
		this.w = w;
		this.h = h;
		Vector2 fontSize = Util.getTextSize(FontAsset.CatV, text, size);
		fontW = fontSize.x;
		fontH = fontSize.y;
		this.color = color.cpy();
		this.size = size;
		this.lineWidth = lineWidth;
	}
	
	public boolean contains(int x, int y){
		Vector2 pos = position.getPosition();
		return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
	}
	
	public void draw(){
		Vector2 pos = position.getPosition();
		Core.graphics.drawRectangle(pos.x, pos.y, w, h, lineWidth, color);
		pos.add(w/2, h/2).sub(fontW/2, -fontH/2);
		Core.graphics.drawText(text, pos, size, color);
	}
	
	public void animateColor(int x, int y, Color selected, Color normal){
		if(contains(x, y)){
			color.set(selected);
		}else{
			color.lerp(normal, 1.5f * Gdx.graphics.getDeltaTime());
		}
	}
}
