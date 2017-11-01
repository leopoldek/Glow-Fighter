package com.slurpy.glowfighter.managers;

import java.util.EnumMap;
import java.util.HashMap;

import com.slurpy.glowfighter.utils.Difficulty;

public class PropertiesManager {
	
	private final EnumMap<Difficulty, HashMap<String, Object>> difficultyProperties;
	private final HashMap<String, Object> properties;
	
	public PropertiesManager(){
		difficultyProperties = new EnumMap<>(Difficulty.class);
		properties = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDifficultyProperty(String property, Difficulty diffculty){
		HashMap<String, Object> map = difficultyProperties.get(diffculty);
		if(!map.containsKey(property))return null;
		return (T)map.get(property);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String property){
		if(!properties.containsKey(property))return null;
		return (T)properties.get(property);
	}
	
	public void setProperty(String property, Object value){
		if(!properties.containsKey(property))throw new IllegalArgumentException("Can't set a property that doesn't exist!");
		properties.put(property, value);
	}
}
