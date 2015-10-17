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
import lejos.utility.Delay;
//bc: 136.167.209.221
//home: 192.168.1.15

public class Part3 {
	
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	static RegulatedMotor left ;
	static RegulatedMotor right ;
	static float turnRadius = (float) wheelDiameter / 2;  //wheel diameter is 5.6 cm (wheel is marked 56 mm)
	static NXTTouchSensor ts;
	static NXTUltrasonicSensor sonic;
	public static void main(String[] args) {
		ts = new NXTTouchSensor(SensorPort.S1);
		sonic = new NXTUltrasonicSensor(SensorPort.S2);
		double wheelCircunference = wheelDiameter * Math.PI;
		float degrees;
		
		//SampleProvider distance = sonic.getDistanceMode();
		Brick brick = BrickFinder.getDefault(); // get specifics about this
		//float[] sample = new float[distance.sampleSize()];							// robot
		left = new NXTRegulatedMotor(brick.getPort("A"));
		right = new NXTRegulatedMotor(brick.getPort("B"));

		SampleProvider distance = sonic.getDistanceMode();
		float[] sample = new float[distance.sampleSize()];
		globalData touchedData = new globalData(); // set up global data for
													// touch thread
		Thread t = new Thread(new touchThread(touchedData, ts)); // create touch
																	// thread
		t.setDaemon(true); // when the main thread terminates, the created
							// thread terminates
							// if this is false, the created thread continues
							// when the main thread terminates
		t.start();
		// Delay.msDelay(5000);
		
		LCD.drawString("Touch Test", 0, 0); // Writes program name to LCD
		
		//first move
		Button.waitForAnyPress(); // Waits for button press

		
		 // establish a fail-safe: pressing Escape quits
        brick.getKey("Escape").addKeyListener(new KeyListener() {
     
                public void keyPressed(Key k) {
    				left.stop(true);
    				right.stop();
                   	sonic.close();
        				left.close();
        				right.close();
        				
                        System.exit(1);
                }

        
                public void keyReleased(Key k) {
    				left.stop(true);
    				right.stop();
                	
    				left.close();
    				sonic.close();
    				right.close();
                    System.exit(1);
                }
        });
        
		left.setSpeed(200);
		right.setSpeed(200);
		left.synchronizeWith(new RegulatedMotor[] { right });

		left.forward();
		right.forward();
		int i = 0;
		boolean count = true;
		while (count==true){
			
			distance.fetchSample(sample, 0);
			
			
			//System.out.println("d[" + i + "]: " + touchedData.getTouched());
			
			 if (touchedData.getTouched()) {
				left.stop(true);
				right.stop();
				left.rotate(-200,true);
				right.rotate(-200);
				Sound.systemSound(true, 3);
				degrees=(int)Math.round((((robotTrack*Math.PI)/6)/wheelCircunference)*360.0);
				LCD.drawString("deg: " + degrees, 0, 3);
				left.startSynchronization();
				left.rotate((int)(-degrees), true);
				right.rotate((int)degrees);
				left.endSynchronization();
				left.waitComplete();
				right.waitComplete();
				left.forward();
	            right.forward();
	            touchedData.resetTouched();
				//what to do if touched?
				//put your code here, don't forget to reset the global variable to false
				//thik how to do this
			}
			 if (sample[0] > 0.3) {
					Sound.systemSound(true, 3);
					left.stop(true);
					right.stop();
					left.rotate(200,true);
					right.rotate(200);
					Sound.systemSound(true, 3);
					degrees=(int)Math.round((((robotTrack*Math.PI)/6)/wheelCircunference)*360.0);
					LCD.drawString("deg: " + degrees, 0, 3);
					left.startSynchronization();
					left.rotate((int)(degrees), true);
					right.rotate((int)-degrees);
					left.endSynchronization();
					left.waitComplete();
					right.waitComplete();
					left.setSpeed(200);
					right.setSpeed(200);

					left.forward();
					right.forward();
		            count = false; // The while loop will now terminate
				}
		
			
		}
		LCD.drawString("Touch Test", 0, 0);
		while(true){
			left.forward();
			right.forward();
		}
		
	}

}