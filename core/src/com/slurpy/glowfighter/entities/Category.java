package com.slurpy.glowfighter.entities;

public enum Category {
	ENTITY(0b100, 0b111), BULLET(0b010, 0b101), WALL(0b001, 0b110);
	
	public final short categoryBits;
	public final short maskBits;
	
	Category(int category, int mask){
		categoryBits = (short)category;
		maskBits = (short)mask;
	}
}
