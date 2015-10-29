import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.HiTechnicEOPD;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Delay;

public class Bug2 {
	private static globalUltrasonicData sonicData = new globalUltrasonicData();
	private static NXTUltrasonicSensor us = new NXTUltrasonicSensor(SensorPort.S2);
	
	private static globalUltrasonicData sonicData2 = new globalUltrasonicData();
	private static NXTUltrasonicSensor us2 = new NXTUltrasonicSensor(SensorPort.S1);
//	
//	private static globalEOPDData EOPDData = new globalEOPDData();
//	private static HiTechnicEOPD eopd = new HiTechnicEOPD(SensorPort.S1);
	
	// Instantiate Pilot/PoseProvider
		private static double wheelDiameter = 6.8;
		private static double trackWidth = 12.50;
	
		@SuppressWarnings("deprecation")
		private static DifferentialPilot pilot = new DifferentialPilot(
				wheelDiameter, trackWidth, Motor.B, Motor.A);
		private static Navigator nav = new Navigator(pilot);	
		@SuppressWarnings("unused")
		private static OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
		
		//some waypoints
		private static Waypoint finish = new Waypoint(100, 0, 0);
		
		
		public static boolean onTheLine(float x, float y, float b, float m){
			float test_y = m*x + b;
			if(test_y >= y-5 && test_y <= y+5){ ///if test_y is +- = y
				LCD.drawString("line true", 0, 6);
				Sound.systemSound(true, 3);
				return true;
			}
			LCD.drawString("line fail", 0, 0);
			return false;
			
		}
		
		
		@SuppressWarnings("deprecation")
		public static void main(String[] args) {
			float[] points = new float[opp.sampleSize()];
			int count = 0;

			Thread u = new Thread(new sonicThread(sonicData, us));
			u.setDaemon(true);
			u.start();
			
			Thread u2 = new Thread(new sonicThread(sonicData2, us2));
			u2.setDaemon(true);
			u2.start();

			// establish a fail-safe: pressing Escape quits
			Brick brick = BrickFinder.getDefault(); // get specifics about this
			// robot
			brick.getKey("Escape").addKeyListener(new KeyListener() {
				
				public void keyPressed(Key k) {
					pilot.stop();
					us.close();
					Motor.A.close();	
					Motor.B.close();
					System.exit(1);
				}

				
				public void keyReleased(Key k) {
					System.exit(1);
				}
			});
			
			pilot.setLinearSpeed(10);  //sets straight ahead speed
			pilot.setAngularSpeed(50); //sets turning speed
			pilot.addMoveListener(opp);  //adds the listerner to capture x,y,heading values
			
			//Button.waitForAnyPress();
			
			nav.goTo(finish);
			int distance = sonicData.getSonicValue();
			int distance2 = sonicData2.getSonicValue();
			LCD.drawString("starting", 0, 1);
			pilot.steer(0);
			
			while ( true ){ //while goal hasn't been reached yet
				//pilot.forward();
				//distance = sonicData.getSonicValue();
				//distance2 = sonicData2.getSonicValue();
//				if (nav.pathCompleted()){
//					LCD.drawString("test5", 0, 0);
//					Sound.systemSound(true, 3); //play victory call
//					System.exit(1); //exit
//				}
				while (true){
					distance2 = sonicData2.getSonicValue();
					LCD.drawString(String.valueOf(distance2), 0, 1);
					if (distance2 < 15) {
						LCD.drawString("hit the wall", 0, 1);
						nav.stop();  //stops pilot as well
						Delay.msDelay(2000);
						break;
					}
					if (nav.pathCompleted()){
						LCD.drawString("test5", 0, 0);
						Sound.systemSound(true, 3); //play victory call
						System.exit(1); //exit
					}
				}
				opp.fetchSample(points, 0);
				float m = (finish.y - points[1])/(finish.x - points[0]);
				float b = points[1] - m*points[0];
				pilot.rotate(90);
				LCD.drawString("hugging wall", 0, 1);
				//LCD.drawString(String.valueOf(m), 0, 2);
				//LCD.drawString(String.valueOf(b), 0, 3);
				pilot.travel(15);
				while (true) {
					//Delay.msDelay(2000);
					pilot.steer(0);
					opp.fetchSample(points, 0);
					distance = sonicData.getSonicValue();
					//System.out.printf("p[%d]: %.2f, %.2f\n", count, points[0], points[1]);
					
					while(!onTheLine(points[0],points[1], b, m)){
						distance = sonicData.getSonicValue();
						//LCD.drawString(String.valueOf(distance), 0, 1);
						//Sound.systemSound(true, 3);
						if (distance < 25) { // if it's hugging the wall
							//Sound.systemSound(true, 3);
							LCD.drawString("following", 0, 1);
							opp.fetchSample(points, 0);
							System.out.printf("p[%d]: %.2f, %.2f\n", count, points[0], points[1]);
							distance = sonicData.getSonicValue();
							}
						else{
						Sound.systemSound(true, 3);
						pilot.stop();  //or use nav.stop()
						LCD.drawString("corner", 0, 1);
						//nav.stop();
						pilot.travel(10);
						pilot.rotate(-90); // right turn with -
						pilot.travel(20);
						distance = sonicData.getSonicValue();
						pilot.forward();
						}
						opp.fetchSample(points, 0);
					}
					LCD.drawString("on the line", 0, 4);
					pilot.rotate(90);
					nav.goTo(finish);
					break;
					}
				}
			}
		}

		
		//	if(distance <5){ //Hit a boundary 
				//record the y value of my location
				//move 90 degrees to the left
				//while y is not equal to the y at the boundary location
					//make rotations around around the boundary (maybe  use test of nav around wall?)
				
				//if y is more or less equal to the the boundary y:
					//turn right 90 degrees. keep on goin until goal.
				
			//}
			