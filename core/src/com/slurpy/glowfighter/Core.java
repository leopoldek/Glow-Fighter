package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.TestEntity;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.EntityManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.managers.PhysicsManager;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.KeyBindings;

public class Core extends ApplicationAdapter {
	
	public static AssetManager assets;
	public static GraphicsManager graphics;
	public static PhysicsManager physics;
	public static KeyBindings bindings;
	public static EntityManager entities;
	
	@Override
	public void create () {
		assets = AssetManager.getAssetManager();
		graphics = GraphicsManager.getGraphicsManager();
		physics = PhysicsManager.getPhysicsManager();
		
		bindings = KeyBindings.createNewBinding();
		Gdx.input.setInputProcessor(bindings);
		bindings.addBinding(Action.moveUp, Keys.W);
		bindings.addBinding(Action.moveLeft, Keys.A);
		bindings.addBinding(Action.moveDown, Keys.S);
		bindings.addBinding(Action.moveRight, Keys.D);
		bindings.addBinding(Action.moveSlow, Keys.SHIFT_LEFT);
		bindings.addBinding(Action.primary, KeyBindings.LEFT);
		
		entities = EntityManager.getEntityManager();
		entities.addEntity(new TestEntity(new Vector2(0, 0), 0f, Color.GREEN));
		entities.addEntity(new TestEntity(new Vector2(10, 0), 10f, Color.RED));
		entities.addEntity(new TestEntity(new Vector2(-10, -10), 60f, Color.BLUE));
		Player player = new Player(new Vector2(), 0);
		entities.addEntity(player);
		graphics.follow(player);
		//graphics.look(new Vector2(100, 100));
	}

	@Override
	public void render () {
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
