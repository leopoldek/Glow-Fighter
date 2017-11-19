package com.slurpy.glowfighter.managers;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;

public class EntityManager {
	
	private Array<Entity> entities = new Array<>(false, 200);
	private ObjectMap<String, ObjectSet<Entity>> groups = new ObjectMap<>();
	
	private boolean paused = false;
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public void addEntity(Entity entity, String...groupNames){
		entities.add(entity);
		for(String groupName : groupNames){
			if(!groups.containsKey(groupName))groups.put(groupName, new ObjectSet<Entity>());
			groups.get(groupName).add(entity);
		}
	}
	
	public ObjectSet<Entity> getGroup(String groupName){
		return groups.get(groupName);
	}
	
	public boolean isInGroup(Entity entity, String groupName){
		return groups.get(groupName).contains(entity);
	}
	
	public void update(){
		for(Iterator<Entity> i = entities.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!entity.isDeleted() && !paused)entity.update();
			if(entity.isDeleted()){
				i.remove();
				for(Iterator<ObjectSet<Entity>> groupIt = groups.values().iterator(); groupIt.hasNext();){
					ObjectSet<Entity> group = groupIt.next();
					group.remove(entity);
					if(group.size == 0)groupIt.remove();
				}
				Core.physics.destroy(entity.body);
			}
		}
	}
	
	public void draw(){
		for(Entity entity : entities){
			if(!entity.isDeleted())
				entity.draw();
		}
	}
	
	public int amount(){
		return entities.size;
	}
	
	public void clear(){
		for(Entity entity : entities){
			Core.physics.destroy(entity.body);
		}
		entities.clear();
		groups.clear();
	}
	
	public void setPaused(boolean paused){
		this.paused = paused;
	}
	
	public boolean isPaused(){
		return paused;
	}
}
