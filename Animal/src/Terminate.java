import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;


public class Terminate implements Behavior {
	private boolean suppressed = false;
	@Override
	public boolean takeControl() {
		return (Animal3.finish);
	}

	@Override
	public void action() {
		//Animal3.pilot.stop();
		//Animal3.light.close();
		//Animal3.ultra.close();
		//Animal3.touch.close();
		File ff = new File("starwars.wav");
		try {
			Sound.playSample(ff, 100);
		      }
		catch (Exception ex) {}
		System.exit(1);
	}

	@Override
	public void suppress() {
		suppressed = true;		
	}
}
