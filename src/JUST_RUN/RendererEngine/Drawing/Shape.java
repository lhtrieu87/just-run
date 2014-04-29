/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.2 -- adding several abstract methods
 */
package JUST_RUN.RendererEngine.Drawing;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Vector;

public abstract class Shape {
	protected float[] m_fVMatrix = new float[16]; // View matrix
	protected float[] m_TransformMatrix = new float[16]; // transformation matrix
	
	public abstract void Render();
	public abstract Camera getCamera();
	public abstract Shader getShader();
	public abstract Lightning getLightning();
	public abstract Material getMaterial();
	public abstract int getTextureID();
	
	public abstract void setCamera(Camera camera);
	public abstract void setShader(Shader shader);
	public abstract void setLightning(Lightning lightning);
	public abstract void setMaterial(Material material);
	public abstract void setTextureID(int textureID);
	
	// new edit for v0.2
	public abstract GraphicsAPI.SupportType getRenderingType();
	public abstract FloatBuffer getVertexBuffer();
	public abstract ShortBuffer getIndexBuffer();
	public abstract int getTotal_F_Components();
	public abstract boolean getTextureEnable();
	public abstract MeshObject getMesh();
	public abstract void setMesh(MeshObject mesh);
	public abstract void setRenderingType(GraphicsAPI.SupportType type);
	public abstract void setTextureEnable(boolean value);
	public final void ResetViewMatrix()
	{
		this.getCamera().initCameraPosition(m_fVMatrix);
		Camera.setIdentity(m_TransformMatrix);
	}
	public void Transform(float[] m_fTransform)
	{
		Camera.setViewTransform(m_fVMatrix, m_fTransform);
	}
	public void Rotate(float angle_inDegree, Vector axis)
	{
		Camera.setViewRotation(m_fVMatrix, angle_inDegree, axis);
		Camera.setViewRotation(m_TransformMatrix, angle_inDegree, axis);
	}
	public void Translate(Vector position)
	{
		Camera.setViewTranslation(m_fVMatrix, position);
		Camera.setViewTranslation(m_TransformMatrix, position);
	}
	public void Scale(Vector scale)
	{
		Camera.setViewScaling(m_fVMatrix, scale);
		Camera.setViewScaling(m_TransformMatrix, scale);
	}
	// end edit
	
	public abstract boolean getTextureColorOnlyEnable();
	public abstract void setTextureColorOnlyEnable(boolean value);
}
