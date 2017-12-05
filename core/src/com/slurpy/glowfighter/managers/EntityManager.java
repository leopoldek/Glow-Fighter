package com.slurpy.glowfighter.managers;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.decor.Decor;
import com.slurpy.glowfighter.entities.Entity;

public class EntityManager {
	
	private Array<Decor> decors = new Array<>(false, 200);
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
	
	public void addDecor(Decor decor){
		decors.add(decor);
	}
	
	public ObjectSet<Entity> getGroup(String groupName){
		return groups.get(groupName);
	}
	
	public boolean isInGroup(Entity entity, String groupName){
		return groups.get(groupName).contains(entity);
	}
	
	public void update(){
		if(paused)return;
		
		for(int i = 0; i < decors.size; i++){
			decors.get(i).update();
		}
		
		for(int i = 0; i < entities.size; i++){
			entities.get(i).update();
		}
	}
	
	public void clean(){
		for(int i = decors.size - 1; i >= 0; i--){
			if(decors.get(i).isDeleted())decors.removeIndex(i);
		}
		
		for(int i = entities.size - 1; i >= 0; i--){
			Entity entity = entities.get(i);
			if(entity.isDeleted())entities.removeIndex(i);
			for(Iterator<ObjectSet<Entity>> groupIt = groups.values().iterator(); groupIt.hasNext();){
				ObjectSet<Entity> group = groupIt.next();
				group.remove(entity);
				if(group.size == 0)groupIt.remove();
			}
			Core.physics.destroy(entity.body);
		}
	}
	
	public void draw(){
		for(int i = 0; i < entities.size; i++){
			Entity entity = entities.get(i);
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
