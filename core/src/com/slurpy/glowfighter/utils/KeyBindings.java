package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
	
	public KeyBindings(){
		multiplexer = new InputMultiplexer(this);
		
		keyBindings = new IntMap<>();
		actionBindings = new ObjectMap<>();
		for(Action action : Action.values()){
			actionBindings.put(action, new IntArray(true, 4));
		}
		
		listeners = new ObjectMap<>();
		for(Action action : Action.values()){
			listeners.put(action, null);
		}
		
		Preferences pref = Gdx.app.getPreferences(Constants.SETTINGS_FILE);
		setBinding(Action.moveUp, 0, pref.getInteger(Action.moveUp.name() + '0', Keys.W));
		if(pref.contains(Action.moveUp.name() + '1')){
			setBinding(Action.moveUp, 1, pref.getInteger(Action.moveUp.name() + '1'));
		}
		setBinding(Action.moveDown, 0, pref.getInteger(Action.moveDown.name() + '0', Keys.S));
		if(pref.contains(Action.moveDown.name() + '1')){
			setBinding(Action.moveDown, 1, pref.getInteger(Action.moveDown.name() + '1'));
		}
		setBinding(Action.moveLeft, 0, pref.getInteger(Action.moveLeft.name() + '0', Keys.A));
		if(pref.contains(Action.moveLeft.name() + '1')){
			setBinding(Action.moveLeft, 1, pref.getInteger(Action.moveLeft.name() + '1'));
		}
		setBinding(Action.moveRight, 0, pref.getInteger(Action.moveRight.name() + '0', Keys.D));
		if(pref.contains(Action.moveRight.name() + '1')){
			setBinding(Action.moveRight, 1, pref.getInteger(Action.moveRight.name() + '1'));
		}
		setBinding(Action.primary, 0, pref.getInteger(Action.primary.name() + '0', KeyBindings.LEFT));
		if(pref.contains(Action.primary.name() + '1')){
			setBinding(Action.primary, 1, pref.getInteger(Action.primary.name() + '1'));
		}
		setBinding(Action.boost, 0, pref.getInteger(Action.boost.name() + '0', Keys.SPACE));
		if(pref.contains(Action.boost.name() + '1')){
			setBinding(Action.boost, 1, pref.getInteger(Action.boost.name() + '1'));
		}
		setBinding(Action.moveSlow, 0, pref.getInteger(Action.moveSlow.name() + '0', Keys.SHIFT_LEFT));
		if(pref.contains(Action.moveSlow.name() + '1')){
			setBinding(Action.moveSlow, 1, pref.getInteger(Action.moveSlow.name() + '1'));
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
	
	public void deleteBinding(int bind){
		if(!keyBindings.containsKey(bind))return;
		Action action = keyBindings.get(bind);
		keyBindings.remove(bind);
		actionBindings.get(action).removeValue(bind);
	}
	
	public void setBinding(Action action, int index, int bind){
		IntArray bindings = actionBindings.get(action);
		if(index > bindings.size)throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + bindings.size);
		keyBindings.put(bind, action);
		if(index == bindings.size)bindings.add(bind);
		else bindings.set(index, bind);
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
	
	public void save(){
		Preferences pref = Gdx.app.getPreferences(Constants.SETTINGS_FILE);
		IntArray bindings;
		
		bindings = actionBindings.get(Action.moveUp);
		pref.putInteger(Action.moveUp.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.moveUp.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.moveDown);
		pref.putInteger(Action.moveDown.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.moveDown.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.moveLeft);
		pref.putInteger(Action.moveLeft.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.moveLeft.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.moveRight);
		pref.putInteger(Action.moveRight.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.moveRight.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.primary);
		pref.putInteger(Action.primary.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.primary.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.boost);
		pref.putInteger(Action.boost.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.boost.name() + '1', bindings.get(1));
		
		bindings = actionBindings.get(Action.moveSlow);
		pref.putInteger(Action.moveSlow.name() + '0', bindings.get(0));
		if(bindings.size > 1)pref.putInteger(Action.moveSlow.name() + '1', bindings.get(1));
		
		pref.flush();
	}
	
	public void reset(){
		keyBindings.clear();
		actionBindings.forEach(entry -> entry.value.clear());
		
		setBinding(Action.moveUp, 0, Keys.W);
		setBinding(Action.moveDown, 0, Keys.S);
		setBinding(Action.moveLeft, 0, Keys.A);
		setBinding(Action.moveRight, 0, Keys.D);
		setBinding(Action.primary, 0, KeyBindings.LEFT);
		setBinding(Action.boost, 0, Keys.SPACE);
		setBinding(Action.moveSlow, 0, Keys.SHIFT_LEFT);
	}
	
	public static int convertMouseBinding(int bind){
		return -bind - 1;
	}

	public static String toString(int binding) {
		switch(binding){
		case -1:{
			return "LMB";
		}
		case -2:{
			return "RMB";
		}
		case -3:{
			return "MMB";
		}
		case -4:{
			return "SCROLL DOWN";
		}
		case -5:{
			return "SCROLL UP";
		}
		}
		throw new IllegalArgumentException("Binding must be between -1 and -5 inclusive!");
	}
}
