package UserCustomization.Configuration;

import java.util.ArrayList;

import android.util.Log;

import UserCustomization.Character.Character;
import UserCustomization.Item.BuildableItem;
import UserCustomization.Item.BuildableItem.BuildableItemType;

import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;

public class AI {
	private WorldMap m_World;
	private ArrayList<GameObject> m_ObjectList;
	private int m_Start, m_End;
	
	public AI(WorldMap world, ArrayList<GameObject> obj, int playerStartIndex, int playerEndIndex)
	{ 
		m_ObjectList = obj;
		m_Start = playerStartIndex;
		m_End = playerEndIndex;
		m_World = world;
	}
	
	public void Update(long ctime)
	{
		// for each AI player in world map
		for(int i = m_Start; i <= m_End; ++i)
		{
			// get next waypoint across all lane, future step
			if(m_ObjectList.get(i).getType() != GameObject.Type.Character)
				continue;
			Character character = (Character) m_ObjectList.get(i);
			if(character.IsMainCharacter())
				continue;
			
			int next = character.getNextWayPoint(), lane = character.getLane();
			// WHY next WILL RETURN NEGATIVE NUMBER ??  
			if(next < 0)
				Log.e("","" +next);
			int goldlane = 0, obstaclelane = 0, nextlane = lane;
			for(int x=0; x < m_World.getLaneNum(); x++)
			{
				// if resource,i.e. gold, found
				if(m_World.getMapInfo_Status(x, next) == WorldMap.State.COLLECTABLE_ITEM)
				{
					// store all the resources index
					goldlane |= (1 << x);
				}
				/*
				 * Error found, obstaclelane always zero. Why ???
				 */
				// if obstacle found
				else if(m_World.getMapInfo_Status(x, next) == WorldMap.State.BUIDABLE_ITEM)
				{
					// store all the obstacles index
					obstaclelane |= (1 << x);
				}
			}
			
			/*
			 * Start single path finding algorithm
			 * The following algorithm is used to find the optimal path for single node.
			 * Thus, it forms the base case for high-level path finding solver.
			 * In order to have a better AI, it needs to find a list of possible paths then choose the
			 * optimal path.
			 */
			boolean isResourcePick = false;
			// try to pick any resource
			if(goldlane != 0)
			{
				// player is at the middle lane
				if(lane == 1)
				{
					// resources found either inner or outer lane
					if((goldlane & 5) != 0)
					{
						isResourcePick = true;
						// tossing a coin
						byte coin = (byte)(ctime & 0x1);
						// if coin face is head
						if(coin == 1)
							nextlane = 2; // go to outer lane
						// if coin face is tail
						else
							nextlane = 0; // go to inner lane
					}
					// resource found on inner lane
					else if((goldlane & 1) == 1)
					{
						nextlane = 0; // go to the inner lane
						isResourcePick = true;
					}
					// resource found on outer lane
					else if((goldlane & 4) == 4)
					{
						nextlane = 2; // go to outer lane
						isResourcePick = true;
					}
				}
				// resource found on middle lane and player located either at inner or outer lane
				else if((goldlane & 2) == 2 && Math.abs(1 - lane) <= 1)
				{
					nextlane = 1; // go to middle lane
					isResourcePick = true;
				}
			}

			// no gold to pick then try to avoid any obstacle if possible
			if(!isResourcePick && obstaclelane != 0)
			{
				// obstacle found on either inner or outer lane
				if(((obstaclelane & 1) == 1 && lane == 0) || ((obstaclelane & 4) == 4 && lane == 2))
				{
					nextlane = 1; // go to middle lane
				}
				// obstacle found on middle lane
				else if((obstaclelane & 2) == 2 && lane == 1)
				{
					// obstacle found on inner lane
					if((obstaclelane & 1) == 1)
						nextlane = 2; // go to outer lane
					// obstacle found on outer lane
					else if((obstaclelane & 4) == 1)
						nextlane = 0; // go to inner lane
					// obstacle found on neither inner or outer lane
					else
					{
						// tossing a coin
						byte coin = (byte)(ctime & 0x1);
						// if coin face is head
						if(coin == 1)
							nextlane = 2; // go to outer lane
						// if coin face is tail
						else
							nextlane = 0; // go to inner lane
					}
				}
			}
			/*
			 * End single path finding algorithm
			 */

			if(nextlane != lane)
			{
				// update AI player position
				if(nextlane - lane < 0)
				{
					character.SwitchLeftLane();
				}
				else
				{
					character.SwitchRightLane();
				}
			}

			BuildableItem item = character.BuildItem(BuildableItemType.ROCK);
			if(item != null)
				EventManager.getManager().add1FrontQueue(new Event<BuildableItem>(GameScene.SceneEvent.AI_ADD_ITEM.getID(),
						item));
		}
	}
}
