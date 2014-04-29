/**
 * @author Le Hong Trieu
 */
package UserCustomization.Item;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import UserCustomization.Character.Character.CharacterType;
import UserCustomization.Configuration.GameResource;

public class Rock extends BuildableItem
{
	public Rock (Camera camera, Lightning light)
	{
		super (camera, light, GameResource.ROCK_MESH);
		
		m_price = GameResource.ROCK_PRICE;
		m_damagePoints = GameResource.ROCK_DAMAGE_POINT;
		m_dead = true;
		
		m_cheaperPriceFor = CharacterType.ANCIENT_GUY;
		m_cheapPrice = GameResource.ROCK_CHEAPER_PRICE;
		
		m_moreDamageFor = CharacterType.VAMPIRE;
		m_moreDamagePoints = GameResource.ROCK_MORE_DAMAGE_POINT;
		
		m_objMesh.setMaterial (new Material (SystemColor.Gray,
											 SystemColor.Brown,
											 SystemColor.Black,
   				 							 0));
		m_objMesh.setTextureID(GameResource.ROCK_TEXTURE);
		m_objMesh.setTextureEnable(true);
	}
	
	// testing method, it should be removed when the proper texture of rock has done.
	@Override
	public void Render()
	{
		m_objMesh.ResetViewMatrix();
		m_objMesh.Translate(m_vecPosition);
		m_objMesh.Render();
	}
}
