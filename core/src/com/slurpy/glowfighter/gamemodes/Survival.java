package com.slurpy.glowfighter.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.GunPickup;
import com.slurpy.glowfighter.entities.LineWall;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.enemies.BallLaunchingEnemy;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;
import com.slurpy.glowfighter.guns.BurstGun;
import com.slurpy.glowfighter.guns.Gun;
import com.slurpy.glowfighter.guns.RocketLauncher;
import com.slurpy.glowfighter.guns.RocketRepeater;
import com.slurpy.glowfighter.guns.Shotgun;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.utils.Util;
import com.slurpy.glowfighter.utils.animation.AnimationBuilder;
import com.slurpy.glowfighter.utils.animation.KeyFrame;

public class Survival implements Gamemode{
	
	private static final float length = 70;
	private static final float height = 50;
	private static final float wallWidth = 5;
	private static final float spawnRange = Math.min(length, height) - 5f;
	
	private final SurvivalGui gui;
	
	private float timer;
	private float accumulator = 0;
	private float ticks = 0;
	private int level = 1;
	
	private GunPickup pickup;
	
	public Survival(){
		gui = new SurvivalGui();
	}

	@Override
	public void start(){
		Core.entities.addEntity(new LineWall(new Vector2(0, height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(0, -height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(-length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		
		Player player = new Player(new Vector2(), 0);
		Core.entities.addEntity(player, "player");
		Core.graphics.follow(player);
		//graphics.look(new Vector2(100, 100));
		
		Music music = Core.audio.getMusic(MusicAsset.BackgroundTechno);
		music.setLooping(true);
		music.play();
		
		//timer = Core.properties.getDifficultyProperty("SpawnRate", difficulty);
		timer = 0.2f;
		
		spawnPickup();
	}

	@Override
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
				gui.animateNextLevel();
				ticks = 0;
			}
		}
		timer = 1 / (1 / timer + 0.0000001f / Gdx.graphics.getDeltaTime());
	}
	
	private void spawnPickup(){
		if(pickup != null)pickup.delete();
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

	@Override
	public Gui getGui(){
		return gui;
	}
	
	public class SurvivalGui extends Gui{
		private static final float INDICATOR_SIZE = 15;
		
		private final Position healthBarPos = new Position(50, 50, Anchor.end, Anchor.start);
		private final Position gunStatsPos = new Position(50, 50, Anchor.start, Anchor.start);
		private final Position fpsPos = new Position(10, 10, Anchor.start, Anchor.end);
		private final Position levelPos = new Position(-40, 10, Anchor.center, Anchor.end);
		private final PolygonPart arrowIndicator = new PolygonPart(new Vector2[]{
				new Vector2(INDICATOR_SIZE, 0),
				new Vector2(-INDICATOR_SIZE, -INDICATOR_SIZE),
				new Vector2(-INDICATOR_SIZE, INDICATOR_SIZE)
		}, 6);
		
		private float levelTextSize = 32;
		
		public SurvivalGui(){
			
		}
		
		@Override
		public void draw(){
			Player player = (Player)Core.entities.getGroup("player").iterator().next();
			
			Vector2 playerPos = Core.graphics.project(player.getPosition());
			Vector2 pickupPos = Core.graphics.project(pickup.getPosition());
			if(!Util.isInsideRect(playerPos, pickupPos, width, height)){
				Vector2 indicatorPos = Util.getBoundryPoint(playerPos, pickupPos, width - 40, height - 40);
				arrowIndicator.draw(indicatorPos, pickupPos.sub(playerPos).angleRad(), Color.LIME);
			}
			
			Vector2 start = healthBarPos.getPosition();
			float health = player.getHealth();
			Vector2 end = new Vector2(start).add(health * -2, 0);
			Core.graphics.drawLine(start, end, 20f, health <= 0 ? Color.GRAY : Color.RED);
			
			start = gunStatsPos.getPosition();
			if(!player.usingDefault()){
				Gun gun = player.getGun();
				float time = gun.getTimeLeft() / gun.maxTime;
				end.set(start).add(time * 200, 0);
				Core.graphics.drawLine(start, end, 20f, Color.RED.cpy().lerp(Color.CHARTREUSE, time));
				Core.graphics.drawText(gun.getName(), start.add(0, 40), 32, Color.NAVY);
			}
			
			Core.graphics.drawText(Integer.toString(Gdx.graphics.getFramesPerSecond()), fpsPos.getPosition(), 24, Color.WHITE);
			Core.graphics.drawText("Level " + level, levelPos.getPosition(), levelTextSize, Color.WHITE);
		}
		
		public void animateNextLevel(){
			AnimationBuilder builder = new AnimationBuilder();
			builder.addKeyFrame(new KeyFrame(){//TODO Clean up and make pretty!
				@Override
				public void start() {}
				@Override
				public void act(float progress, float frameProgress) {
					levelPos.x = Interpolation.circleOut.apply(-40, -55, frameProgress);
					levelPos.y = Interpolation.circleOut.apply(10, 300, frameProgress);
					levelTextSize = Interpolation.circleOut.apply(32, 58, frameProgress);
				}
				@Override
				public void end() {
					levelPos.x = -55;
					levelPos.y = 300;
					levelTextSize = 58;
					level++;
				}
			}, 1.5f);
			builder.addKeyFrame(new KeyFrame(){
				@Override
				public void start() {}
				@Override
				public void act(float progress, float frameProgress) {}
				@Override
				public void end() {}
			}, 1.2f);
			builder.addKeyFrame(new KeyFrame(){
				@Override
				public void start() {}
				@Override
				public void act(float progress, float frameProgress) {
					levelPos.x = Interpolation.circleOut.apply(-55, -40, frameProgress);
					levelPos.y = Interpolation.circleOut.apply(300, 10, frameProgress);
					levelTextSize = Interpolation.circleOut.apply(58, 32, frameProgress);
				}
				@Override
				public void end() {
					levelPos.x = -40;
					levelPos.y = 10;
					levelTextSize = 32;
				}
			}, 1.5f);
			Core.tasks.addAnimation(builder);
		}
	}
}
