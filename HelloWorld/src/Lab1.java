
import lejos.robotics.navigation.Move;
import lejos.utility.Delay;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;
public class Lab1 {
	public static void main(String[] args) {
		LCD.drawString("Distance Program", 0, 0); // Writes program name to LCD
		//first move
		
		//float angle = Motor.A.getTachoCount();
		float turnRadius = (float) 5.6 / 2;
		// convert method
		float angle = Move.convertDistanceToAngle(100, turnRadius;
		//float distance = Move.convertAngleToDistance((float) angle, turnRadius);
		Button.waitForAnyPress(); // Waits for button press
		Motor.A.setSpeed(angle); // sets speed to 2 revolutions (360 deg each)
								// per second
		Motor.B.setSpeed(angle); // sets speed to 2 revolutions (360 deg each)
		// per second
		Motor.A.forward();
		Motor.B.forward();
		LCD.clear();
		Delay.msDelay(2000); // 2 second delay (measured in milliseconds) -
								// it has gone 4 revolutions (approx)
								// 2*(720/360)
		LCD.drawInt(Motor.A.getTachoCount(), 0, 0); // Writes count (motor
													// angle) to LCD first
													// time
		Motor.A.stop(true);
		Motor.B.stop();
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
		LCD.drawString("d1: " + distance, 0, 3);
		float d2 = (float) (2 * Math.PI * turnRadius * (angle / 360)); // arc
																		// formula
		LCD.drawString("d2: " + d2, 0, 4);
		
		Button.waitForAnyPress();
		
		//second move
		Motor.A.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor A
		Motor.B.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor B

		Motor.A.setSpeed(360); // sets speed to 1 revolutions (360 deg each)
		// per second
		Motor.B.setSpeed(360); // sets speed to 1 revolutions (360 deg each)
		// per second
		Motor.A.forward();
		Motor.B.forward();
		LCD.clear();
		Delay.msDelay(4000); // 4 second delay (measured in milliseconds) -
		// it has gone 4 revolutions (approx)
		// 4*(360/360)
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2); // Writes count (motor
		// angle) to LCD first
		// time
		Motor.A.stop(true);
		Motor.B.stop();
		LCD.drawInt(Motor.A.getTachoCount(), 0, 3);
		angle = Motor.A.getTachoCount();
		turnRadius = (float) 5.6 / 2;  //wheel diameter is 5.6 cm (wheel is marked 56 mm)
		// convert method
		distance = Move.convertAngleToDistance((float) angle, turnRadius);

		LCD.drawString("d1: " + distance, 0, 4);
		d2 = (float) (2 * Math.PI * turnRadius * (angle / 360)); // arc formula
		LCD.drawString("d2: " + d2, 0, 4);
		LCD.drawString("angle: " + Move.convertDistanceToAngle(distance, turnRadius), 0, 5);
    Button.waitForAnyPress();
	}

}