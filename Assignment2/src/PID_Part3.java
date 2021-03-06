import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.utility.Delay;

public class PID_Part3 {
	
	
	

	
	
	//private int distance;
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;

	//public static boolean finish = false;
	
	public static void main(String[] args) {
		NXTUltrasonicSensor ultra = new NXTUltrasonicSensor(SensorPort.S2);
		// static NXTLightSensor light = new NXTLightSensor(SensorPort.S3);
		NXTTouchSensor touch = new NXTTouchSensor(SensorPort.S1);
		// static NXTSoundSensor sound = new NXTSoundSensor(SensorPort.S4);
		Brick brick = BrickFinder.getDefault();
		NXTRegulatedMotor left = new NXTRegulatedMotor(brick.getPort("A"));
		NXTRegulatedMotor right = new NXTRegulatedMotor(brick.getPort("B"));
		
		globalData touchedData = new globalData(); 
		globalUltrasonicData sonicData = new globalUltrasonicData();
		
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
				

				Thread u = new Thread(new sonicThread(sonicData, ultra));
				u.setDaemon(true); // when the main thread terminates, the created
				// thread terminates
				// if this is false, the created thread continues
				// when the main thread terminates
				u.start();
				
		final int desiredDistance = 10;

		// initialize PID constants
		final double Kp = .8;
		final double Ki = 0.000001;
		final double Kd = 2;

		// unaltered turn power
		final int Tp = 200;

		double error;
		double accumError = 0;
		double lastError = 0;
		double errorDiff;
		int distance;
		
		brick.getKey("Escape").addKeyListener(new KeyListener() {
			//@Override
			public void keyPressed(Key k) {
			
//				left.controlMotor(0, flt);
//				right.controlMotor(0, flt);
//				left.close();
//				right.close();
				
				//File ff = new File("starwars.wav");
				//try {
				//	Sound.playSample(ff, 100);
				//      }
				//catch (Exception ex) {}
				System.exit(1);
			}

		//	@Override
			public void keyReleased(Key k) {
			
				System.exit(1);
				

			}
		});
		
		left.setSpeed(Tp);
		right.setSpeed(Tp);
		left.forward();
		right.forward();
		while(true){
			distance = sonicData.getSonicValue();
			LCD.drawString(Integer.toString(distance), 0, 0);
			error = distance - desiredDistance;
			accumError += error;
			errorDiff = error - lastError;
			lastError = error;
			if (errorDiff >= 30 || accumError>=30){
				errorDiff = 0;
				accumError = 0;
			}
				// set PID values
			double P = Kp * error;
			double I = Ki * accumError;
			double D = Kd * errorDiff;

			double turn = P + I + D;
			
			int upPower = (int) (Tp + turn);
			int downPower = (int) (Tp - turn);
			if (error > 0) {
				left.setSpeed(upPower);
				right.setSpeed(downPower);
				}
			else if(error<0) {
				right.setSpeed(downPower);
				left.setSpeed(upPower);
				}
			left.forward();
			right.forward();
			if(distance>30){
				double wheelCircunference = wheelDiameter * Math.PI;
				left.flt(true);
				right.flt();
				Delay.msDelay(2000);
				int Tp1 = 100;
				left.setSpeed(Tp1);
				right.setSpeed(Tp1);
				left.rotate(150,true);
				right.rotate(150);
				Delay.msDelay(2000);
					//Sound.systemSound(true, 3);
					
				int degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
				//right.startSynchronization();
				left.rotate((int)(degrees+30),true);
				right.rotate((int)(-degrees-30));
				//Part5_main.left.endSynchronization();
				left.waitComplete();
				right.waitComplete();
				Delay.msDelay(2000);
				left.rotate(400,true);
				right.rotate(400);
				left.waitComplete();
				right.waitComplete();
				left.forward();
				right.forward();
				accumError = 0;
			}
			boolean b = touchedData.getTouched();
			if(b){
				touchedData.resetTouched();
			}
			if(b){
				touchedData.resetTouched();
				b = false;
				left.stop(true);
				right.stop();
				//Delay.msDelay(2000);
				double wheelCircunference = wheelDiameter * Math.PI;
				left.rotate(-150,true);
				right.rotate(-150);
				LCD.drawString("back up now", 0, 0);
				int degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
				right.startSynchronization();
				left.rotate((int)(-degrees-30),true);
				right.rotate((int)(degrees+30));
				right.endSynchronization();
				//Sound.systemSound(true, 4);
				left.waitComplete();
				accumError = 0;
				touchedData.resetTouched();
			}
			
		}
		
	
}
	}