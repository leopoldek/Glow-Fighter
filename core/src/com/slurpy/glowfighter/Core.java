package com.slurpy.glowfighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.TestEntity;
import com.slurpy.glowfighter.managers.GraphicsManager;

public class Core extends ApplicationAdapter {
	
	public static GraphicsManager graphics;
	
	private Array<Entity> entities;
	
	@Override
	public void create () {
		graphics = new GraphicsManager();
		entities = new Array<>(false, 16);
		entities.add(new TestEntity(new Vector2(0, 0), 0f, Color.GREEN));
		entities.add(new TestEntity(new Vector2(100, 0), 10f, Color.RED));
		entities.add(new TestEntity(new Vector2(-100, -100), 60f, Color.BLUE));
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
	}
}
