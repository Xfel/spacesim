package test;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import spacegame.ai.scripted.DynamicAdjuster;
import spacegame.ai.scripted.Stabilizer;

import com.jme3.math.FastMath;

public class DynamicAdjusterTest {

	private static final int MAX_ACCEL = 3;

	public static void main(String[] args) throws Exception {

		PrintStream ps = new PrintStream("Data.csv");

		float value = 0;

		float speed = 20;

//		float dst = (float) (Math.random()*2000);
		float dst = 200;

		ps.println("Position; Speed; Acceleration");
		System.out.println("Position; Speed; Acceleration");

		int tc=0;
		
		DynamicAdjuster adjuster=new DynamicAdjuster();
		
		while (tc<300) {
			if (FastMath.abs(speed) < FastMath.ZERO_TOLERANCE && FastMath.abs(value - dst) < FastMath.ZERO_TOLERANCE) {
				break;
			}
			tc++;
			
			if(tc==75){
				speed=-20;
			}
			
			float accel = adjuster.getAcceleration(dst-value, speed);
			
			accel*=MAX_ACCEL;

//			System.out.printf("% 3.3f; % 3.3f; % 3.3f%n", value, speed, accel);
			ps.printf("% 3.3f; % 3.3f; % 3.3f%n", value, speed, accel);

//			Thread.sleep(100);

			speed += accel * .25f;

			value += speed * .25f;

		}

	}
}
