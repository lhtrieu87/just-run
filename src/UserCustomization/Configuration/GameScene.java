package UserCustomization.Configuration;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.GameplaySystem.Clock;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.VirtualScreen;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import JUST_RUN.RendererEngine.Drawing.Menu.Canvas;
import JUST_RUN.RendererEngine.Drawing.Menu.Component;
import JUST_RUN.RendererEngine.Drawing.Menu.Font;
import JUST_RUN.RendererEngine.Drawing.Menu.Label;
import JUST_RUN.RendererEngine.SceneManagement.Scene;
import JUST_RUN.HCI.HCI;
import UserCustomization.Character.AncientGuy;
import UserCustomization.Character.Character;
import UserCustomization.Character.Magician;
import UserCustomization.Character.Vampire;
import UserCustomization.Character.VoidObject;
import UserCustomization.Environment.Landscape;
import UserCustomization.Environment.Sky;
import UserCustomization.Environment.TestingLines;
import UserCustomization.Item.BuildableItem;
import UserCustomization.Item.BuildableItem.BuildableItemType;
import UserCustomization.Item.Gold;
import UserCustomization.Item.Item;

public class GameScene extends Scene {
	
	public static enum SceneEvent
	{
		AI_ADD_ITEM (EventManager.getNewID());
		
		private final int ID;
		
		private SceneEvent(int ID)
		{
			this.ID = ID;
		}
		
		public int getID() {
			return ID;
		}
	}
	
	private static final long TARGETED_FRAME_RATE = 33333333; // in nano seconds, approximately 33.33 milliseconds
	
	public static int PLAYER_START_INDEX = 0;
	public static int NUM_OF_PLAYER = 4;
	public static int ITEM_START_INDEX = PLAYER_START_INDEX + NUM_OF_PLAYER;
	private static int MAX_GOLD_RESOURCE = 150;
	private int m_intGold = 0;
	private GameHUD m_objHUD;
	private Camera m_camera, m_ortho;
	private Lightning m_light;
	
	protected Character m_mainCharacter;
	
	protected GameObject m_objLandScape, m_objSky;
	protected GameObject m_objLine1, m_objLine2, m_objLine3;
	
	private WorldMap m_objWorld = new WorldMap();
	private AI m_objAI;
	
	private long m_startTime, m_elapsedTime;
	
	public ArrayList<GameObject> m_objRenderList = new ArrayList<GameObject>();
	
	// debug purpose
	private Label m_lblFrameRate, m_lblUpdateRate;
	private int m_intRenderCount = 0;

	public GameScene (int width, int height)
	{ 
		initialize (width, height);
	}
	
	public Character getMainCharacter ()
	{
		return m_mainCharacter;
	}
	
	public GameHUD getSceneHUD()
	{
		return m_objHUD;
	}
	
	public WorldMap getWorldMap()
	{
		return m_objWorld;
	}

	private void initialize (int width, int height)
	{
		m_camera = new Camera ();
		m_camera.setEyePosition(new Vector(-5,50,-5));
		m_camera.setLookAt(new Vector(0,0,0));
		m_camera.setUpVector(new Vector(1,0,0));
		m_ortho = new Camera();
		m_light = new Lightning (new Vector (0.0f, 10.0f, -10.0f), 
				                 new Material (new Vector (0.4f, 0.4f, 0.04f),
						                       SystemColor.White,
						                       new Vector (0.2f, 0.2f, 0.01f), 0));	
		
		m_objSky = new Sky(m_camera, GameResource.SKY_MESH);
		
		m_objLandScape = new Landscape (m_camera, m_light, GameResource.LANDSCAPE1_MESH);	
		m_objLandScape.setPosition (new Vector (0.0f, -1.0f, 0.0f));
		
		m_objLine1 = new TestingLines (m_camera, GameResource.LINE1_1, SystemColor.Red);
		m_objLine2 = new TestingLines (m_camera, GameResource.LINE1_2, SystemColor.Green);
		m_objLine3 = new TestingLines (m_camera, GameResource.LINE1_3, SystemColor.Blue);

		/*addGameObject (m_objLine1);
		addGameObject (m_objLine2);
		addGameObject (m_objLine3);*/
		
		m_objWorld.setWayPoint(m_objLine1.getShape ().getMesh ().getVertices ());
		m_objWorld.setWayPoint(m_objLine2.getShape ().getMesh ().getVertices ());
		m_objWorld.setWayPoint(m_objLine3.getShape ().getMesh ().getVertices ());
		
		m_mainCharacter = new Vampire (m_camera, m_light, m_objWorld, true, 0, 1);
		addGameObject(m_mainCharacter);
		Vampire vam = new Vampire(m_camera, m_light, m_objWorld, false, m_objWorld.getTotalNode(1) / 2, 1);
		addGameObject(vam);
		vam = new Vampire(m_camera, m_light, m_objWorld, false, m_objWorld.getTotalNode(1) / 4, 1);
		addGameObject(vam);
		//addGameObject(new VoidObject());
		//addGameObject(new VoidObject());
		addGameObject(new VoidObject());
		
		float ratio = (float) width / height;
		m_camera.setProjectionFrustum (-ratio, ratio, -1, 1, 1, 1000000);
		if(width == 0)
		{
			try
			{
				m_ortho.setOrthographics(0, VirtualScreen.getScreenWidth(), 0, VirtualScreen.getScreenHeight(), 0, 1000);
			}catch(Exception e){}
		}
		else
			m_ortho.setOrthographics(0, width, 0, height, 0, 1000);
		m_objHUD = new GameHUD(width, height, m_ortho);
		m_objHUD.setHealth(m_mainCharacter.getFullHealth());
		
		Font font = Font.getHandle();
		font.Size(18);
		font.ForeColor(SystemColor.Azure);
		font.BackColor(SystemColor.Transparent);
		m_lblFrameRate = new Label("", font, 0, 20, true, m_ortho);
		m_lblUpdateRate = new Label("", font, 0, 50, true, m_ortho);
		
		m_objAI = new AI(m_objWorld, m_objGameObject, PLAYER_START_INDEX, ITEM_START_INDEX - 1);
		m_startTime = Clock.GetCurrentTime_Nano();
		
		Log.e("Log Scene","Started");
	}
	
