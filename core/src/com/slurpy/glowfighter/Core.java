package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.LineWall;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.TestEntity;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.AssetManager.MusicAsset;
import com.slurpy.glowfighter.managers.AudioManager;
import com.slurpy.glowfighter.managers.EntityManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.managers.PhysicsManager;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.FPSCounter;
import com.slurpy.glowfighter.utils.KeyBindings;
import com.slurpy.glowfighter.utils.Spawner;

public class Core extends ApplicationAdapter {
	
	public static AssetManager assets;
	public static GraphicsManager graphics;
	public static PhysicsManager physics;
	public static AudioManager audio;
	public static EntityManager entities;
	public static KeyBindings bindings;
	
	private FPSCounter fps = new FPSCounter(1);
	private Spawner spawner = new Spawner(0.2f);
	
	@Override
	public void create () {
		assets = AssetManager.getAssetManager();
		graphics = GraphicsManager.getGraphicsManager();
		physics = PhysicsManager.getPhysicsManager();
		audio = AudioManager.getAudioManager();
		entities = EntityManager.getEntityManager();
		
		bindings = KeyBindings.createNewBinding();
		Gdx.input.setInputProcessor(bindings);
		bindings.addBinding(Action.moveUp, Keys.W);
		bindings.addBinding(Action.moveLeft, Keys.A);
		bindings.addBinding(Action.moveDown, Keys.S);
		bindings.addBinding(Action.moveRight, Keys.D);
		bindings.addBinding(Action.moveSlow, Keys.SHIFT_LEFT);
		bindings.addBinding(Action.primary, KeyBindings.LEFT);
		bindings.addBinding(Action.nextWeapon, KeyBindings.SCROLLED_UP);
		bindings.addBinding(Action.lastWeapon, KeyBindings.SCROLLED_DOWN);
		
		entities.addEntity(new TestEntity(new Vector2(0, 0), 0f, Color.GREEN));
		entities.addEntity(new TestEntity(new Vector2(10, 0), 10f, Color.RED));
		entities.addEntity(new TestEntity(new Vector2(-10, -10), 60f, Color.BLUE));
		
		//Walls
		float length = 50;
		float height = 25;
		float width = 3;
		//entities.addEntity(new Wall(new Vector2(0, height), new Vector2(length - width, width), 0f, Color.WHITE));//Top
		//entities.addEntity(new Wall(new Vector2(0, -height), new Vector2(length - width, width), 0f, Color.WHITE));//Bot
		//entities.addEntity(new Wall(new Vector2(length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Right
		//entities.addEntity(new Wall(new Vector2(-length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Left
		entities.addEntity(new LineWall(new Vector2(0, height), length, width, 0f, Color.WHITE));
		entities.addEntity(new LineWall(new Vector2(0, -height), length, width, 0f, Color.WHITE));
		entities.addEntity(new LineWall(new Vector2(-length, 0), height, width, MathUtils.PI / 2, Color.WHITE));
		entities.addEntity(new LineWall(new Vector2(length, 0), height, width, MathUtils.PI / 2, Color.WHITE));
		
		Player player = new Player(new Vector2(), 0);
		entities.addEntity(player, "player");
		graphics.follow(player);
		//graphics.look(new Vector2(100, 100));
		
		Music music = Core.audio.getMusic(MusicAsset.BackgroundTechno);
		music.setLooping(true);
		music.play();
	}
	
	@Override
	public void render () {
		graphics.drawText("Glow Fighter", new Vector2(10, 10), 1.5f, Color.WHITE);
		spawner.spawn(Gdx.graphics.getDeltaTime());
		fps.update(Gdx.graphics.getDeltaTime());
		entities.update();
		physics.update();
		graphics.begin();
		entities.draw();
		graphics.end();
	}
	
	@Override
	public void resize(int width, int height) {
		graphics.resize(width, height);
	}
	
	@Override
	public void dispose () {
		physics.dispose();
		graphics.dispose();
		assets.dispose();
	}
}
