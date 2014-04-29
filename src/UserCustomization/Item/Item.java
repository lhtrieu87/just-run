/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 * Edited : 
 */
package UserCustomization.Item;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.TriangleMesh;
import UserCustomization.Character.Character;
import UserCustomization.Character.Character.CharacterType;

/*
 * Game-specific object API
 */
public abstract class Item extends GameObject {
	
	protected int m_lane;
	protected int m_wayPoint;
	protected boolean m_dead = false;
	@Override
	public final Type getType()
	{
		return Type.Item;
	}
	
	@Override
	public final void setType(Type type)
	{ }
	
	public void setLane(int lane)
	{
		m_lane = lane;
	}
	
	public int getLane()
	{
		return m_lane;
	}
	
	public void setWayPoint (int wayPoint)
	{
		m_wayPoint = wayPoint;
	}
	
	public int getWayPoint ()
	{
		return m_wayPoint;
	}
	
	public boolean isDead ()
	{
		return m_dead;
	}
	
	public Item (Camera camera, Lightning light, TriangleMesh mesh)
	{
		m_objMesh = mesh;
		m_objMesh.setRenderingType (GraphicsAPI.SupportType.Triangles);
		
		m_objMesh.setCamera (camera);
		m_objMesh.setMaterial (new Material (new Vector (0.765f, 0.557f, 0.78f),
											 new Vector (0.3f, 0.7f, 0.2f),
											 new Vector (0.6f, 0.5f, 0.4f),
											 20));
		m_objMesh.setLightning (light);
		m_objMesh.setTextureEnable (false);
	}

	public abstract int getDamage(CharacterType characterType);
	public abstract int getValue();
	public abstract void setDamage(int damage);
	public abstract void setValue(int value);
	// Activate side effects beside health reduction.
	public void Activate(Character character)
	{
		character.ReduceHealth(this.getDamage(character.getCharacterType()));
		character.IncreaseGold(this.getValue());
		m_dead = true;
	}
	
	@Override
	public void Update (long currentTime) 
	{ }

	@Override
	public void Render ()
	{
		ResetTransformation ();
		Translate (m_vecPosition);
		super.Render ();
	}
}
