/**
 * @author Han Lin
 * Created by Han Lin by migrating from v0.1 to v0.2
 * Edited :
 */
package UserCustomization.Configuration;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.Core.R;
import JUST_RUN.Core.SystemConfiguration;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.GameplaySystem.Clock;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.HCI.HCI;
import JUST_RUN.RendererEngine.SceneManagement.Scene;
import JUST_RUN.Resource.ResourceManager;
import UserCustomization.Character.Character;
import UserCustomization.Character.VoidObject;
import UserCustomization.Item.BuildableItem;
import UserCustomization.Item.Item;

/*
 * Game-specific components
 */
public class GameManager extends SystemConfiguration 
{
	private static final long TARGETED_UPDATE_RATE = 15; // 15 milliseconds, approximately half the targeted frame rate

	public enum GameState
	{
		LOADING_STATE(EventManager.getNewID()),
		MAIN_GAME_SCENE (EventManager.getNewID()),
		// Choose track, no of AIs...
		TRACK_CHOOSING_SCENE (EventManager.getNewID()),
		// Wait to connect with other players in multi-player mode.
		CONNECTION_SCENE (EventManager.getNewID()),
		PLAYER_WIN_STATE (EventManager.getNewID()),
		PLAYER_LOSE_STATE (EventManager.getNewID());
		
		private final int ID;
		
		private GameState(int ID)
		{
			this.ID = ID;
		}
		
		public int getID() {
			return ID;
		}
	}
	
	private enum RegularState
	{
		UPDATE_PLAYER_POSITION (EventManager.getNewID()),
		UPDATE_GAME_COMPONENT (EventManager.getNewID()),
		UPDATE_COLLISION_DETECTION (EventManager.getNewID()),
		UPDATE_GAME_STATE (EventManager.getNewID());
		
		private final int ID;
		
		private RegularState(int ID)
		{
			this.ID = ID;
		}
		
		public int getID() {
			return ID;
		}
	}
	
	private Scene m_objCurrentScene;
	private EventManager m_objEventMgr;
	private GameClock m_objClock = new GameClock();
	private int m_intUpdateCount = 0;
	private long m_DebugStartTime = 0;
	private int m_intScreenWidth, m_intScreenHeight;
	private GameState m_objCurrentState;
	private Context m_objContext;
	
	public GameManager(Context context)
	{ 
		m_objContext = context;
		m_objEventMgr = EventManager.getManager();
		m_objEventMgr.clearFrontQueue();
	}
	
	@Override
	public void StartUpInitialization() {
		
	}
	
