/**
 * @author Le Hong Trieu
 */

package UserCustomization.Item;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Drawing.TriangleMesh;
import UserCustomization.Character.Character.CharacterType;

public abstract class BuildableItem extends Item
{
	public enum BuildableItemType
	{
		ROCK,
		POISON,
		CONFUSION,
		MINE
	}
	
	protected int m_price;
	protected int m_damagePoints;
	
	protected CharacterType m_cheaperPriceFor;
	protected int m_cheapPrice;
	
	protected CharacterType m_moreDamageFor;
	protected int m_moreDamagePoints;

	public BuildableItem (Camera camera, Lightning light, TriangleMesh mesh)
	{
		super (camera, light, mesh);
	}

	public int GetPrice (CharacterType characterType)
	{
		if (characterType == m_cheaperPriceFor)
			return m_cheapPrice;
		return m_price;
	}
	
	@Override
	public int getDamage (CharacterType characterType) 
	{
		if (characterType == m_moreDamageFor)
			return m_moreDamagePoints;
		return m_damagePoints;
	}
	
	@Override
	public void setDamage(int damage)
	{ 
		m_damagePoints = damage;
	}
	
	

	@Override
	public void Update(long currentTime)
	{

	}
	
	

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(int value) {
		// TODO Auto-generated method stub
		
	}
	
	public long GetWaiveTime()
	{
		return 0;
	} 
}
