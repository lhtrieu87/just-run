/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.RendererEngine;

import JUST_RUN.RendererEngine.Vector;;

public class Material {

	private Vector m_objAmbient, m_objDiffuse, m_objSpecular;
	private float m_fShineness;
		
	public Material()
	{ }
	
	public Material(Vector ambient, Vector diffuse, Vector specular, float shineness)
	{
		m_objAmbient = ambient;
		m_objDiffuse = diffuse;
		m_objSpecular = specular;
		m_fShineness = shineness;
	}
	
	public Vector getAmbient()
	{
		return m_objAmbient;
	}
	
	public Vector getDiffuse()
	{
		return m_objDiffuse;
	}
	
	public Vector getSpecular()
	{
		return m_objSpecular;
	}
	
	public float getShineness()
	{
		return m_fShineness;
	}
	
	public void setAmbient(Vector vec)
	{
		m_objAmbient = vec;
	}
	
	public void setDiffuse(Vector vec)
	{
		m_objDiffuse = vec;
	}
	
	public void setSpecular(Vector vec)
	{
		m_objSpecular = vec;
	}
	
	public void setShineness(float value)
	{
		m_fShineness = value;
	}
}
