package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.Rocket;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class RocketRepeater extends Gun{
	
	private static final float cooldown = 0.25f;
	
	private float accumulator = cooldown;
	
	public RocketRepeater(){
		super(15f);
	}
	
	@Override
	public void start(Entity entity) {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		if(shoot){
			while(accumulator >= cooldown){
				Core.entities.addEntity(new Rocket(pos, new Vector2(50, 0).rotateRad(rot), Color.GRAY, Color.RED, Team.FRIENDLY, 40f, 2.5f));
				Core.audio.playSound(SoundAsset.Shoot);
				accumulator -= cooldown;
			}
		}else{
			if(accumulator > cooldown)accumulator = cooldown;
		}
	}

	@Override
	public void end() {
		
	}

	@Override
	public String getName() {
		return "Rocket Repeater";
	}
}
