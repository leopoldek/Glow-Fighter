package com.slurpy.glowfighter.gui;

public interface Gui {
	public void draw();
	public default void resize(int width, int height){}
}
