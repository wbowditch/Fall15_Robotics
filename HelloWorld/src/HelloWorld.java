import lejos.hardware.lcd.LCD;

import lejos.utility.Delay;
public class HelloWorld {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LCD.drawString("hey hannah",0,0);
		Delay.msDelay(2000);
	}

}
