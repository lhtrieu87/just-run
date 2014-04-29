/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 */ 
package JUST_RUN.HCI;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorUtil {
	
	public abstract Sensor getSensor();
	public abstract SensorEventListener getSensorListener();
	public abstract int getSensorDelay();
	public abstract SensorManager getSensorManager();
	
	/*
	 * Find a list of all the available sensors of a particular type.
	 * By convention, any hardware Sensor implementations are returned at the top
	 * of the list, with virtual corrected implementations last.
	 */
	public final Sensor getHardwareSensor(int type)
	{
		/*
		 * Get virtual implementation if any; otherwise, default hardware 
		 * implementation is used.
		 */
		List<Sensor> sensor =  getSensorManager().getSensorList(type);
		if(sensor.isEmpty())
			return null;
		return sensor.get(sensor.size() - 1);
	}
}