	public void DecreaseGold(int value)
	{
		m_intGold -= value;
	}
	
	@Override
	public void Update (long currentTime)
	{
		m_objHUD.DisplayHealth(m_mainCharacter.GetHealth());
		m_objHUD.DisplayGold(m_mainCharacter.GetGold());
		
		// spawn game resources
		Gold g = new Gold (m_camera, m_light);
		if(m_intGold < MAX_GOLD_RESOURCE)
		{
			Random m_rand = new Random();
			int lane = m_rand.nextInt (m_objWorld.getLaneNum ());
			
			int offset = m_rand.nextInt (m_objWorld.getTotalNode(lane));
			if(m_objWorld.getMapInfo_Status(lane, offset) == WorldMap.State.EMPTY)
			{
				m_intGold += g.getValue ();
				g.setLane(lane);
				g.setWayPoint(offset);
				g.setPosition (m_objWorld.getPosition(lane, offset));
				m_objWorld.Update(lane, offset, offset, WorldMap.State.COLLECTABLE_ITEM);
				m_objGameObject.add(g);
			}
		}
		
		if(m_mainCharacter.getSkillState() == Character.SkillState.READY)
		{
			Canvas temp = (Canvas)m_objHUD.getComponents().get(GameHUD.CanvasLabel.ActivateButton.getIndex());
			temp.setForegroundColor(new Vector(temp.getForegroundColor(), 1));
		}
	}
	
	@Override
	public synchronized void FrameStart()
	{
		// calculate elapsed time in highest precision
		long ctime = Clock.GetCurrentTime_Nano();
		//Log.e("",(m_startTime / 1000000) + " " + (ctime / 1000000));
		m_elapsedTime = ctime - m_startTime;
		if(m_elapsedTime >= TARGETED_FRAME_RATE)
		{
			// record current time
			m_startTime = ctime;
			
			// perform scene management
			m_objRenderList.clear();
			int i=0;
			m_objRenderList.add(m_mainCharacter);
			
			// forward checking -- this applied if and only if the character is move forward
			// in the way point system.
			int i1, i2, i3 = 0, i4 = 0, lane = m_mainCharacter.getLane();
			int visibility = m_objWorld.getTotalNode(lane) >> 2; // 25 %
			boolean is2Interval = false;
			i1 = m_mainCharacter.getPrevWayPointIndex(); // initial way point
			i2 = i1 + visibility;
			
			if(i2 >= m_objWorld.getTotalNode(lane))
			{
				
				i4 = i2 - m_objWorld.getTotalNode(lane);
				i2 = m_objWorld.getTotalNode(lane) - 1;
				is2Interval = true;
			}
			
			for(i = 1;i<m_objGameObject.size();i++)
			{
				GameObject temp = m_objGameObject.get(i);
				int index = -1;
				if(temp instanceof Item)
				{
					Item t = (Item)temp;
					index = t.getWayPoint();
				}
				else if(temp instanceof Character)
				{
					Character c = (Character)temp;
					index = c.getPrevWayPointIndex();
				}
				else
					continue;
				
				if(is2Interval)
				{
					if(index >= i1 && index <= i2)
						m_objRenderList.add(temp);
					else if(index >= i3 && index <= i4)
						m_objRenderList.add(temp);
				}
				else if(index >= i1 && index <= i2)
					m_objRenderList.add(temp);
			}
		}
		else
		{
			try
			{
				long temp = (TARGETED_FRAME_RATE - m_elapsedTime) / 1000000;
				if(temp > 0)
				{
					// haven't reach targeted frame time so make current thread into sleep
					this.wait(temp);
				}
			}catch(InterruptedException e)
			{ }
		}
	}
	@Override
	public void CreateScene()
	{
		++m_intRenderCount;
		//Refresh (SystemColor.CadetBlue);
		Refresh (SystemColor.Magenta);
		GraphicsAPI.setBackfaceCulling(false);
		GraphicsAPI.setDepthTest(false);
		m_objSky.Render();
		GraphicsAPI.setDepthTest(true);
		GraphicsAPI.setBackfaceCulling(true);
		m_objLandScape.Render();
		for(int i=0; i < m_objRenderList.size(); i++)
			m_objRenderList.get(i).Render();
		GraphicsAPI.setDepthTest(false);
		for(Component obj : m_objHUD.getComponents())
			obj.Render();
		m_lblFrameRate.Render();
		m_lblUpdateRate.Render();
		GraphicsAPI.setDepthTest(true);
	}
	
