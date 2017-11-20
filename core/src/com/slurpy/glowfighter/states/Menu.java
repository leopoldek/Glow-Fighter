package com.slurpy.glowfighter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.gui.Button;
import com.slurpy.glowfighter.gui.Gui;
import com.slurpy.glowfighter.gui.Label;
import com.slurpy.glowfighter.gui.Position;
import com.slurpy.glowfighter.gui.Rectangle;
import com.slurpy.glowfighter.gui.Slider;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;
import com.slurpy.glowfighter.utils.Action;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.KeyBindings;
import com.slurpy.glowfighter.utils.SoundType;
import com.slurpy.glowfighter.utils.Util;
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
	private final Position titlePos = new Position(center, titleTop, -280, 0);
	private final Color titleColor = new Color();
	private final Task titleColorShift;
	private final Button playButton = new Button("PLAY", new Position(right, 0.5f, -250, -30),  500, 60, Color.WHITE, 48f, 10f);
	private final Button optionsButton = new Button("OPTIONS", new Position(right, 0.5f, -250, -120),  500, 60, Color.WHITE, 48f, 10f);
	private final Button exitButton = new Button("EXIT", new Position(right, 0.5f, -250, -210),  500, 60, Color.WHITE, 48f, 10f);
	
	//Options Menu
	private final Button gameButton = new Button("GAME SETTINGS", new Position(right, 0.4f, -250, 135), 500, 60, Color.WHITE, 48f, 10f);
	private final Button soundButton = new Button("SOUND SETTINGS", new Position(right, 0.4f, -250, 45), 500, 60, Color.WHITE, 48f, 10f);
	private final Button graphicsButton = new Button("GRAPHICS SETTINGS", new Position(right, 0.4f, -250, -45), 500, 60, Color.WHITE, 48f, 10f);
	private final Button optionsBackButton = new Button("BACK", new Position(right, 0.4f, -250, -135), 500, 60, Color.WHITE, 48f, 10f);
	
	//Game Menu
	private final Button keyBindingsButton = new Button("KEY BINDINGS", new Position(right, 0.5f, -250, 160), 500, 60, Color.WHITE, 48f, 10f);
	private final Button creditsButton = new Button("CREDITS", new Position(right, 0.5f, -250, 70), 500, 60, Color.WHITE, 48f, 10f);
	private final Button resetPreferencesButton = new Button("RESET PREFERENCES", new Position(right, 0.5f, -250, -20), 500, 60, Color.WHITE, 48f, 10f);
	private final Rectangle guiBox = new Rectangle(new Position(right, 0.5f, -250, -180) ,500, 130, Color.BLUE.cpy(), 10);
	private final Button showGuiButton = new Button("SHOW GUI", new Position(right, 0.5f, -235, -105), 225, 40, Color.GRAY, 20f, 5f);
	private final Button showPickupIndicatorButton = new Button("SHOW PICKUP INDICATOR", new Position(right, 0.5f, 10, -105), 225, 40, Color.GRAY, 20f, 5f);
	private final Button showFPSButton = new Button("SHOW FPS", new Position(right, 0.5f, -235, -165), 225, 40, Color.GRAY, 20f, 5f);
	private final Button showDamageButton = new Button("SHOW DAMAGE", new Position(right, 0.5f, 10, -165), 225, 40, Color.GRAY, 20f, 5f);
	private final Button gameBackButton = new Button("BACK", new Position(right, 0.5f, -250, -270), 500, 60, Color.WHITE, 48f, 10f);
	
	//Sound Menu
	private final Position masterLabelPos = new Position(right, 0.5f, -368, 100);
	private final Position effectLabelPos = new Position(right, 0.5f, -383, 40);
	private final Position musicLabelPos = new Position(right, 0.5f, -352, -20);
	private final Position interfaceLabelPos = new Position(right, 0.5f, -414, -80);
	private final Slider masterVolume = new Slider(new Position(right, 0.5f, -250, 90), 500f);
	private final Slider effectVolume = new Slider(new Position(right, 0.5f, -250, 30), 500f);
	private final Slider musicVolume = new Slider(new Position(right, 0.5f, -250, -30), 500f);
	private final Slider interfaceVolume = new Slider(new Position(right, 0.5f, -250, -90), 500f);
	private final Button soundBackButton = new Button("BACK", new Position(right, 0.5f, -250, -210), 500, 60, Color.WHITE, 48f, 10f);
	
	//Graphics Menu
	private final Label[] displayLabels;
	private final Button leftButton = new Button("<", new Position(right, 0.5f, -250, 0), 235, 60, Color.WHITE, 48f, 10f);
	private final Button rightButton = new Button(">", new Position(right, 0.5f, 15, 0), 235, 60, Color.WHITE, 48f, 10f);
	private final Button fullscreenButton = new Button("FULLSCREEN", new Position(right, 0.5f, -250, -90), 500, 60, Color.WHITE, 48f, 10f);
	private final Button vsyncButton = new Button("VSYNC", new Position(right, 0.5f, -250, -180), 500, 60, Color.WHITE, 48f, 10f);
	private final Button graphicsBackButton = new Button("BACK", new Position(right, 0.5f, -250, -270), 500, 60, Color.WHITE, 48f, 10f);
	private final Label graphicsInfoLabel;
	private final DisplayMode[] displays;
	private int selectedDisplayMode;
	
	//Keybindings Menu
	private final Label moveUpLabel = new Label(Action.moveUp.toString(), new Position(right, 0.6f, -300, 210), Color.WHITE, 32);
	private final Label moveDownLabel = new Label(Action.moveDown.toString(), new Position(right, 0.6f, -300, 140), Color.WHITE, 32);
	private final Label moveLeftLabel = new Label(Action.moveLeft.toString(), new Position(right, 0.6f, -300, 70), Color.WHITE, 32);
	private final Label moveRightLabel = new Label(Action.moveRight.toString(), new Position(right, 0.6f, -300, 0), Color.WHITE, 32);
	private final Label primaryLabel = new Label(Action.primary.toString(), new Position(right, 0.6f, -300, -70), Color.WHITE, 32);
	private final Label boostLabel = new Label(Action.boost.toString(), new Position(right, 0.6f, -300, -140), Color.WHITE, 32);
	private final Label moveSlowLabel = new Label(Action.moveSlow.toString(), new Position(right, 0.6f, -300, -210), Color.WHITE, 32);
	//Binding buttons
	private final Button moveUpBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, 190), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveUpBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, 190), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveDownBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, 120), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveDownBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, 120), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveLeftBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, 50), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveLeftBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, 50), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveRightBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, -20), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveRightBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, -20), 220, 40, Color.WHITE, 32f, 7f);
	private final Button primaryBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, -90), 220, 40, Color.WHITE, 32f, 7f);
	private final Button primaryBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, -90), 220, 40, Color.WHITE, 32f, 7f);
	private final Button boostBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, -160), 220, 40, Color.WHITE, 32f, 7f);
	private final Button boostBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, -160), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveSlowBinding1Button = new Button("No Binding", new Position(right, 0.6f, -180, -230), 220, 40, Color.WHITE, 32f, 7f);
	private final Button moveSlowBinding2Button = new Button("No Binding", new Position(right, 0.6f, 100, -230), 220, 40, Color.WHITE, 32f, 7f);
	//Back button
	private final Button bindingsBackButton = new Button("BACK", new Position(right, 0.5f, -250, -290), 500, 60, Color.WHITE, 48f, 10f);
	//Bind key dialog
	private Action bindingAction;
	private int bindingIndex;
	private boolean currentlyBinding = false;
	private Button currentlyBindingButton;
	
	//Credits
	private final String credits1 = "Game designed and programmed by Daniel Eliasinski";
	private final String credits2 = "Music by Eric Matyas";
	private final String credits3 = "Font by HolyBlackCat";
	private final String credits4 = "UI sounds by Michael Vogler";
	private final String credits5 = "Other sounds made by Daniel Eliasinski with Bfxr";
	private final Position credits1Pos = new Position(right, 0.6f, -350, 100);
	private final Position credits2Pos = new Position(right, 0.6f, -150, 50);
	private final Position credits3Pos = new Position(right, 0.6f, -150, 0);
	private final Position credits4Pos = new Position(right, 0.6f, -200, -50);
	private final Position credits5Pos = new Position(right, 0.6f, -350, -100);
	private final Color creditsColor = new Color(Color.CHARTREUSE);
	
	//Saved
	private final Position savedPos = new Position(1f, 0f, -144f, 64f);
	private final Color savedColor = Color.YELLOW.cpy();
	
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
		
		savedColor.a = 0f;
		
		displays = Util.getDisplayModes(Gdx.graphics.getPrimaryMonitor());
		displayLabels = new Label[displays.length];
		DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode(Gdx.graphics.getPrimaryMonitor());
		for(int i = 0; i < displayLabels.length; i++){
			DisplayMode display = displays[i];
			if(display.width == currentDisplayMode.width
					&& display.height == currentDisplayMode.height)this.selectedDisplayMode = i;
			String text = display.width + "x" + display.height + "|" + display.refreshRate + "hz";
			displayLabels[i] = new Label(text, new Position(right, 0.5f, 0, 0), Color.WHITE, 48f);
		}
		
		graphicsInfoLabel = new Label(Gdx.graphics.getGLVersion().getDebugVersionString(), new Position(-1f, 0f, 0f, 0f), Color.GRAY, 24f);
		graphicsInfoLabel.position.x = graphicsInfoLabel.getFontW()/2 + 20;
		graphicsInfoLabel.position.y = graphicsInfoLabel.getFontH()/2 + 20;
		
		menuState = MenuState.switching;
	}
	
	@Override
	public void start() {
		Core.bindings.addProcessor(this);
		Core.tasks.addTask(titleColorShift);
		
		TaskBuilder exitButtonBuilder = new TaskBuilder();
		exitButtonBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				exitButton.position.rx = Interpolation.swingOut.apply(right, center, progress);
			}
			@Override
			public void end() {
				menuState = MenuState.main;
				exitButton.position.rx = center;
			}
		}, 1f);
		
		TaskBuilder optionsButtonBuilder = new TaskBuilder();
		optionsButtonBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				optionsButton.position.rx = Interpolation.swingOut.apply(right, center, progress);
			}
			@Override
			public void end() {
				Core.tasks.addTask(exitButtonBuilder);
			}
		}, 0.25f);
		optionsButtonBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				optionsButton.position.rx = Interpolation.swingOut.apply(right, center, progress);
			}
			@Override
			public void end() {
				optionsButton.position.rx = center;
			}
		}, 0.75f);
		
		TaskBuilder playButtonBuilder = new TaskBuilder();
		playButtonBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				playButton.position.rx = Interpolation.swingOut.apply(right, center, progress);
			}
			@Override
			public void end() {
				Core.tasks.addTask(optionsButtonBuilder);
			}
		}, 0.25f);
		playButtonBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				playButton.position.rx = Interpolation.swingOut.apply(right, center, progress);
			}
			@Override
			public void end() {
				playButton.position.rx = center;
			}
		}, 0.75f);
		
		TaskBuilder titleBuilder = new TaskBuilder();
		titleBuilder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				titlePos.ry = Interpolation.bounceOut.apply(titleTop, titleCenter, frameProgress);
			}
			@Override
			public void end() {
				titlePos.ry = titleCenter;
			}
		}, 1.5f);
		
		Core.tasks.addTask(playButtonBuilder);
		Core.tasks.addTask(titleBuilder);
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
		
		//Game
		keyBindingsButton.animateColor(x, y, selected, normal);
		creditsButton.animateColor(x, y, selected, normal);
		resetPreferencesButton.animateColor(x, y, selected, normal);
		showGuiButton.animateColor(x, y, Constants.SHOW_GUI ? Color.CYAN : Color.RED, Constants.SHOW_GUI ? Color.GREEN : Color.GRAY);
		showPickupIndicatorButton.animateColor(x, y, Constants.SHOW_INDICATOR ? Color.CYAN : Color.RED, Constants.SHOW_INDICATOR ? Color.GREEN : Color.GRAY);
		showFPSButton.animateColor(x, y, Constants.SHOW_FPS ? Color.CYAN : Color.RED, Constants.SHOW_FPS ? Color.GREEN : Color.GRAY);
		showDamageButton.animateColor(x, y, Constants.SHOW_DAMAGE ? Color.CYAN : Color.RED, Constants.SHOW_DAMAGE ? Color.GREEN : Color.GRAY);
		gameBackButton.animateColor(x, y, selected, normal);
		
		//Bindings
		moveUpBinding1Button.animateColor(x, y, selected, normal);
		moveUpBinding2Button.animateColor(x, y, selected, normal);
		moveDownBinding1Button.animateColor(x, y, selected, normal);
		moveDownBinding2Button.animateColor(x, y, selected, normal);
		moveLeftBinding1Button.animateColor(x, y, selected, normal);
		moveLeftBinding2Button.animateColor(x, y, selected, normal);
		moveRightBinding1Button.animateColor(x, y, selected, normal);
		moveRightBinding2Button.animateColor(x, y, selected, normal);
		primaryBinding1Button.animateColor(x, y, selected, normal);
		primaryBinding2Button.animateColor(x, y, selected, normal);
		boostBinding1Button.animateColor(x, y, selected, normal);
		boostBinding2Button.animateColor(x, y, selected, normal);
		moveSlowBinding1Button.animateColor(x, y, selected, normal);
		moveSlowBinding2Button.animateColor(x, y, selected, normal);
		bindingsBackButton.animateColor(x, y, selected, normal);
		
		//Sound
		soundBackButton.animateColor(x, y, selected, normal);
		
		//Graphics
		final int range = 4;
		for(int i = 0; i < displayLabels.length; i++){
			Label displayLabel = displayLabels[i];
			float target = MathUtils.clamp(i - selectedDisplayMode, -range, range) / (float)range;
			float actual =  1 - displayLabel.color.a;
			if(displayLabel.position.x < 0)actual = -actual;
			float a = actual + ((target - actual) / 2) * Math.min(Gdx.graphics.getDeltaTime() * 10, 2f);
			displayLabel.position.x = a * 400;
			displayLabel.position.y = a * 200 + 200;
			a = 1 - Math.abs(a);
			displayLabel.color.a = a;
			displayLabel.setText(displayLabel.getText(), Math.max(a * 48, 12));
		}
		leftButton.animateColor(x, y, selected, normal);
		rightButton.animateColor(x, y, selected, normal);
		fullscreenButton.animateColor(x, y, Constants.useFullscreen ? Color.CYAN : Color.RED, Constants.useFullscreen ? Color.GREEN : Color.GRAY);
		vsyncButton.animateColor(x, y, Constants.useVsync ? Color.CYAN : Color.RED, Constants.useVsync ? Color.GREEN : Color.GRAY);
		graphicsBackButton.animateColor(x, y, selected, normal);
	}
	
	@Override
	public void draw() {
		//Play
		Core.graphics.drawText("GLOW FIGHTER", titlePos.getPosition(), 100, titleColor);
		playButton.draw();
		optionsButton.draw();
		exitButton.draw();
		
		//Options
		gameButton.draw();
		soundButton.draw();
		graphicsButton.draw();
		optionsBackButton.draw();
		
		//Sound
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
		guiBox.draw();
		showGuiButton.draw();
		showPickupIndicatorButton.draw();
		showFPSButton.draw();
		showDamageButton.draw();
		gameBackButton.draw();
		
		//Credits
		Core.graphics.drawText(credits1, credits1Pos.getPosition(), 28, creditsColor);
		Core.graphics.drawText(credits2, credits2Pos.getPosition(), 28, creditsColor);
		Core.graphics.drawText(credits3, credits3Pos.getPosition(), 28, creditsColor);
		Core.graphics.drawText(credits4, credits4Pos.getPosition(), 28, creditsColor);
		Core.graphics.drawText(credits5, credits5Pos.getPosition(), 28, creditsColor);
		
		//Bindings
		moveUpLabel.draw();
		moveDownLabel.draw();
		moveLeftLabel.draw();
		moveRightLabel.draw();
		primaryLabel.draw();
		boostLabel.draw();
		moveSlowLabel.draw();
		moveUpBinding1Button.draw();
		moveUpBinding2Button.draw();
		moveDownBinding1Button.draw();
		moveDownBinding2Button.draw();
		moveLeftBinding1Button.draw();
		moveLeftBinding2Button.draw();
		moveRightBinding1Button.draw();
		moveRightBinding2Button.draw();
		primaryBinding1Button.draw();
		primaryBinding2Button.draw();
		boostBinding1Button.draw();
		boostBinding2Button.draw();
		moveSlowBinding1Button.draw();
		moveSlowBinding2Button.draw();
		bindingsBackButton.draw();
		
		//Graphics
		for(Label displayLabel : displayLabels){
			displayLabel.draw();
		}
		leftButton.draw();
		rightButton.draw();
		fullscreenButton.draw();
		vsyncButton.draw();
		graphicsBackButton.draw();
		graphicsInfoLabel.draw();
		
		//Saved
		Core.graphics.drawText("SAVED", savedPos.getPosition(), 42f, savedColor);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(currentlyBinding){
			endBinding(KeyBindings.convertMouseBinding(button));
			return true;
		}
		
		screenY = Gdx.graphics.getHeight() - screenY;
		if(button == Buttons.LEFT){
			if(menuState == MenuState.main){
				if(playButton.contains(screenX, screenY)){
					Core.state.setState(new Survival());
					return true;
				}
				if(optionsButton.contains(screenX, screenY)){
					mainToOptions();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(exitButton.contains(screenX, screenY)){
					Gdx.app.exit();
					return true;
				}
			}else if(menuState == MenuState.options){
				if(gameButton.contains(screenX, screenY)){
					optionsToGame();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(soundButton.contains(screenX, screenY)){
					//Set volumes
					masterVolume.sliderPosition = Core.audio.getMasterVolume();
					effectVolume.sliderPosition = Core.audio.getVolume(SoundType.effect);
					musicVolume.sliderPosition = Core.audio.getVolume(SoundType.music);
					interfaceVolume.sliderPosition = Core.audio.getVolume(SoundType.userInterface);
					
					optionsToSound();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(graphicsButton.contains(screenX, screenY)){
					optionsToGraphics();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(optionsBackButton.contains(screenX, screenY)){
					optionsToMain();
					//Core.audio.playSound(SoundAsset.Select);
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
					saved();
					soundToOptions();
					return true;
				}
			}else if(menuState == MenuState.game){
				if(keyBindingsButton.contains(screenX, screenY)){
					//Set bindings
					IntArray bindings;
					int binding;
					bindings = Core.bindings.getKeys(Action.moveUp);
					binding = bindings.get(0);
					moveUpBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						moveUpBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						moveUpBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.moveDown);
					binding = bindings.get(0);
					moveDownBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						moveDownBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						moveDownBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.moveLeft);
					binding = bindings.get(0);
					moveLeftBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						moveLeftBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						moveLeftBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.moveRight);
					binding = bindings.get(0);
					moveRightBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						moveRightBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						moveRightBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.primary);
					binding = bindings.get(0);
					primaryBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						primaryBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						primaryBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.boost);
					binding = bindings.get(0);
					boostBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						boostBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						boostBinding2Button.setText("NO BINDING", 32f);
					}
					
					bindings = Core.bindings.getKeys(Action.moveSlow);
					binding = bindings.get(0);
					moveSlowBinding1Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					if(bindings.size > 1){
						binding = bindings.get(1);
						moveSlowBinding2Button.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
					}else{
						moveSlowBinding2Button.setText("NO BINDING", 32f);
					}
					
					gameToBindings();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(creditsButton.contains(screenX, screenY)){
					gameToCredits();
					Core.audio.playSound(SoundAsset.Select);
					return true;
				}
				if(resetPreferencesButton.contains(screenX, screenY)){
					Core.audio.setMasterVolume(1f);
					for(SoundType type : SoundType.values()){
						Core.audio.setVolume(type, 1f);
					}
					Core.audio.saveVolumes();
					
					Core.bindings.reset();
					Core.bindings.save();
					
					
					
					saved();
					return true;
				}
				if(showGuiButton.contains(screenX, screenY)){
					Constants.SHOW_GUI = !Constants.SHOW_GUI;
					return true;
				}
				if(showPickupIndicatorButton.contains(screenX, screenY)){
					Constants.SHOW_INDICATOR = !Constants.SHOW_INDICATOR;
					return true;
				}
				if(showFPSButton.contains(screenX, screenY)){
					Constants.SHOW_FPS = !Constants.SHOW_FPS;
					return true;
				}
				if(showDamageButton.contains(screenX, screenY)){
					Constants.SHOW_DAMAGE = !Constants.SHOW_DAMAGE;
					return true;
				}
				if(gameBackButton.contains(screenX, screenY)){
					gameToOptions();
					return true;
				}
			}else if(menuState == MenuState.credits){
				if(gameBackButton.contains(screenX, screenY)){
					creditsToGame();
					return true;
				}
			}else if(menuState == MenuState.bindings){
				if(moveUpBinding1Button.contains(screenX, screenY)){
					startBinding(Action.moveUp, 0, moveUpBinding1Button);
					return true;
				}
				if(moveUpBinding2Button.contains(screenX, screenY)){
					startBinding(Action.moveUp, 1, moveUpBinding2Button);
					return true;
				}
				if(moveDownBinding1Button.contains(screenX, screenY)){
					startBinding(Action.moveDown, 0, moveDownBinding1Button);
					return true;
				}
				if(moveDownBinding2Button.contains(screenX, screenY)){
					startBinding(Action.moveDown, 1, moveDownBinding2Button);
					return true;
				}
				if(moveLeftBinding1Button.contains(screenX, screenY)){
					startBinding(Action.moveLeft, 0, moveLeftBinding1Button);
					return true;
				}
				if(moveLeftBinding2Button.contains(screenX, screenY)){
					startBinding(Action.moveLeft, 1, moveLeftBinding2Button);
					return true;
				}
				if(moveRightBinding1Button.contains(screenX, screenY)){
					startBinding(Action.moveRight, 0, moveRightBinding1Button);
					return true;
				}
				if(moveRightBinding2Button.contains(screenX, screenY)){
					startBinding(Action.moveRight, 1, moveRightBinding2Button);
					return true;
				}
				if(primaryBinding1Button.contains(screenX, screenY)){
					startBinding(Action.primary, 0, primaryBinding1Button);
					return true;
				}
				if(primaryBinding2Button.contains(screenX, screenY)){
					startBinding(Action.primary, 1, primaryBinding2Button);
					return true;
				}
				if(boostBinding1Button.contains(screenX, screenY)){
					startBinding(Action.boost, 0, boostBinding1Button);
					return true;
				}
				if(boostBinding2Button.contains(screenX, screenY)){
					startBinding(Action.boost, 1, boostBinding2Button);
					return true;
				}
				if(moveSlowBinding1Button.contains(screenX, screenY)){
					startBinding(Action.moveSlow, 0, moveSlowBinding1Button);
					return true;
				}
				if(moveSlowBinding2Button.contains(screenX, screenY)){
					startBinding(Action.moveSlow, 1, moveSlowBinding2Button);
					return true;
				}
				if(bindingsBackButton.contains(screenX, screenY)){
					bindingsToGame();
					Core.bindings.save();
					saved();
					return true;
				}
			}else if(menuState == MenuState.graphics){
				if(leftButton.contains(screenX, screenY)){
					selectedDisplayMode--;
					if(selectedDisplayMode < 0)selectedDisplayMode = 0;
					return true;
				}
				if(rightButton.contains(screenX, screenY)){
					selectedDisplayMode++;
					if(selectedDisplayMode >= displays.length)selectedDisplayMode = displays.length - 1;
					return true;
				}
				if(fullscreenButton.contains(screenX, screenY)){
					Constants.useFullscreen = !Constants.useFullscreen;
					return true;
				}
				if(vsyncButton.contains(screenX, screenY)){
					Constants.useVsync = !Constants.useVsync;
					return true;
				}
				if(graphicsBackButton.contains(screenX, screenY)){
					graphicsToOptions();
					if(Constants.useFullscreen){
						if(!Gdx.graphics.isFullscreen() || Gdx.graphics.getDisplayMode() != displays[selectedDisplayMode])
							Gdx.graphics.setFullscreenMode(displays[selectedDisplayMode]);
					}else{
						if(Gdx.graphics.isFullscreen())
							Gdx.graphics.setWindowedMode(Constants.minWidth, Constants.minHeight);
					}
					Gdx.graphics.setVSync(Constants.useVsync);
					//TODO Save graphics settings!
					return true;
				}
			}
		}
		return false;
	}
	
	private void startBinding(Action action, int index, Button bindButton){
		bindingAction = action;
		bindingIndex = index;
		currentlyBinding = true;
		currentlyBindingButton = bindButton;
		bindButton.setText("Press something...", 22f);
	}
	
	private void endBinding(int binding){
		Core.bindings.setBinding(bindingAction, bindingIndex, binding);
		currentlyBindingButton.setText(binding < 0 ? KeyBindings.toString(binding) : Keys.toString(binding), 32f);
		currentlyBinding = false;
	}
	
	private void saved(){
		Core.audio.playSound(SoundAsset.Saved);
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame((progress, frameProgress) -> savedColor.a = Interpolation.circleOut.apply(frameProgress), 0.75f);
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void act(float progress, float frameProgress) {
				savedColor.a = Interpolation.circleOut.apply(1f - frameProgress);
			}
			@Override
			public void end() {
				savedColor.a = 0f;
			}
		}, 0.75f);
		Core.tasks.addTask(builder);
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
				guiBox.position.rx = Interpolation.sine.apply(right, center, frameProgress);
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
				guiBox.position.rx = Interpolation.sine.apply(center, right, frameProgress);
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
	
	private void gameToBindings(){
		if(menuState != MenuState.game)throw new IllegalArgumentException("Must be in options state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				keyBindingsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				guiBox.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				gameBackButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				
				moveUpLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveDownLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveLeftLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveRightLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				primaryLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				boostLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveSlowLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveUpBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveUpBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveDownBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveDownBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveLeftBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveLeftBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveRightBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveRightBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				primaryBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				primaryBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				boostBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				boostBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveSlowBinding1Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				moveSlowBinding2Button.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				bindingsBackButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.bindings;
				
				keyBindingsButton.position.rx = left;
				creditsButton.position.rx = left;
				resetPreferencesButton.position.rx = left;
				guiBox.position.rx = left;
				showGuiButton.position.rx = left;
				showPickupIndicatorButton.position.rx = left;
				showFPSButton.position.rx = left;
				showDamageButton.position.rx = left;
				gameBackButton.position.rx = left;
				
				moveUpLabel.position.rx = center;
				moveDownLabel.position.rx = center;
				moveLeftLabel.position.rx = center;
				moveRightLabel.position.rx = center;
				primaryLabel.position.rx = center;
				boostLabel.position.rx = center;
				moveSlowLabel.position.rx = center;
				moveUpBinding1Button.position.rx = center;
				moveUpBinding2Button.position.rx = center;
				moveDownBinding1Button.position.rx = center;
				moveDownBinding2Button.position.rx = center;
				moveLeftBinding1Button.position.rx = center;
				moveLeftBinding2Button.position.rx = center;
				moveRightBinding1Button.position.rx = center;
				moveRightBinding2Button.position.rx = center;
				primaryBinding1Button.position.rx = center;
				primaryBinding2Button.position.rx = center;
				boostBinding1Button.position.rx = center;
				boostBinding2Button.position.rx = center;
				moveSlowBinding1Button.position.rx = center;
				moveSlowBinding2Button.position.rx = center;
				bindingsBackButton.position.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void bindingsToGame(){
		if(menuState != MenuState.bindings)throw new IllegalArgumentException("Must be in options state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				keyBindingsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				guiBox.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				gameBackButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				
				moveUpLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveDownLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveLeftLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveRightLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				primaryLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				boostLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveSlowLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveUpBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveUpBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveDownBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveDownBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveLeftBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveLeftBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveRightBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveRightBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				primaryBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				primaryBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				boostBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				boostBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveSlowBinding1Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				moveSlowBinding2Button.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				bindingsBackButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.game;
				
				keyBindingsButton.position.rx = center;
				creditsButton.position.rx = center;
				resetPreferencesButton.position.rx = center;
				guiBox.position.rx = center;
				showGuiButton.position.rx = center;
				showPickupIndicatorButton.position.rx = center;
				showFPSButton.position.rx = center;
				showDamageButton.position.rx = center;
				gameBackButton.position.rx = center;
				
				moveUpLabel.position.rx = left;
				moveDownLabel.position.rx = left;
				moveLeftLabel.position.rx = left;
				moveRightLabel.position.rx = left;
				primaryLabel.position.rx = left;
				boostLabel.position.rx = left;
				moveSlowLabel.position.rx = left;
				moveUpBinding1Button.position.rx = left;
				moveUpBinding2Button.position.rx = left;
				moveDownBinding1Button.position.rx = left;
				moveDownBinding2Button.position.rx = left;
				moveLeftBinding1Button.position.rx = left;
				moveLeftBinding2Button.position.rx = left;
				moveRightBinding1Button.position.rx = left;
				moveRightBinding2Button.position.rx = left;
				primaryBinding1Button.position.rx = left;
				primaryBinding2Button.position.rx = left;
				boostBinding1Button.position.rx = left;
				boostBinding2Button.position.rx = left;
				moveSlowBinding1Button.position.rx = left;
				moveSlowBinding2Button.position.rx = left;
				bindingsBackButton.position.rx = left;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void gameToCredits(){
		if(menuState != MenuState.game)throw new IllegalArgumentException("Must be in sound state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				keyBindingsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				guiBox.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(center, left, frameProgress);
				
				credits1Pos.rx = Interpolation.sine.apply(right, center, frameProgress);
				credits2Pos.rx = Interpolation.sine.apply(right, center, frameProgress);
				credits3Pos.rx = Interpolation.sine.apply(right, center, frameProgress);
				credits4Pos.rx = Interpolation.sine.apply(right, center, frameProgress);
				credits5Pos.rx = Interpolation.sine.apply(right, center, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.credits;
				
				keyBindingsButton.position.rx = left;
				creditsButton.position.rx = left;
				resetPreferencesButton.position.rx = left;
				guiBox.position.rx = left;
				showGuiButton.position.rx = left;
				showPickupIndicatorButton.position.rx = left;
				showFPSButton.position.rx = left;
				showDamageButton.position.rx = left;
				
				credits1Pos.rx = center;
				credits2Pos.rx = center;
				credits3Pos.rx = center;
				credits4Pos.rx = center;
				credits5Pos.rx = center;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void creditsToGame(){
		if(menuState != MenuState.credits)throw new IllegalArgumentException("Must be in sound state!");
		TaskBuilder builder = new TaskBuilder();
		builder.addKeyFrame(new KeyFrame(){
			@Override
			public void start() {
				menuState = MenuState.switching;
			}
			@Override
			public void act(float progress, float frameProgress) {
				keyBindingsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				creditsButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				resetPreferencesButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				guiBox.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showGuiButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showPickupIndicatorButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showFPSButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				showDamageButton.position.rx = Interpolation.sine.apply(left, center, frameProgress);
				
				credits1Pos.rx = Interpolation.sine.apply(center, right, frameProgress);
				credits2Pos.rx = Interpolation.sine.apply(center, right, frameProgress);
				credits3Pos.rx = Interpolation.sine.apply(center, right, frameProgress);
				credits4Pos.rx = Interpolation.sine.apply(center, right, frameProgress);
				credits5Pos.rx = Interpolation.sine.apply(center, right, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.game;
				
				keyBindingsButton.position.rx = center;
				creditsButton.position.rx = center;
				resetPreferencesButton.position.rx = center;
				guiBox.position.rx = center;
				showGuiButton.position.rx = center;
				showPickupIndicatorButton.position.rx = center;
				showFPSButton.position.rx = center;
				showDamageButton.position.rx = center;
				
				credits1Pos.rx = right;
				credits2Pos.rx = right;
				credits3Pos.rx = right;
				credits4Pos.rx = right;
				credits5Pos.rx = right;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void optionsToGraphics(){
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
				
				for(Label displayLabel : displayLabels){
					displayLabel.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				}
				leftButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				rightButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				fullscreenButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				vsyncButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				graphicsBackButton.position.rx = Interpolation.sine.apply(right, center, frameProgress);
				graphicsInfoLabel.position.rx = Interpolation.sine.apply(-1f, 0f, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.graphics;
				
				gameButton.position.rx = left;
				soundButton.position.rx = left;
				graphicsButton.position.rx = left;
				optionsBackButton.position.rx = left;
				
				for(Label displayLabel : displayLabels){
					displayLabel.position.rx = center;
				}
				leftButton.position.rx = center;
				rightButton.position.rx = center;
				fullscreenButton.position.rx = center;
				vsyncButton.position.rx = center;
				graphicsBackButton.position.rx = center;
				graphicsInfoLabel.position.rx = 0f;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	private void graphicsToOptions(){
		if(menuState != MenuState.graphics)throw new IllegalArgumentException("Must be in options state!");
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
				
				for(Label displayLabel : displayLabels){
					displayLabel.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				}
				leftButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				rightButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				fullscreenButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				vsyncButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				graphicsBackButton.position.rx = Interpolation.sine.apply(center, right, frameProgress);
				graphicsInfoLabel.position.rx = Interpolation.sine.apply(0f, -1f, frameProgress);
			}
			@Override
			public void end() {
				menuState = MenuState.options;
				
				gameButton.position.rx = center;
				soundButton.position.rx = center;
				graphicsButton.position.rx = center;
				optionsBackButton.position.rx = center;
				
				for(Label displayLabel : displayLabels){
					displayLabel.position.rx = right;
				}
				leftButton.position.rx = right;
				rightButton.position.rx = right;
				fullscreenButton.position.rx = right;
				vsyncButton.position.rx = right;
				graphicsBackButton.position.rx = right;
				graphicsInfoLabel.position.rx = -1;
			}
		}, 0.6f);
		Core.tasks.addTask(builder);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(currentlyBinding){
			if(keycode == Keys.ESCAPE)return false;
			endBinding(keycode);
			return true;
		}else{
			return false;
		}
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
		main, options, game, sound, graphics, bindings, credits, switching
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
