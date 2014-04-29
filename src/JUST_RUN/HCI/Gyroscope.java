/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 */ 
package JUST_RUN.HCI;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Gyroscope extends SensorUtil {
	private static Gyroscope m_objGyro;
	private SensorManager m_objSensorMgr;
	private Sensor m_objSensor;
	
	private Gyroscope(SensorManager sensorMgr)
	{
		m_objSensorMgr = sensorMgr;
		m_objSensor = getHardwareSensor(Sensor.TYPE_GYROSCOPE);
	}
	
	public final static Gyroscope getHandle(SensorManager sensorMgr)
	{
		if(m_objGyro == null)
			m_objGyro = new Gyroscope(sensorMgr);
		return m_objGyro;
	}
	
	@Override
	public Sensor getSensor() {
		return m_objSensor;
	}

	@Override
	public SensorEventListener getSensorListener() {
		return m_objGyroListener;
	}

	@Override
	public int getSensorDelay() {
		return SensorManager.SENSOR_DELAY_NORMAL;
	}

	@Override
	public SensorManager getSensorManager()
	{
		return m_objSensorMgr;
	}
	
	private final SensorEventListener m_objGyroListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		public void onSensorChanged(SensorEvent event) {

		}
	};
}
