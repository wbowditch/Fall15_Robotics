import java.io.File;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

public class Avoid implements Behavior {

	private boolean suppressed = false;
	private int distance;
	private Random random = new Random();
	private boolean turning = false;

	public boolean takeControl() {
		distance = Animal3.sonicData.getSonicValue();
		if (distance < 20)
			return true;
		else
			return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		//Sound.buzz();
		File ff = new File("wall.wav");
		try {
			Sound.playSample(ff, 100);
		      }
		catch (Exception ex) {}
		Animal3.pilot.stop();
		Animal3.mediumTravel();
		Animal3.fastRotation();

		// Randomly determine whether the turn will be left or right
		int turn = Math.random() > 0.5 ? -1 : 1;
		// determine the angle of the turn
		int angle = 60 + random.nextInt(120);

		// Turn
		turning = true;
		Animal3.pilot.rotate(turn * angle, true);
		while (!suppressed && Animal3.pilot.isMoving())
			Thread.yield();
		turning = false;

		// Move forward
		//Animal3.pilot.travel(20 + random.nextInt(30), true);
		//while (!suppressed && Animal3.pilot.isMoving())
		//	Thread.yield();

		// AutAnimal.pilot.stop();
		Animal3.slowRotation();
		Animal3.slowTravel();

		/*
		 * int turn = Math.random() > 0.5 ? -1 : 1; // Determine which way to
		 * turn int angle = 15 + random.nextInt(60); Animal3.pilot.rotate(turn *
		 * angle, true);
		 */
		// Motor.B.rotate(180, true);
		// Motor.A.rotate(-180);
		// Motor.B.resetTachoCount();
		// Motor.A.resetTachoCount();
	}
}
