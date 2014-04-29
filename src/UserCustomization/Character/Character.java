/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 * Edited : Le Hong Trieu on v0.2 
 *       -- added a constructor
 *       -- implemented the MoveForward function, ReachedNextWayPoint, ChangeNextWayPoint and Update function
 *                                    
 */
package UserCustomization.Character;
/*
 * Import necessary library 
 */
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.SpriteAnimator;
import JUST_RUN.RendererEngine.Vector;
import UserCustomization.Configuration.CameraTracker;
import UserCustomization.Configuration.GameResource;
import UserCustomization.Configuration.WorldMap;
import UserCustomization.Item.BuildableItem;
import UserCustomization.Item.Confusion;
import UserCustomization.Item.Mine;
import UserCustomization.Item.Rock;
import UserCustomization.Item.BuildableItem.BuildableItemType;
import UserCustomization.Item.Poison;
/*
 * Game-specific object API
 * This class is created solely for the game "JUST RUN!".
 * Besides, this class is inherited from the default GameObject class.
 */
public abstract class Character extends GameObject 
{
	public static enum CharacterType
	{
		ANCIENT_GUY,		
		MAGICIAN,  
		VAMPIRE,
		CENTURY_21ST_GIRL,
		FUTURISTIC_RACER,
		ROBOT
	} 

	protected CharacterType m_characterType;
	
	/**
	 * m_vecDirection must be a unit vector to ensure the character moves
	 * at the speed specified by m_fSpeed.
	 */
	protected Vector 	m_vecDirection; 	// Need to be updated when changing lane/after reaching the next way point.
	/**
	 * How far the character moves during a game time period, NOT a spanning time of a frame.
	 */
	protected float 	m_fSpeed;
	
	/**
	 * Where the character is currently heading to. 
	 */
	protected Vector    m_vecNextDestination;  
	
	
	protected WorldMap m_objWayPoint; 
	
	/**
	 * Which way point the character is currently heading to? 
	 */
	protected int      m_nextWayPoint;
	
	/**
	 * The camera follows the main character, who is controlled by the player.
	 */
	protected boolean m_controllable;
	protected CameraTracker m_objCameraTracker;
	protected Camera m_objCamera;
	
	protected int m_totalHealth;
	protected int m_health;
	
	// Parameters for a unique skill.
	public static enum SkillState
	{
		COOLING,		
		READY,  
		ACTIVE 
	}
	protected long m_activePeriod;	// In millisecond.
	protected long m_coolingPeriod;	// In millisecond.
	protected SkillState m_skillState;
	// Counting down the cooling time.
	protected long m_coolingTime;
	// Counting down the active time.
	protected long m_activeTime;
	
	protected int m_golds;
	
	protected Lightning m_light;
	
	protected int m_lane;
	protected int m_prevWayPointIndex;
	
	protected boolean m_checkCollide;
	
	protected boolean m_changingLane = false;
	
	protected BuildableItem m_effect = null;
	
	protected boolean m_isGhost = false;
	
	protected SpriteAnimator m_objAnimator;
	
	public Character (CharacterType characterType, WorldMap wayPoint, boolean controllable, 
			Camera camera, Lightning light, int initialwaypoint, int initialLane)
	{
		m_light = light;
		
		m_characterType = characterType;
		
		m_objWayPoint = wayPoint;
		// edit on v0.4 -- by Han Lin
		m_lane = initialLane;
		// end edit
		m_nextWayPoint = initialwaypoint + 1;
		if(m_nextWayPoint == m_objWayPoint.getTotalNode(m_lane))
			m_nextWayPoint = 0;
		m_prevWayPointIndex = m_nextWayPoint - 1;
		if(m_prevWayPointIndex < 0)
			m_prevWayPointIndex += m_objWayPoint.getTotalNode(m_lane);
		setPosition (m_objWayPoint.getMapInfo_Position(m_lane, m_prevWayPointIndex));
		m_vecNextDestination = m_objWayPoint.getPosition(m_lane, m_nextWayPoint);
		setDirection (Vector.Subtract3v (m_vecNextDestination, m_vecPosition));
		
		m_objCameraTracker = null;
		m_controllable = controllable;
		m_objCamera = camera;
		if (m_controllable)
		{
			m_objCameraTracker = new CameraTracker (this, camera, wayPoint);
		}
		
		m_skillState = SkillState.READY;
		
		m_golds = GameResource.INITIAL_PLAYER_GOLD;
		
		SetCollideCheck (true);
	}
	
