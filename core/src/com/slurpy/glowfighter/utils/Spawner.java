package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;

public class Spawner {
	
	private static final float spawnRange = 20f;
	
	private final float timer;
	
	private float accumulator = 0;
	
	public Spawner(float timer){
		this.timer = timer;
	}
	
	public void spawn(float delta){
		accumulator += delta;
		while(accumulator > timer){
			Core.entities.addEntity(new MissileEnemy(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), MathUtils.random(MathUtils.PI2)));
			accumulator = 0;
		}
	}
}
