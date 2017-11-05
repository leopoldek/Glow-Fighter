package com.slurpy.glowfighter.entities;

public enum Category {
	ENTITY(0b1000, 0b1111), BULLET(0b0100, 0b1010), WALL(0b0010, 0b1100)/*, ITEM(0b0001, 0b1000)*/;
	
	public final short categoryBits;
	public final short maskBits;
	
	Category(int category, int mask){
		categoryBits = (short)category;
		maskBits = (short)mask;
	}
}
