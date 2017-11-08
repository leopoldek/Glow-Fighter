package com.slurpy.glowfighter.states;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;
import com.slurpy.glowfighter.utils.Util;
import com.slurpy.glowfighter.utils.tasks.Task;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class Menu extends Gui implements State, InputProcessor{
	
	private final Position titlePos = new Position(0.5f, 0.85f, -280, 0);
	private final Color titleColor = new Color();
	private final Task titleColorShift;
	
	private final Button playButton = new Button("PLAY", new Position(0.5f, 0.5f, -250, -30),  500, 60, Color.WHITE);
	private final Button optionsButton = new Button("OPTIONS", new Position(0.5f, 0.5f, -250, -120),  500, 60, Color.WHITE);
	private final Button exitButton = new Button("EXIT", new Position(0.5f, 0.5f, -250, -210),  500, 60, Color.WHITE);
	
	public Menu(){
		TaskBuilder builder = new TaskBuilder();
		final Color[] colors = new Color[]{
				Color.RED,
				Color.BLUE,
				Color.GREEN,
				Color.ORANGE,
				Color.PURPLE
		};
		titleColor.set(colors[0]);
		for(int i = 0; i < colors.length - 1; i++){
			final Color color1 = colors[i];
			final Color color2 = colors[i+1];
			builder.addKeyFrame((progress, frameProgress) -> titleColor.set(color1).lerp(color2, frameProgress), 2f);
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
		final Color selected = Color.RED;
		final Color normal = Color.WHITE;
		float screenX = Gdx.graphics.getWidth() - Gdx.input.getX();
		float screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if(playButton.contains(screenX, screenY)){
			playButton.color.set(selected);
		}else{
			playButton.color.lerp(normal, 1.5f * Gdx.graphics.getDeltaTime());
		}
		
		if(optionsButton.contains(screenX, screenY)){
			optionsButton.color.set(selected);
		}else{
			optionsButton.color.lerp(normal, 1.5f * Gdx.graphics.getDeltaTime());
		}
		
		if(exitButton.contains(screenX, screenY)){
			exitButton.color.set(selected);
		}else{
			exitButton.color.lerp(normal, 1.5f * Gdx.graphics.getDeltaTime());
		}
	}
	
	@Override
	public void draw() {
		Core.graphics.drawText("GLOW FIGHTER", titlePos.getPosition(), 100, titleColor);
		playButton.draw();
		optionsButton.draw();
		exitButton.draw();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Buttons.LEFT){
			if(playButton.contains(screenX, screenY)){
				Core.state.setState(new Survival());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
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
	
	private class Button{
		
		private final String text;
		private final Position position;
		private float w, h;
		private final float fontW, fontH;
		private Color color;
		
		private Button(String text, Position pos, int w, int h, Color color) {
			this.text = text;
			this.position = pos;
			this.w = w;
			this.h = h;
			Vector2 fontSize = Util.getTextSize(FontAsset.CatV, text, 48f);
			fontW = fontSize.x;
			fontH = fontSize.y;
			this.color = color.cpy();
		}
		
		private boolean contains(float x, float y){
			Vector2 pos = position.getPosition();
			return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
		}
		
		private void draw(){
			Vector2 pos = position.getPosition();
			Core.graphics.drawRectangle(pos.x, pos.y, w, h, 10, color);
			pos.add(w/2, h/2).sub(fontW/2, -fontH/2);
			Core.graphics.drawText(text, pos, 48f, color);
		}
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
	public boolean scrolled(int amount) {
		return false;
	}
}
