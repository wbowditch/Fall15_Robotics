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

public class PartThree {
	
	static double wheelDiameter = 6.8;
	static double robotTrack = 16.7;
	static RegulatedMotor left ;
	static RegulatedMotor right ;
	static float turnRadius = (float) wheelDiameter / 2;  //wheel diameter is 5.6 cm (wheel is marked 56 mm)
	static NXTUltrasonicSensor sonic;
	public static void main(String[] args) {
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
        
		left.setSpeed(100);
		right.setSpeed(100);
		left.synchronizeWith(new RegulatedMotor[] { right });

		left.forward();
		right.forward();
		int corners = 0;
		float total = 0;
		while (corners<4){
			distance.fetchSample(sample, 0);
			if (sample[0] > 0.3) {
				corners = corners + 1;
				left.stop(true);
				right.stop();
				float turnRadius = (float) 6.8 / 2;
				// convert method
				float distance1 = Move.convertAngleToDistance((float) right.getTachoCount(), turnRadius);
				total = total + distance1;
				LCD.drawString("Distance1: " + total, 0, 3);
				Delay.msDelay(3000);
				left.rotate(150,true);
				right.rotate(150);
				Sound.systemSound(true, 3);
				
				degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
				left.startSynchronization();
				LCD.drawString("degrees: " + degrees, 0, 3);
				Delay.msDelay(3000);
				left.rotate((int)(degrees+50), true);
				right.rotate((int)(-degrees-50));
				left.endSynchronization();
				left.waitComplete();
				right.waitComplete();
				Sound.systemSound(true, 1);
				left.rotate(300,true);
				right.rotate(300);
				left.resetTachoCount();
				right.resetTachoCount();
				left.waitComplete();
				right.waitComplete();
				left.forward();
				right.forward();
				 }

			}
		
			
		
		left.stop(true);
		right.stop();
		Sound.systemSound(true, 3);
		//LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
		float turnRadius = (float) 5.6 / 2;
		// convert method
		LCD.drawString("Distance3: " + total, 0, 3);
        Delay.msDelay(5000);
        System.exit(0);
		
		}
		
	}

