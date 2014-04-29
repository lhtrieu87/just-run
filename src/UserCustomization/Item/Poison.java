/**
 * @author Le Hong Trieu
 */
package UserCustomization.Item;

import JUST_RUN.GameplaySystem.Clock;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import UserCustomization.Character.Character;
import UserCustomization.Configuration.GameResource;

public class Poison extends BuildableItem
{
	protected long m_activeTime; // In millisecond. 
	protected long m_waiveTime;
	
	public Poison (Camera camera, Lightning light)
	{
		super (camera, light, GameResource.ROCK_MESH);
		
		m_price = GameResource.POISON_PRICE;
		m_damagePoints = GameResource.POISON_DAMAGE_POINT;
		m_dead = true;
		
		m_cheaperPriceFor = null;
		m_cheapPrice = GameResource.POISON_CHEAPER_PRICE;
		
		m_moreDamageFor = null;
		m_moreDamagePoints = GameResource.POISON_MORE_DAMAGE_POINT;
		
		m_objMesh.setMaterial (new Material (SystemColor.Gray,
											 SystemColor.Brown,
											 SystemColor.Black,
   				 							 0));
		m_objMesh.setTextureID(GameResource.ROCK_TEXTURE);
		m_objMesh.setTextureEnable(true);
		
		m_activeTime = GameResource.POISON_ACTIVE_TIME;
	}
	
	// testing method, it should be removed when the proper texture of rock has done.
	@Override
	public void Render()
	{
		m_objMesh.ResetViewMatrix();
		m_objMesh.Translate(m_vecPosition);
		m_objMesh.Render();
	}
	
	public long GetWaiveTime()
	{
		return m_waiveTime;
	}
	
	@Override
	public void Activate(Character character) 
	{
		m_waiveTime = Clock.GetCurrentTime_Milli() + m_activeTime;
		character.ApplyEffect(this);
		m_dead = true;
	}
}
