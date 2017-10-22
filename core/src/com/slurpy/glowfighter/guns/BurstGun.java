package com.slurpy.glowfighter.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.LaserShot;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;

public class BurstGun extends Gun{
	
	private static float BURST_INTERVAL = 0.05f;
	private static float COOLDOWN = 0.8f;
	private static int BURST_COUNT = 4;
	
	private float accumulator = 0f;
	private float burstAccumulator = 0f;
	private int shotsLeft = 0;

	public BurstGun(Entity entity) {
		super(entity);
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update(boolean shoot, Vector2 pos, float rot) {
		accumulator += Gdx.graphics.getDeltaTime();
		if(shoot){
			while(accumulator >= COOLDOWN){
				shotsLeft += BURST_COUNT;
				accumulator -= COOLDOWN;
			}
		}else{
			if(accumulator > COOLDOWN)accumulator = COOLDOWN;
		}
		
		burstAccumulator += Gdx.graphics.getDeltaTime();
		if(shotsLeft != 0){
			while(burstAccumulator >= BURST_INTERVAL){
				Color color = Color.BLUE.cpy().add(0.2f * (4 - shotsLeft), 0.2f * (4 - shotsLeft), 0, 0);
				Core.entities.addEntity(new LaserShot(pos, new Vector2(50, 0).rotateRad(rot), color, Team.FRIENDLY, 45f));
				Core.audio.playSound(SoundAsset.Shoot, 0.2f);
				shotsLeft--;
				burstAccumulator -= BURST_INTERVAL;
			}
		}else{
			if(burstAccumulator > BURST_INTERVAL)burstAccumulator = BURST_INTERVAL;
		}
	}

	@Override
	public void end() {
		
	}
}
