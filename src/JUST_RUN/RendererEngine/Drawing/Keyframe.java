/**
 * @author Han Lin
 * Created by Han Lin on v0.5
 */
package JUST_RUN.RendererEngine.Drawing;

public class Keyframe extends MeshObject
{
	private float[] m_fVertex, m_fNormal; // vertices coordinate and its normal coordinate
	private int m_intIndex; // index of the keyframe
	
	public Keyframe(int index, float[] vertices, float[] normal) throws Exception
	{
		if(vertices.length != normal.length)
			throw new Exception("Error at Keyframe.class: The number of vertices and normals does not match.");
		if(index < 0)
			throw new Exception("Error at Keyframe.class: The index of keyframe cannot be zero.");
		m_intIndex = index;
		m_fVertex = vertices;
		m_fNormal = normal;
		this.ComputeBuffer(vertices, normal, null, null);
	}
	
	public int getNumberOfVertices()
	{
		return m_fVertex.length;
	}
	
	public int Index()
	{
		return m_intIndex;
	}
	
	public float[] getVertices()
	{
		return m_fVertex;
	}
	
	public float[] getNormals()
	{
		return m_fNormal;
	}
}