package com.slurpy.glowfighter.managers;

import java.util.EnumMap;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.Difficulty;

public class PropertiesManager {
	
	private final EnumMap<Difficulty, HashMap<String, Object>> difficultyProperties;
	private final HashMap<String, Object> properties;
	
	public PropertiesManager(){
		difficultyProperties = new EnumMap<>(Difficulty.class);
		for(Difficulty difficulty : Difficulty.values()){
			difficultyProperties.put(difficulty, new HashMap<>());
		}
		properties = new HashMap<>();
		
		JsonValue difficultyJson = new JsonReader().parse(Gdx.files.internal(Constants.DIFFICULTY_PROPERTIES_LOCATION).readString());
		for(Difficulty difficulty : Difficulty.values()){
			JsonValue difficultyData = difficultyJson.get(difficulty.toString());
			for(JsonValue data : difficultyData.iterator()){
				Object value;
				if(data.isBoolean())value = data.asBoolean();
				else if(data.isLong())value = data.asInt();
				else if(data.isDouble())value = data.asFloat();
				else value = data.asString();
				
				difficultyProperties.get(difficulty).put(data.name(), value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDifficultyProperty(String property, Difficulty difficulty){
		HashMap<String, Object> map = difficultyProperties.get(difficulty);
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
