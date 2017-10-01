package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.DeceptivePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class TestEntity extends Entity{
	
	public TestEntity(Vector2 pos, float rot, Color color){
		super(getEntityDef(pos, rot, color));
	}
	
	@Override
	public void update(){
		body.setTransform(body.getPosition(), body.getAngle() + Gdx.graphics.getDeltaTime() * 0.1f);
	}

	@Override
	public void hit(Entity other) {
		
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float rot, Color color){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.parts = new Part[]{new DeceptivePart(new PolygonPart(polygon, 0.2f), 12, 3f, 6f, true)};
		entityDef.setColor(color);
		return entityDef;
	}
	
	private static final float size = 1;
	private static Vector2[] polygon = new Vector2[]{
			new Vector2(-size, -size),
			new Vector2(-size, size),
			new Vector2(size, size),
			new Vector2(size, -size)
	};
	
	static{
		entityDef.polygon = polygon;
		entityDef.category = Category.ENTITY;
		entityDef.team = 0;
		entityDef.bullet = false;
	}
}
