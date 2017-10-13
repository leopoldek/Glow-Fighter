package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Util{
	
	/**
	 * Rotates the effect to point in the desired direction. Why this wasn't added to the library
	 * in the first place is one of the great mysteries of our time.
	 * 
	 * @param effect Effect you'd like to rotate.
	 * @param targetAngle Angle which you'd like the effect to point in.
	 */
	public static void rotateEffect(ParticleEffect effect, float targetAngle){
		for (ParticleEmitter emitter : effect.getEmitters()){
			ScaledNumericValue angle = emitter.getAngle();
			
			float spanHighHalf = (angle.getHighMax() - angle.getHighMin()) / 2f;
			angle.setHigh(targetAngle - spanHighHalf / 2.0f, targetAngle + spanHighHalf);
			
			float spanLowHalf = (angle.getLowMax() - angle.getLowMin()) / 2f;
			angle.setLow(targetAngle - spanLowHalf, targetAngle + spanLowHalf);
		}
	}
	
	public static Vector2 setVector2(Vector2 vector2, Vector3 vector3){
		return vector2.set(vector3.x, vector3.y);
	}
}
