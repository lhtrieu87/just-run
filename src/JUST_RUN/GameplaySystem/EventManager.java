/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.3 --  adding actual implementation
 */
package JUST_RUN.GameplaySystem;

import JUST_RUN.Core.DataStructures.EmptyQueue;
import JUST_RUN.Core.DataStructures.Queue;

/*
 * EventManager class is designed as singleton.
 * Two queue are implemented in this class, namely front-queue and back-queue.
 * Front-queue is served to store any events need to update immediately; while, 
 * back-queue is served to store any regular update events. 
 * For fast comparison, error event ID is assigned to negative integer and 
 * other event ID is assigned to positive integer.
 */
public class EventManager {
	private static int EVNET_ID_SIZE = 2;
	private static int NEW_EVENT_ID_CODE = 0;
	private static int NEW_ERROR_EVENT_ID_CODE = 1;
	private static int[] ID = new int[EVNET_ID_SIZE];
	private static EventManager m_objManager;
	
	/*
	 * Store immediate update event
	 */
	private Queue<Event<?>> m_objFrontQueue = new Queue<Event<?>>();
	/*
	 * Store regular update event
	 */
	private Queue<Event<?>> m_objBackQueue = new Queue<Event<?>>();
	
	private EventManager()
	{ 
		ID[NEW_EVENT_ID_CODE] = 1;
		ID[NEW_ERROR_EVENT_ID_CODE] = -1;
	}
	
	public static EventManager getManager()
	{
		if(m_objManager == null)
			m_objManager = new EventManager();
		return m_objManager;
	}
	
	/*
	 * Generate a unique, new event ID 
	 * Since this system does not manage memory, hash table is not needed.
	 * Thus, an incremental ID is adequate for unique identification for each event.
	 * Note that the ID is started at 1 and progressively increasing until the maximum of positive integer capacity.
	 */
	public static int getNewID()
	{
		return ID[NEW_EVENT_ID_CODE]++;
	}
	
	/*
	 * Preview last generated ID
	 * Note: this value is associated to the size of the generated event ID.
	 */
	public static int getCurrentID()
	{
		return ID[NEW_EVENT_ID_CODE];
	}
	
	/*
	 * Generate a unique, new error event ID 
	 * Since this system does not manage memory, hash table is not needed.
	 * Thus, an decreasing ID is adequate for unique identification for each event.
	 * Note that the ID is started at -1 and progressively decreasing until the minimum of negative integer capacity.
	 */
	public static int getNewErrorID()
	{
		return ID[NEW_ERROR_EVENT_ID_CODE]--;
	}
	
	public boolean IsFrontQueueEmpty()
	{
		return m_objFrontQueue.isEmpty();
	}
	
	public boolean IsBackQueueEmpty()
	{
		return m_objBackQueue.isEmpty();
	}
	
	public void clearFrontQueue()
	{
		m_objFrontQueue = new Queue<Event<?>>();
	}
	
	public void clearBackQueue()
	{
		m_objBackQueue = new Queue<Event<?>>();
	}
	
	/*
	 * Add event to the first element from front queue.
	 */
	public void add1FrontQueue(Event<?> e)
	{
		m_objFrontQueue.CutFrontQueue(e);
	}
	
	/*
	 * Add event to the first element from back queue.
	 */
	public void add1BackQueue(Event<?> e)
	{
		m_objBackQueue.CutFrontQueue(e);
	}
	
	/*
	 * Add event to the last element from front queue.
	 */
	public void add2FrontQueue(Event<?> e)
	{
		m_objFrontQueue.Enqueue(e);
	}
	
	/*
	 * Add event to the last element from back queue.
	 */
	public void add2BackQueue(Event<?> e)
	{
		m_objBackQueue.Enqueue(e);
	}
	
	/*
	 * Delete last element from front queue
	 */
	public void delete2FrontQuque() throws EmptyQueue
	{
		m_objFrontQueue.Dequeue();
	}

	/*
	 * Delete last element from back queue
	 */
	public void delete2BackQuque() throws EmptyQueue
	{
		m_objBackQueue.Dequeue();
	}
	
	/*
	 * Dispatch current element for front queue.
	 * Note: The retrieved element will be removed after process.
	 */
	public Event<?> DispatchFrontQueue()
	{
		try
		{
			return m_objFrontQueue.Dequeue();
		}catch(EmptyQueue e)
		{
			return null;
		}
	}
	
	/*
	 * Dispatch current element for back queue.
	 * Note: The retrieved element will be removed after process.
	 */
	public Event<?> DispatchBackQueue()
	{
		try
		{
			return m_objBackQueue.Dequeue();
		}catch(EmptyQueue e)
		{
			return null;
		}
	}
}
