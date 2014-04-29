package UserCustomization.Environment;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.Line2D;
import JUST_RUN.RendererEngine.Drawing.MeshObject;

public class MiniMap  extends GameObject{
	private Line2D m_objLine;
	
	public MiniMap(Camera camera, MeshObject mesh, Vector color)
	{
		m_objLine = new Line2D(camera, mesh, color);
		m_objMesh.setRenderingType(GraphicsAPI.SupportType.Lines);
		m_objMesh.setCamera(camera);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(Type type) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void Update(long currentTime) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void Render ()
	{
		m_objLine.ResetTransMatrix();
		Translate (m_vecPosition);
		m_objLine.Render();
	}
	
	
}
