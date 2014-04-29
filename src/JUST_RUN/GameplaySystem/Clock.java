/**
 * The clock runs on a separate thread from the game loop.
 * It is used to update game properties, which are required to
 * be updated in a fixed real-time interval. 
 * @author Le Hong Trieu
 */

package JUST_RUN.GameplaySystem;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Clock 
{
	private Timer m_timer;
	private static long m_period = 100; // default value; 100 milliseconds
	private long m_startTime;
	
	public void SetUpdatePeriod (long period)
	{
		m_period = period;
	}
	
	public static long GetCurrentTime_Nano ()
	{
		return System.nanoTime();
	}
	
	public static long GetCurrentTime_Milli ()
	{
		return GetCurrentTime_Nano() / 1000000;
	}

	public long getStartTime_Nano()
	{
		return m_startTime;
	}
	
	public long getStartTime_Milli()
	{
		return m_startTime / 1000000;
	}
	
	public long getElapsedTime_Nano()
	{
		// get current time
		long ctime = Clock.GetCurrentTime_Nano(); 
		// calculate time elapsed since last recored time
		long temp = ctime - m_startTime;
		// store current time
		m_startTime = ctime;
		return temp;
	}
	
	public long getElapsedTime_Milli()
	{
		return getElapsedTime_Nano() / 1000000;
	}
	
	public void Start ()
	{
		if(m_timer == null) // this is essential
			m_timer = new Timer ();
		m_timer.schedule (new UpdateTask (), 0, m_period);
		m_startTime = Clock.GetCurrentTime_Nano();
	}
	
	public void Stop ()
	{
		if (m_timer != null)
		{
			m_timer.cancel ();
			m_timer = null; // this is essential
		}
	}
	
	/**
	 * The update () method is executed per fixed real-time interval by the clock.
	 */
	protected abstract void tick ();
	
	private class UpdateTask extends TimerTask
	{
		@Override
		public synchronized void run () 
		{
			tick ();
		}
	}
}
