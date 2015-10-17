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
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.sensor.NXTLightSensor;

/**
 * The light sensor should be connected to port 3. The left motor should be
 * connected to port A and the right motor to port B.
 * 
 * controlMotor public void controlMotor(int power,int mode)
 * 
 * Low-level method to control a motor. Specified by: controlMotor in interface
 * BasicMotorPort Parameters: power - power from 0-100 mode - 1=forward,
 * 2=backward, 3=stop, 4=float
 */
public class HW2_Part4 {
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	//static NXTColorSensor light = new NXTColorSensor(SensorPort.S3);
	static NXTLightSensor light = new NXTLightSensor(SensorPort.S3);
	//static NXTUltrasonicSensor sonic;
	static NXTTouchSensor ts;
	public static void main(String[] aArg) throws Exception {
		ts = new NXTTouchSensor(SensorPort.S1);
		int upPower;
		int downPower;
		//sonic = new NXTUltrasonicSensor(SensorPort.S2);
		double wheelCircunference = wheelDiameter * Math.PI;
		float degrees;
		boolean downhill = false;
//		SampleProvider distance = sonic.getDistanceMode();
//		float[] sample = new float[distance.sampleSize()];
		
		globalData touchedData = new globalData();
		Thread t = new Thread(new touchThread(touchedData, ts));
		t.setDaemon(true);
		t.start();
		
		String leftString = "Turn left ";
		String rightString = "Turn right";
		int white,black,blackWhiteThreshold;
		TachoMotorPort left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // right
		TachoMotorPort right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class); // left

		final int forward = 1;
		final int stop = 3;
		final int flt = 4;
		
		// initialize PID constants
		final double Kp = 7;
		final double Ki = 0.000001;
		final double Kd = 1;

				// unaltered turn power
		int Tp = 30;


		double error;
		double accumError = 0;
		double lastError = 0;
		double errorDiff;
		
		// Use the light sensor as a reflection sensor
		//SampleProvider lightDist = light.getRGBMode();  //color light with a source
		SampleProvider lightDist = light.getRedMode();  //light with a source
		
		//now calibrate light/color sensor
		
		Calibrate c = new Calibrate(lightDist);
		//calibrate white
		white = c.calWhite();
		Delay.msDelay(1000);
		//calibrate black
		black = c.calBlack();
		Delay.msDelay(1000);
		//find average
		blackWhiteThreshold = c.getAvgEx();

		LCD.clear();
		LCD.drawString("white: " + white,0,1);
		LCD.drawString("black: " + black,0,2);
		LCD.drawString("threshold: " + blackWhiteThreshold,0,3);
		Button.waitForAnyPress();


		// Follow line until ESCAPE is pressed
		LCD.drawString("Press ESCAPE", 0, 4);
		LCD.drawString("to stop ", 0, 5);
		Delay.msDelay(1000);
		// get an instance of this sensor in measurement mode
		//SampleProvider lightDist = light.getAmbientMode();  //light without a source
		// Initialize an array of floats for fetching samples
		float[] lightSample = new float[lightDist.sampleSize()];

		LCD.clear();
		
		while (!Button.ESCAPE.isDown()) {
			lightDist.fetchSample(lightSample, 0);
			// set various error values
			error = lightSample[0]*100 - blackWhiteThreshold;
			accumError += error;
			errorDiff = error - lastError;
			lastError = error;
			// set PID values
			double P = Kp * error;
			double I = Ki * accumError;
			double D = Kd * errorDiff;

			double turn = P + I + D;
						
			if(downhill){
				Tp = 30;
				upPower = (int) (Tp - turn);
				downPower = (int) (Tp + turn);
			}
			else{
				Tp = 40;
				upPower = (int) (Tp + turn);
				downPower = (int) (Tp - turn);
			}
			
			//upPower = (int) (Tp + turn);
			//downPower = (int) (Tp - turn);
			
			if ((int)(lightSample[0]*100) > error) {
				// On white, turn right
				LCD.drawString(rightString, 0, 1);
				left.controlMotor(upPower, forward);
				right.controlMotor(downPower, forward);
	
				//MotorPort.B.controlMotor(0, stop);
				//MotorPort.A.controlMotor(power, forward);
			} else {
				// On black, turn left
				LCD.drawString(leftString, 0, 1);
				left.controlMotor(downPower, forward);
				right.controlMotor(upPower, forward);

				//MotorPort.B.controlMotor(power, forward);
				//MotorPort.A.controlMotor(0, stop);
			}
			//distance.fetchSample(sample, 0);
			if (touchedData.getTouched()) {
				left.close();
				right.close();
				downhill = true;
				Brick brick = BrickFinder.getDefault(); 
				NXTRegulatedMotor left1 = new NXTRegulatedMotor(brick.getPort("A"));
				NXTRegulatedMotor right1 = new NXTRegulatedMotor(brick.getPort("B"));
				left1.rotate(-75,true);
				right1.rotate(-75);
				degrees=(int)Math.round((((robotTrack*Math.PI)/4)/wheelCircunference)*360.0);
				left1.startSynchronization();
				left1.rotate((int)(-degrees)-70,true);
				right1.rotate((int)(degrees)+70,true);
				left1.endSynchronization();
				left1.waitComplete();
				right1.waitComplete();
				left1.close();
				right1.close();
		
				left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // right
				right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class);
				accumError = 0;
				touchedData.resetTouched();
					}
			Delay.msDelay(10);

		}
		//light.close();
		// Stop car gently with free wheel drive
		left.controlMotor(0, flt);
		right.controlMotor(0, flt);
		//MotorPort.B.controlMotor(0, flt);
		//MotorPort.A.controlMotor(0, flt);
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Delay.msDelay(1000);
		left.close();
		right.close();
	}
}