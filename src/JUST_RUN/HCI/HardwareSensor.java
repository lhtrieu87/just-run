/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 */ 
package JUST_RUN.HCI;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

/*
 * Sensor Type recorded at 29 September 2012:
 * Since API 3:
 *    -- TYPE_ACCELEROMETER, value = 1
 *    -- TYPE_ALL, value = -1
 *    -- TYPE_GYROSCOPE, value = 4
 *    -- TYPE_LIGHT, value = 5
 *    -- TYPE_MAGNETIC_FIELD, value = 2
 *    -- TYPE_ORIENTATION, value = 3 (@deprecated, SensorManager.getOrientation())
 *    -- TYPE_PRESSURE, value = 6
 *    -- TYPE_PROXIMITY, value = 8
 *    -- TYPE_TEMPERATURE, value = 7 (@deprecated, use Sensor.TYPE_AMBIENT_TEMPERATURE )
 *  Since API 9:
 *    -- TYPE_GRAVITY, value = 9
 *    -- TYPE_LINEAR_ACCELERATION, value = 10
 *    -- TYPE_ROTATION_VECTOR,value 11
 *  Since API 14:
 *    -- TYPE_AMBIENT_TEMPERATURE, value 13
 *    -- TYPE_RELATIVE_HUMIDITY, value = 12
 */
public class HardwareSensor{
	private static HardwareSensor m_objSensor;
	//private SystemConfiguration m_objSysMgr;
	private SensorManager m_objSensorMgr;
	private ArrayList<SensorUtil> m_objSensorsList = new ArrayList<SensorUtil>();
	
	private HardwareSensor(Context context)
	{ 
		//m_objSysMgr = sysMgr;
		m_objSensorMgr = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public final static HardwareSensor getHandle(Context context)
	{
		if(m_objSensor == null)
			m_objSensor = new HardwareSensor(context);
		return m_objSensor;
	}
	
	/*
	 * get current Sensor Manager
	 */
	public final SensorManager getManager()
	{
		return m_objSensorMgr;
	}
	
	/*
	 * Add sensor to global sensor list for later registration
	 */
	public final void addSensor(SensorUtil sensor)
	{
		if(sensor.getSensor()  == null || sensor.getSensorListener() == null)
			return;
		m_objSensorsList.add(sensor);
	}
	
	/*
	 * Add sensor to global sensor list and register immediately
	 */
	public final void addSensorAndRegister(SensorUtil sensor)
	{
		m_objSensorsList.add(sensor);
		try
		{
			if(sensor.getSensor()  == null || sensor.getSensorListener() == null)
				return;
			m_objSensorMgr.registerListener(sensor.getSensorListener(), 
				sensor.getSensor(), sensor.getSensorDelay());
		}catch(Exception e)
		{
			Log.e("Sensor registration error","Unable to register sensor !\n" + e.toString());
		}
	}
	/*
	 * Register Sensor Listener
	 */
	public final void RegisterSensor()
	{
		try
		{
			for(SensorUtil sensor : m_objSensorsList)
			{
				if(sensor.getSensor()  == null || sensor.getSensorListener() == null)
					continue;
				m_objSensorMgr.registerListener(sensor.getSensorListener(), 
					sensor.getSensor(), sensor.getSensorDelay());
			}
		}catch(Exception e)
		{
			Log.e("Sensor registration error","Unable to register sensor !\n" + e.toString());
		}
	}
	
	/*
	 * Unregister Sensor Listener
	 */
	public final void UnregisterListener()
	{
		for(SensorUtil sensor : m_objSensorsList)
		{
			m_objSensorMgr.unregisterListener(sensor.getSensorListener());
		}
	}
}
