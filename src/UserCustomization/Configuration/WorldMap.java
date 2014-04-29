/**
 * @author Han Lin
 * Created by Han Lin on v0.4 by upgrading previous waypoint system
 * Edited :
 */
package UserCustomization.Configuration;

import java.util.ArrayList;

import android.util.Log;

import JUST_RUN.RendererEngine.Vector;

public class WorldMap {

	private ArrayList<ArrayList<MapInfo>> m_objWayPoint = new ArrayList<ArrayList<MapInfo>>();
	
	public static enum State
	{
		COLLECTABLE_ITEM,
		BUIDABLE_ITEM,
		EMPTY;
	}
		
	public WorldMap()
	{ }
	
	public void setWayPoint(float[] wayPoint)
	{
		// instantiate new way point
		m_objWayPoint.add(new ArrayList<MapInfo>());
		// store and initialize new way point
		int lane = m_objWayPoint.size() - 1;
		for(int i = 0; i < wayPoint.length; i+=3)
		{
			Vector v = new Vector(wayPoint[i], wayPoint[i + 1], wayPoint[i + 2]);
			m_objWayPoint.get(lane).add(new MapInfo(State.EMPTY, v));
		}
	}
	
	public void Update(int lane, int currentIndex, int targetIndex, State state)
	{
		// clear current index
		m_objWayPoint.get(lane).get(currentIndex).m_objStatus = State.EMPTY;
		// update target index
		m_objWayPoint.get(lane).get(targetIndex).m_objStatus = state;
	}
	
	public Vector getPosition(int lane, int index)
	{
		if(lane >= getLaneNum())
			lane -= getLaneNum();
		if(lane < 0)
			lane = 0;
		if(index >= getTotalNode(lane))
			index -= getTotalNode(lane);
		else if(index < 0)
			index += getTotalNode(lane);
		return m_objWayPoint.get(lane).get(index).m_objPosition;
	}
	
	public int getNextPosition(int lane, int currentIndex)
	{
		int index = currentIndex + 1;
		if(lane >= getLaneNum())
			lane -= getLaneNum();
		if(lane < 0)
			lane = 0;
		if(index >= getTotalNode(lane))
			index -= getTotalNode(lane);
		else if(index < 0)
			index += getTotalNode(lane);
		return index;
	}
	
	public int getPrevPosition(int lane, int currentIndex)
	{
		int index = currentIndex - 1;
		if(lane >= getLaneNum())
			lane -= getLaneNum();
		if(lane < 0)
			lane = 0;
		if(index >= getTotalNode(lane))
			index -= getTotalNode(lane);
		else if(index < 0)
			index += getTotalNode(lane);
		return index;
	}
	
	public int getLaneNum(){
		return m_objWayPoint.size();
	}
	
	public int getTotalNode(int lane)
	{
		return m_objWayPoint.get(lane).size();
	}
	
	public State getMapInfo_Status(int lane, int index)
	{
		if(lane >= getLaneNum())
			lane -= getLaneNum();
		if(lane < 0)
			lane = 0;
		if(index >= getTotalNode(lane))
			index -= getTotalNode(lane);
		else if(index < 0)
			index += getTotalNode(lane);
		return m_objWayPoint.get(lane).get(index).m_objStatus;
	}
	
	public Vector getMapInfo_Position(int lane, int index)
	{
		if(lane >= getLaneNum())
			lane -= getLaneNum();
		if(index >= getTotalNode(lane))
			index -= getTotalNode(lane);
		return m_objWayPoint.get(lane).get(index).m_objPosition;
	}
	
	// For each way point, it just can store one status.
	private class MapInfo
	{
		protected State m_objStatus;
		protected Vector m_objPosition;
		
		public MapInfo(State state, Vector position)
		{
			m_objStatus = state;
			m_objPosition = position;
		}
	}
}
