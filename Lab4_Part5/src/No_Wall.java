
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



public class No_Wall implements Behavior {
	private int distance;
	static double wheelDiameter = 5.6;
	static double robotTrack = 16.7;
	private boolean suppressed = false;
	private boolean isRunning1 = false;

	public boolean takeControl() {
		distance = Part5_main.sonicData.getSonicValue();
		if(distance> 30){
			return true;
		}
		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		isRunning1 = true;
		double wheelCircunference = wheelDiameter * Math.PI;
		Part5_main.left.flt(true);
		Part5_main.right.flt();
		Delay.msDelay(2000);
		int Tp = 100;
		Part5_main.left.setSpeed(Tp);
		Part5_main.right.setSpeed(Tp);
		Part5_main.left.rotate(150,true);
		Part5_main.right.rotate(150);
		Delay.msDelay(2000);
			//Sound.systemSound(true, 3);
			
		int degrees=(int)Math.round((((robotTrack*Math.PI)/8)/wheelCircunference)*360.0);
		//Part5_main.left.startSynchronization();
		Part5_main.left.rotate((int)(degrees+30),true);
		Part5_main.right.rotate((int)(-degrees-30));
		//Part5_main.left.endSynchronization();
		Part5_main.left.waitComplete();
		Part5_main.right.waitComplete();
		Delay.msDelay(2000);
		Part5_main.left.rotate(400,true);
		Part5_main.right.rotate(400);
		Part5_main.left.waitComplete();
		Part5_main.right.waitComplete();
		while (!suppressed && Part5_main.left.isMoving() && Part5_main.right.isMoving()){
			Thread.yield();
	}
		isRunning1 = false;
		Part5_main.left.forward();
		Part5_main.right.forward();
		suppress();
//			Part5_main.left.close();
//			Part5_main.right.close();
//			
//			TachoMotorPort Part5_main.left = (TachoMotorPort) MotorPort.A.open(TachoMotorPort.class); // Part5_main.right
//			TachoMotorPort Part5_main.right =(TachoMotorPort) MotorPort.B.open(TachoMotorPort.class);
//			
			
	
	}
}


