package com.slurpy.glowfighter.utils.animation;

public class Task implements KeyFrame{
	private final TaskAction task;
	public Task(TaskAction task){
		this.task = task;
	}
	
	@Override
	public final void start(){
		
	}
	@Override
	public final void act(float progress, float frameProgress){
		
	}
	@Override
	public final void end(){
		task.act();
	}
	
	public static interface TaskAction{
		public void act();
	}
}
