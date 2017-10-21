package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.enemies.BallLaunchingEnemy;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;

public class Spawner {
	
	private static final float spawnRange = 20f;
	
	private float timer;
	
	private float accumulator = 0;
	
	private float ticks = 0;
	
	public Spawner(float timer){
		this.timer = timer;
	}
	
	public void spawn(float delta){
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
		timer = 1 / (1 / timer + 0.000001f / delta);
	}
}