	public boolean IsMainCharacter()
	{
		return m_controllable;
	}
	
	public void SetCollideCheck (boolean check)
	{
		m_checkCollide = check;
	}
	
	public boolean checkCollision ()
	{
		return m_checkCollide;
	}
	
	public CharacterType getCharacterType ()
	{
		return m_characterType;
	}
	
	public void setDirection (Vector dir)
	{
		m_vecDirection = dir.getUnitVector3v ();
	}
	
	public Vector getDirection ()
	{
		return m_vecDirection;
	}
	
	public float getSpeed ()
	{
		return m_fSpeed;
	}
	
	public int getNextWayPoint ()
	{
		return m_nextWayPoint;
	}
	
	public int getPrevWayPointIndex ()
	{
		return m_prevWayPointIndex;
	}
	
	public boolean isGhost()
	{
		return m_isGhost;
	}
	
	public boolean ChangingLane()
	{
		return m_changingLane;
	}
	
	
	// SIDE EFFECTS ***************************
	// Apply effect on the character.
	public void ApplyEffect(BuildableItem effect)
	{
		m_effect = effect;
	}
	// Remedy the character from the affected effect.
	public void RemoveEffect ()
	{
		m_effect = null;
	}
	
	public boolean IsPoisoned()
	{
		return (m_effect != null && m_effect instanceof Poison);
	}
	
	public boolean IsConfused()
	{
		return (m_effect != null && m_effect instanceof Confusion);
	}
	
	public boolean IsEffectActive()
	{
		return m_effect != null;
	}
	
	protected void updateEffects(long currentTime)
	{
		
		
		if(IsEffectActive())
		{
			if(currentTime >= m_effect.GetWaiveTime())
				RemoveEffect();
		}
	}
	
	// SIDE EFFECTS END***************************
	
	
	@Override
	public void Update (long currentTime)
	{ 
		updateEffects(currentTime);
		if (m_objCameraTracker != null)
			m_objCameraTracker.UpdateObjState ();
		MoveForward ();
		updateSkillState (currentTime);
	}
	
