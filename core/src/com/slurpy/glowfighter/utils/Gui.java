package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.guns.Gun;

public class Gui{
	
	private int width, height;
	
	private final Position healthBar = new Position(50, 50, Anchor.end, Anchor.start);
	private final Position gunStats = new Position(50, 50, Anchor.start, Anchor.start);
	private final Position fps = new Position(10, 10, Anchor.start, Anchor.end);
	private final Position level = new Position(-40, 10, Anchor.center, Anchor.end);
	
	public Gui(){
		
	}
	
	public void draw(){
		Player player = (Player)Core.entities.getGroup("player").iterator().next();
		Vector2 start = healthBar.getPosition();
		float health = player.getHealth();
		Vector2 end = new Vector2(start).add(health * -2, 0);
		Core.graphics.drawLine(start, end, 20f, health <= 0 ? Color.GRAY : Color.RED);
		
		start = gunStats.getPosition();
		if(!player.usingDefault()){
			Gun gun = player.getGun();
			float time = gun.getTimeLeft() / gun.maxTime;
			end.set(start).add(time * 200, 0);
			Core.graphics.drawLine(start, end, 20f, Color.RED.cpy().lerp(Color.CHARTREUSE, time));
			Core.graphics.drawText(gun.getName(), start.add(0, 40), 32, Color.NAVY);
		}
		
		Core.graphics.drawText(Integer.toString(Gdx.graphics.getFramesPerSecond()), fps.getPosition(), 24, Color.WHITE);
		Core.graphics.drawText("Level " + Core.game.getLevel(), level.getPosition(), 32, Color.WHITE);
	}
	
	public void resize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	private enum Anchor{
		start, center, end
	}
	
	private class Position{
		
		private float x;
		private float y;
		private Anchor horizontal;
		private Anchor vertical;
		
		private final Vector2 pos = new Vector2();
		
		private Position(float x, float y, Anchor horizontal, Anchor vertical) {
			this.x = x;
			this.y = y;
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
		
		private Vector2 getPosition(){
			if(horizontal == Anchor.start){
				pos.x = x;
			}else if(horizontal == Anchor.center){
				pos.x = x + width/2;
			}else{
				pos.x = width - x;
			}
			
			if(vertical == Anchor.start){
				pos.y = y;
			}else if(vertical == Anchor.center){
				pos.y = y + height/2;
			}else{
				pos.y = height - y;
			}
			return pos;
		}
	}
}
