package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.utils.Array;
import com.slurpy.glowfighter.entities.Entity;

public class EntityManager {
	
	private static EntityManager singleton;
	
	public static EntityManager getEntityManager(){
		if(singleton == null)singleton = new EntityManager();
		return singleton;
	}
	
	private Array<Entity> entities = new Array<>(false, 16);
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public void update(){
		for(Entity entity : entities){
			entity.update();
		}
	}
	
	public void postUpdate(){
		for(Entity entity : entities){
			entity.postUpdate();
		}
	}
	
	public void draw(){
		for(Entity entity : entities){
			entity.draw();
		}
	}
}
