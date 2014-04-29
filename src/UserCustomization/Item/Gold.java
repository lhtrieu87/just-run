package UserCustomization.Item;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import UserCustomization.Character.Character.CharacterType;
import UserCustomization.Configuration.GameResource;

public class Gold extends Item 
{
	protected int m_value;
	
	public Gold (Camera camera, Lightning light) 
	{
		super (camera, light, GameResource.GOLD_MESH);
		setValue (GameResource.GOLD_VALUE_1);
		
		m_objMesh.setMaterial (new Material (SystemColor.Gray,
											 SystemColor.GhostWhite,
											 SystemColor.White,
				 			   				 200));
		
		m_objMesh.setTextureID(GameResource.DIAMOND_TEXTURE);
		m_objMesh.setTextureEnable(true);
	}
	
	@Override
	public void setValue (int value)
	{
		m_value = value;
	}
	
	@Override
	public int getValue ()
	{
		return m_value;
	}

	@Override
	public int getDamage (CharacterType characterType) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Render()
	{
		m_objMesh.ResetViewMatrix();
		m_objMesh.Translate(m_vecPosition);
		m_objMesh.Scale(new Vector(0.5f, 0.5f, 0.5f));
		m_objMesh.Render();
	}
}
