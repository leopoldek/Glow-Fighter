package com.slurpy.glowfighter.managers;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.entities.traits.Knockback;
import com.slurpy.glowfighter.entities.traits.KnockbackMultiplier;
import com.slurpy.glowfighter.utils.Constants;
import com.slurpy.glowfighter.utils.tasks.TaskBuilder;

public class PhysicsManager implements Disposable{
	
	private World world;
	
	private float accumulator = 0;
	private Array<Body> queuedDelete = new Array<>(false, 8);
	private boolean paused = false;
	
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
				
				boolean isA = contact.getFixtureA().isSensor();
				boolean isB = contact.getFixtureB().isSensor();
				if(isA && isB)return;
				if(isA){
					entityA.hit(entityB);
					return;
				}
				if(isB){
					entityB.hit(entityA);
					return;
				}
				//Neither are sensors so continue with normal collision.
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
				
				if(entityA instanceof Health && entityB instanceof Damage){
					final float dmg = ((Damage)entityB).getDamage();
					((Health)entityA).takeDamage(dmg);
					
					if(Constants.SHOW_DAMAGE){
						final String num = Integer.toString((int)dmg);
						final Vector2 collision = contact.getWorldManifold().getPoints()[0].cpy();
						TaskBuilder builder = new TaskBuilder();
						builder.addKeyFrame((progress, frameProgress) -> {
							frameProgress = Interpolation.circleOut.apply(frameProgress);
							Core.graphics.drawText(num, new Vector2(collision).add(0, frameProgress * 6),
									0.7f, Color.WHITE.cpy().lerp(Color.CLEAR, frameProgress));
						}, 4f);
						Core.tasks.addTask(builder);
					}
				}
				if(entityB instanceof Health && entityA instanceof Damage){
					float dmg = ((Damage)entityA).getDamage();
					((Health)entityB).takeDamage(dmg);
					
					if(Constants.SHOW_DAMAGE){
						final String num = Integer.toString((int)dmg);
						final Vector2 collision = contact.getWorldManifold().getPoints()[0].cpy();
						TaskBuilder builder = new TaskBuilder();
						builder.addKeyFrame((progress, frameProgress) -> {
							frameProgress = Interpolation.circleOut.apply(frameProgress);
							Core.graphics.drawText(num, new Vector2(collision).add(0, frameProgress * 6),
									0.7f, Color.WHITE.cpy().lerp(Color.CLEAR, frameProgress));
						}, 4f);
						Core.tasks.addTask(builder);
					}
				}
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
	
	public ObjectSet<Entity> getEntitiesInRadius(final Vector2 pos, final float radius){
		final ObjectSet<Entity> entities = new ObjectSet<>();
		final float radius2 = radius * radius;
		world.QueryAABB(fixture -> {
			if(fixture.getBody().getPosition().dst2(pos) <= radius2)entities.add((Entity)fixture.getBody().getUserData());
			return true;
		}, pos.x - radius, pos.y - radius, pos.x + radius, pos.y + radius);
		return entities;
	}
	
	public ObjectSet<Entity> getEntitiesInSight(final Vector2 pos, final float radius){
		final ObjectSet<Entity> entities = getEntitiesInRadius(pos, radius);
		filter:for(Iterator<Entity> i = entities.iterator(); i.hasNext();){
			Entity entity = i.next();
			for(Fixture fixture : entity.body.getFixtureList()){
				//We have to do this because raycast omits bodies inside the starting pos.
				if(fixture.testPoint(pos))continue filter;
			}
			world.rayCast((fixture, point, normal, fraction) -> {
				if(fixture.getBody().getUserData() != entity){
					i.remove();
					return 0;
				}
				return fraction;
			}, pos, entity.getPosition());
		}
		return entities;
	}
	
	public void update(){
		//Destroy bodies.
		while(queuedDelete.size != 0){
			world.destroyBody(queuedDelete.first());
			queuedDelete.removeIndex(0);
		}
		if(paused)return;
		// fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
	    accumulator += frameTime;
	    while(accumulator >= Constants.TIME_STEP){
	        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
	        accumulator -= Constants.TIME_STEP;
	    }
	}
	
	/*public void clear(){
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for(Body body : bodies){
			world.destroyBody(body);
		}
		
		Array<Joint> joints = new Array<>();
		world.getJoints(joints);
		for(Joint joint : joints){
			world.destroyJoint(joint);
		}
		
	}*/
	
	public void setPaused(boolean paused){
		this.paused = paused;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}
}
