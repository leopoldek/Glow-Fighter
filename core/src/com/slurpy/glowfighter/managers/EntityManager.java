package com.slurpy.glowfighter.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.utils.Array;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;

public class EntityManager {
	
	private static EntityManager singleton;
	
	public static EntityManager getEntityManager(){
		if(singleton == null)singleton = new EntityManager();
		return singleton;
	}
	
	private Array<Entity> entities = new Array<>(false, 100);
	private HashMap<String, HashSet<Entity>> groups = new HashMap<>();
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public void addEntity(Entity entity, String...groupNames){
		entities.add(entity);
		for(String groupName : groupNames){
			if(!groups.containsKey(groupName))groups.put(groupName, new HashSet<Entity>());
			groups.get(groupName).add(entity);
		}
	}
	
	public Set<Entity> getGroup(String groupName){
		return groups.get(groupName);
	}
	
	public boolean isInGroup(Entity entity, String groupName){
		return groups.get(groupName).contains(entity);
	}
	
	public void update(){
		for(Iterator<Entity> i = entities.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!entity.isDeleted())entity.update();
			if(entity.isDeleted()){
				i.remove();
				for(Iterator<HashSet<Entity>> groupIt = groups.values().iterator(); groupIt.hasNext();){
					HashSet<Entity> group = groupIt.next();
					group.remove(entity);
					if(group.isEmpty())groupIt.remove();
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
}
