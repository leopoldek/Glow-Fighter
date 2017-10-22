package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.LaserShot;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class PeaShooter extends Gun{
	
	private float accumulator = 0f;

	public PeaShooter(Entity entity) {
		super(entity);
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		float cooldown = 0.1f;
		if(shoot){
			while(accumulator > cooldown){
				Core.entities.addEntity(new LaserShot(pos, new Vector2(50, 0).rotateRad(rot), Color.GOLD, Team.FRIENDLY, 45f));
				Core.audio.playSound(SoundAsset.Shoot, 0.2f);
				accumulator -= cooldown;
			}
		}else{
			accumulator = cooldown;
		}
	}

	@Override
	public void end() {
		
	}
}