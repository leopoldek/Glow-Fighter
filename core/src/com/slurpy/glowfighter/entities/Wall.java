package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class Wall extends Entity {
	
	public Wall(Vector2 pos, Vector2 size, float rot, Color color) {
		super(getEntityDef(pos, size, rot, color));
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other) {
		
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, Vector2 size, float rot, Color color){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.polygon = new Vector2[]{
				new Vector2(-size.x, -size.y),
				new Vector2(-size.x, size.y),
				new Vector2(size.x, size.y),
				new Vector2(size.x, -size.y)
		};
		entityDef.parts = new Part[]{new PolygonPart(entityDef.polygon, 0.5f)};
		entityDef.setColor(color);
		return entityDef;
	}
	
	static{
		entityDef.category = Category.WALL;
		entityDef.team = 0;
		entityDef.bullet = false;
	}
}
