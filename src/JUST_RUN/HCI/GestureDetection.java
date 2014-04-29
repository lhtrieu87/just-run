/**
 * @author Million
 * Created by Million on v0.1
 * Edited : by Han Lin on v0.3 -- migrated to single public class with singleton design 
 */ 
package JUST_RUN.HCI;

import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.HCI.HCI.Gesture;
import JUST_RUN.RendererEngine.Vector;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureDetection extends  GestureDetector.SimpleOnGestureListener
{
	private static final int SWIPE_MIN_DISTANCE = 25; // X direction
	private static final int SWIPE_MAX_OFF_PATH = 140; // Y direction
	private static final int SWIPE_THRESHOLD_VELOCITY = 40; // X direction
	
	private GestureDetector m_objGestureDetector;
	private static GestureDetection m_objDetection;
	private EventManager m_ObjEventManager = EventManager.getManager();
	
	private GestureDetection()
	{ 
		m_objGestureDetector = new GestureDetector(this);
	}
	
	public static GestureDetection getGestureDetection()
	{
		if(m_objDetection == null)
			m_objDetection = new GestureDetection();
		return m_objDetection;
	}
	
	public void setProcessMotionEvent(MotionEvent e)
	{
		m_objGestureDetector.onTouchEvent(e);
	}
	
	@Override
	public boolean onSingleTapUp (MotionEvent e)
	{
		m_ObjEventManager.add1FrontQueue(new Event<Vector>(Gesture.SingleTap.getID(), new Vector(e.getRawX(), e.getRawY())));
		return true;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		float dX = e2.getX() - e1.getX();
		float dY = e1.getY() - e2.getY();
		
		if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && 
			Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && 
			Math.abs(dX) >= SWIPE_MIN_DISTANCE) 
		{
			if (dX > 0) 
			{
				m_ObjEventManager.add1FrontQueue(new Event<Vector>(Gesture.SwipeRight.getID(), new Vector()));
			} 
			else 
			{
				m_ObjEventManager.add1FrontQueue(new Event<Vector>(Gesture.SwipeLeft.getID(), new Vector()));
			}

			return true;

		} 
		else if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && 
				 Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY && 
				 Math.abs(dY) >= SWIPE_MIN_DISTANCE ) 
		{
			if (dY > 0) 
			{
				m_ObjEventManager.add1FrontQueue(new Event<Vector>(Gesture.SwipeUp.getID(), new Vector()));
			} 
			else 
			{
				m_ObjEventManager.add1FrontQueue(new Event<Vector>(Gesture.SwipeDown.getID(), new Vector()));
			}
			return true;
		}
		return false;
	}
}