	@Override
	public void Render ()
	{
		// Reset the model matrix of this character to the identity matrix.
		ResetTransformation ();
		Translate (m_vecPosition);
		
		Vector x = Vector.Cross (new Vector (0.0f, 1.0f, 0.0f), m_vecDirection);
		Vector y = Vector.Cross (m_vecDirection, x);
		float[] temp = new float[]{x.X, x.Y, x.Z, 0.0f, y.X, y.Y, y.Z, 0.0f, m_vecDirection.X, m_vecDirection.Y, m_vecDirection.Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
		Transform (temp);
		
		/*
		 * Edited by Han Lin on V0.5
		 */
		m_objAnimator.Interpolate();
		/*
		 * End edited
		 */
		
		super.Render ();
	}
	
	
	
	
	/*
	 ***************************NAVIGATION METHODS***************************
	 */
	
	/**
	 * All characters automatically move along their current lane on the track.
	 */
	protected final void MoveForward ()
	{
		if(m_changingLane)
			m_fSpeed = GameResource.CHANGING_LANE_SPEED;
		else
			m_fSpeed = GameResource.INITIAL_CHARACTER_SPEED;
		MoveForward (m_fSpeed, true);
	}
	
	protected void MoveForward (float speed, boolean updatePrevWayPoint)
	{
		setPosition (Vector.Add3v (m_vecPosition, Vector.Multiply3v (m_vecDirection, speed)));
			
		// Check whether the character has passed over the next way point.
		Vector toNextWayPoint = Vector.Subtract3v (m_vecNextDestination, m_vecPosition);
		float distance2NextWayPoint = Vector.Dot3v (toNextWayPoint, m_vecDirection);
		// Over-passed.
		if (distance2NextWayPoint <= 0)
		{
			// Changed to the next waypoint segment,
			// the character can hit any obstacle now.
			SetCollideCheck (true);
			
			if (updatePrevWayPoint)
			{
				// edited on v0.4 by Han Lin
				// update game world on player information
				m_prevWayPointIndex = m_nextWayPoint;
			}
			
			m_changingLane = false;
			
			Vector nextWayPoint = new Vector ();
			m_nextWayPoint = m_objWayPoint.getNextPosition(m_lane, m_nextWayPoint);
			nextWayPoint = m_objWayPoint.getPosition(m_lane, m_nextWayPoint);
			setDirection (Vector.Subtract3v (nextWayPoint, m_vecNextDestination));
			setPosition (m_vecNextDestination);
			m_vecNextDestination = nextWayPoint;
			
			MoveForward (-distance2NextWayPoint, false);
		}
	}
	
	public void ChangeLane(boolean left)
	{
		// When the character is in confusion, he cannot change lane.
		if(IsConfused())
			return;
		
		if(left)
		{
			// The character is poisoned, reverting
			// the left-right control. But this is only 
			// applicable for PC character, not for NPCs.
			if(IsPoisoned() && m_controllable)
				SwitchRightLane();
			else
				SwitchLeftLane();
		}
		else
		{
			if(IsPoisoned() && m_controllable)
				SwitchLeftLane();
			else
				SwitchRightLane();
		}
	}
	
	public void SwitchLeftLane ()
	{
		if (m_lane > 0 && !m_changingLane)
		{
			m_changingLane = true;
			m_prevWayPointIndex = m_nextWayPoint;
			
			m_lane--;
			Vector destNode = m_objWayPoint.getPosition(m_lane, m_nextWayPoint);
			Vector toDest = Vector.Subtract3v (destNode, m_vecPosition);
			float projOnDir = Vector.Dot3v (toDest, m_vecDirection);
			
			int wayPoint = m_nextWayPoint;
			
			if (projOnDir > 0)
			{
				while (projOnDir > 0)
				{
					m_vecNextDestination = destNode;
					m_nextWayPoint = wayPoint;
					wayPoint--;
					destNode = m_objWayPoint.getPosition(m_lane, wayPoint);
					toDest = Vector.Subtract3v (destNode, m_vecPosition);
					projOnDir = Vector.Dot3v (toDest, m_vecDirection);
				}
			}
			else
			{
				while (projOnDir <= 0)
				{
					wayPoint++;
					destNode = m_objWayPoint.getPosition(m_lane, wayPoint);
					toDest = Vector.Subtract3v (destNode, m_vecPosition);
					projOnDir = Vector.Dot3v (toDest, m_vecDirection);
					
					m_vecNextDestination = destNode;
					m_nextWayPoint = wayPoint;
				}
			}
			
			
			setDirection (Vector.Subtract3v (m_vecNextDestination, m_vecPosition));
		}
	}
	
	public void SwitchRightLane ()
	{
		if (m_lane < m_objWayPoint.getLaneNum () - 1 && !m_changingLane)
		{
			m_changingLane = true;
			m_prevWayPointIndex = m_nextWayPoint;
			
			m_lane++;
			Vector destNode = m_objWayPoint.getPosition(m_lane, m_nextWayPoint);
			Vector toDest = Vector.Subtract3v (destNode, m_vecPosition);
			float projOnDir = Vector.Dot3v (toDest, m_vecDirection);
			
			int wayPoint = m_nextWayPoint;
			
			if (projOnDir > 0)
			{
				while (projOnDir > 0)
				{
					m_vecNextDestination = destNode;
					m_nextWayPoint = wayPoint;
					wayPoint--;
					destNode = m_objWayPoint.getPosition(m_lane, wayPoint);
					toDest = Vector.Subtract3v (destNode, m_vecPosition);
					projOnDir = Vector.Dot3v (toDest, m_vecDirection);
				}
			}
			else
			{
				while (projOnDir <= 0)
				{
					wayPoint++;
					destNode = m_objWayPoint.getPosition(m_lane, wayPoint);
					toDest = Vector.Subtract3v (destNode, m_vecPosition);
					projOnDir = Vector.Dot3v (toDest, m_vecDirection);
					
					m_vecNextDestination = destNode;
					m_nextWayPoint = wayPoint;
				}
			}
			
			
			setDirection (Vector.Subtract3v (m_vecNextDestination, m_vecPosition));
		}
	}
	
	/*
	 ***************************END - NAVIGATION METHODS***************************
	 */
	
	@Override
	public final Type getType ()
	{
		return Type.Character;
	}
	
	@Override
	public final void setType (Type type) {}
	
	
	public int getFullHealth()
	{
		return m_totalHealth;
	}
	
	public int GetHealth ()
	{
		return m_health;
	}
	
	public void ReduceHealth (int amount)
	{
		m_health -= amount;
		if(m_health < 0)
			m_health = 0;
	}
	
	public void IncreaseHealth (int amount)
	{
		m_health += amount;
		if(m_health > m_totalHealth)
			m_health = m_totalHealth;
	}
	
	public void IncreaseGold (int amt)
	{
		m_golds += amt;
	}
	
	public int GetGold ()
	{
		return m_golds;
	}
	
	public int getLane()
	{
		return m_lane;
	}
	
	public void setLane(int lane)
	{
		if(lane < m_objWayPoint.getLaneNum()) // edited on v0.4 by Han Lin
		{
			m_lane = lane;
			// edited on v0.4 by Han Lin.
			setPosition (m_objWayPoint.getMapInfo_Position(m_lane, m_prevWayPointIndex));
		}
	}
	
	
	/*
	 ***************************SKILL METHODS***************************
	 */
	protected void setSkillState (SkillState state)
	{
		m_skillState = state;
	}
	
	public SkillState getSkillState()
	{
		return m_skillState;
	}
	
	/**
	 * This is the function to implement each character's unique ability.
	 */
	public void ActivateAbility (long currentTime)
	{
		switch (m_skillState)
		{
		case READY:
			setSkillState (SkillState.ACTIVE);
			m_activeTime = currentTime + m_activePeriod;
			skillOnActive ();
			break;
		default:
			break;
		}
	}
	
	protected void updateSkillState (long currentTime)
	{
		switch (m_skillState)
		{
		case ACTIVE:
			if (currentTime >= m_activeTime)
			{
				setSkillState (SkillState.COOLING);
				m_coolingTime = m_activeTime + m_coolingPeriod;
				skillOnCooling ();
			}
			break;
		case COOLING:
			if (currentTime >= m_coolingTime)
				setSkillState (SkillState.READY);
			break;
		default:
			break;
		}
	}
	
	protected abstract void skillOnActive ();
	protected abstract void skillOnCooling ();
	
	/*
	 ***************************END - SKILL METHODS***************************
	 */
	
	/**
	 * @param type What item is purchased to build on the track?
	 * @return null if the character does not have enough gold to purchase the item. Otherwise, 
	 * return the corresponding BuildableItem object and decrease the character's gold amount.
	 * @throws Exception
	 */
	public BuildableItem BuildItem (BuildableItemType type)
	{
		BuildableItem item = null;
		
		switch (type)
		{
		case ROCK:
			item = new Rock (m_objCamera, m_light);
			break;
		case POISON:
			item = new Poison(m_objCamera, m_light);
			break;
		case CONFUSION:
			item = new Confusion(m_objCamera, m_light);
			break;
		case MINE:
			item = new Mine(m_objCamera, m_light);
			break;
		default:
			break;
		}

		item.setLane(m_lane);
		item.setWayPoint(m_prevWayPointIndex);

		// Item cannot be null here.
		// Do not have enough gold to purchase this item.
		int itemPrice = item.GetPrice (m_characterType);
		if (itemPrice > m_golds)
		{
			item = null;
		}
		// We have gold to buy it.
		else
		{
			/////////////////////////////////////////////////////////////////////////////
			// Temporarily locate a newly built item at the character's current location.
			// Should avoid clustering traps/obstacles.
			/////////////////////////////////////////////////////////////////////////////
			// m_vecPosition is incorrect at the beginning
			item.setPosition (m_objWayPoint.getPosition(item.getLane(), item.getWayPoint()));
			m_golds -= itemPrice;
			// edited on v0.4 by Han Lin -- The following statement should located at here not on above !!!!!
			// collision flag should be set if and only if the item has been build successfully.
			SetCollideCheck (false);
		}
		
		return item;
	}
}