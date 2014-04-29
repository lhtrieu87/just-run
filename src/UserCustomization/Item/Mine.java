package UserCustomization.Item;

import android.util.Log;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import UserCustomization.Character.Character;
import UserCustomization.Character.Character.CharacterType;
import UserCustomization.Configuration.GameResource;

public class Mine extends BuildableItem
{
	protected boolean m_active = false;
	
	public Mine(Camera camera, Lightning light)
	{
		super (camera, light, GameResource.MINE_MESH);
		
		m_price = GameResource.MINE_PRICE;
		m_damagePoints = GameResource.MINE_DAMAGE_POINT;
		m_dead = true;
		
		m_cheaperPriceFor = null;
		m_cheapPrice = GameResource.MINE_CHEAPER_PRICE;
		
		m_moreDamageFor = CharacterType.ANCIENT_GUY;
		m_moreDamagePoints = GameResource.MINE_MORE_DAMAGE_POINT;
		
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

	@Override
	public void Activate(Character character) 
	{
		if(m_active)
		{
			character.ReduceHealth(this.getDamage(character.getCharacterType()));
			m_dead = true;
			Log.e("*********BOMB***********", "*********BOMB***********" + character.getPrevWayPointIndex() + "   " + character.getNextWayPoint() + "   " + this.m_wayPoint);
		}
		else
		{
			Log.e("*********TRIGGER MINE***********", "*********TRIGGER MINE***********" + character.getPrevWayPointIndex() + "   " + character.getNextWayPoint() + "   " + this.m_wayPoint);
			// Should not check collision now. 
			// Otherwise, the mine explodes immediately after being triggered.
			// When the character reaches its next way point, the collision is automatically
			// turned on.
			character.SetCollideCheck(false);
			m_dead = false;
		}
		
		m_active = true;
	}
}
