import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
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

public class MotorTutor3 {

	public static void main(String[] args) {
		LCD.drawString("Push a button", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		Motor.A.rotate(1440);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 0);
		Motor.A.rotateTo(0);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
		Button.waitForAnyPress();

	}

}

/*
What the program should do
    Rotate the motor 4 complete revolutions (since 1440/360 = 4)
    Display the tachometer reading on the on the LCD (what should it be?)
    Rotate the motor to angle 0 (since tach count NOT reset, it should make 4 revs since count = 1440)
    Display the tachometer reading on the on the LCD, next row (what should it be?)
    Wait for a button press.

The motor usually stops within 1 degree of the specified angle if the motor 
regulator is doing its job. It works by calculating how far the motor will 
continue to turn after the brake has been applied and applies the brake 
before reaching the specified angle.

Observe: Once the motor has stopped if you try to turn it by hand it will 
resist you and will even return nack to the stopped position. This is 
because when the regulator will continue to control the motor 
at its stopped position
*/