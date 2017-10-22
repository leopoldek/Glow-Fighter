package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.entities.traits.KnockbackMultiplier;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;

public class LineWall extends Entity implements KnockbackMultiplier{
	public LineWall(Vector2 pos, float length, float width, float rot, Color color) {
		super(getEntityDef(pos, length, width, rot, color));
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other) {
		
	}
	
	@Override
	public float getMultiplier() {
		return 2f;
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float length, float width, float rot, Color color){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		float halfWidth = width/2;
		entityDef.polygon = new Vector2[]{
				new Vector2(-length, -halfWidth),
				new Vector2(-length, halfWidth),
				new Vector2(length, halfWidth),
				new Vector2(length, -halfWidth)
		};
		entityDef.parts = new Part[]{new LinePart(new Vector2(-length, 0), new Vector2(length, 0), width)};
		entityDef.setColor(color);
		return entityDef;
	}
	
	static{
		entityDef.category = Category.WALL;
		entityDef.team = Team.NEUTRAL;
		entityDef.bullet = false;
		entityDef.bodyType = BodyType.StaticBody;
	}
}
