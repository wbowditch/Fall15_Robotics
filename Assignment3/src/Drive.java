import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

import java.io.File;
import java.util.Random;

//import src.Animal3;

public class Drive implements Behavior {
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	private boolean suppressed = false;
	private int Tp = 3000;

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		while (!suppressed) {
	
			Sumo.left.setSpeed(Tp);
			Sumo.right.setSpeed(Tp);
			Sumo.left1.setSpeed(Tp);
			Sumo.right1.setSpeed(Tp);
			Sumo.left.backward();
			Sumo.right.backward();
			Sumo.left1.backward();
			Sumo.right1.backward();
			while (!suppressed && Sumo.left.isMoving() && Sumo.right.isMoving()  ) {
				Thread.yield();
			}
			
		
	}
	}
}


