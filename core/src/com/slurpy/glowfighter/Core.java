package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.slurpy.glowfighter.managers.AssetManager;
import com.slurpy.glowfighter.managers.AudioManager;
import com.slurpy.glowfighter.managers.EntityManager;
import com.slurpy.glowfighter.managers.GraphicsManager;
import com.slurpy.glowfighter.managers.PhysicsManager;
import com.slurpy.glowfighter.managers.StateManager;
import com.slurpy.glowfighter.managers.TaskManager;
import com.slurpy.glowfighter.states.Menu;
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
		boolean undo = false;//TODO Later change window to unresizable and add resize option in graphics settings.
		if(width < Constants.minWidth){
			width = Constants.minWidth;
			undo = true;
		}
		if(height < Constants.minHeight){
			height = Constants.minHeight;
			undo = true;
		}
		if(undo){
			Gdx.graphics.setWindowedMode(width, height);
		}else{
			graphics.resize(width, height);
		}
	}
	
	public static void reset(){
		tasks.clear();
		entities.clear();
		audio.stopAll();
		graphics.clearDrawCalls();
	}
	
	public static void setPaused(boolean paused){
		tasks.setPaused(paused);
		state.setPaused(paused);
		entities.setPaused(paused);
		physics.setPaused(paused);
		/*try {
			Gdx.graphics.getClass().getField("deltaTime").setFloat(Gdx.graphics, 0f);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}*/
	}
	
	@Override
	public void dispose () {
		physics.dispose();
		graphics.dispose();
		assets.dispose();
	}
}
