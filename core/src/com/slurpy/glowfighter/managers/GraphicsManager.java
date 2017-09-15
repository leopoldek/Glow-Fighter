package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class GraphicsManager implements Disposable{
	
	private AssetManager assets;
	private SpriteBatch batch;
	private ShaderProgram glowShader;
	
	public GraphicsManager(){
		batch = new SpriteBatch();
		
		//glowShader = new ShaderProgram(Gdx.files.internal("shaders/Vertex.glsl"), Gdx.files.internal("shaders/GlowFragment.glsl"));
	}
	
	public void begin(){
		batch.begin();
	}
	
	public void drawLine(Vector2 start, Vector2 end, float width, Color color){
		//Set color
		batch.setColor(color);
		//Draw start circle
		
		//Draw end circle
		
		//Draw rectangle between circles.
	}
	
	public void end(){
		batch.end();
	}
	
	public void look(Vector2 look){
		
	}
	
	public void dispose(){
		glowShader.dispose();
		batch.dispose();
		assets.dispose();
	}
}
