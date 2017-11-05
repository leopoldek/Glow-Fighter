package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.entities.traits.KnockbackMultiplier;
import com.slurpy.glowfighter.guns.Gun;
import com.slurpy.glowfighter.parts.CirclePart;
import com.slurpy.glowfighter.parts.Part;

public class GunPickup extends Entity implements KnockbackMultiplier{
	
	private final Gun gun;
	
	public GunPickup(Vector2 pos, Gun gun) {
		super(getEntityDef(pos));
		this.gun = gun;
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other){
		if(other instanceof Player){
			Player player = (Player)other;
			player.setGun(gun);
			delete();
		}
	}
	
	@Override
	public float getMultiplier() {
		return 0;
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos){
		entityDef.pos.set(pos);
		return entityDef;
	}
	
	private static float size = 0.5f;
	private static Vector2[] polygon = new Vector2[]{
			new Vector2(size, size),
			new Vector2(size, -size),
			new Vector2(-size, -size),
			new Vector2(-size, size)
	};
	
	static{
		entityDef.polygon = polygon;
		entityDef.parts = new Part[]{new CirclePart(new Vector2(), size)};
		entityDef.category = Category.ENTITY;
		entityDef.bullet = false;
		entityDef.sensor = true;
		entityDef.setColor(Color.GREEN);
		entityDef.team = Team.NEUTRAL;
		entityDef.bodyType = BodyType.StaticBody;
	}
}
