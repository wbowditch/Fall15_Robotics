import java.io.File;

import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class FindFood implements Behavior {
	private boolean suppressed = false;
	private int count = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean takeControl() {
		int lightValue = Animal3.lightData.getLightValue();
		return (lightValue > 48);
	}

	@Override
	public void action() {
		suppressed = false;
		Animal3.pilot.stop();
		File ff = new File("food1.wav");
		try {
			Sound.playSample(ff, 100);
		      }
		catch (Exception ex) {}
		
		
		// Sound.twoBeeps(); //food found
		
		while (count < 3 && !suppressed) {
			Sound.twoBeeps(); // food found
			// light.setFloodlight(-1);
			Delay.msDelay(100);
			// light.setFloodlight(0);
			// Delay.msDelay(100);
			count++;
		}
		count = 0;
		Animal3.pilot.rotate(180);
		// Delay.msDelay(100);
		suppressed = true;

	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
