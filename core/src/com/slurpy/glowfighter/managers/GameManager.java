package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.GunPickup;
import com.slurpy.glowfighter.entities.LineWall;
import com.slurpy.glowfighter.entities.enemies.BallLaunchingEnemy;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;
import com.slurpy.glowfighter.guns.BurstGun;
import com.slurpy.glowfighter.guns.RocketLauncher;
import com.slurpy.glowfighter.guns.RocketRepeater;
import com.slurpy.glowfighter.guns.Shotgun;
import com.slurpy.glowfighter.utils.Difficulty;

public class GameManager {
	
	private static final Difficulty difficulty = Difficulty.normal;
	
	private static final float length = 70;
	private static final float height = 50;
	private static final float wallWidth = 5;
	private static final float spawnRange = Math.min(length, height) - 5f;
	
	private float timer;
	private float accumulator = 0;
	private float ticks = 0;
	private int level = 1;
	
	private GunPickup pickup;
	
	public GameManager(){
		//Walls
		//Core.entities.addEntity(new Wall(new Vector2(0, height), new Vector2(length - width, width), 0f, Color.WHITE));//Top
		//Core.entities.addEntity(new Wall(new Vector2(0, -height), new Vector2(length - width, width), 0f, Color.WHITE));//Bot
		//Core.entities.addEntity(new Wall(new Vector2(length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Right
		//Core.entities.addEntity(new Wall(new Vector2(-length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Left
		Core.entities.addEntity(new LineWall(new Vector2(0, height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(0, -height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(-length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		
		timer = Core.properties.getDifficultyProperty("SpawnRate", difficulty);
		
		spawnPickup();
	}
	
	public void update(){
		if(pickup.isDeleted()){
			spawnPickup();
		}
		
		accumulator += Gdx.graphics.getDeltaTime();
		while(accumulator > timer){
			Core.entities.addEntity(new MissileEnemy(new Vector2(MathUtils.randomTriangular(-spawnRange, spawnRange), MathUtils.randomTriangular(-spawnRange, spawnRange)), MathUtils.random(MathUtils.PI2)));
			accumulator -= timer;
			ticks++;
			if(ticks > 200){
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(spawnRange, 0)));
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(-spawnRange, 0)));
				ticks = 0;
			}
		}
		timer = 1 / (1 / timer + 0.0000001f / Gdx.graphics.getDeltaTime());
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
	
	public int getLevel(){
		return level;
	}
}
