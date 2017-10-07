package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.TestEntity;
import com.slurpy.glowfighter.entities.Wall;
import com.slurpy.glowfighter.managers.AssetManager;
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
	public static KeyBindings bindings;
	public static EntityManager entities;
	
	private FPSCounter fps = new FPSCounter(1);
	private Spawner spawner = new Spawner(0.05f);
	
	@Override
	public void create () {
		assets = AssetManager.getAssetManager();
		graphics = GraphicsManager.getGraphicsManager();
		physics = PhysicsManager.getPhysicsManager();
		entities = EntityManager.getEntityManager();
		
		bindings = KeyBindings.createNewBinding();
		Gdx.input.setInputProcessor(bindings);
		bindings.addBinding(Action.moveUp, Keys.W);
		bindings.addBinding(Action.moveLeft, Keys.A);
		bindings.addBinding(Action.moveDown, Keys.S);
		bindings.addBinding(Action.moveRight, Keys.D);
		bindings.addBinding(Action.moveSlow, Keys.SHIFT_LEFT);
		bindings.addBinding(Action.primary, KeyBindings.LEFT);
		
		entities.addEntity(new TestEntity(new Vector2(0, 0), 0f, Color.GREEN));
		entities.addEntity(new TestEntity(new Vector2(10, 0), 10f, Color.RED));
		entities.addEntity(new TestEntity(new Vector2(-10, -10), 60f, Color.BLUE));
		
		//Walls
		float length = 50;
		float height = 25;
		float width = 3;
		entities.addEntity(new Wall(new Vector2(0, height), new Vector2(length - width, width), 0f, Color.WHITE));//Top
		entities.addEntity(new Wall(new Vector2(0, -height), new Vector2(length - width, width), 0f, Color.WHITE));//Bot
		entities.addEntity(new Wall(new Vector2(length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Right
		entities.addEntity(new Wall(new Vector2(-length, 0), new Vector2(width, height + width), 0f, Color.WHITE));//Left
		
		Player player = new Player(new Vector2(), 0);
		entities.addEntity(player, "player");
		graphics.follow(player);
		//graphics.look(new Vector2(100, 100));
	}
	
	@Override
	public void render () {
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
		graphics.dispose();
		assets.dispose();
	}
}
