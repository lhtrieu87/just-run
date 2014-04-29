/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package UserCustomization.Environment;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.TriangleMesh;
import UserCustomization.Configuration.GameResource;

public class Sky  extends GameObject{
	
	public Sky(Camera camera, TriangleMesh mesh)
	{
		m_objMesh = mesh;
		m_objMesh.setRenderingType (GraphicsAPI.SupportType.Triangles);
		m_objMesh.setCamera(camera);
		//m_objMesh.setShader (Resource.PHONG_SHADER);
		m_objMesh.setTextureID(GameResource.SKY_TEXTURE);
		m_objMesh.setTextureColorOnlyEnable(true);
		m_objMesh.setTextureEnable (true);
	}
	
	@Override
	public Type getType() {
		return Type.UserDefined;
	}

	@Override
	public void setType(Type type) { }

	@Override
	public void Update(long currentTime) { }
	
	@Override
	public void Render()
	{
		ResetTransformation();
		Translate(m_vecPosition);
		Scale(new Vector(100,100,100));
		super.Render();
	}
}
