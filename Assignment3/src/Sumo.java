//import globalUltrasonicData;
//import sonicThread;

import java.io.File;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Sumo {
	
	static NXTLightSensor light = new NXTLightSensor(SensorPort.S4);
	
	static Brick brick = BrickFinder.getDefault();
	static NXTRegulatedMotor left = new NXTRegulatedMotor(brick.getPort("A"));
	static NXTRegulatedMotor right = new NXTRegulatedMotor(brick.getPort("B"));
	static NXTRegulatedMotor left1 = new NXTRegulatedMotor(brick.getPort("C"));
	static NXTRegulatedMotor right1 = new NXTRegulatedMotor(brick.getPort("D"));
	
	static globalLightData lightData = new globalLightData();


	public static boolean finish = false;
	
	public static void main(String[] args) {
		
		Thread l = new Thread(new lightThread(lightData, light));
		l.setDaemon(true); // when the main thread terminates, the created
		// thread terminates
		// if this is false, the created thread continues
		// when the main thread terminates
		l.start();
		// Delay.msDelay(5000);
		
		
		brick.getKey("Escape").addKeyListener(new KeyListener() {
			//@Override
			public void keyPressed(Key k) {
				finish = true;
//				left.controlMotor(0, flt);
//				right.controlMotor(0, flt);
//				left.close();
//				right.close();
				light.close();
				
				//File ff = new File("starwars.wav");
				//try {
				//	Sound.playSample(ff, 100);
				//      }
				//catch (Exception ex) {}
				System.exit(1);
			}

		//	@Override
			public void keyReleased(Key k) {
				finish = true;
				System.exit(1);
				

			}
		});

		Behavior b1 = new Drive();
		Behavior b2 = new Boundary();
		
		
		//Behavior b3 = new FindFood();
		//Behavior b4 = new Scared();
		Behavior[] bArray = { b1,b2};
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	}

}
