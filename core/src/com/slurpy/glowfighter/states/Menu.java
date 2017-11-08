package com.slurpy.glowfighter.states;

public class Menu implements State {
	
	private final Gui gui;
	
	public Menu(){
		gui = new MenuGui();
	}
	
	@Override
	public void start() {
		
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void end() {
		
	}
	
	@Override
	public Gui getGui() {
		return gui;
	}
	
	private class MenuGui extends Gui{
		@Override
		public void draw() {
			
		}
	}
}
