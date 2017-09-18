package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.TestEntity;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.KeyBindings;

public class Core extends ApplicationAdapter {
	
	public static AssetManager assets;
	public static GraphicsManager graphics;
	public static KeyBindings bindings;
	
	private Array<Entity> entities;
	
	@Override
	public void create () {
		assets = AssetManager.getAssetManager();
		graphics = GraphicsManager.getGraphicsManager();
		
		bindings = KeyBindings.createNewBinding();
		bindings.addBinding(Action.moveUp, Keys.W);
		bindings.addBinding(Action.moveLeft, Keys.A);
		bindings.addBinding(Action.moveDown, Keys.S);
		bindings.addBinding(Action.moveRight, Keys.D);
		bindings.addBinding(Action.moveSlow, Keys.SHIFT_LEFT);
		
		entities = new Array<>(false, 16);
		entities.add(new TestEntity(new Vector2(0, 0), 0f, Color.GREEN));
		entities.add(new TestEntity(new Vector2(100, 0), 10f, Color.RED));
		entities.add(new TestEntity(new Vector2(-100, -100), 60f, Color.BLUE));
		entities.add(new Player(new Vector2(), 0));
		//graphics.look(new Vector2(100, 100));
	}

	@Override
	public void render () {
		for(Entity entity : entities){
			entity.update();
		}
		
		graphics.begin();
		for(Entity entity : entities){
			entity.draw();
		}
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
