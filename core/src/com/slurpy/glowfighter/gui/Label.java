package com.slurpy.glowfighter.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;
import com.slurpy.glowfighter.utils.Util;

public class Label {
	private String text;
	public final Position position;
	private float fontW, fontH;
	public final Color color;
	private float size;
	
	public Label(String text, Position position, Color color, float size) {
		this.text = text;
		this.position = position;
		Vector2 fontSize = Util.getTextSize(FontAsset.CatV, text, size);
		fontW = fontSize.x;
		fontH = fontSize.y;
		this.color = color.cpy();
		this.size = size;
	}
	
	public void draw(){
		Vector2 pos = position.getPosition();
		pos.sub(fontW/2, -fontH/2);
		Core.graphics.drawText(text, pos, size, color);
	}
	
	public void setText(String text, float size){
		this.text = text;
		this.size = size;
		Vector2 fontSize = Util.getTextSize(FontAsset.CatV, text, size);
		fontW = fontSize.x;
		fontH = fontSize.y;
	}
	
	public String getText(){
		return text;
	}

	public float getFontW() {
		return fontW;
	}

	public float getFontH() {
		return fontH;
	}

	public float getSize() {
		return size;
	}
}
