package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class TexturePart extends Part {
	
	private TextureRegion region;
	private Vector2 pos;
	private Vector2 size;
	private Vector2 origin;
	private float rot;
	
	private final Vector2 tempPos = new Vector2();
	
	public TexturePart(TextureRegion region, Vector2 pos, Vector2 size, Vector2 origin, float rot){
		this.region = region;
		this.pos = pos;
		this.size = size;
		this.origin = origin;
		this.rot = rot;
	}

	public TexturePart(TextureRegion region, Vector2 pos, Vector2 size, float rot) {
		this.region = region;
		this.pos = pos;
		this.size = size;
		origin = new Vector2(size).scl(0.5f);
		this.rot = rot;
	}

	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		tempPos.set(this.pos).rotateRad(rot).sub(origin).add(pos);
		Core.graphics.drawTexture(region, tempPos, size, origin, rot + this.rot);
	}
}
