package com.slurpy.glowfighter.states;

import com.badlogic.gdx.InputProcessor;
import com.slurpy.glowfighter.Core;

public class Menu implements State {
	
	private final MenuGui gui;
	
	public Menu(){
		gui = new MenuGui();
	}
	
	@Override
	public void start() {
		Core.bindings.addProcessor(gui);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void end() {
		Core.bindings.removeProcessor(gui);
		Core.reset();
	}
	
	@Override
	public Gui getGui() {
		return gui;
	}
	
	private class MenuGui extends Gui implements InputProcessor{
		@Override
		public void draw() {
			
		}
		
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			
			return false;
		}
		
		@Override
		public boolean keyDown(int keycode) {
			return false;
		}
		@Override
		public boolean keyUp(int keycode) {
			return false;
		}
		@Override
		public boolean keyTyped(char character) {
			return false;
		}
		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}
		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}
		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}
		@Override
		public boolean scrolled(int amount) {
			return false;
		}
	}
}
