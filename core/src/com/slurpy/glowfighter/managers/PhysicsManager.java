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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.entities.traits.Knockback;
import com.slurpy.glowfighter.entities.traits.KnockbackMultiplier;
import com.slurpy.glowfighter.utils.Constants;

public class PhysicsManager implements Disposable{
	
	private static PhysicsManager singleton;
	
	public static PhysicsManager getPhysicsManager(){
		if(singleton == null)singleton = new PhysicsManager();
		return singleton;
	}
	
	private World world;
	
	private float accumulator = 0;
	private Array<Body> queuedDelete = new Array<>(false, 8);
	
	public PhysicsManager(){
		world = new World(new Vector2(), false);
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
				
				Vector2 normal = contact.getWorldManifold().getNormal();
				normal.nor();
				if(entityA instanceof Knockback){
					Vector2 pos = entityA.getPosition();
					float knockback = ((Knockback)entityA).getKnockback();
					if(entityB instanceof KnockbackMultiplier)knockback *= ((KnockbackMultiplier)entityB).getMultiplier();
					entityA.body.applyLinearImpulse(-normal.x * knockback, -normal.y * knockback, pos.x, pos.y, true);
				}
				if(entityB instanceof Knockback){
					Vector2 pos = entityB.getPosition();
					float knockback = ((Knockback)entityB).getKnockback();
					if(entityA instanceof KnockbackMultiplier)knockback *= ((KnockbackMultiplier)entityA).getMultiplier();
					entityB.body.applyLinearImpulse(normal.x * knockback, normal.y * knockback, pos.x, pos.y, true);
				}
				
				if(entityA instanceof Health && entityB instanceof Damage)((Health)entityA).takeDamage(((Damage)entityB).getDamage());
				if(entityB instanceof Health && entityA instanceof Damage)((Health)entityB).takeDamage(((Damage)entityA).getDamage());
			}
		});
	}
	
	public Body createEntityBody(Entity entity, BodyDef bodyDef){
		Body body = world.createBody(bodyDef);
		body.setUserData(entity);
		return body;
	}
	
	public void queueDestroy(Body body){
		queuedDelete.add(body);
	}
	
	public void destroy(Body body){
		world.destroyBody(body);
	}
	
	//TODO Use raycasts instead
	public ObjectSet<Entity> getEntitiesInRadius(final Vector2 pos, final float radius){
		final ObjectSet<Entity> entities = new ObjectSet<>();
		final float radius2 = radius * radius;
		world.QueryAABB(fixture -> {
			if(fixture.getBody().getPosition().dst2(pos) <= radius2)entities.add((Entity)fixture.getBody().getUserData());
			return true;
		}, pos.x - radius, pos.y - radius, pos.x + radius, pos.y + radius);
		return entities;
	}
	
	public void update(){
		//Destroy bodies.
		while(queuedDelete.size != 0){
			world.destroyBody(queuedDelete.first());
			queuedDelete.removeIndex(0);
		}
		
		// fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
	    accumulator += frameTime;
	    while(accumulator >= Constants.TIME_STEP){
	        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
	        accumulator -= Constants.TIME_STEP;
	    }
	}

	@Override
	public void dispose() {
		world.dispose();
	}
}
