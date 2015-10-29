
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;



public class Boundary implements Behavior {
	private int light;
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	private boolean suppressed = false;
	private boolean isRunning1 = false;

	public boolean takeControl() {
		light = Sumo.lightData.getLightValue();
		LCD.drawString(Integer.toString(light), 0, 0);
		if(light <40){
			return true;
		}
		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		LCD.drawString("Touch Test", 0, 0);
		suppressed = false;
		isRunning1 = true;
		double wheelCircunference = wheelDiameter * Math.PI;
		Sumo.left.stop(true);
		Sumo.right.stop();
		int Tp = 3000;
		Sumo.left.setSpeed(Tp);
		Sumo.right.setSpeed(Tp);
		Sumo.left.rotate(1000,true);
		Sumo.right.rotate(1000);
		
		int degrees=(int)Math.round((((robotTrack*Math.PI)/4)/wheelCircunference)*360.0);
		Sumo.left.rotate(-2000,true);
		Sumo.right.rotate(2000);
		Sumo.left1.rotate(-2000,true);
		Sumo.right1.rotate(2000);

//		while (!suppressed && Sumo.left.isMoving() && Sumo.right.isMoving()){
//			Thread.yield();
//	}
		isRunning1 = false;
		Sumo.left.forward();
		Sumo.right.forward();
		suppress();
//			Part5_main.left.close();
//			Part5_main.right.close();
//			
//			TachoMotorPort Part5_main.left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // Part5_main.right
//			TachoMotorPort Part5_main.right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class);
//			
			
	
	}
}


