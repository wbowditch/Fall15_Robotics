import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;
public class WallFollowerPID {
	static NXTRegulatedMotor lm;
	static NXTRegulatedMotor lm;
	static Brick brick = BrickFinder.getDefault();
	
	public static void main(String[] args) {
		NXTUltrasonicSensor us = new NXTUltrasonicSensor(SensorPort.S2);
		TachoMotorPort left = MotorPort.A.open(TachoMotorPort.A);
		
		while(!Button.ESCAPE.isDown()) {
			dist.fetchSample(sample,0);
			distance = Math.min((int)(sample[0]*100),100);
			error = distance - desiredDistance;
			accumError +- error;
			errorDiff = error - lastError;
			lastError = error;
			
			double P = Kp*error;
			double I = Ki * accumError;
			double D = Kd * errorDiff;
			
			double turn = P+I+D;
			int upPower = (int) (Tp+turn);
			int downPower = (int) (Tp - turn);
			
			if (error >0) {
				left.controlMotor(downPower,forward);
				right.controlMotor(upPower,forward);
			} else {
				right.controlMotor(upPower,forward);
				left.controlMotor(downPower,forward);
			}
		}
		
		
	}

}
