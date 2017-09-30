package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Entity;

public class BasicEnemy extends Entity{

	public BasicEnemy(Vector2 pos, float rot) {
		super(pos, rot, null, null, null);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void hit(Entity other) {
		
	}
}
