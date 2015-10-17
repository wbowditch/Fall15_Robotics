import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.utility.Delay;

public class CheckSonic {

	/*
	 * This version includes partial code to turn around. A specific number of
	 * rotarions (excluding slippage) could be calculated by 
	 * 1. Measuring the diameter of the wheel and then calculating the circumference 
	 * 2. Measuring the distance between the wheels and then calculating the distance
	 * travelled by a wheel (circumference of half the turn circle) 
	 * 3.Determining how many circumferences of the wheel fit into 1/2 the
	 * circumference of the turn circle - this is the number of rotations 
	 * An equally sound approach, however, is to just guess and adjust!
	 */

	public static void main(String[] args) {
		{

			// Get an instance of the Ultrasonic sensor
			NXTUltrasonicSensor sonic = new NXTUltrasonicSensor(SensorPort.S2);
			// get an instance of this sensor in measurement mode
			SampleProvider distance = sonic.getDistanceMode();
			// Initialize an array of floats for fetching samples
			float[] sample = new float[distance.sampleSize()];
			Motor.A.setSpeed(200);  //2 revs per second
			Motor.B.setSpeed(200);
			Motor.A.forward();
			Motor.B.forward();
			boolean count = true;
			while (count == true)
			{
				distance.fetchSample(sample, 0);
			
				if (sample[0] > 0.3) {
					Sound.systemSound(true, 3);
					count = false; // The while loop will now terminate
					Motor.A.stop();
					Motor.B.stop();
				}
			}

			// First time using rotateTo method, so starts at 0.
			// True parameter forces stop at end of rotateTo() method
			
			//Motor.A.rotateTo(-720, true);
			//Motor.B.rotateTo(-720, true);
			//Delay.msDelay(1000); // pause is needed before next step
			//Motor.A.resetTachoCount(); // making sure the start point is 0
			//Motor.B.resetTachoCount();
			//Motor.A.rotateTo(1100); // started with a guess and adjusted
			//Sound.systemSound(true, 4);
			//Delay.msDelay(1000); // delay so the beeps do not overlap
			Sound.systemSound(true, 2);
			//System.exit(1);
		}

	}

}
//resetTachCount()        
//This method not only sets the tacho count (motor angle) to 0 
//but also resets the origin used by the regulator thread in 
//deciding when to stop a rotation task. 