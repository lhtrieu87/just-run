package JUST_RUN.RendererEngine.Drawing;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.Core.GraphicsAPI.SupportType;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.Resource.ResourceManager;

public class Line2D  extends Shape {
	private Shader m_objShader = ResourceManager.SIMPLE_SHADER; // default shader
	private Camera m_objCamera;
	private Vector m_objColor;
	private MeshObject m_objMesh;
	
	public Line2D(Camera camera, MeshObject mesh, Vector color)
	{
		m_objCamera = camera;
		m_objColor = color;
		m_objMesh = mesh;
	}
	
	public final void ResetTransMatrix()
	{
		Camera.setIdentity(m_fVMatrix);
	}
	
	@Override
	public void setCamera(Camera camera) {
		m_objCamera = camera;
	}

	@Override
	public void setShader(Shader shader) {
		m_objShader = shader;
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
		return 0;
	}

	@Override
	public void setLightning(Lightning lightning) {}

	@Override
	public void setMaterial(Material material) {}

	@Override
	public void setTextureID(int textureID) {}

	@Override
	public SupportType getRenderingType() {
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
	public MeshObject getMesh() {
		return m_objMesh;
	}

	@Override
	public void setMesh(MeshObject mesh) {
		m_objMesh = mesh;
	}

	@Override
	public void setRenderingType(SupportType type) {}

	@Override
	public void setTextureEnable(boolean value) {}

	@Override
	public boolean getTextureColorOnlyEnable() {
		return false;
	}

	@Override
	public void setTextureColorOnlyEnable(boolean value) {}

}
