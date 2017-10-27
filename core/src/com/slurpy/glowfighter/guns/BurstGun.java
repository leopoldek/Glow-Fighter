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
	
	private static final float burstInterval = 0.05f;
	private static final float cooldown = 0.8f;
	private static final int burstCount = 4;//If you edit this edit the color chooser in update(...) also
	
	private float accumulator = cooldown;
	private float burstAccumulator = 0f;
	private int shotsLeft = 0;
	
	public BurstGun(){
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
				shotsLeft += burstCount;
				accumulator -= cooldown;
			}
		}else{
			if(accumulator > cooldown)accumulator = cooldown;
		}
		
		burstAccumulator += Gdx.graphics.getDeltaTime();
		if(shotsLeft != 0){
			while(burstAccumulator >= burstInterval){
				Color color = Color.BLUE.cpy().add(0.2f * (4 - shotsLeft), 0.2f * (4 - shotsLeft), 0, 0);
				Core.entities.addEntity(new LaserShot(pos, new Vector2(50, 0).rotateRad(rot), color, Team.FRIENDLY, 45f));
				Core.audio.playSound(SoundAsset.Shoot, 0.2f);
				shotsLeft--;
				burstAccumulator -= burstInterval;
			}
		}else{
			if(burstAccumulator > burstInterval)burstAccumulator = burstInterval;
		}
	}

	@Override
	public void end() {
		
	}
}
