/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 */
package JUST_RUN.RendererEngine.Drawing;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.Resource.ResourceManager;

public class Line3D extends Shape {
	private Shader m_objShader = ResourceManager.SIMPLE_SHADER; // default shader
	private Camera m_objCamera;
	private Vector m_objColor;
	
	private MeshObject m_objMesh;
	
	public Line3D(MeshObject mesh, Vector color)
	{
		this.m_objMesh = mesh;
		this.m_objColor = color;
	}
	
	public final void setColor(Vector color)
	{
		this.m_objColor = color;
	}
	
	public final Vector getColor()
	{
		return m_objColor;
	}
	
	@Override
	public void Render() {
		int shader = m_objShader.getShaderProgramHandle();
		GraphicsAPI.setGraphicsShader(shader);
		this.m_objCamera.computeModelViewProjectionMatrix(m_fVMatrix);
		/*
		 * Set up Model-View Projection
		 */
		GraphicsAPI.setShaderModelViewProjection(
				m_objCamera.getModelViewProjectionMatrix(), shader);
		/*
		 * Render Lines
		 */
		GraphicsAPI.renderLines(shader, m_objMesh.getTotal_F_Components(), m_objMesh.getIndexBuffer(),
				m_objMesh.getVertexBuffer(), m_objColor);
	}

	@Override
	public Camera getCamera() {
		return m_objCamera;
	}

	@Override
	public Shader getShader() {
		return m_objShader;
	}

	@Override
	public Lightning getLightning() {
		return null;
	}

	@Override
	public Material getMaterial() {
		return null;
	}

	@Override
	public int getTextureID() {
		return -1;
	}

	@Override
	public void setCamera(Camera camera) {
		this.m_objCamera = camera;
	}

	@Override
	public void setShader(Shader shader) {
		this.m_objShader = shader;
	}

	@Override
	public void setLightning(Lightning lightning) { }

	@Override
	public void setMaterial(Material material) { }

	@Override
	public void setTextureID(int texture) { }

	@Override
	public GraphicsAPI.SupportType getRenderingType() {
		return GraphicsAPI.SupportType.Lines;
	}

	@Override
	public FloatBuffer getVertexBuffer() {
		return m_objMesh.getVertexBuffer();
	}

	@Override
	public ShortBuffer getIndexBuffer() {
		return m_objMesh.getIndexBuffer();
	}

	@Override
	public int getTotal_F_Components() {
		return m_objMesh.getTotal_F_Components();
	}

	@Override
	public boolean getTextureEnable() {
		return false;
	}
	
	@Override
	public boolean getTextureColorOnlyEnable()
	{
		return false;
	}
	
	@Override
	public MeshObject getMesh()
	{
		return m_objMesh;
	}
	
	@Override
	public void setMesh(MeshObject mesh) {
		m_objMesh = mesh;
	}

	@Override
	public void setRenderingType(GraphicsAPI.SupportType type) { }

	@Override
	public void setTextureEnable(boolean value) { }
	
	@Override
	public void setTextureColorOnlyEnable(boolean value){ }
}
