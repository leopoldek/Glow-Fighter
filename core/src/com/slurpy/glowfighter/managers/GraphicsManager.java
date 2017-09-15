package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class GraphicsManager implements Disposable{
	
	private final AssetManager assets;
	private final SpriteBatch batch;
	//private final ShaderProgram glowShader;
	
	private final Texture circle;
	private final TextureRegion square;
	
	public GraphicsManager(){
		assets = new AssetManager();
		assets.load("WhiteCircle.png", Texture.class);
		assets.finishLoading();
		
		circle = assets.get("WhiteCircle.png", Texture.class);
		square = new TextureRegion(circle, 254, 254, 4, 4);
		
		batch = new SpriteBatch();
		
		//glowShader = new ShaderProgram(Gdx.files.internal("shaders/Vertex.glsl"), Gdx.files.internal("shaders/GlowFragment.glsl"));
	}
	
	public void begin(){
		batch.begin();
	}
	
	public void drawLine(Vector2 start, Vector2 end, float width, Color color){
		float halfWidth = width / 2;
		//Set color
		batch.setColor(color);
		//Draw start circle
		batch.draw(circle, start.x - halfWidth, start.y - halfWidth, width, width);
		//Draw end circle
		batch.draw(circle, end.x - halfWidth, end.y - halfWidth, width, width);
		//Draw rectangle between circles.
		end.sub(start);
		batch.draw(square, start.x, start.y - halfWidth, 0, halfWidth, end.len(), width, 1, 1, end.angle());
	}
	
	public void end(){
		batch.end();
	}
	
	public void look(Vector2 look){
		
	}
	
	public void dispose(){
		//glowShader.dispose();
		batch.dispose();
		assets.dispose();
	}
}
