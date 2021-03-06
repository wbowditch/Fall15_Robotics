import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

public class HW2_Part3 {
	private int distance;
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	//static NXTRegulatedMotor lm;
	//static NXTRegulatedMotor rm;
	static Brick brick = BrickFinder.getDefault(); // get specifics about this
	static NXTTouchSensor ts;
	public static void main(String[] args) {
		double wheelCircunference = wheelDiameter * Math.PI;
		float degrees;
		// initialize sensor
		NXTUltrasonicSensor us = new NXTUltrasonicSensor(SensorPort.S2);
		ts = new NXTTouchSensor(SensorPort.S1);
		TachoMotorPort left = (TachoMotorPort)MotorPort.A.open(TachoMotorPort.class); // right
		TachoMotorPort right = (TachoMotorPort)MotorPort.B.open(TachoMotorPort.class); // left

		// get an instance of this sensor in measurement mode
		//SampleProvider dist = us.getDistanceMode();
		// Initialize an array of floats for fetching samples
		//float[] sample = new float[dist.sampleSize()];
		
		globalData touchedData = new globalData();
		Thread t = new Thread(new touchThread(touchedData, ts));
		t.setDaemon(true);
		t.start();
		
		globalUltrasonicData sonicData = new globalUltrasonicData();
		Thread u = new Thread(new sonicThread(sonicData, us));
		u.setDaemon(true); // when the main thread terminates, the created
		u.start();
		// Delay.msDelay(5000);
		
		// move modes
		final int forward = 1;
		final int stop = 3;
		final int flt = 4;

		// configure values
		final int desiredDistance = 11;
		final int noWall = 250;

		// initialize PID constants
		final double Kp = .75;
		final double Ki = 0.000001;
		final double Kd = 0.55;

		// unaltered turn power
		final int Tp = 20;

		double error;
		double accumError = 0;
		double lastError = 0;
		double errorDiff;


		// before wall follower starts
		LCD.drawString("Press LEFT", 0, 2);
		LCD.drawString("to start", 0, 3);
		while (!Button.LEFT.isDown()) {
			int distance = sonicData.getSonicValue();
			LCD.drawString(Integer.toString(distance), 0, 0);

		}

		// wall follower
		LCD.drawString("Press ESCAPE", 0, 2);
		LCD.drawString("to stop", 0, 3);
		while (!Button.ESCAPE.isDown()) {
			int distance = sonicData.getSonicValue();
			LCD.drawString(Integer.toString(distance), 0, 0);
			// uncomment so that he stops at the end of the wall instead of
			// attempting to follow it's turn

		
			//
			// else{

			// set various error values
			error = distance - desiredDistance;
			accumError += error;
			errorDiff = error - lastError;
			lastError = error;
			 if (distance >= noWall){
				 left.controlMotor(0, stop);
				 right.controlMotor(0, stop);
				 accumError = 0;
				
				 }
			// set PID values
			double P = Kp * error;
			double I = Ki * accumError;
			double D = Kd * errorDiff;

			double turn = P + I + D;

			int upPower = (int) (Tp + turn);
			int downPower = (int) (Tp - turn);

			// turn the Motors
			if(distance>60){
				left.close();
				right.close();
				
				Brick brick = BrickFinder.getDefault(); 
				NXTRegulatedMotor left1 = new NXTRegulatedMotor(brick.getPort("A"));
				NXTRegulatedMotor right1 = new NXTRegulatedMotor(brick.getPort("B"));
				
				left1.stop(true);
				right1.stop();
				left1.rotate(150,true);
				right1.rotate(150);
				//Sound.systemSound(true, 3);
				
				degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
				left1.startSynchronization();
				left1.rotate((int)(degrees+40), true);
				right1.rotate((int)(-degrees-40));
				left1.endSynchronization();
				left1.waitComplete();
				right1.waitComplete();
				//Sound.systemSound(true, 1);
				left1.rotate(400,true);
				right1.rotate(400);
				left1.waitComplete();
				right1.waitComplete();
	
				left1.close();
				right1.close();
				left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // right
				right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class);
				accumError = 0;
				//lastError = error;
			}
			if (error > 0) {
				left.controlMotor(upPower, forward);
				right.controlMotor(downPower, forward);
			} else if(error<0) {
				//Sound.systemSound(true, 1);
				right.controlMotor(downPower, forward);
				left.controlMotor(upPower, forward);
			}
			if (touchedData.getTouched()) {
				left.close();
				right.close();
				
				Brick brick = BrickFinder.getDefault(); 
				NXTRegulatedMotor left1 = new NXTRegulatedMotor(brick.getPort("A"));
				NXTRegulatedMotor right1 = new NXTRegulatedMotor(brick.getPort("B"));
				left1.setSpeed(200);
				right1.setSpeed(200);
				left1.rotate(-100,true);
				right1.rotate(-100);
				degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
				//left1.startSynchronization();
				left1.rotate((int)(-degrees-40), true);
				right1.rotate((int)(degrees+40));
				//left1.endSynchronization();
				left1.waitComplete();
				right1.waitComplete();
				left1.close();
				right1.close();
	
				left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // right
				right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class);
				accumError = 0;
				//lastError = error;
				touchedData.resetTouched();
			}


		}

		left.controlMotor(0, flt);
		right.controlMotor(0, flt);
		left.close();
		right.close();
		us.close();
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Delay.msDelay(1000);

	}
}