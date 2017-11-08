package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator;
import com.slurpy.glowfighter.utils.tasks.Task;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class TaskManager{
	
	private ObjectSet<Task> tasks = new ObjectSet<>();
	
	public TaskManager(){
		
	}
	
	public void update(){
		for(ObjectSetIterator<Task> i = tasks.iterator(); i.hasNext;){
			Task a = i.next();
			a.update();
			if(a.isDone())i.remove();
		}
	}
	
	public void addTask(TaskBuilder builder){
		addTask(builder.build());
	}
	
	public void addTask(Task task){
		tasks.add(task);
		task.start();
	}
	
	public void removeTask(Task animation){
		tasks.remove(animation);
	}
	
	public void clear(){
		tasks.clear();
	}
}