	@Override
	public void FrameEnd()
	{
		// advancing animation frame
	}
	
	public void ResetRenderingCount()
	{
		m_intRenderCount = 0;
	}
	
	public int getRenderingCount()
	{
		return m_intRenderCount;
	}
	
	public void setFrameRateLabel(int fps)
	{
		m_lblFrameRate.setText("Frame Rate = " + fps + " fps");
	}
	
	public void setUpdateRateLabel(int fps)
	{
		m_lblUpdateRate.setText("Update Rate = " + fps + " fps");
	}
	
	public void addObstacle(BuildableItem item)
	{
		if(item != null && 
		   m_objWorld.getMapInfo_Status(item.getLane(), item.getWayPoint()) == WorldMap.State.EMPTY)
		{
			m_objGameObject.add(item);
			m_objWorld.Update(item.getLane(), item.getWayPoint(), item.getWayPoint(), WorldMap.State.BUIDABLE_ITEM);
		}
	}
	
	public void AdvancingAI(long currentTime)
	{
		m_objAI.Update(currentTime);
	}
	
	protected void addObstacle(BuildableItemType obstacle)
	{
		// check world map before adding into game object list
		if (m_objWorld.getMapInfo_Status (m_mainCharacter.getLane(), 
										  m_mainCharacter.getPrevWayPointIndex()) 
			== WorldMap.State.EMPTY)
		{
			BuildableItem item = m_mainCharacter.BuildItem(obstacle);
			
			if (item != null)
			{
				this.addGameObject (item);
				m_objWorld.Update(item.getLane(), item.getWayPoint(), item.getWayPoint(), WorldMap.State.BUIDABLE_ITEM);
			}
		}
	}
	
	@Override
	public void HandleInteraction (Event<Vector> e, long currentTime) 
	{
		int id = e.getID ();

		if(id == HCI.Gesture.SwipeLeft.getID ())
		{
			m_mainCharacter.SwitchLeftLane ();
		}
		else if(id == HCI.Gesture.SwipeRight.getID ())
		{
			m_mainCharacter.SwitchRightLane ();
		}
		else if(id == HCI.Gesture.SingleTap.getID ())
		{
			/*
			 * Coordinate conversion is needed since the origin is located at
			 * bottom-left corner in OpenGL rendering whereas the origin is located
			 * at top-left corner in android display system. Thus, the y-axis needs to be 
			 * flip.
			 */
			Vector v = e.getArgument();
			
			try
			{
				v.Y = VirtualScreen.getScreenHeight() - v.Y;
			}catch(Exception unused)
			{ /* never happened, in this case */}
			
			if(checkInteraction(GameHUD.CanvasLabel.SelectableItem_1, v))
			{
				addObstacle(BuildableItemType.ROCK);
			}
			else if(checkInteraction(GameHUD.CanvasLabel.SelectableItem_2, v))
			{	
				addObstacle(BuildableItemType.POISON);
			}
			else if(checkInteraction(GameHUD.CanvasLabel.SelectableItem_3, v))
			{
				addObstacle(BuildableItemType.CONFUSION);
			}
			else if(checkInteraction(GameHUD.CanvasLabel.SelectableItem_4, v))
			{
				addObstacle(BuildableItemType.MINE);
			}
			else if(checkInteraction(GameHUD.CanvasLabel.SelectableItem_5, v))
			{

			}
			else if(checkInteraction(GameHUD.CanvasLabel.ActivateButton, v))
			{
				m_mainCharacter.ActivateAbility (currentTime);
				Canvas temp = (Canvas)m_objHUD.getComponents().get(GameHUD.CanvasLabel.ActivateButton.getIndex());
				temp.setForegroundColor(new Vector(temp.getForegroundColor(), 0.2f));
			}
		}
	}
	
	private boolean checkInteraction(GameHUD.CanvasLabel label, Vector v)
	{
		return ((Canvas)m_objHUD.getComponents().get(label.getIndex())).Intersect(v);
	}
}
