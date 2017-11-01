package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.GunPickup;
import com.slurpy.glowfighter.entities.enemies.BallLaunchingEnemy;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;
import com.slurpy.glowfighter.guns.BurstGun;
import com.slurpy.glowfighter.guns.RocketLauncher;
import com.slurpy.glowfighter.guns.RocketRepeater;
import com.slurpy.glowfighter.guns.Shotgun;

public class Spawner {
	
	private static final float spawnRange = 20f;
	
	private float timer;
	private float accumulator = 0;
	private float ticks = 0;
	
	private GunPickup pickup;
	
	public Spawner(float timer){
		this.timer = timer;
		spawnPickup();
	}
	
	public void spawn(float delta){
		if(pickup.isDeleted()){
			spawnPickup();
		}
		
		accumulator += delta;
		while(accumulator > timer){
			Core.entities.addEntity(new MissileEnemy(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), MathUtils.random(MathUtils.PI2)));
			accumulator -= timer;
			ticks++;
			if(ticks > 200){
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(40, 0)));
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(-40, 0)));
				ticks = 0;
			}
		}
		timer = 1 / (1 / timer + 0.0000001f / delta);
	}
	
	private void spawnPickup(){
		int random = MathUtils.random(3);
		if(random == 0){
			pickup = new GunPickup(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), new Shotgun());
		}else if(random == 1){
			pickup = new GunPickup(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), new BurstGun());
		}else if(random == 2){
			pickup = new GunPickup(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), new RocketLauncher());
		}else{
			pickup = new GunPickup(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), new RocketRepeater());
		}
		Core.entities.addEntity(pickup);
	}
}
