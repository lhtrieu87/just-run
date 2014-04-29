/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 * Edited :
 */
package JUST_RUN.GameSubSystem;

import JUST_RUN.RendererEngine.Drawing.Shape;
import JUST_RUN.RendererEngine.Vector;

/*
 * Universal Game Object API
 */
public abstract class GameObject {
	protected Vector m_vecPosition = new Vector();
	protected Shape m_objMesh;
	
	public static enum Type
	{
		Character, // define this object is a game character
		Item, // define this object is a game item
		UserDefined // customise this object for other purpose
	}
	
	// define this game object shape
	public final Shape getShape ()
	{
		return m_objMesh;
	}
	public final void setShape(Shape shape)
	{
		m_objMesh = shape;
	}
	// define this game object type
	public abstract Type getType(); 
	public abstract void setType(Type type); 
	
	// define this game object position
	public final Vector getPosition()
	{
		return m_vecPosition;
	}
	
	public void setPosition(Vector vector)
	{
		//m_objPreviousLoc = m_vecPosition;
		m_vecPosition = vector;
	}
	
	public void Transform(float[] m_fTransform)
	{
		this.getShape().Transform(m_fTransform);
	}
	
	public void Rotate(float angle, Vector axis)
	{
		this.getShape().Rotate(angle, axis);
	}
	
	public void Scale(Vector scale)
	{
		this.getShape().Scale(scale);
	}
	
	public void Translate(Vector position)
	{
		this.getShape().Translate(position);
	}
	
	public final void ResetTransformation()
	{
		this.getShape().ResetViewMatrix();
	}
	public void Render()
	{
		this.getShape().Render();
	}
	
	public abstract void Update(long currentTime);
	
}
