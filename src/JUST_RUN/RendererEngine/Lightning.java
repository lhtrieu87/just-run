/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.RendererEngine;

import JUST_RUN.RendererEngine.Vector;

public class Lightning {
	private Vector m_objPosition, m_objWeight;
	private Material m_objMaterial;
	
	public Lightning()
	{ }
	
	public Lightning(Vector position, Material material)
	{
		this(position, null, material);
	}
	
	public Lightning(Vector position, Vector weight, Material material)
	{
		m_objPosition = position;
		m_objMaterial = material;
		m_objWeight = weight;
	}
	
	public void setLightWeight(Vector weight)
	{
		m_objWeight = weight;
	}
	
	public float[] getLightWeight()
	{
		return m_objWeight.getArray3v();
	}
	
	public Material getMaterial()
	{
		return m_objMaterial;
	}
	
	public void setMaterial(Material material)
	{
		m_objMaterial = material;
	}
	
	public float[] getAmbient()
	{
		return m_objMaterial.getAmbient().getArray4v();
	}
	
	public float[] getDiffuse()
	{
		return m_objMaterial.getDiffuse().getArray4v();
	}
	
	public float[] getSpecular()
	{
		return m_objMaterial.getSpecular().getArray4v();
	}
	
	public void setPosition(Vector vec)
	{
		m_objPosition = vec;
	}
	
	public float[] getPosition()
	{
		return m_objPosition.getArray4v();
	}
}
