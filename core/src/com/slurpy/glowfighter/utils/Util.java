package com.slurpy.glowfighter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.managers.AssetManager.FontAsset;

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
	
	public static JsonValue loadJson(String file){
		return new JsonReader().parse(Gdx.files.internal(file).readString());
	}
	
	public static Vector2 getBoundryPoint(Vector2 center, Vector2 point, float width, float height){
		float x = point.x - center.x;
		float y = point.y - center.y;
		float a = width/2;
		float b = height/2;
		
		float ay = a * y;
		float bx = b * x;
		
		Vector2 answer = new Vector2();
		if(Math.abs(ay) < Math.abs(bx)){
			if(x > 0){
				answer.set(a, ay / x);
			}else{
				answer.set(-a, -ay / x);
			}
		}else{
			if(y > 0){
				answer.set(bx / y, b);
			}else{
				answer.set(-bx / y, -b);
			}
		}
		return answer.add(center);
	}
	
	public static boolean isInsideRect(Vector2 center, Vector2 point, float width, float height){
		float halfWidth = width/2;
		float halfHeight = height/2;
		if(point.x > center.x + halfWidth || point.x < center.x - halfWidth)return false;
		if(point.y > center.y + halfHeight || point.y < center.y - halfHeight)return false;
		return true;
	}
	
	public static Vector2 randomTriangularVector(float range){
		return new Vector2(MathUtils.randomTriangular(-range, range), MathUtils.randomTriangular(-range, range));
	}
	
	public static Vector2 randomTriangularVector(float width, float height){
		return new Vector2(MathUtils.randomTriangular(-width, width), MathUtils.randomTriangular(-height, height));
	}
	
	public static Vector2 getTextSize(FontAsset fontAsset, String text, float scale){
		BitmapFont font = Core.assets.getFont(fontAsset);
		font.getData().setScale(scale / 48f);
		GlyphLayout layout = new GlyphLayout(font, text);
		return new Vector2(layout.width, layout.height);
	}
	
	public static String splitCamelCase(String s) {
		s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
		return s.replaceAll(
				String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"),
				" ");
	}
	
	public static DisplayMode[] getDisplayModes(Monitor monitor){
		Array<DisplayMode> displays = new Array<>(Gdx.graphics.getDisplayModes(monitor));
		
		for(int i = displays.size - 1; i >= 0; i--){
			DisplayMode display = displays.get(i);
			//Removes modes that are smaller than Constants.minWidth x Constants.minHeight
			if(display.width < Constants.minWidth || display.height < Constants.minHeight){
				displays.removeIndex(i);
				continue;
			}
			//Removes modes that have a refresh rate lower than 50hz
			if(display.refreshRate < 50){
				displays.removeIndex(i);
			}
		}
		
		//Removes display modes that have same settings except for refresh rate. Assume same bpp.
		for(int i = displays.size - 1; i >= 0; i--){
			DisplayMode display1 = displays.get(i);
			for(int nested = 0; nested < displays.size; nested++){
				DisplayMode display2 = displays.get(nested);
				if(display1 != display2 && display1.width == display2.width
						&& display1.height == display2.height
						&& display1.refreshRate <= display2.refreshRate){
					displays.removeIndex(i);
					break;
				}
			}
		}
		
		//Sorts display modes by width, then height.
		displays.sort((DisplayMode display1, DisplayMode display2) -> {
			if(display1.width > display2.width){
				return 1;
			}else if(display1.width < display2.width){
				return -1;
			}else{
				if(display1.height > display2.height){
					return 1;
				}else if(display1.height < display2.height){
					return -1;
				}else{
					return 0;
				}
			}
		});
		
		return displays.toArray(DisplayMode.class);
	}
}
