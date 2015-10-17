import java.io.ObjectOutputStream;
import java.net.Socket;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.navigation.Move;

public class PartOne {
	public static NXTTouchSensor ts = new NXTTouchSensor(SensorPort.S1);
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	static float turnRadius = (float) wheelDiameter / 2;  //wheel diameter is 5.6 cm (wheel is marked 56 mm)
	public static void main(String[] args) throws Exception {
		
		globalData touchedData = new globalData(); 
		Thread t = new Thread(new touchThread(touchedData, ts)); 	
		t.setDaemon(true); 
		t.start();
							
		LCD.drawString("Lab 3: Part 1", 0, 0); 
		
		Button.waitForAnyPress(); 
		
		Motor.A.setSpeed(200); 
		Motor.B.setSpeed(200); 
		Motor.A.forward();
		Motor.B.forward();
		LCD.clear();
		
		//LCD.drawInt(Motor.A.getTachoCount(), 0, 0); // Writes count (motor
													// angle) to LCD first
													// time
		
		while (true){

			if (touchedData.getTouched()) {
				Motor.A.stop(true);
				Motor.B.stop();
				Sound.systemSound(true, 3);
				LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
				float angle = Motor.A.getTachoCount();
				float turnRadius = (float) 5.6 / 2;
				// convert method
				float distance = Move.convertAngleToDistance((float) angle, turnRadius);
				LCD.drawString("Distance: " + distance, 0, 3);
	            touchedData.resetTouched();
	            Delay.msDelay(5000);
	            System.exit(0);
				
			}
			
		}
		

	}

}