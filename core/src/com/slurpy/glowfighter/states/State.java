package com.slurpy.glowfighter.states;

import com.slurpy.glowfighter.gui.Gui;

public interface State {
	public void start();
	public void update();
	public void end();
	public Gui getGui();
}
