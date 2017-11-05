package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.LaserShot;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class Minigun extends Gun{
	
	private static final float cooldown = 0.05f;
	private static final float spread = 0.25f;
	
	private float accumulator = cooldown;
	
	public Minigun(){
		super(10f);
	}
	
	@Override
	public void start(Entity entity) {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		if(shoot){
			while(accumulator >= cooldown){
				Core.entities.addEntity(new LaserShot(pos, new Vector2(50, 0).rotateRad(rot + MathUtils.randomTriangular(-spread, spread)), Color.GOLD, Team.FRIENDLY, 20f));
				Core.audio.playSound(SoundAsset.Shoot, 0.1f);
				accumulator -= cooldown;
			}
		}else{
			if(accumulator > cooldown)accumulator = cooldown;
		}
	}

	@Override
	public void end() {
		
	}
}
