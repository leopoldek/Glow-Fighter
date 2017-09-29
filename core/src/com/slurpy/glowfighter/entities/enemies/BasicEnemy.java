package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.parts.Part;

public class BasicEnemy extends Entity{

	public BasicEnemy(Vector2 pos, float rot) {
		super(pos, rot, parts);
	}
}
