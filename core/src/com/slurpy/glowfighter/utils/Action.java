package com.slurpy.glowfighter.utils;

public enum Action {
	moveUp, moveLeft, moveRight, moveDown, moveSlow, boost, primary/*, item*/;
	
	public String toString(){
		return Util.splitCamelCase(super.toString());
	}
}
