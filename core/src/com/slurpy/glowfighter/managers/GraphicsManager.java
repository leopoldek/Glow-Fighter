package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.managers.AssetManager.EffectAsset;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.Util;

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
	
	private final Vector2 cameraPos = new Vector2();
	private Entity follow;
	private float shake = 0f;
	
	private final BitmapFont font;
	
	private final Queue<TextDraw> drawTextQueue = new Queue<>();
	private final Queue<PooledEffect> drawEffectQueue = new Queue<>();
	private final Queue<TextureDraw> drawTextureQueue = new Queue<>();
	private final Array<PooledEffect> effectArray = new Array<>(false, 16);
	
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
		
		//Create BitMapFont
		font = Core.assets.getFont(FontAsset.CatV);
		font.setColor(Color.WHITE);
		font.setUseIntegerPositions(false);
	}
	
	public void begin(){
		screenFBO.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(follow != null)cameraPos.set(follow.getPosition());
		Camera cam = viewport.getCamera();
		if(shake > 0f){
			Vector2 actualPos = new Vector2().setToRandomDirection().scl(shake);
			actualPos.add(cameraPos);
			cam.position.set(actualPos, 0);
			shake -= 2.1f * Gdx.graphics.getDeltaTime();
		}else{
			cam.position.set(cameraPos, 0);
			shake = 0f;
		}
		cam.update();
		
		shapeBatch.setProjectionMatrix(viewport.getCamera().combined);
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
			shapeBatch.arc(points[i].x, points[i].y, w, start, degrees, 3);
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
	private int wrap(int i){//TODO Get rid of this soon
		if(i < 0)i += len;
		return i%len;
	}
	
	public void drawText(String text, Vector2 pos, float size){
		drawTextQueue.addLast(new TextDraw(text, pos.x, pos.y, size));
	}
	
	public void drawParticle(EffectAsset effect, Vector2 pos, float rot, float scl){
		PooledEffect pooledEffect = Core.assets.getEffect(effect);
		pooledEffect.setEmittersCleanUpBlendFunction(false);
		pooledEffect.setPosition(pos.x, pos.y);
		Util.rotateEffect(pooledEffect, rot);
		pooledEffect.scaleEffect(scl);
		effectArray.add(pooledEffect);
	}
	
	public void drawParticle(EffectAsset effect, Vector2 pos){
		PooledEffect pooledEffect = Core.assets.getEffect(effect);
		pooledEffect.setEmittersCleanUpBlendFunction(false);
		pooledEffect.setPosition(pos.x, pos.y);
		effectArray.add(pooledEffect);
	}
	
	public void drawParticle(PooledEffect effect){
		drawEffectQueue.addLast(effect);
	}
	
	public void drawTexture(TextureRegion region, Vector2 pos, Vector2 size, Vector2 origin, float rot){
		drawTextureQueue.addLast(new TextureDraw(region, pos.x, pos.y, size.x, size.y, origin.x, origin.y, rot));
	}
	
	public void end(){
		shapeBatch.end();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		
		while(drawTextQueue.size != 0){
			TextDraw text = drawTextQueue.removeFirst();
			font.getData().setScale(text.size * Constants.MPP);//Font size is 48 for CatV
			font.draw(batch, text.text, text.x, text.y);
		}
		
		while(drawEffectQueue.size != 0){
			drawEffectQueue.removeFirst().draw(batch, Gdx.graphics.getDeltaTime());
		}
		
		while(drawTextureQueue.size != 0){
			TextureDraw texture = drawTextureQueue.removeFirst();
			batch.draw(texture.texture, texture.x, texture.y, texture.originX, texture.origonY, texture.width, texture.height, 1, 1, texture.rot * MathUtils.radiansToDegrees);
		}
		
		for(int i = effectArray.size - 1; i >= 0; i--){
			PooledEffect effect = effectArray.get(i);
			effect.draw(batch, Gdx.graphics.getDeltaTime());
			if(effect.isComplete()){
				effect.free();
				effectArray.removeIndex(i);
			}
		}
		
		//TODO Make separate draw loop for additive particle effects
		batch.flush();
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
		follow = null;
		Camera cam = viewport.getCamera();
		cam.position.set(look, 0);
		cam.update();
	}
	
	public void follow(Entity entity){
		follow = entity;
	}
	
	public Vector2 getCameraPosition(){
		return cameraPos.cpy();
	}
	
	public void shake(float amount){
		shake += amount;
		if(shake > Constants.MAX_SHAKE)shake = Constants.MAX_SHAKE;
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
	
	private class TextDraw{
		private final String text;
		private final float x;
		private final float y;
		private final float size;
		private TextDraw(String text, float x, float y, float size){
			this.text = text;
			this.x = x;
			this.y = y;
			this.size = size;
		}
	}
	private class TextureDraw{
		private final TextureRegion texture;
		private final float x;
		private final float y;
		private final float width;
		private final float height;
		private final float originX;
		private final float origonY;
		private final float rot;
		public TextureDraw(TextureRegion texture, float x, float y, float width, float height, float originX, float origonY, float rot) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.originX = originX;
			this.origonY = origonY;
			this.rot = rot;
		}
	}
}
