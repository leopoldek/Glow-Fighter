package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.slurpy.glowfighter.gui.Gui;
import com.slurpy.glowfighter.states.State;

public class StateManager {
	
	private State state = null;
	
	public void setState(State newState){
		if(newState == null)throw new IllegalArgumentException("You can't pass a null state!");
		if(state != null){
			state.end();
		}
		state = newState;
		state.start();
		state.getGui().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void update(){
		state.update();
	}
	
	public Gui getGui(){
		return state.getGui();
	}
}
