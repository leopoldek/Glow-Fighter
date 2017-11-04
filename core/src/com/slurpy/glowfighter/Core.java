package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.slurpy.glowfighter.gamemodes.Gamemode;
import com.slurpy.glowfighter.gamemodes.Survival;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.AudioManager;
import com.slurpy.glowfighter.managers.EntityManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.managers.PhysicsManager;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.KeyBindings;

public class Core extends ApplicationAdapter {
	
	public static AssetManager assets;
	public static GraphicsManager graphics;
	public static PhysicsManager physics;
	public static AudioManager audio;
	public static EntityManager entities;
	
	public static KeyBindings bindings;
	
	public static Gamemode game;
	
	@Override
	public void create () {
		assets = new AssetManager();
		graphics = new GraphicsManager();
		physics = new PhysicsManager();
		audio = new AudioManager();
		entities = new EntityManager();
		
		bindings = KeyBindings.createNewBinding();
		Gdx.input.setInputProcessor(bindings);
		bindings.addBinding(Action.moveUp, Keys.W);
		bindings.addBinding(Action.moveLeft, Keys.A);
		bindings.addBinding(Action.moveDown, Keys.S);
		bindings.addBinding(Action.moveRight, Keys.D);
		bindings.addBinding(Action.moveSlow, Keys.SHIFT_LEFT);
		bindings.addBinding(Action.primary, KeyBindings.LEFT);
		bindings.addBinding(Action.nextWeapon, KeyBindings.SCROLLED_UP);
		bindings.addBinding(Action.nextWeapon, Keys.E);
		bindings.addBinding(Action.lastWeapon, KeyBindings.SCROLLED_DOWN);
		bindings.addBinding(Action.lastWeapon, Keys.Q);
		bindings.addBinding(Action.boost, Keys.SPACE);
		
		game = new Survival();
		game.start();
	}
	
	@Override
	public void render () {
		game.update();
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
