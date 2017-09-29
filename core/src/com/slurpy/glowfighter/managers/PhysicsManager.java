package com.slurpy.glowfighter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.utils.Constants;

public class PhysicsManager {
	
	private static PhysicsManager singleton;
	
	public static PhysicsManager getPhysicsManager(){
		if(singleton == null)singleton = new PhysicsManager();
		return singleton;
	}
	
	private World world;
	private float accumulator = 0;
	
	public PhysicsManager(){
		world = new World(new Vector2(), true);
		world.setContactListener(new ContactListener() {
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			@Override
			public void endContact(Contact contact) {}
			@Override
			public void beginContact(Contact contact) {
				Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
				Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();
				entityA.hit(entityB);
				entityB.hit(entityA);
				
				//contact.getFixtureA().getBody().applyLinearImpulse(contact.getWorldManifold().getNormal(), contact.getWorldManifold().get, wake);
			}
		});
	}
	
	public Body createEntityBody(Entity entity, Vector2[] points, boolean bullet){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.active = true;
		//def.position.set(pos);
		//def.angle = rot;
		def.fixedRotation = true;
		def.linearDamping = 1f;
		
		Body body = world.createBody(def);
		body.setUserData(entity);
		
		return body;
	}
	
	public void queueDestroy(Body body){
		
	}
	
	public void update(){
		// fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
	    accumulator += frameTime;
	    while(accumulator >= Constants.TIME_STEP){
	        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
	        accumulator -= Constants.TIME_STEP;
	    }
	}
}
