package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class KeyBindings extends InputAdapter{
	
	public static final int LEFT = -1;
	public static final int RIGHT = -2;
	public static final int MIDDLE = -3;
	public static final int BACK = -4;
	public static final int FOWARD = -5;
	public static final int SCROLLED_DOWN = -6;
	public static final int SCROLLED_UP = -7;
	
	private final InputMultiplexer multiplexer;
	
	private IntMap<Action> keyBindings;
	private ObjectMap<Action, IntArray> actionBindings;
	
	private ObjectMap<Action, ActionListener> listeners;
	
	private KeyBindings(){
		multiplexer = new InputMultiplexer(this);
		
		keyBindings = new IntMap<>();
		actionBindings = new ObjectMap<>();
		for(Action action : Action.values()){
			actionBindings.put(action, new IntArray(false, 4));
		}
		
		listeners = new ObjectMap<>();
		for(Action action : Action.values()){
			listeners.put(action, null);
		}
	}
	
	public void bind() {
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	public void addProcessor(InputProcessor processor){
		if(processor == this)throw new IllegalArgumentException("You can't add this processor to itself!");
		multiplexer.addProcessor(0, processor);
	}
	
	public void removeProcessor(InputProcessor processor){
		if(processor == this)throw new IllegalArgumentException("You can't remove this processor from itself!");
		multiplexer.removeProcessor(processor);
	}
	
	public void addBinding(Action action, int bind){
		keyBindings.put(bind, action);
		actionBindings.get(action).add(bind);
	}
	
	public void deleteBinding(int bind){
		if(!keyBindings.containsKey(bind))return;
		Action action = keyBindings.get(bind);
		keyBindings.remove(bind);
		actionBindings.get(action).removeValue(bind);
	}
	
	public void clearBindings(Action action){
		actionBindings.get(action).clear();
	}
	
	public boolean isActionPressed(Action action){
		if(!actionBindings.containsKey(action))return false;
		for(int bind : actionBindings.get(action).items){
			if(bind < -5){//Scroll
				throw new IllegalArgumentException("Must use listener for scroll wheel(For now...)");
			}else if(bind < 0){//Button
				if(Gdx.input.isButtonPressed(-bind - 1))return true;
			}else if(Gdx.input.isKeyPressed(bind))return true;
		}
		return false;
	}
	
	public void subscribe(Action action, ActionListener listener){
		listeners.put(action, listener);
	}
	
	public void unsubscribe(Action action){
		listeners.put(action, null);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(!keyBindings.containsKey(keycode))return false;
		ActionListener listener = listeners.get(keyBindings.get(keycode));
		if(listener == null)return false;
		listener.actionPressed();
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		button = -button - 1;
		if(!keyBindings.containsKey(button))return false;
		ActionListener listener = listeners.get(keyBindings.get(button));
		if(listener == null)return false;
		listener.actionPressed();
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		if(amount < 0){
			if(!keyBindings.containsKey(SCROLLED_DOWN))return false;
			ActionListener listener = listeners.get(keyBindings.get(SCROLLED_DOWN));
			if(listener == null)return false;
			listener.actionPressed();
			return true;
		}else{
			if(!keyBindings.containsKey(SCROLLED_UP))return false;
			ActionListener listener = listeners.get(keyBindings.get(SCROLLED_UP));
			if(listener == null)return false;
			listener.actionPressed();
			return true;
		}
	}
	
	public IntArray getKeys(Action action){
		return new IntArray(actionBindings.get(action));
	}
	
	public static KeyBindings load(FileHandle file){
		throw new UnsupportedOperationException("File/json loading not available yet.");
	}
	
	public static KeyBindings createDefaultBinding(){
		return new KeyBindings();
	}
}
