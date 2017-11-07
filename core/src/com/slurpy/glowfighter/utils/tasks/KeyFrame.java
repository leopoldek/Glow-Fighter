package com.slurpy.glowfighter.utils.tasks;

public interface KeyFrame {
	public void start();
	public void act(float progress, float frameProgress);
	public void end();
}
