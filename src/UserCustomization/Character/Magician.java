package UserCustomization.Character;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import UserCustomization.Configuration.GameResource;
import UserCustomization.Configuration.WorldMap;

public class Magician extends Character
{
	private float m_fFlyHeight = 0.0f;
	
	
	public Magician (Camera camera, Lightning light, WorldMap wayPoint, boolean controllable, int initialWaypoint,
			int initialLane) 
	{
		super (CharacterType.MAGICIAN, wayPoint, controllable, camera, light, initialWaypoint, initialLane);
		
		m_objMesh = GameResource.MAGICIAN_MESH;
		m_objMesh.setRenderingType (GraphicsAPI.SupportType.Triangles);
		
		m_objMesh.setCamera (camera);
		m_objMesh.setMaterial (new Material (new Vector (0.973f, 0.5f, 0.106f),
											 new Vector (0.5f, 0.5f, 0.5f),
											 new Vector (1, 1, 1),
											 10));
		m_objMesh.setLightning (light);
		
		m_objMesh.setTextureEnable (false);
		
		m_fSpeed = GameResource.INITIAL_CHARACTER_SPEED;
		
		m_totalHealth = 100;
		m_health = 50;
		
		
		m_skillState = SkillState.READY;
		m_activePeriod = 7500;
		m_coolingPeriod = 10 * 1000;
	}

	@Override
	protected void skillOnActive () 
	{
		m_fFlyHeight = 30.0f;
		m_objCameraTracker.SetFlyHeight (m_fFlyHeight);
		m_isGhost = true;
	}
	
	@Override
	protected void skillOnCooling () 
	{
		m_fFlyHeight = 0.0f;
		m_objCameraTracker.StopFlying ();
		m_isGhost = false;
	}
	
	@Override
	protected void MoveForward (float speed, boolean updatePrevWayPoint)
	{
		m_vecPosition = Vector.Add3v (m_vecPosition, Vector.Multiply3v (m_vecDirection, speed));
			
		// Check whether the character has passed over the next way point.
		Vector toNextWayPoint = Vector.Subtract3v (m_vecNextDestination, m_vecPosition);
		float distance2NextWayPoint = Vector.Dot3v (toNextWayPoint, m_vecDirection);
		// Over-passed.
		if (distance2NextWayPoint <= 0)
		{
			if (updatePrevWayPoint)
				m_prevWayPointIndex = m_nextWayPoint;
			
			Vector nextWayPoint = new Vector ();
			m_nextWayPoint = m_objWayPoint.getNextPosition(m_lane, m_nextWayPoint);
			nextWayPoint = m_objWayPoint.getPosition(m_lane, m_nextWayPoint);
			setDirection (Vector.Subtract3v (nextWayPoint, m_vecNextDestination));
			
			Vector trackTangent = Vector.Cross (new Vector (0.0f, 1.0f, 0.0f), m_vecDirection);
			Vector trackNormal = Vector.Cross (m_vecDirection, trackTangent);
			
			m_vecPosition = Vector.Add3v (m_vecNextDestination, Vector.Multiply3v (trackNormal, m_fFlyHeight));
			m_vecNextDestination = nextWayPoint;
			
			MoveForward (-distance2NextWayPoint, false);
		}
	}
	
	@Override
	public void SwitchLeftLane ()
	{
		if (m_lane > 0)
		{
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
			
			Vector trackTangent = Vector.Cross (new Vector (0.0f, 1.0f, 0.0f), m_vecDirection);
			Vector trackNormal = Vector.Cross (m_vecDirection, trackTangent);
			
			Vector p = Vector.Add3v (m_vecNextDestination, Vector.Multiply3v (trackNormal, m_fFlyHeight));
			
			setDirection (Vector.Subtract3v (p, m_vecPosition));
		}
	}
	
	@Override
	public void SwitchRightLane ()
	{
		if (m_lane < m_objWayPoint.getLaneNum () - 1)
		{
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
			
			
			Vector trackTangent = Vector.Cross (new Vector (0.0f, 1.0f, 0.0f), m_vecDirection);
			Vector trackNormal = Vector.Cross (m_vecDirection, trackTangent);
			
			Vector p = Vector.Add3v (m_vecNextDestination, Vector.Multiply3v (trackNormal, m_fFlyHeight));
			
			setDirection (Vector.Subtract3v (p, m_vecPosition));
		}
	}
}