package com.slurpy.glowfighter.states;

public interface State {
	public void start();
	public void update();
	public void end();
	public Gui getGui();
}
