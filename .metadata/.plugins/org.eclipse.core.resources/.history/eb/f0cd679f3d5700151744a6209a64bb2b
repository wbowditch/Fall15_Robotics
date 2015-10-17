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

public class motorTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Button.waitForAnyPress();
		
		test1();
		Motor.A.resetTachoCount(); // reset tachometer count to 0 and halts
		// motor A
		Motor.B.resetTachoCount(); // reset tachometer count to 0 and halts
		// motor B

		Button.waitForAnyPress();
		test2();
	}

	public static void test1() {
		// Go Forward, Stop, Face Other Direction
		// First method called returns immediately
		// since true keyword used in parameter list -
		// prevents sequential running of the motors
		Motor.A.setSpeed(360);
		Motor.B.setSpeed(360);
		Motor.A.rotateTo(720, true); // returns control for execution to this
										// point - continues immediately to next
										// command
		Motor.B.rotateTo(720);
		System.out.println("A: " + Motor.A.getTachoCount());
		System.out.println("B: " + Motor.B.getTachoCount());
		
		Motor.A.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor A
		Motor.B.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor B
		Motor.A.rotateTo(610, true); // rotation begins at 0
		Motor.B.rotateTo(-610); // rotates in a negative or
								// counterclockwisedirection, starting at 0
		System.out.println("A: " + Motor.A.getTachoCount());
		System.out.println("B: " + Motor.B.getTachoCount());

	}

	public static void test2() {
		// Two Two Motors Running Sequentially
		// First method called does not return immediately (true keyword not
		// used in the parameter list)

		Motor.A.setSpeed(360);
		Motor.B.setSpeed(360);
		Motor.A.rotateTo(720); // the parameter true not included, so this
								// command must run before the next one can
								// execute
		Motor.B.rotateTo(720);
		Motor.A.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor
		Motor.B.resetTachoCount(); // reset tachometer count to 0 and halts
									// motor
	}

}