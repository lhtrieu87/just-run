/**
 * @author Million
 * Created by Million on v0.1
 * Edited : by Han Lin on v0.3 -- change Gesture enumeration to support EventManager
 *                             -- migrate GestureDetector class to single public class
 *                             -- added HCI_addNewSensor method
 *                             -- added HCI_registerSensor method
 *                             -- added HCI_unregisterSensor method
 *                             -- added init_default_sensor method
 */
package JUST_RUN.HCI;

import android.content.Context;
import android.view.MotionEvent;

import JUST_RUN.GameplaySystem.EventManager;

public class HCI
{
	public static enum Gesture
	{
		SingleTap(EventManager.getNewID()),
		SwipeLeft(EventManager.getNewID()),
		SwipeRight(EventManager.getNewID()),
		SwipeUp(EventManager.getNewID()),
		SwipeDown(EventManager.getNewID());
		
		private final int ID;
		
		private Gesture(int ID)
		{
			this.ID = ID;
		}
		
		public int getID() {
			return ID;
		}
	}
	
	private GestureDetection m_objGestureDetector;
	private HardwareSensor m_objHardwareSensor;
	
	public HCI (Context context)
	{
		m_objGestureDetector = GestureDetection.getGestureDetection();
		m_objHardwareSensor = HardwareSensor.getHandle(context);
		init_default_sensor();
	}
	
	public void HCI_processMotionEvent (MotionEvent e)
	{
		m_objGestureDetector.setProcessMotionEvent(e);
	}
	
	public void HCI_addNewSensor(SensorUtil sensor)
	{
		m_objHardwareSensor.addSensorAndRegister(sensor);
	}
	
	public void HCI_registerSensor()
	{
		m_objHardwareSensor.RegisterSensor();
	}
	
	public void HCI_unregisterSensor()
	{
		m_objHardwareSensor.UnregisterListener();
	}
	
	private void init_default_sensor()
	{
		m_objHardwareSensor.addSensor(Accelometer.getHandle(
				m_objHardwareSensor.getManager()));
		m_objHardwareSensor.addSensor(Gyroscope.getHandle(
				m_objHardwareSensor.getManager()));
		HCI_registerSensor();
	}
}


