package com.slurpy.glowfighter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.gui.Button;
import com.slurpy.glowfighter.gui.Gui;
import com.slurpy.glowfighter.gui.Position;
import com.slurpy.glowfighter.gui.Slider;
import com.slurpy.glowfighter.utils.SoundType;
import com.slurpy.glowfighter.utils.tasks.KeyFrame;
import com.slurpy.glowfighter.utils.tasks.Task;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class Menu implements Gui, State, InputProcessor{//TODO Refactor class into smaller menu state classes.
	
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
	private final Button playButton = new Button("PLAY", new Position(center, 0.5f, -250, -30),  500, 60, Color.WHITE, 48f);
	private final Button optionsButton = new Button("OPTIONS", new Position(center, 0.5f, -250, -120),  500, 60, Color.WHITE, 48f);
	private final Button exitButton = new Button("EXIT", new Position(center, 0.5f, -250, -210),  500, 60, Color.WHITE, 48f);
	
	//Options Menu
	private final Button gameButton = new Button("GAME SETTINGS", new Position(right, 0.4f, -250, 135), 500, 60, Color.WHITE, 48f);
	private final Button soundButton = new Button("SOUND SETTINGS", new Position(right, 0.4f, -250, 45), 500, 60, Color.WHITE, 48f);
	private final Button graphicsButton = new Button("GRAPHICS SETTINGS", new Position(right, 0.4f, -250, -45), 500, 60, Color.WHITE, 48f);
	private final Button optionsBackButton = new Button("BACK", new Position(right, 0.4f, -250, -135), 500, 60, Color.WHITE, 48f);
	
	//Game Menu
	private final Button keyBindingsButton = new Button("KEY BINDINGS", new Position(right, 0.5f, -250, 160), 500, 60, Color.WHITE, 48f);
	private final Button creditsButton = new Button("CREDITS", new Position(right, 0.5f, -250, 70), 500, 60, Color.WHITE, 48f);
	private final Button resetPreferencesButton = new Button("RESET PREFERENCES", new Position(right, 0.5f, -250, -20), 500, 60, Color.WHITE, 48f);
	private final Position guiBox = new Position(right, 0.5f, -250, -180);
	private final float guiBoxWidth = 500, guiBoxHeight = 130;
	private final Button showGuiButton = new Button("SHOW GUI", new Position(right, 0.5f, -230, -100), 215, 30, Color.WHITE, 20f);
	private final Button showPickupIndicatorButton = new Button("SHOW PICKUP IND.", new Position(right, 0.5f, 15, -100), 215, 30, Color.WHITE, 20f);
	private final Button showFPSButton = new Button("SHOW FPS", new Position(right, 0.5f, -230, -160), 215, 30, Color.WHITE, 20f);
	private final Button showDamageButton = new Button("SHOW DAMAGE", new Position(right, 0.5f, 15, -160), 215, 30, Color.WHITE, 20f);
	private final Button gameBackButton = new Button("BACK", new Position(right, 0.5f, -250, -270), 500, 60, Color.WHITE, 48f);
	
	//Sound Menu
	private final Position masterLabelPos = new Position(right, 0.5f, -368, 100);
	private final Position effectLabelPos = new Position(right, 0.5f, -383, 40);
	private final Position musicLabelPos = new Position(right, 0.5f, -352, -20);
	private final Position interfaceLabelPos = new Position(right, 0.5f, -414, -80);
	private final Slider masterVolume = new Slider(new Position(right, 0.5f, -250, 90), 500f);
	private final Slider effectVolume = new Slider(new Position(right, 0.5f, -250, 30), 500f);
	private final Slider musicVolume = new Slider(new Position(right, 0.5f, -250, -30), 500f);
	private final Slider interfaceVolume = new Slider(new Position(right, 0.5f, -250, -90), 500f);
	private final Button soundBackButton = new Button("BACK", new Position(right, 0.5f, -250, -210), 500, 60, Color.WHITE, 48f);
	
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
		
		//Set volumes
		masterVolume.sliderPosition = Core.audio.getMasterVolume();
		effectVolume.sliderPosition = Core.audio.getVolume(SoundType.effect);
		musicVolume.sliderPosition = Core.audio.getVolume(SoundType.music);
		interfaceVolume.sliderPosition = Core.audio.getVolume(SoundType.userInterface);
		
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
		final int x = Gdx.input.getX();
		final int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		//Main
		playButton.animateColor(x, y, selected, normal);
		optionsButton.animateColor(x, y, selected, normal);
		exitButton.animateColor(x, y, selected, normal);
		
		//Options
		gameButton.animateColor(x, y, selected, normal);
		soundButton.animateColor(x, y, selected, normal);
		graphicsButton.animateColor(x, y, selected, normal);
		optionsBackButton.animateColor(x, y, selected, normal);
		
		//Sound
		soundBackButton.animateColor(x, y, selected, normal);
		
		//Game
		keyBindingsButton.animateColor(x, y, selected, normal);
		creditsButton.animateColor(x, y, selected, normal);
		resetPreferencesButton.animateColor(x, y, selected, normal);
		showGuiButton.animateColor(x, y, selected, normal);
		showPickupIndicatorButton.animateColor(x, y, selected, normal);
		showFPSButton.animateColor(x, y, selected, normal);
		showDamageButton.animateColor(x, y, selected, normal);
		gameBackButton.animateColor(x, y, selected, normal);
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
		optionsBackButton.draw();
		
		Core.graphics.drawText("Master:", masterLabelPos.getPosition(), 32, Color.WHITE);
		Core.graphics.drawText("Effects:", effectLabelPos.getPosition(), 32, Color.WHITE);
		Core.graphics.drawText("Music:", musicLabelPos.getPosition(), 32, Color.WHITE);
		Core.graphics.drawText("Interface:", interfaceLabelPos.getPosition(), 32, Color.WHITE);
		masterVolume.draw();
		effectVolume.draw();
		musicVolume.draw();
		interfaceVolume.draw();
		soundBackButton.draw();
		
		keyBindingsButton.draw();
		creditsButton.draw();
		resetPreferencesButton.draw();
		Vector2 guiBoxPos = guiBox.getPosition();
		Core.graphics.drawRectangle(guiBoxPos.x, guiBoxPos.y, guiBoxWidth, guiBoxHeight, 10, Color.BLUE);
		showGuiButton.draw();
		showPickupIndicatorButton.draw();
		showFPSButton.draw();
		showDamageButton.draw();
		gameBackButton.draw();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;
		if(button == Buttons.LEFT){
			if(menuState == MenuState.main){
				if(playButton.contains(screenX, screenY)){
					Core.state.setState(new Survival());
					return true;
				}
				if(optionsButton.contains(screenX, screenY)){
					mainToOptions();
					return true;
				}
				if(exitButton.contains(screenX, screenY)){
					Gdx.app.exit();
					return true;
				}
			}else if(menuState == MenuState.options){
				if(gameButton.contains(screenX, screenY)){
					optionsToGame();
					return true;
				}
				if(soundButton.contains(screenX, screenY)){
					optionsToSound();
					return true;
				}
				if(graphicsButton.contains(screenX, screenY)){
					
					return true;
				}
				if(optionsBackButton.contains(screenX, screenY)){
					optionsToMain();
					return true;
				}
			}else if(menuState == MenuState.sound){
				if(masterVolume.sliderPressed(screenX, screenY)){
					Core.audio.setMasterVolume(masterVolume.sliderPosition);
					return true;
				}else if(effectVolume.sliderPressed(screenX, screenY)){
					Core.audio.setVolume(SoundType.effect, effectVolume.sliderPosition);
					return true;
				}else if(musicVolume.sliderPressed(screenX, screenY)){
					Core.audio.setVolume(SoundType.music, musicVolume.sliderPosition);
					return true;
				}else if(interfaceVolume.sliderPressed(screenX, screenY)){
					Core.audio.setVolume(SoundType.userInterface, interfaceVolume.sliderPosition);
					return true;
				}else if(soundBackButton.contains(screenX, screenY)){
					Core.audio.saveVolumes();
					soundToOptions();
					return true;
				}
			}else if(menuState == MenuState.game){
				if(keyBindingsButton.contains(screenX, screenY)){
					
					return true;
				}
				if(creditsButton.contains(screenX, screenY)){
					
					return true;
				}
				if(resetPreferencesButton.contains(screenX, screenY)){
					
					return true;
				}
				if(showGuiButton.contains(screenX, screenY)){
					
					return true;
				}
				if(showPickupIndicatorButton.contains(screenX, screenY)){
					
					return true;
				}
				if(showFPSButton.contains(screenX, screenY)){
					
					return true;
				}
				if(showDamageButton.contains(screenX, screenY)){
					
					return true;
				}
				if(gameBackButton.contains(screenX, screenY)){
					gameToOptions();
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean released = false;
		released = masterVolume.sliderReleased();
		if(!released)released = effectVolume.sliderReleased();
		if(!released)released = musicVolume.sliderReleased();
		if(!released)released = interfaceVolume.sliderReleased();
		return released;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(masterVolume.sliderDragged(screenX, screenY)){
			Core.audio.setMasterVolume(masterVolume.sliderPosition);
			return true;
		}else if(effectVolume.sliderDragged(screenX, screenY)){
			Core.audio.setVolume(SoundType.effect, effectVolume.sliderPosition);
			return true;
		}else if(musicVolume.sliderDragged(screenX, screenY)){
			Core.audio.setVolume(SoundType.music, musicVolume.sliderPosition);
			return true;
		}else if(interfaceVolume.sliderDragged(screenX, screenY)){
			Core.audio.setVolume(SoundType.userInterface, interfaceVolume.sliderPosition);
			return true;
		}
		return false;
	}
	
	private void mainToOptions(){
		if(menuState != MenuState.main)throw new IllegalArgumentException("Must be in main state!");
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
				optionsBackButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
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
				optionsBackButton.position.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void optionsToMain(){
		if(menuState != MenuState.options)throw new IllegalArgumentException("Must be in options state!");
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
				optionsBackButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
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
				optionsBackButton.position.rx = right;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void optionsToGame(){
		if(menuState != MenuState.options)throw new IllegalArgumentException("Must be in options state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				gameButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				optionsBackButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				
				keyBindingsButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				guiBox.rx = Interpolation.sine.apply(right, center, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				gameBackButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.game;
				
				gameButton.position.rx = left;
				soundButton.position.rx = left;
				graphicsButton.position.rx = left;
				optionsBackButton.position.rx = left;
				
				keyBindingsButton.position.rx = center;
				creditsButton.position.rx = center;
				resetPreferencesButton.position.rx = center;
				showGuiButton.position.rx = center;
				showPickupIndicatorButton.position.rx = center;
				showFPSButton.position.rx = center;
				showDamageButton.position.rx = center;
				gameBackButton.position.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void gameToOptions(){
		if(menuState != MenuState.game)throw new IllegalArgumentException("Must be in game state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				gameButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				optionsBackButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				
				keyBindingsButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				guiBox.rx = Interpolation.sine.apply(center, right, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				gameBackButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.options;
				
				gameButton.position.rx = center;
				soundButton.position.rx = center;
				graphicsButton.position.rx = center;
				optionsBackButton.position.rx = center;
				
				keyBindingsButton.position.rx = right;
				creditsButton.position.rx = right;
				resetPreferencesButton.position.rx = right;
				showGuiButton.position.rx = right;
				showPickupIndicatorButton.position.rx = right;
				showFPSButton.position.rx = right;
				showDamageButton.position.rx = right;
				gameBackButton.position.rx = right;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void optionsToSound(){
		if(menuState != MenuState.options)throw new IllegalArgumentException("Must be in options state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				gameButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				optionsBackButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				
				masterLabelPos.rx = Interpolation.sine.apply(right, center, frameProgress);
				effectLabelPos.rx = Interpolation.sine.apply(right, center, frameProgress);
				musicLabelPos.rx = Interpolation.sine.apply(right, center, frameProgress);
				interfaceLabelPos.rx = Interpolation.sine.apply(right, center, frameProgress);
				masterVolume.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				effectVolume.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				musicVolume.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				interfaceVolume.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				soundBackButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.sound;
				
				gameButton.position.rx = left;
				soundButton.position.rx = left;
				graphicsButton.position.rx = left;
				optionsBackButton.position.rx = left;
				
				masterLabelPos.rx = center;
				effectLabelPos.rx = center;
				musicLabelPos.rx = center;
				interfaceLabelPos.rx = center;
				masterVolume.position.rx = center;
				effectVolume.position.rx = center;
				musicVolume.position.rx = center;
				interfaceVolume.position.rx = center;
				soundBackButton.position.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void soundToOptions(){
		if(menuState != MenuState.sound)throw new IllegalArgumentException("Must be in sound state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				gameButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				soundButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				graphicsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				optionsBackButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				
				masterLabelPos.rx = Interpolation.sine.apply(center, right, frameProgress);
				effectLabelPos.rx = Interpolation.sine.apply(center, right, frameProgress);
				musicLabelPos.rx = Interpolation.sine.apply(center, right, frameProgress);
				interfaceLabelPos.rx = Interpolation.sine.apply(center, right, frameProgress);
				masterVolume.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				effectVolume.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				musicVolume.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				interfaceVolume.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				soundBackButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.options;
				
				gameButton.position.rx = center;
				soundButton.position.rx = center;
				graphicsButton.position.rx = center;
				optionsBackButton.position.rx = center;
				
				masterLabelPos.rx = right;
				effectLabelPos.rx = right;
				musicLabelPos.rx = right;
				interfaceLabelPos.rx = right;
				masterVolume.position.rx = right;
				effectVolume.position.rx = right;
				musicVolume.position.rx = right;
				interfaceVolume.position.rx = right;
				soundBackButton.position.rx = right;
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
	
	private enum MenuState{
		main, options, game, bindings, sound, graphics, switching
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
