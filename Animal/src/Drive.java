import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

import java.io.File;
import java.util.Random;

public class Drive implements Behavior {
	private int distance;
	private boolean suppressed = false;
	private Random random = new Random();

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		File ff = new File("move.wav");
		try {
			Sound.playSample(ff, 100);
		} catch (Exception ex) {
		}
		Animal3.slowTravel();
		Animal3.slowRotation();
		while (!suppressed) {
			// Determine what action to take, 66% chance to travel, 33% chance
			// to turn
			int action = random.nextInt(3) + 1;
			// Travel
			if (action <= 2) {
				int direction = Math.random() < 0.25 ? -1 : 1; // 25% chance to
																// travel
																// backwards,
																// 75% to travel
																// forward
				Animal3.pilot.travel(direction * (random.nextInt(30) + 10),
						true);
			}
			// Turn
			if (action > 2) {
				int turn = Math.random() > 0.5 ? -1 : 1; // Determine which way
															// to turn
				int angle = 15 + random.nextInt(60);
				Animal3.pilot.rotate(turn * angle, true);
			}

			while (!suppressed && Animal3.pilot.isMoving()) {
				Thread.yield();
			}
		}
		Animal3.pilot.stop();
	}

}
