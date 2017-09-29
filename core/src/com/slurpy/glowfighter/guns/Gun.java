package com.slurpy.glowfighter.guns;

public class Gun {
	
	public final float maxTime;
	
	private float timeLeft;
	
	public Gun(float maxTime){
		this.maxTime = maxTime;
	}
	
	public void update(boolean shoot){
		
	}
	
	public float getTimeLeft(){
		return timeLeft;
	}
}
