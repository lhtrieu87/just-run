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
import JUST_RUN.RendererEngine.Drawing.Line3D;
import JUST_RUN.RendererEngine.Drawing.MeshObject;

public class TestingLines extends GameObject{

	public TestingLines(Camera camera, MeshObject mesh, Vector color)
	{
		m_objMesh = new Line3D(mesh, color);
		m_objMesh.setRenderingType(GraphicsAPI.SupportType.Lines);
		m_objMesh.setCamera(camera);
		
		m_vecPosition = new Vector (0, 0.0f, 0);
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
	public void Render ()
	{
		ResetTransformation ();
		Translate (m_vecPosition);
		super.Render();
	}
}
