package com.slurpy.glowfighter.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.tasks.Task;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class Menu extends Gui implements State, InputProcessor{
	
	private final Position titlePos = new Position(0.5f, 0.85f, -280, 0);
	private final Color titleColor = new Color();
	private final Task titleColorShift;
	
	public Menu(){
		TaskBuilder builder = new TaskBuilder();
		final Color[] colors = new Color[]{
				Color.RED,
				Color.BLUE,
				Color.GREEN,
				Color.ORANGE,
				Color.PURPLE
		};
		for(int i = 0; i < colors.length - 1; i++){
			final Color color1 = colors[i];
			final Color color2 = colors[i+1];
			builder.addKeyFrame((progress, frameProgress) -> {
				titleColor.set(color1).lerp(color2, frameProgress);
			}, 2f);
		}
		builder.addKeyFrame((progress, frameProgress) -> titleColor.set(colors[colors.length-1]).lerp(colors[0], frameProgress), 1f);
		titleColorShift = builder.build();
		titleColorShift.loop(true);
	}
	
	@Override
	public void start() {
		Core.bindings.addProcessor(this);
		Core.tasks.addTask(titleColorShift);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void draw() {
		Core.graphics.drawText("GLOW FIGHTER", titlePos.getPosition(), 100, titleColor);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}
	
	@Override
	public void end() {
		Core.bindings.removeProcessor(this);
		Core.reset();
	}
	
	@Override
	public Gui getGui() {
		return this;
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
