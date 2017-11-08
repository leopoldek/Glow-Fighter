package com.slurpy.glowfighter.utils.tasks;

public interface KeyFrame {
	public default void start(){}
	public void act(float progress, float frameProgress);
	public default void end(){}
}
