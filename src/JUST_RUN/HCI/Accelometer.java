/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 */ 
package JUST_RUN.HCI;

import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.HCI.HCI.Gesture;
import JUST_RUN.RendererEngine.Vector;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelometer extends SensorUtil
{
	private static Accelometer m_objAcc;
	private SensorManager m_objSensorMgr;
	private Sensor m_objSensor;
	private long m_startTime;
	private float m_fX, m_fY, m_fZ;
	
	private Accelometer(SensorManager sensorMgr)
	{
		m_objSensorMgr = sensorMgr;
		m_objSensor = getHardwareSensor(Sensor.TYPE_ACCELEROMETER);
		m_startTime = System.currentTimeMillis();
	}
	
	public final static Accelometer getHandle(SensorManager sensorMgr)
	{
		if(m_objAcc == null)
			m_objAcc = new Accelometer(sensorMgr);
		return m_objAcc;
	}
	
	@Override
	public Sensor getSensor() {
		return m_objSensor;
	}

	@Override
	public SensorEventListener getSensorListener() {
		return m_objAccelometerListener;
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
	
	private final SensorEventListener m_objAccelometerListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		public void onSensorChanged(SensorEvent event) {
			long cTime = System.currentTimeMillis();
			long elapsed = cTime  - m_startTime;
			// check every 100 ms
			if(elapsed > 100)
			{
				/*
				 * Edited by million on V0.5
				 */
				// Use pitch rotation to check whether the device is tiled to the left or right.
				// Left.
				float threshold = 3;
				if(m_fY > threshold && event.values[1] < threshold)
					EventManager.getManager().add1FrontQueue(new Event<Vector>(Gesture.SwipeLeft.getID(), new Vector()));
				else if(m_fY < -threshold && event.values[1] > -threshold)
					EventManager.getManager().add1FrontQueue(new Event<Vector>(Gesture.SwipeRight.getID(), new Vector()));
				else if(m_fY >= -threshold && m_fY <= threshold)
					if(event.values[1] > threshold)
						EventManager.getManager().add1FrontQueue(new Event<Vector>(Gesture.SwipeRight.getID(), new Vector()));
					else if(event.values[1] < -threshold)
						EventManager.getManager().add1FrontQueue(new Event<Vector>(Gesture.SwipeLeft.getID(), new Vector()));
				/*
				 * End edited
				 */
				m_startTime = cTime;
				m_fX = event.values[0];
				m_fY = event.values[1];
				m_fZ = event.values[2];
			}
		}
	};
}
