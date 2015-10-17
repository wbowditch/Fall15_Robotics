
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
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

public class testLightSensor {

	public static void main(String[] args) {
		// initialize sensors
		//NXTColorSensor light = new NXTColorSensor(SensorPort.S3);
		NXTLightSensor light = new NXTLightSensor(SensorPort.S3);;
		light.setFloodlight(true);
		Calibrate c = new Calibrate(light);
		int w = c.calWhite();
		int b = c.calBlack();
		int a = c.getAvgEx();
		Button.waitForAnyPress();
		LCD.clear();
		LCD.drawString("w: " + w,0,1);
		LCD.drawString("b: " + b,0,2);
		LCD.drawString("a: " + a,0,3);
		Button.waitForAnyPress();
	}

}