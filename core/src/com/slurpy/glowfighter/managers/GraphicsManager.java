package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slurpy.glowfighter.utils.Constants;

public class GraphicsManager implements Disposable{
	
	private final AssetManager assets;//TODO Move to own manager.
	private final SpriteBatch batch;
	private final ScreenViewport viewport;
	private Matrix4 fboProj;
	
	private FrameBuffer screenFBO;
	private FrameBuffer pingFBO;
	private FrameBuffer pongFBO;
	private final ShaderProgram glowShader;
	
	private final Texture circle;
	private final TextureRegion square;
	
	public GraphicsManager(){
		assets = new AssetManager();
		assets.load("WhiteCircle.png", Texture.class);
		assets.finishLoading();
		
		circle = assets.get("WhiteCircle.png", Texture.class);
		square = new TextureRegion(circle, 254, 254, 4, 4);
		
		batch = new SpriteBatch();
		
		viewport = new ScreenViewport();
		//viewport.setUnitsPerPixel(1 / 100f);
		
		glowShader = new ShaderProgram(Gdx.files.internal("shaders/Vertex.glsl"), Gdx.files.internal("shaders/GlowFragment.glsl"));
		//glowShader.pedantic = false;
		if(!glowShader.isCompiled())System.out.println(glowShader.getLog());
	}
	
	public void begin(){
		screenFBO.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
	}
	
	private Vector2 temp = new Vector2();
	public void drawLine(Vector2 start, Vector2 end, float width, Color color){
		float halfWidth = width / 2;
		batch.setColor(color);
		//Draw start circle
		batch.draw(circle, start.x - halfWidth, start.y - halfWidth, width, width);
		//Draw end circle
		batch.draw(circle, end.x - halfWidth, end.y - halfWidth, width, width);
		//Draw rectangle between circles.
		temp.x = end.x - start.x;
		temp.y = end.y - start.y;
		batch.draw(square, start.x, start.y - halfWidth, 0, halfWidth, temp.len(), width, 1, 1, temp.angle());
	}
	
	public void drawPolygon(Vector2[] points, float width, Color color){
		float halfWidth = width / 2;
		int last = points.length - 1;
		batch.setColor(color);
		for(int i = 0; i < last; i++){
			//Draw start circle
			batch.draw(circle, points[i].x - halfWidth, points[i].y - halfWidth, width, width);
			//Draw end circle
			batch.draw(circle, points[i+1].x - halfWidth, points[i+1].y - halfWidth, width, width);
			//Draw rectangle between circles.
			temp.x = points[i+1].x - points[i].x;
			temp.y = points[i+1].y - points[i].y;
			batch.draw(square, points[i].x, points[i].y - halfWidth, 0, halfWidth, temp.len(), width, 1, 1, temp.angle());
		}
		//Draw last edge.
		temp.x = points[0].x - points[last].x;
		temp.y = points[0].y - points[last].y;
		batch.draw(square, points[last].x, points[last].y - halfWidth, 0, halfWidth, temp.len(), width, 1, 1, temp.angle());
	}
	
	public void end(){
		batch.flush();
		screenFBO.end();
		batch.setShader(glowShader);
		batch.setColor(Color.WHITE);
		batch.setProjectionMatrix(fboProj);
		
		for(int i = 0; i < Constants.BLUR_PASSES; i++){
			pingFBO.begin();
			glowShader.setUniformi("horizontal", 0);
			if(i == 0)renderFBO(screenFBO);
			else renderFBO(pongFBO);
			batch.flush();
			pingFBO.end();
			
			pongFBO.begin();
			glowShader.setUniformi("horizontal", 1);
			renderFBO(pingFBO);
			batch.flush();
			pongFBO.end();
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setShader(null);
		renderFBO(pongFBO);
		renderFBO(screenFBO);
		batch.end();
	}
	
	private void renderFBO(FrameBuffer fbo){
		Texture tex = fbo.getColorBufferTexture();
		batch.draw(tex, 0, tex.getHeight(), tex.getWidth(), -tex.getHeight());
	}
	
	public void look(Vector2 look){
		Camera cam = viewport.getCamera();
		cam.position.set(look, 0);
		cam.update();
	}
	
	public void resize(int width, int height){
		//System.out.println("RESIZED");
		viewport.update(width, height);
		fboProj = new Matrix4().setToOrtho2D(0, 0, width, height);
		if(screenFBO != null){
			screenFBO.dispose();
			pingFBO.dispose();
			pongFBO.dispose();
		}
		screenFBO = new FrameBuffer(Format.RGBA4444, width, height, false, false);
		pingFBO = new FrameBuffer(Format.RGBA4444, width, height, false, false);
		pongFBO = new FrameBuffer(Format.RGBA4444, width, height, false, false);
	}
	
	public void dispose(){
		glowShader.dispose();
		batch.dispose();
		screenFBO.dispose();
		pingFBO.dispose();
		pongFBO.dispose();
		assets.dispose();
	}
}
