package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.AudioManager;
import com.slurpy.glowfighter.managers.EntityManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.managers.PhysicsManager;
import com.slurpy.glowfighter.managers.StateManager;
import com.slurpy.glowfighter.managers.TaskManager;
import com.slurpy.glowfighter.states.Menu;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.KeyBindings;

public class Core extends ApplicationAdapter {
	
	public static AssetManager assets;
	public static GraphicsManager graphics;
	public static PhysicsManager physics;
	public static AudioManager audio;
	public static EntityManager entities;
	public static TaskManager tasks;
	public static StateManager state;
	
	public static KeyBindings bindings;
	
	@Override
	public void create () {
		//Create preference file if it doesn't exist now since it's a heavy task.
		Preferences pref = Gdx.app.getPreferences(Constants.SETTINGS_FILE);
		pref.flush();
		
		assets = new AssetManager();
		graphics = new GraphicsManager();
		physics = new PhysicsManager();
		audio = new AudioManager();
		entities = new EntityManager();
		tasks = new TaskManager();
		state = new StateManager();
		
		bindings = new KeyBindings();
		bindings.bind();
		
		state.setState(new Menu());
	}
	
	@Override
	public void render () {
		tasks.update();
		state.update();
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
	
	public static void reset(){
		tasks.clear();
		entities.clear();
		audio.stopAll();
		graphics.clearDrawCalls();
	}
	
	@Override
	public void dispose () {
		physics.dispose();
		graphics.dispose();
		assets.dispose();
	}
}