	@Override
	public void RenderEngine_onSurfaceCreated()
	{
		// initialize texture resources
		GameResource.SKY_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.sky_texture);
		GameResource.LANDSCAPE_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.landscape_texture);
		GameResource.VAMPIRE_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.vampire_texture);
		GameResource.ANCIENTGUY_SKILL_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.skill_slow);
		GameResource.VAMPIRE_SKILL_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.skill_plushp);
		GameResource.DIAMOND_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.diamond_texture);
		GameResource.HUD_ROCK_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.hud_rock_texture);
		GameResource.ROCK_TEXTURE = ResourceManager.getTextureHandle().addTexture(R.raw.rock_texture);
	}
	
	@Override
	public void RenderEngine_onSurfaceChanged(int width, int height)
	{
		m_intScreenWidth = width;
		m_intScreenHeight = height;
		/*
		 * This event is added to event manager if and only if current state is empty.
		 * Thus, when the activity resume, it resumed correctly.
		 * Note: Removing this condition checking will cause a runtime exception. 
		 * Edit on V0.4 by Han Lin
		 */
		if(m_objCurrentScene == null)
			synchronized(m_objEventMgr)
			{
				m_objEventMgr.add2FrontQueue(new Event<Object>(GameState.LOADING_STATE.getID(), null));
			}
	}
	
	@Override
	public Scene getScene()
	{
		return m_objCurrentScene;
	}
	
	@Override
	public void Exiting()
	{
		m_objClock.Stop();
	}
	
	@Override
	public void Entering()
	{
		m_objClock.Start();
	}
	
	@Override
	public void Resource_Task_Load() throws Exception
	{
		// load game character geometry mesh
		GameResource.LoadCharacters();
		// load game item geometry mesh
		GameResource.LoadItems();
		// load game landscape geometry mesh
		GameResource.LoadLandscape1();
		// load game environment geometry mesh
		GameResource.LoadSky();
	}
	
	@Override
	public void Resource_Task_Done() throws Exception
	{
		synchronized(m_objEventMgr)
		{
			Log.e("Log start","Yes");
			m_objEventMgr.add2FrontQueue(new Event<Object>(GameState.MAIN_GAME_SCENE.getID(), null));
		}
	}
	
	private void setRegularEvent()
	{
		synchronized(m_objEventMgr)
		{
			m_objEventMgr.add2BackQueue(new Event<Object>(RegularState.UPDATE_PLAYER_POSITION.getID(), null));
			m_objEventMgr.add2BackQueue(new Event<Object>(RegularState.UPDATE_GAME_COMPONENT.getID(), null));
			m_objEventMgr.add2BackQueue(new Event<Object>(RegularState.UPDATE_COLLISION_DETECTION.getID(), null));
			m_objEventMgr.add2BackQueue(new Event<Object>(RegularState.UPDATE_GAME_STATE.getID(), null));
		}
	}
	
	private class GameClock extends Clock 
	{
		private GameClock()
		{ 
			m_DebugStartTime = Clock.GetCurrentTime_Milli();
			this.SetUpdatePeriod(TARGETED_UPDATE_RATE);
		}
		
		@Override
		protected synchronized void tick()
		{
			// calculate elapsed time in high precision
			long time = this.getElapsedTime_Milli();
			long cTime = Clock.GetCurrentTime_Milli();
			
			if(cTime - m_DebugStartTime >= 1000)
			{
				if(m_objCurrentState == GameState.MAIN_GAME_SCENE)
				{
					if(m_objCurrentScene instanceof GameScene)
					{
						int framerate = ((GameScene)m_objCurrentScene).getRenderingCount();
						((GameScene)m_objCurrentScene).ResetRenderingCount();
						((GameScene)m_objCurrentScene).setFrameRateLabel(framerate);
						((GameScene)m_objCurrentScene).setUpdateRateLabel(m_intUpdateCount);
						m_intUpdateCount = 0;
						m_DebugStartTime = cTime;
						Log.e("Log Render","Render : " + ((GameScene)m_objCurrentScene).m_objRenderList.size() + "HUD : " + 
								((GameScene)m_objCurrentScene).getSceneHUD().getComponents().size());
						Log.e("Log event","Started " + m_objCurrentScene + " " + m_objCurrentState);
					}
				}
			}
			
			if(time >= TARGETED_UPDATE_RATE)
			{
				/* reach update time */
				++m_intUpdateCount;
				// dispatch foreground event
				/*
				 * For detecting the event, it is dangerous to use else statement.
				 * A better way to get rid of it is to use bounding value to check. 
				 */
				Event e;
				synchronized(m_objEventMgr)
				{
					while ((e = m_objEventMgr.DispatchFrontQueue ()) != null)
					{
						int id = e.getID ();
	
						if (id < 0)
						{
							// Error event detected
							GraphicsAPI.AlertBox(m_objContext, "Error", e.toString());
						}
						/*
						 * Game state events
						 */
						else if(id == GameState.LOADING_STATE.getID())
						{
							m_objCurrentScene = new LoadingScene(m_intScreenWidth, m_intScreenHeight);
						}
						else if(id == GameState.CONNECTION_SCENE.getID())
						{
							
						}
						else if(id == GameState.MAIN_GAME_SCENE.getID())
						{
							m_objCurrentScene = new GameScene (m_intScreenWidth, m_intScreenHeight);
							m_objCurrentState = GameState.MAIN_GAME_SCENE;
						}
						else if(id == GameState.TRACK_CHOOSING_SCENE.getID())
						{
							
						}
						else if(id == GameState.PLAYER_LOSE_STATE.getID())
						{
							m_objCurrentScene = new LosingScene(m_intScreenWidth, m_intScreenHeight);
						}
						else if(id == GameState.PLAYER_WIN_STATE.getID())
						{
							m_objCurrentScene = new WinningScene(m_intScreenWidth, m_intScreenHeight);
						}
						/*
						 * HCI events
						 */
						else if (m_objCurrentScene != null && id >= HCI.Gesture.SingleTap.getID() &&
								id <= HCI.Gesture.SwipeDown.getID())
						{
							m_objCurrentScene.HandleInteraction (e, cTime);
						}
						else if(m_objCurrentState == GameState.MAIN_GAME_SCENE && 
								id == GameScene.SceneEvent.AI_ADD_ITEM.getID())
						{
							if(m_objCurrentScene instanceof GameScene)
							{
								((GameScene)m_objCurrentScene).addObstacle((BuildableItem)e.getArgument());
							}
						}
					}
					
					// dispatch background event
					// the background events are dispatch in sequence.
					while ((e = m_objEventMgr.DispatchBackQueue()) != null)
					{
						if(m_objCurrentScene instanceof GameScene)
						{
							if(e.getID() == RegularState.UPDATE_PLAYER_POSITION.getID())
							{
								for (int i = GameScene.PLAYER_START_INDEX; i < GameScene.PLAYER_START_INDEX + 4; i++)
								{
									if(i >= m_objCurrentScene.getGameObjects().size() ||
									   m_objCurrentScene.getGameObjects().get(i).getType() == GameObject.Type.UserDefined)
										continue;
									Character character = (Character) (m_objCurrentScene.getGameObjects().get(i));
									character.Update(cTime);
								}
							}
							else if(e.getID() == RegularState.UPDATE_GAME_COMPONENT.getID())
							{
								// AI should update after player moved and before collision detection.
								// Therefore, this is the suitable place to update AI component.
								((GameScene)m_objCurrentScene).AdvancingAI(cTime);
							}
							else if(e.getID() == RegularState.UPDATE_COLLISION_DETECTION.getID())
							{
								collisionDetection();
							}
							else if(e.getID() == RegularState.UPDATE_GAME_STATE.getID())
							{
								// update game state as last stage for regular update since all other
								// essential components have been updated.
								updateGameState();
							}
						}
					}
	
					if(m_objCurrentState != null)
					{
						// Finishing all event update. Now, perform the last update, that is, the scene update.
						// This update should not affect the current game state.
						m_objCurrentScene.Update(cTime);
						
						if(m_objCurrentState.getID() ==  GameState.MAIN_GAME_SCENE.getID())
						{
							setRegularEvent(); // put back regular update event
						}
					}
				}
			}
			else
			{
				try
				{
					// haven't reach targeted update time so make current thread into sleep
					this.wait(TARGETED_UPDATE_RATE - time);
				}catch(InterruptedException e)
				{ }
			}
		}
		
		private void collisionDetection()
		{
			GameScene game = (GameScene)m_objCurrentScene;
			ArrayList<GameObject> temp = m_objCurrentScene.getGameObjects();
			for(int i = GameScene.PLAYER_START_INDEX; i < GameScene.ITEM_START_INDEX; ++i)
			{
				if(i >= m_objCurrentScene.getGameObjects().size() || 
				   m_objCurrentScene.getGameObjects().get(i).getShape() == null)
					continue;
				Character character = (Character) (m_objCurrentScene.getGameObjects().get(i));
				//Log.e("Collision",character.getLane() + " " + character.getPrevWayPointIndex() + " " + character.getNextWayPoint());
				int lane = character.getLane();
				for(int j = GameScene.ITEM_START_INDEX; j < temp.size(); ++j)
				{
					Item item = (Item)temp.get(j);
					int iwaypoint = item.getWayPoint();
					// character.checkCollision() is slightly incorrect to check in this way 
					// because it ignore resource(s) at the same time. Since we assumed
					// that on each moving step player can only collide with one item at a time, 
					// this will not affect the player on next moving step.
					if (((character.getNextWayPoint() > character.getPrevWayPointIndex() &&
							iwaypoint >= character.getPrevWayPointIndex() && 
							iwaypoint <= character.getNextWayPoint()) || 
						(character.getNextWayPoint() < character.getPrevWayPointIndex() &&
							(iwaypoint >= character.getPrevWayPointIndex() || 
							iwaypoint <= character.getNextWayPoint()))) &&
						lane == item.getLane() && character.checkCollision())
					{
						// ignore any collision happened when player changing lane
						if(character.ChangingLane() || character.isGhost())
							continue;
						item.Activate(character);
						// We collect money resource, reduce the current total amount
						// of money value.
						if(item.getValue() > 0)
							game.DecreaseGold(item.getValue());
						
						if(item.isDead())
						{
							game.getWorldMap().Update(lane, iwaypoint,
									iwaypoint, WorldMap.State.EMPTY);
							temp.remove(j);
						}
					}
				}
			}
		}
		
		private void updateGameState()
		{
			// check game state
			if(m_objCurrentState == GameState.MAIN_GAME_SCENE)
			{
				GameScene scene = (GameScene)m_objCurrentScene;
				int charleft = 0;
				for(int i=GameScene.PLAYER_START_INDEX; i < GameScene.ITEM_START_INDEX; ++i)
				{
					if(scene.getGameObjects().get(i).getType() == GameObject.Type.Character)
					{
						charleft++;
						Character character = (Character)scene.getGameObjects().get(i);
						// check whether this character has health
						if(character.GetHealth() == 0)
						{
							// Main character has no more health
							if(character.IsMainCharacter())
							{
								// clear any event in foreground event queue
								m_objEventMgr.clearFrontQueue();
								// add losing event to foreground event queue
								m_objEventMgr.add1FrontQueue(new Event<Object>(GameState.PLAYER_LOSE_STATE.getID(), null));
								// clear any event in background event queue
								m_objEventMgr.clearBackQueue();
								// update game state
								m_objCurrentState = GameState.PLAYER_LOSE_STATE;
							}
							// AI player has no more health
							else
							{
								// remove it from game object list
								scene.getGameObjects().set(i , new VoidObject());
							}
						}
					}
				}
				
				// only main character left which means all AI player has dead
				if(charleft == 1)
				{
					// clear any event in foreground event queue
					m_objEventMgr.clearFrontQueue();
					// add losing event to foreground event queue
					m_objEventMgr.add1FrontQueue(new Event<Object>(GameState.PLAYER_WIN_STATE.getID(), null));
					// clear any event in background event queue
					m_objEventMgr.clearBackQueue();
					// update game state
					m_objCurrentState = GameState.PLAYER_WIN_STATE;
				}
			}
		}
	} // End Game Clock
}
