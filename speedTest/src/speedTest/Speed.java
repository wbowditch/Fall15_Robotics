package speedTest;


import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Speed {
	static Brick brick = BrickFinder.getDefault();
	static NXTRegulatedMotor left = new NXTRegulatedMotor(brick.getPort("A"));
	static NXTRegulatedMotor right = new NXTRegulatedMotor(brick.getPort("B"));
	static NXTRegulatedMotor left1 = new NXTRegulatedMotor(brick.getPort("C"));
	static NXTRegulatedMotor right1 = new NXTRegulatedMotor(brick.getPort("D"));
	
	public static void main(String[] args) {
		
		brick.getKey("Escape").addKeyListener(new KeyListener() {
			//@Override
			public void keyPressed(Key k) {
				
//				left.controlMotor(0, flt);
//				right.controlMotor(0, flt);
//				left.close();
//				right.close();
				//light.close();
				
				//File ff = new File("starwars.wav");
				//try {
				//	Sound.playSample(ff, 100);
				//      }
				//catch (Exception ex) {}
				System.exit(1);
			}

		//	@Override
			public void keyReleased(Key k) {
				//finish = true;
				System.exit(1);
				

			}
		});
		left.setSpeed(3000);
		right.setSpeed(3000);
		left1.setSpeed(3000);
		right1.setSpeed(3000);
		while(true){
		left.backward();
		right.backward();
		left1.backward();
		right1.backward();
		}
	}

}
