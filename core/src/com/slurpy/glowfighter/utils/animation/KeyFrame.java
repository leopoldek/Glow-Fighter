package com.slurpy.glowfighter.utils.animation;

public interface KeyFrame {
	public void start();
	public void act(float progress, float frameProgress);
	public void end();
}
