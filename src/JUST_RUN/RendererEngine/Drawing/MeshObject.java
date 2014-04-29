/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 */
package JUST_RUN.RendererEngine.Drawing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshObject {
	/*
	 * FloatBuffer and ShortBuffer objects can't pass to other class as reference via parameter argument
	 * because it contains references of the object that putting into the buffer. Since these two buffer contains
	 * the memory reference of the ArrayList objects (m_objVertices), thus, there must created in this class or any 
	 * other classes that contain the original reference.
	 */
	protected FloatBuffer m_objVertexBuffer, m_objTextureBuffer, m_objNormalBuffer;
	protected ShortBuffer m_objIndexBuffer;
	protected float[] m_fVertices;
	protected boolean m_bHasNormal, m_bHasTexture;
	protected int m_intFcomponent;
	
	public MeshObject(float[] vertices, float[] normal, float[] texture, short[] indices, int Fcomponents)
	{
		try
		{
			m_intFcomponent = Fcomponents;
			m_fVertices = vertices;
			ComputeBuffer(vertices, normal, texture, indices);
		}catch(Exception e){ }
	}
	
	public MeshObject(){ };
	
	public final int getTotal_F_Components()
	{
		return m_intFcomponent;
	}
	
	// modifable function
	public float[] getVertices()
	{
		return m_fVertices;
	}
	
	// Fixed functions
	public final FloatBuffer getVertexBuffer()
	{
		return m_objVertexBuffer;
	}
	
	public final FloatBuffer getNormalBuffer()
	{
		return m_objNormalBuffer;
	}
	
	public final FloatBuffer getTextureBuffer()
	{
		return m_objTextureBuffer;
	}
	
	public final ShortBuffer getIndexBuffer()
	{
		return m_objIndexBuffer;
	}
	
	public final boolean hasNormalVector()
	{
		return m_bHasNormal;
	}
	
	public final boolean hasTextureCoordinate()
	{
		return m_bHasTexture;
	}
	
	public final void setBuffer(FloatBuffer vertices, FloatBuffer normal)
	{
		m_objVertexBuffer = vertices;
		m_objNormalBuffer = normal;
	}
	
	public final void ComputeBuffer(float[] vertices, float[] normal, float[] texture, short[] indices) //throws Exception
	{
		if(vertices != null)
		{
			// generate vertex buffer for OpenGL rendering
		    m_objVertexBuffer = ByteBuffer.allocateDirect(
		    		vertices.length * 4 // allocate 4 bytes for float data type
		    		).order(ByteOrder.nativeOrder()).asFloatBuffer();
		    m_objVertexBuffer.put(vertices);
		    m_objVertexBuffer.position(0);
		}
	    
	    if(indices != null)
	    {
		    // generate index buffer for OpenGL indexing
		    m_objIndexBuffer = ByteBuffer.allocateDirect(
		    		indices.length * 2 // allocate 2 bytes for short data type
		    		).order(ByteOrder.nativeOrder()).asShortBuffer();
		    m_objIndexBuffer.put(indices);
		    m_objIndexBuffer.position(0);
	    }
		
		if(normal == null)
		{
			m_bHasNormal = false;
		}
		else
		{
			// generate vertex's normal buffer for OpenGL rendering
		    m_objNormalBuffer = ByteBuffer.allocateDirect(
		    		normal.length * 4 // allocate 4 bytes for float data type
		    		).order(ByteOrder.nativeOrder()).asFloatBuffer();
		    m_objNormalBuffer.put(normal);
		    m_objNormalBuffer.position(0);
		}
		
		if(texture == null)
		{
			m_bHasTexture = false;
		}
		else
		{
			// generate vertex's texture coordinate buffer for OpenGL rendering
		    m_objTextureBuffer = ByteBuffer.allocateDirect(
		    		texture.length * 4 // allocate 4 bytes for float data type
		    		).order(ByteOrder.nativeOrder()).asFloatBuffer();
		    m_objTextureBuffer.put(texture);
		    m_objTextureBuffer.position(0);
		}
	}
}
