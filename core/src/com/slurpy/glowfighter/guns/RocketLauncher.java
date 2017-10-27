package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.Rocket;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class RocketLauncher extends Gun{
	
	private float accumulator = 0f;

	public RocketLauncher(Entity entity) {
		super(entity);
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		float cooldown = 2.0f;
		if(shoot){
			while(accumulator >= cooldown){
				Core.entities.addEntity(new Rocket(pos, new Vector2(50, 0).rotateRad(rot), Color.GRAY, Color.GOLDENROD, Team.FRIENDLY, 350f, 7.5f));
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
