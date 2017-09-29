package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slurpy.glowfighter.utils.Constants;

public class GraphicsManager implements Disposable{
	
	private static GraphicsManager singleton;
	
	public static GraphicsManager getGraphicsManager(){
		if(singleton == null)singleton = new GraphicsManager();
		return singleton;
	}
	
	private final SpriteBatch batch;
	private final ScreenViewport viewport;
	private Matrix4 fboProj;
	private ShapeRenderer shapeBatch;
	
	private FrameBuffer screenFBO;//TODO MSAA does not work on FBO. Must use "GL_EXT_framebuffer_multisample" extension or create a FXAA shader.
	//Another solution but not recommended is to draw to default buffer and then copy it to a texture and clear the buffer.
	private FrameBuffer pingFBO;
	private FrameBuffer pongFBO;
	private final ShaderProgram glowShader;
	
	private GraphicsManager(){
		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();
		//batch.setBlendFunction(-1, -1);
		//Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(Constants.MPP);
		
		glowShader = new ShaderProgram(Gdx.files.internal("shaders/Vertex.glsl"), Gdx.files.internal("shaders/GlowFragment.glsl"));
		//glowShader.pedantic = false;
		if(!glowShader.isCompiled())System.out.println(glowShader.getLog());
	}
	
	public void begin(){
		screenFBO.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
		Gdx.gl.glEnable(GL20.GL_BLEND);
		//Gdx.gl.glHint(GL20.GL_PO, GL20.GL_NICEST);
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeBatch.setProjectionMatrix(viewport.getCamera().combined);
		//shapeBatch.setAutoShapeType(true);
		shapeBatch.begin(ShapeType.Filled);
	}
	
	private Vector2 temp = new Vector2();
	public void drawLine(Vector2 start, Vector2 end, float width, Color color){
		shapeBatch.setColor(color);
		shapeBatch.rectLine(start, end, width);
		temp.set(end).sub(start);
		float angle = temp.angle() + 90;
		shapeBatch.arc(start.x, start.y, width/2, angle, 180, Math.max(1, (int)((float)Math.cbrt(width) * angle * (5 / 3))));
		temp.set(start).sub(end);
		shapeBatch.arc(end.x, end.y, width/2, angle - 180, 180, Math.max(1, (int)((float)Math.cbrt(width) * angle * (5 / 3))));
	}
	
	public void drawPolygon(Vector2[] points, float w, Color color){
		shapeBatch.setColor(color);
		len = points.length;
		
		for(int i = 0; i < points.length; i++){
			float a1 = new Vector2(points[wrap(i-1)]).sub(points[i]).angle();
			float a2 = new Vector2(points[wrap(i+1)]).sub(points[i]).angle();
			
			float start = a2 + 90;
			if(start >= 360)start -= 360;
			float degrees = a1 - 90 - start;
			if(degrees < 0)degrees += 360;
			
			//shapeBatch.arc(p3.x, p3.y, w, a1-180, 180-a);
			shapeBatch.arc(points[i].x, points[i].y, w, start, degrees, Math.max(1, (int)((float)Math.cbrt(w) * degrees * (5 / 3))));
			shapeBatch.rect(points[i].x, points[i].y, 0, 0, Vector2.dst(points[i].x, points[i].y, points[wrap(i+1)].x, points[wrap(i+1)].y), w, 1, 1, a2);
		}
	}
	
	public void drawPolyline(Vector2[] points, float w, Color color){
		
	}
	
	public void drawGradientPolyline(Vector2[] points, float w, Color[] color){
		
	}
	
	public void drawGradientPolygon(Vector2[] points, float w, Color[] color){
		
	}
	
	public void drawCircle(Vector2 pos, float radius, Color color){
		shapeBatch.setColor(color);
		shapeBatch.circle(pos.x, pos.y, radius);
	}
	
	private int len;
	private int wrap(int i){
		if(i < 0)i += len;
		return i%len;
	}
	
	public void end(){
		shapeBatch.end();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		//TODO Draw glowing images
		screenFBO.end();
		batch.setShader(glowShader);
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
		//TODO Draw gui pictures n' stuff
		batch.end();
	}
	
	private void renderFBO(FrameBuffer fbo){
		batch.draw(fbo.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
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
		shapeBatch.dispose();
		screenFBO.dispose();
		pingFBO.dispose();
		pongFBO.dispose();
	}
}
