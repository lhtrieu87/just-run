/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.2 -- removing the OBJ Format reader
 */
package JUST_RUN.RendererEngine.Drawing;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.Resource.ResourceManager;

public class TriangleMesh extends Shape {
	private boolean m_bTexture, m_bTextureColor;
	
	private Shader m_objShader = /*ResourceManager.GOURAUD_SHADER;*/ResourceManager.PHONG_SHADER;
	private Camera m_objCamera;
	private int m_intTextureID;
	private Lightning m_objLightning;
	private Material m_objMaterial;
	
	private MeshObject m_objMesh;
	
	private GraphicsAPI.SupportType m_objRenderType;
	
	public TriangleMesh(MeshObject mesh)
	{
		this.m_objMesh = mesh;
	}
	
	@Override
	public GraphicsAPI.SupportType getRenderingType() {
		return m_objRenderType;
	}

	@Override
	public void setRenderingType(GraphicsAPI.SupportType type) {
		m_objRenderType = type;
	}
	
	@Override
	public FloatBuffer getVertexBuffer()
	{
		return m_objMesh.getVertexBuffer();
	}
	
	@Override
	public ShortBuffer getIndexBuffer()
	{
		return m_objMesh.getIndexBuffer();
	}
	
	@Override
	public int getTotal_F_Components()
	{
		return m_objMesh.getTotal_F_Components();
	}
	
	@Override
	public boolean getTextureEnable()
	{
		return m_bTexture;
	}
	
	@Override
	public void setTextureEnable(boolean value)
	{
		m_bTexture = value;
	}
	
	@Override
	public boolean getTextureColorOnlyEnable()
	{
		return m_bTextureColor;
	}
	
	@Override
	public void setTextureColorOnlyEnable(boolean value)
	{
		m_bTextureColor = value;
	}

	@Override
	public void setMesh(MeshObject mesh)
	{
		m_objMesh = mesh;
	}
	
	@Override
	public MeshObject getMesh()
	{
		return m_objMesh;
	}
	
	@Override
	public Camera getCamera()
	{
		return m_objCamera;
	}
	
	@Override
	public void setCamera(Camera camera)
	{
		this.m_objCamera = camera;
	}

	@Override
	public Shader getShader()
	{
		return m_objShader;
	}
	
	@Override
	public void setShader(Shader shader)
	{
		this.m_objShader = shader;
	}
	
	@Override 
	public Lightning getLightning()
	{
		return m_objLightning;
	}
	
	@Override
	public void setLightning(Lightning lightning)
	{
		this.m_objLightning = lightning;
	}
	
	@Override 
	public Material getMaterial()
	{
		return m_objMaterial;
	}
	
	@Override
	public void setMaterial(Material material)
	{
		this.m_objMaterial = material;
	}
	
	@Override 
	public int getTextureID()
	{
		return m_intTextureID;
	}
	
	@Override
	public void setTextureID(int texture)
	{
		m_intTextureID = texture;
	}
	
	@Override
	public void Render()
	{
		if(m_objShader == null)
			return;
		int shader = m_objShader.getShaderProgramHandle();
		GraphicsAPI.setGraphicsShader(shader);
		this.m_objCamera.computeModelViewProjectionMatrix(m_fVMatrix);
		/*
		 * Set up Model-View Projection
		 */
		GraphicsAPI.setShaderModelViewProjection(
				m_objCamera.getModelViewProjectionMatrix(), shader);
		/*
		 * Set up Model-View
		 */
		GraphicsAPI.setShaderModelView(m_fVMatrix, shader);
		/*
		 * Set up Lightning
		 */
		if(m_objLightning != null)
			GraphicsAPI.setLightning(m_objLightning, shader);
		/*
		 * Set up Material
		 */
		if(m_objMaterial != null)
			GraphicsAPI.setMaterial(m_objMaterial, shader);
		/*
		 * Render Triangle Mesh
		 */
		GraphicsAPI.renderTriangle(m_objRenderType, shader, m_objMesh.getTotal_F_Components(), m_objMesh.getIndexBuffer(),
				m_objMesh.getVertexBuffer(), m_objMesh.getTextureBuffer(), m_objMesh.getNormalBuffer(),
				m_intTextureID, m_bTexture, m_bTextureColor);
	}
}
