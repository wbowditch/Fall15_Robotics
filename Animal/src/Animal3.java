import globalUltrasonicData;
import sonicThread;

import java.io.File;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Animal3 {
	static NXTColorSensor light = new NXTColorSensor(SensorPort.S3);
	static NXTUltrasonicSensor ultra = new NXTUltrasonicSensor(SensorPort.S2);
	// static NXTLightSensor light = new NXTLightSensor(SensorPort.S3);
	static NXTTouchSensor touch = new NXTTouchSensor(SensorPort.S1);
	// static NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S4);
	static private int distance, leftD, rightD;
	static DifferentialPilot pilot = new DifferentialPilot(5.6, 17.00, Motor.A,
			Motor.B);
	static globalData touchedData = new globalData(); // set up global data for
														// touch thread
	static globalLightData lightData = new globalLightData(); // set up global
																// data for
																// touch thread

	static globalUltrasonicData sonicData = new globalUltrasonicData();

	private static final int veryFastRotation = 250, fastRotation = 80, mediumRotation = 40, slowRotation = 15;
	private static final int fastTravel = 39, mediumTravel = 18, slowTravel = 8;
	public static void veryFastRotation() { pilot.setRotateSpeed(veryFastRotation); }
	public static void fastRotation() { pilot.setRotateSpeed(fastRotation); }
	public static void mediumRotation() { pilot.setRotateSpeed(mediumRotation); }
	public static void slowRotation() { pilot.setRotateSpeed(slowRotation); }
	public static void fastTravel() { pilot.setTravelSpeed(fastTravel); }
	public static void mediumTravel() { pilot.setTravelSpeed(mediumTravel); }
	public static void slowTravel() { pilot.setTravelSpeed(slowTravel); }
	
	public static boolean finish = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t = new Thread(new touchThread(touchedData, touch)); // create
		// touch
		// thread
		t.setDaemon(true); // when the main thread terminates, the created
		// thread terminates
		// if this is false, the created thread continues
		// when the main thread terminates
		t.start();
		// Delay.msDelay(5000);

		Thread l = new Thread(new lightThread(lightData, light));
		l.setDaemon(true); // when the main thread terminates, the created
		// thread terminates
		// if this is false, the created thread continues
		// when the main thread terminates
		l.start();
		// Delay.msDelay(5000);

		Thread u = new Thread(new sonicThread(sonicData, ultra));
		u.setDaemon(true); // when the main thread terminates, the created
		// thread terminates
		// if this is false, the created thread continues
		// when the main thread terminates
		u.start();
		// Delay.msDelay(5000);

		// establish a fail-safe: pressing Escape quits
		Brick brick = BrickFinder.getDefault(); // get specifics about this
		// robot
		brick.getKey("Escape").addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(Key k) {
				finish = true;
				pilot.stop();
				light.close();
				ultra.close();
				touch.close();
				//File ff = new File("starwars.wav");
				//try {
				//	Sound.playSample(ff, 100);
				//      }
				//catch (Exception ex) {}
				//System.exit(1);
			}

			@Override
			public void keyReleased(Key k) {
				finish = true;
				/*pilot.stop();
				light.close();
				ultra.close();
				touch.close();
				System.exit(1);
				*/

			}
		});

		Behavior b1 = new Drive();
		Behavior b2 = new Avoid();
		Behavior b3 = new FindFood();
		Behavior b4 = new Scared();
		Behavior b5 = new Terminate();
		Behavior[] bArray = { b1, b2, b3,b4,b5};
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	}

}
