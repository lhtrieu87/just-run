/**
 * @author Han Lin
 * Created by Han Lin on v0.3
 * Edited :
 */
package UserCustomization.Environment;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.TriangleMesh;
import UserCustomization.Configuration.GameResource;

public class Landscape extends GameObject
{
	public Landscape (Camera camera, Lightning light, TriangleMesh mesh)
	{
		m_objMesh = mesh;
		m_objMesh.setRenderingType (GraphicsAPI.SupportType.Triangles);
		
		m_objMesh.setCamera (camera);

		m_objMesh.setMaterial (new Material (new Vector (0.05f, 0.04f, 0.04f),
				                             new Vector (0.2f, 0.19f, 0.18f),
				                             new Vector (0.0f, 0.0f, 0.0f),
				                             20));
		m_objMesh.setLightning (light);	
		m_objMesh.setTextureID(GameResource.LANDSCAPE_TEXTURE);
		m_objMesh.setTextureEnable (true);
	}
	
	@Override
	public Type getType() 
	{
		return Type.UserDefined;
	}

	@Override
	public void setType(Type type) { }

	@Override
	public void Update(long currentTime) {}
	
	@Override
	public void Render ()
	{
		ResetTransformation ();
		Translate (m_vecPosition);
		super.Render();
	}

}
