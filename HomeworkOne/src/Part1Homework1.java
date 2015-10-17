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
//bc: 136.167.209.221
//home: 192.168.1.15

public class Part1Homework1 {
	
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	static RegulatedMotor left ;
	static RegulatedMotor right ;
	static float turnRadius = (float) wheelDiameter / 2;  //wheel diameter is 5.6 cm (wheel is marked 56 mm)
	static NXTUltrasonicSensor sonic;
	static NXTUltrasonicSensor sonic2;
	public static void main(String[] args) {
		sonic = new NXTUltrasonicSensor(SensorPort.S2);
		sonic2 = new NXTUltrasonicSensor(SensorPort.S1);
		double wheelCircunference = wheelDiameter * Math.PI;
		float degrees;
		
		
		//SampleProvider distance = sonic.getDistanceMode();
		Brick brick = BrickFinder.getDefault(); // get specifics about this
		//float[] sample = new float[distance.sampleSize()];							// robot
		left = new NXTRegulatedMotor(brick.getPort("A"));
		right = new NXTRegulatedMotor(brick.getPort("B"));
		
		SampleProvider distance = sonic.getDistanceMode();
		float[] sample = new float[distance.sampleSize()];
		
		SampleProvider distance2 = sonic2.getDistanceMode();
		float[] sample2 = new float[distance2.sampleSize()];
	
		
		LCD.drawString("Lab 3: Part 3", 0, 0); // Writes program name to LCD
		
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
        
		left.setSpeed(150);
		right.setSpeed(152);
		left.synchronizeWith(new RegulatedMotor[] { right });

		left.forward();
		right.forward();
		int corners = 0;
		while (true){
			distance.fetchSample(sample, 0);
			distance2.fetchSample(sample2, 0);
			 if (sample2[0] < 0.05) {
				 left.stop(true);
					right.stop();
					left.rotate(-75,true);
					right.rotate(-75);
					Sound.systemSound(true, 3);
					
					degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
					left.startSynchronization();
					left.rotate((int)(-degrees-40), true);
					right.rotate((int)(degrees+40));
					left.endSynchronization();
					left.waitComplete();
					right.waitComplete();
					//Sound.systemSound(true, 1);
					left.rotate(-25,true);
					right.rotate(-25);
					left.waitComplete();
					right.waitComplete();
					left.forward();
					right.forward();
					}
			 if (sample[0] > 0.6) {
				 left.stop(true);
					right.stop();
					left.rotate(150,true);
					right.rotate(150);
					Sound.systemSound(true, 3);
					
					degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
					left.startSynchronization();
					left.rotate((int)(degrees+40), true);
					right.rotate((int)(-degrees-40));
					left.endSynchronization();
					left.waitComplete();
					right.waitComplete();
					//Sound.systemSound(true, 1);
					left.rotate(400,true);
					right.rotate(400);
					left.waitComplete();
					right.waitComplete();
					left.forward();
					right.forward();
					distance = sonic.getDistanceMode();
					distance.fetchSample(sample, 0);
					}
		}

		
		}
		
	}

