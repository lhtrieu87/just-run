/**
 * @author Le Hong Trieu
 */

package UserCustomization.Character;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.SpriteAnimator;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.SpriteMesh;
import UserCustomization.Configuration.GameResource;
import UserCustomization.Configuration.WorldMap;

public class Vampire extends Character 
{
	public Vampire (Camera camera, Lightning light, WorldMap wayPoint, boolean controllable, int initialWaypoint,
			int initialLane)
	{
		super (CharacterType.VAMPIRE, wayPoint, controllable, camera, light, initialWaypoint, initialLane);
		
		m_fSpeed = GameResource.INITIAL_CHARACTER_SPEED;
		
		m_objMesh = GameResource.VAMPIRE_MESH;
		m_objMesh.setRenderingType (GraphicsAPI.SupportType.Triangles);
		
		m_objMesh.setCamera (camera);
		m_objMesh.setMaterial (new Material (new Vector (0.973f, 0.5f, 0.106f),
											 new Vector (0.5f, 0.5f, 0.5f),
											 new Vector (0.1f, 0.1f, 0.1f),
											 100));
		m_objMesh.setLightning (light);
		m_objMesh.setTextureID(GameResource.VAMPIRE_TEXTURE);
		m_objMesh.setTextureEnable (true);
		
		m_health = m_totalHealth = 100;
		
		m_skillState = SkillState.READY;
		m_activePeriod = 0;
		m_coolingPeriod = 1000 * 10;
		
		/*
		 * Edited by Han Lin on V0.5
		 */
		m_objAnimator = new SpriteAnimator((SpriteMesh)m_objMesh.getMesh());
		/*
		 * End edited
		 */
	}
	
	@Override
	protected void skillOnActive () 
	{
		m_health += m_totalHealth * 0.25;
		if (m_health >= m_totalHealth)
			m_health = m_totalHealth;
	}

	@Override
	protected void skillOnCooling () 
	{
		
	}
}
