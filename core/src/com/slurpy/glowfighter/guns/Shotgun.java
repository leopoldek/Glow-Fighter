package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.BuckShot;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class Shotgun extends Gun{
	
	private static final float cooldown = 0.7f;
	private static final float spread = 0.3f;
	private static final int shots = 17;
	
	private float accumulator = cooldown;
	
	public Shotgun(){
		super(20f);
	}
	
	@Override
	public void start(Entity entity) {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		if(shoot){
			while(accumulator > cooldown){
				for(int i = 0; i < shots; i++){
					Core.entities.addEntity(new BuckShot(pos,
							new Vector2(MathUtils.random(50f, 75f), 0).rotateRad(rot + MathUtils.random(-spread, spread)),
							Color.FIREBRICK, Team.FRIENDLY, 15f));
				}
				Core.audio.playSound(SoundAsset.Shoot, 0.2f);
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
