import java.io.File;

import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Scared implements Behavior {
	private boolean suppressed = false;
	private boolean control = false;
	private boolean isRunning = false;
	private int count = 0;
	private int n = 0;
	private int temp;

	@Override
	public boolean takeControl() {
		return Animal3.touchedData.getTouched();
		//return false;
	}

	@Override
	public void action() {
		Animal3.pilot.stop();
		Delay.msDelay(100);
		Animal3.touchedData.resetTouched();
		//System.out.println("in fear");
		File ff = new File("monster.wav");
		try {
			Sound.playSample(ff, 100);
		      }
		catch (Exception ex) {}
		// Sound.beepSequence(); // fear behavior
		suppressed = false;
		isRunning = true;
		Animal3.fastRotation();
		Animal3.fastTravel();
		Animal3.pilot.rotate(180, true);
		// dist.fetchSample(sample, 0);
		Delay.msDelay(100);
		while (count < 3) {
			Sound.beep();
			Delay.msDelay(100);
			count++;
		}
		count = 0;
		suppressed = true;
		control = false;
		isRunning = false;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
