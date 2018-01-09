package com.slurpy.glowfighter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
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
import com.slurpy.glowfighter.entities.enemies.DiveStabber;
import com.slurpy.glowfighter.entities.enemies.MissileEnemy;
import com.slurpy.glowfighter.entities.enemies.SneakEnemy;
import com.slurpy.glowfighter.entities.enemies.TurretEnemy;
import com.slurpy.glowfighter.gui.Button;
import com.slurpy.glowfighter.gui.Gui;
import com.slurpy.glowfighter.gui.Label;
import com.slurpy.glowfighter.gui.Position;
import com.slurpy.glowfighter.gui.Rectangle;
import com.slurpy.glowfighter.guns.BurstGun;
import com.slurpy.glowfighter.guns.Gun;
import com.slurpy.glowfighter.guns.Minigun;
import com.slurpy.glowfighter.guns.RocketLauncher;
import com.slurpy.glowfighter.guns.RocketRepeater;
import com.slurpy.glowfighter.guns.Shotgun;
import com.slurpy.glowfighter.guns.SniperRifle;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.SoundType;
import com.slurpy.glowfighter.utils.Util;
import com.slurpy.glowfighter.utils.tasks.KeyFrame;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class Survival implements State, Gui, InputProcessor{
	
	private static final float length = 70;
	private static final float height = 50;
	private static final float wallWidth = 5;
	private static final float spawnRange = Math.min(length, height) - 5f;
	
	private static final int challengeLevel = 3;
	private static final int bossLevel = 5;
	
	private Player player;
	
	private float timer;
	private float accumulator = 0;
	private float ticks = 0;
	private int level = 1;
	
	private GunPickup pickup;
	
	private boolean gameOver = false;
	
	public Survival(){
		
	}

	@Override
	public void start(){
		Core.entities.addEntity(new LineWall(new Vector2(0, height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(0, -height), length, wallWidth, 0f, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(-length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		Core.entities.addEntity(new LineWall(new Vector2(length, 0), height, wallWidth, MathUtils.PI / 2, Color.WHITE));
		
		player = new Player(new Vector2(), 0);
		Core.entities.addEntity(player);
		Core.graphics.follow(player);
		//graphics.look(new Vector2(100, 100));
		
		final Music music = Core.audio.getMusic(MusicAsset.BackgroundTechno);
		music.setLooping(true);
		music.setVolume(0f);
		music.play();
		
		//timer = Core.properties.getDifficultyProperty("SpawnRate", difficulty);
		timer = 0.2f;
		
		spawnPickup();
		
		Core.bindings.addProcessor(this);
		
		Core.graphics.setZoom(300f);
		
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame((float progress, float frameProgress) -> {
			Core.graphics.setZoom(Interpolation.linear.apply(300f, 5f, frameProgress));
			music.setVolume(Interpolation.linear.apply(0f, Core.audio.getActualVolume(SoundType.music), progress));
		}, 0.7f);
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				Core.graphics.setZoom(Interpolation.exp10Out.apply(5f, 1f, frameProgress));
				music.setVolume(Interpolation.linear.apply(0f, Core.audio.getActualVolume(SoundType.music), progress));
			}
			@Override
			public void end() {
				Core.graphics.setZoom(1f);
				music.setVolume(Core.audio.getActualVolume(SoundType.music));
			}
		}, 1.5f);
		Core.tasks.addTask(builder);
	}

	@Override
	public void update(){
		if(pickup.isDeleted()){
			spawnPickup();
		}
		
		accumulator += Gdx.graphics.getDeltaTime();
		while(accumulator > timer){
			if(level % challengeLevel != 0){
				Core.entities.addEntity(new MissileEnemy(Util.randomTriangularVector(spawnRange), MathUtils.random(MathUtils.PI2), player));
				if(MathUtils.randomBoolean(0.04f))Core.entities.addEntity(new DiveStabber(Util.randomTriangularVector(spawnRange), player));
				if(MathUtils.randomBoolean(ticks / 3000f))Core.entities.addEntity(new SneakEnemy(Util.randomTriangularVector(spawnRange), MathUtils.random(MathUtils.PI2), player));
			}else{
				Core.entities.addEntity(new SneakEnemy(Util.randomTriangularVector(spawnRange), MathUtils.random(MathUtils.PI2), player));
			}
			
			accumulator -= timer;
			ticks++;
			if(ticks > 200){
				animateNextLevel();
				ticks = 0;
			}
		}
		timer = 1 / (1 / timer + 0.0000001f / Gdx.graphics.getDeltaTime());
		
		if(player.isDead() && !gameOver){
			menuLabel.setText("GAME OVER", 48f);
			continueButton.setText("NEW GAME", 36f);
			
			continueButton.position.rx = -0.5f;
			quitButton.position.rx = 1.5f;
			
			TaskBuilder builder = new TaskBuilder();
			builder.addKeyFrame(new KeyFrame(){
				@Override
				public void act(float progress, float frameProgress) {
					continueButton.position.rx = Interpolation.sine.apply(-0.5f, center, frameProgress);
					quitButton.position.rx = Interpolation.sine.apply(1.5f, center, frameProgress);
				}
				@Override
				public void end() {
					continueButton.position.rx = center;
					quitButton.position.rx = center;
				}
			}, 0.6f);
			Core.tasks.addTask(builder);
			
			gameOver = true;
		}
	}
	
	@Override
	public void end() {
		Core.bindings.removeProcessor(this);
		Core.reset();
	}
	
	private void spawnPickup(){
		if(pickup != null)pickup.delete();
		int random = MathUtils.random(5);
		Gun gun;
		if(random == 0){
			gun = new Shotgun();
		}else if(random == 1){
			gun = new BurstGun();
		}else if(random == 2){
			gun = new RocketLauncher();
		}else if(random == 3){
			gun = new RocketRepeater();
		}else if(random == 4){
			gun = new SniperRifle();
		}else{
			gun = new Minigun();
		}
		pickup = new GunPickup(Util.randomTriangularVector(spawnRange), gun);
		Core.entities.addEntity(pickup);
	}
	
	private static final float INDICATOR_SIZE = 15;
	private static final float center = 0.5f;
	private static final float specialTextSize = 34f;
	
	private final Position healthBarPos = new Position(1, 0, -50, 50);
	private final Position gunStatsPos = new Position(0, 0, 50, 50);
	private final Position fpsPos = new Position(0, 1, 10, -10);
	private final Label levelInfoLabel = new Label("Level " + level, new Position(0.5f, 1f, 0, -20), Color.GRAY, 32f);
	private final Label specialLevelLabel = new Label("", new Position(0.5f, 0.5f, 0, -70), new Color(1, 1, 1, 0), specialTextSize);
	
	private final PolygonPart arrowIndicator = new PolygonPart(new Vector2[]{
			new Vector2(INDICATOR_SIZE, 0),
			new Vector2(-INDICATOR_SIZE, -INDICATOR_SIZE),
			new Vector2(-INDICATOR_SIZE, INDICATOR_SIZE)
	}, 6);
	
	private final Rectangle menu = new Rectangle(new Position(center, center, -150, -110), 300, 240, Color.CHARTREUSE, 20f);
	private final Label menuLabel = new Label("PAUSED", new Position(center, center, 0, 100), Color.WHITE, 48f);
	private final Button continueButton = new Button("CONTINUE", new Position(center, center, -130, 0), 260, 60, Color.WHITE, 36f, 10f);
	private final Button quitButton = new Button("QUIT TO MENU", new Position(center, center, -130, -90), 260, 60, Color.WHITE, 36f, 10f);
	
	@Override
	public void draw(){
		if(gameOver){
			menuLabel.draw();
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			continueButton.animateColor(x, y, Color.RED, Color.WHITE);
			quitButton.animateColor(x, y, Color.RED, Color.WHITE);
			continueButton.draw();
			quitButton.draw();
		}else if(Core.state.isPaused()){
			menu.draw();
			Vector2 pos = menu.position.getPosition();
			Core.graphics.fillRectangle(pos.x, pos.y, menu.w, menu.h, Color.BLACK);
			menuLabel.draw();
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			continueButton.animateColor(x, y, Color.RED, Color.WHITE);
			quitButton.animateColor(x, y, Color.RED, Color.WHITE);
			continueButton.draw();
			quitButton.draw();
		}
		
		if(!Constants.SHOW_GUI)return;
		
		if(Constants.SHOW_INDICATOR){
			Vector2 playerPos = Core.graphics.project(player.getPosition());
			Vector2 pickupPos = Core.graphics.project(pickup.getPosition());
			if(!Util.isInsideRect(playerPos, pickupPos, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())){
				Vector2 indicatorPos = Util.getBoundryPoint(playerPos, pickupPos, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 40);
				arrowIndicator.draw(indicatorPos, pickupPos.sub(playerPos).angleRad(), Color.LIME);
			}
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
		
		if(Constants.SHOW_FPS)Core.graphics.drawText(Integer.toString(Gdx.graphics.getFramesPerSecond()), fpsPos.getPosition(), 24, Color.GRAY);
		levelInfoLabel.draw();
		specialLevelLabel.draw();
	}
	
	@Override
	public Gui getGui(){
		return this;
	}
	
	public void animateNextLevel(){
		TaskBuilder builder = new TaskBuilder();
		final float ryStart = 1f;
		final float ryEnd = 0.5f;
		final float yStart = -20;
		final float yEnd = 0;
		final float textSizeStart = 32;
		final float textSizeEnd = 58;
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				levelInfoLabel.position.ry = Interpolation.circleOut.apply(ryStart, ryEnd, frameProgress);
				levelInfoLabel.position.y = Interpolation.circleOut.apply(yStart, yEnd, frameProgress);
				levelInfoLabel.setText(levelInfoLabel.getText(), Interpolation.circleOut.apply(textSizeStart, textSizeEnd, frameProgress));
			}
			@Override
			public void end() {
				level++;
				
				levelInfoLabel.position.ry = ryEnd;
				levelInfoLabel.position.y = yEnd;
				levelInfoLabel.setText("Level " + level, textSizeEnd);
				
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(spawnRange, 0)));
				Core.entities.addEntity(new BallLaunchingEnemy(new Vector2(-spawnRange, 0)));
				if(level % 5 == 0)Core.entities.addEntity(new TurretEnemy(new Vector2(), player));
				
				boolean specialLevel = false;
				if(level % bossLevel == 0 && level % challengeLevel == 0){
					specialLevelLabel.setText("            Wow! You made it pretty far!\nBut now you have to fight the boss with a challenge!", specialTextSize);
					specialLevel = true;
				}else if(level % bossLevel == 0){
					specialLevelLabel.setText("Boss Level!", specialTextSize);
					specialLevel = true;
				}else if(level % challengeLevel == 0){//TODO Add multiple challenge levels. (Pick challenge at random)
					specialLevelLabel.setText("Challenge Level!", specialTextSize);
					specialLevel = true;
				}
				if(!specialLevel)return;
				
				TaskBuilder levelInfoBuilder = new TaskBuilder();
				levelInfoBuilder.addKeyFrame(new KeyFrame(){
					@Override
					public void act(float progress, float frameProgress) {
						specialLevelLabel.color.a = Interpolation.circleOut.apply(0f, 1f, frameProgress);
					}
					@Override
					public void end() {
						specialLevelLabel.color.a = 1f;
					}
				}, 0.35f);
				levelInfoBuilder.addKeyFrame(1.7f);
				levelInfoBuilder.addKeyFrame(new KeyFrame(){
					@Override
					public void act(float progress, float frameProgress) {
						specialLevelLabel.color.a = Interpolation.circleIn.apply(1f, 0f, frameProgress);
					}
					@Override
					public void end() {
						specialLevelLabel.color.a = 0f;
					}
				}, 1.4f);
				Core.tasks.addTask(levelInfoBuilder);
			}
		}, 1.1f);
		builder.addKeyFrame(1.2f);
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				levelInfoLabel.position.ry = Interpolation.circleOut.apply(ryEnd, ryStart, frameProgress);
				levelInfoLabel.position.y = Interpolation.circleOut.apply(yEnd, yStart, frameProgress);
				levelInfoLabel.setText(levelInfoLabel.getText(), Interpolation.circleOut.apply(textSizeEnd, textSizeStart, frameProgress));
			}
			@Override
			public void end() {
				levelInfoLabel.position.ry = ryStart;
				levelInfoLabel.position.y = yStart;
				levelInfoLabel.setText(levelInfoLabel.getText(), textSizeStart);
			}
		}, 1.2f);
		Core.tasks.addTask(builder);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ESCAPE && !gameOver){
			Core.setPaused(!Core.state.isPaused());
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;
		if(gameOver){
			if(continueButton.contains(screenX, screenY)){
				Core.state.setState(new Survival());
				return true;
			}
			if(quitButton.contains(screenX, screenY)){
				Core.setPaused(false);
				Core.state.setState(new Menu());
				return true;
			}
		}else if(Core.state.isPaused()){
			if(continueButton.contains(screenX, screenY)){
				Core.setPaused(false);
				return true;
			}
			if(quitButton.contains(screenX, screenY)){
				Core.setPaused(false);
				Core.state.setState(new Menu());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
