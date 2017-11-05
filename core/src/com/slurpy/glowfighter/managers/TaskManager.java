package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator;
import com.slurpy.glowfighter.utils.animation.Animation;
import com.slurpy.glowfighter.utils.animation.AnimationBuilder;

public class TaskManager{
	
	private ObjectSet<Animation> animations = new ObjectSet<>();
	
	public TaskManager(){
		
	}
	
	public void update(){
		for(ObjectSetIterator<Animation> i = animations.iterator(); i.hasNext;){
			Animation a = i.next();
			if(a.update())i.remove();
		}
	}
	
	public void addAnimation(AnimationBuilder builder){
		animations.add(builder.build());
	}
	
	public void removeAnimation(Animation animation){
		animations.remove(animation);
	}
}
