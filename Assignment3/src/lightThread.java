
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;


public class lightThread implements Runnable{
	NXTLightSensor light;
	globalLightData gld;
	
	public lightThread(globalLightData gld, NXTLightSensor light) {
		this.gld = gld;
		this.light = light;
	}


	public void run() {
		SampleProvider lightSampleProvider = light.getRedMode();
		float[] s1 = new float[lightSampleProvider.sampleSize()];
		while (true) {
			try {
				lightSampleProvider.fetchSample(s1, 0);
			      }
			catch (Exception ex) {}
			gld.setLightValue((int)(s1[0]*100));
		}
		
	}

}
