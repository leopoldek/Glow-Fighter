package com.slurpy.glowfighter.states;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;
import com.slurpy.glowfighter.utils.Util;
import com.slurpy.glowfighter.utils.tasks.KeyFrame;
import com.slurpy.glowfighter.utils.tasks.Task;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class Menu extends Gui implements State, InputProcessor{
	
	private static final float titleCenter = 0.85f;
	private static final float titleTop = 1.2f;
	
	private static final float left = -0.5f;
	private static final float center = 0.5f;
	private static final float right = 1.5f;
	
	private MenuState menuState;
	
	//Main Menu
	private final Position titlePos = new Position(center, titleCenter, -280, 0);
	private final Color titleColor = new Color();
	private final Task titleColorShift;
	private final Button playButton = new Button("PLAY", new Position(center, 0.5f, -250, -30),  500, 60, Color.WHITE);
	private final Button optionsButton = new Button("OPTIONS", new Position(center, 0.5f, -250, -120),  500, 60, Color.WHITE);
	private final Button exitButton = new Button("EXIT", new Position(center, 0.5f, -250, -210),  500, 60, Color.WHITE);
	
	//Options Menu
	private final Button gameButton = new Button("GAME SETTINGS", new Position(right, 0.4f, -250, 135), 500, 60, Color.WHITE);
	private final Button soundButton = new Button("SOUND SETTINGS", new Position(right, 0.4f, -250, 45), 500, 60, Color.WHITE);
	private final Button graphicsButton = new Button("GRAPHICS SETTINGS", new Position(right, 0.4f, -250, -45), 500, 60, Color.WHITE);
	private final Button backButton = new Button("BACK", new Position(right, 0.4f, -250, -135), 500, 60, Color.WHITE);
	
	//Game Menu
	
	
	//Sound Menu
	private final Slider masterVolume = new Slider(new Position(0.5f, 0.5f, -250, -100), 500f);
	private final Slider effectVolume = new Slider(new Position(0.5f, 0.5f, -250, -100), 500f);
	private final Slider musicVolume = new Slider(new Position(0.5f, 0.5f, -250, -100), 500f);
	private final Slider interfaceVolume = new Slider(new Position(0.5f, 0.5f, -250, -100), 500f);
	
	//Graphics Menu
	
	
	//Keybindings Menu
	
	
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
		
		menuState = MenuState.main;
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
		final int screenX = Gdx.graphics.getWidth() - Gdx.input.getX();
		final int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
		//Main
		playButton.animateColor(screenX, screenY, selected, normal);
		optionsButton.animateColor(screenX, screenY, selected, normal);
		exitButton.animateColor(screenX, screenY, selected, normal);
		
		//Options
		gameButton.animateColor(screenX, screenY, selected, normal);
		soundButton.animateColor(screenX, screenY, selected, normal);
		graphicsButton.animateColor(screenX, screenY, selected, normal);
		backButton.animateColor(screenX, screenY, selected, normal);
	}
	
	@Override
	public void draw() {
		Core.graphics.drawText("GLOW FIGHTER", titlePos.getPosition(), 100, titleColor);
		playButton.draw();
		optionsButton.draw();
		exitButton.draw();
		
		gameButton.draw();
		soundButton.draw();
		graphicsButton.draw();
		backButton.draw();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = Gdx.graphics.getWidth() - screenX;
		screenY = Gdx.graphics.getHeight() - screenY;
		if(button == Buttons.LEFT){
			if(menuState == MenuState.main){
				if(playButton.contains(screenX, screenY)){
					Core.state.setState(new Survival());
					return true;
				}
				if(optionsButton.contains(screenX, screenY)){
					gotoOptionsMenu();
					return true;
				}
				if(exitButton.contains(screenX, screenY)){
					Gdx.app.exit();
					return true;
				}
			}else if(menuState == MenuState.options){
				if(gameButton.contains(screenX, screenY)){
					
					return true;
				}
				if(soundButton.contains(screenX, screenY)){
					
					return true;
				}
				if(graphicsButton.contains(screenX, screenY)){
					
					return true;
				}
				if(backButton.contains(screenX, screenY)){
					gotoMainMenu();
					return true;
				}
			}
		}
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
	
	public void gotoOptionsMenu(){
		if(menuState != MenuState.main)return;
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				titlePos.ry = Interpolation.sine.apply(titleCenter, titleTop, frameProgress);
				
				playButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				optionsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				exitButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				
				gameButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				backButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.options;
				
				titlePos.ry = titleTop;
				
				playButton.position.rx = left;
				optionsButton.position.rx = left;
				exitButton.position.rx = left;
				
				gameButton.position.rx = center;
				soundButton.position.rx = center;
				graphicsButton.position.rx = center;
				backButton.position.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	public void gotoMainMenu(){
		if(menuState != MenuState.options)return;
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				titlePos.ry = Interpolation.sine.apply(titleTop, titleCenter, frameProgress);
				
				playButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				optionsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				exitButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				
				gameButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				backButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.main;
				
				titlePos.ry = titleCenter;
				
				playButton.position.rx = center;
				optionsButton.position.rx = center;
				exitButton.position.rx = center;
				
				gameButton.position.rx = right;
				soundButton.position.rx = right;
				graphicsButton.position.rx = right;
				backButton.position.rx = right;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
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
		
		private boolean contains(int x, int y){
			Vector2 pos = position.getPosition();
			return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
		}
		
		private void draw(){
			Vector2 pos = position.getPosition();
			Core.graphics.drawRectangle(pos.x, pos.y, w, h, 10, color);
			pos.add(w/2, h/2).sub(fontW/2, -fontH/2);
			Core.graphics.drawText(text, pos, 48f, color);
		}
		
		private void animateColor(int x, int y, Color selected, Color normal){
			if(contains(x, y)){
				color.set(selected);
			}else{
				color.lerp(normal, 1.5f * Gdx.graphics.getDeltaTime());
			}
		}
	}
	
	private class Slider{
		
		private static final float width = 8f;
		private static final float sliderWidth = 12f;
		
		private final Position position;
		private final float length;
		
		private float sliderPosition = 1f;
		private boolean isMoving = false;
		
		private Slider(Position position, float length) {
			this.position = position;
			this.length = length;
		}
		
		private void sliderPressed(int x, int y){
			Vector2 pos = position.getPosition();
			Vector2 center = pos.cpy().add(length / 2, 0);
			if(!Util.isInsideRect(center, new Vector2(x, y), length/2 + width, sliderWidth))return;
			sliderPosition = (x - pos.x) / length;
			sliderPosition = MathUtils.clamp(sliderPosition, 0f, 1f);
			isMoving = true;
		}
		
		private void sliderDragged(int newX, int newY){
			Vector2 pos = position.getPosition();
			sliderPosition = (newX - pos.x) / length;
			sliderPosition = MathUtils.clamp(sliderPosition, 0f, 1f);
		}
		
		private void sliderReleased(){
			isMoving = false;
		}
		
		private void draw(){
			Vector2 start = position.getPosition();
			Vector2 end = new Vector2(start).add(length, 0);
			Core.graphics.drawLine(start, end, width, Color.WHITE);
			
			end.set(start).add(length * sliderPosition, 0);
			Core.graphics.drawCircle(end, sliderWidth, Color.GRAY);
		}
		
		public float getSliderPosition(){
			return sliderPosition;
		}
		
		public void setSliderPosition(float sliderPosition){
			this.sliderPosition = sliderPosition;
		}
	}
	
	private enum MenuState{
		main, options, switching
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
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
