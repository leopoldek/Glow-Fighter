package com.slurpy.glowfighter.gamemodes;

public class Menu implements Gamemode {
	
	private final Gui gui;
	
	public Menu(){
		gui = new Gui(){
			@Override
			public void draw() {
				
			}};
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public Gui getGui() {
		return gui;
	}
}
