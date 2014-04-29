package UserCustomization.Character;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import UserCustomization.Configuration.GameResource;
import UserCustomization.Configuration.WorldMap;

public class AncientGuy extends Character
{
	private float m_fSlowRate = 0.25f;
	
	public AncientGuy (Camera camera, Lightning light, WorldMap wayPoint, boolean controllable, int initialWaypoint,
			int initialLane)
	{
		super (CharacterType.ANCIENT_GUY, wayPoint, controllable, camera, light, initialWaypoint, initialLane);
		
		m_objMesh = GameResource.ANCIENT_GUY_MESH;
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
		m_activePeriod = 1000 * 10;
		m_coolingPeriod = 7500;
	}
	
	@Override
	protected void skillOnActive () 
	{
		m_fSpeed *= m_fSlowRate;
	}
	
	@Override
	protected void skillOnCooling () 
	{
		// Not fair to other characters.
		// The current speed should be set to the uniform speed,
		// which applies to all players at the current time.
		// This uniform speed is increased over time to ensure
		// the game will end.
		m_fSpeed /= m_fSlowRate;
	}
}
