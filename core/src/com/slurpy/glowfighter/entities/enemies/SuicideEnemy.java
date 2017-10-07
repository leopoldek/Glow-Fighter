package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.parts.TrailPart;

public class SuicideEnemy extends Entity{

	public SuicideEnemy(Vector2 pos, float rot) {
		super(getEntityDef(pos, rot));
	}

	@Override
	public void update() {
		
	}

	@Override
	public void hit(Entity other) {
		
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float rot){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		return entityDef;
	}
	
	private static float size = 0.1f;
	private static Vector2[] polygon = new Vector2[]{new Vector2(size, 0), new Vector2(-size, -size), new Vector2(-size, size)};
	
	static{
		entityDef.polygon = polygon;
		entityDef.parts = new Part[2];
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.setColor(Color.WHITE);
		entityDef.parts = new Part[]{
				new TrailPart(new PolygonPart(polygon, 0.1f), 1f, 0.65f),
				new LinePart(new Vector2(0, 0), new Vector2(-0.3f, 0), 0.1f)
		};
	}
}
