/**
 * @author Han Lin
 * Created by Han Lin on v0.2
 * Edited :
 */
package JUST_RUN.Resource;

import JUST_RUN.RendererEngine.Drawing.MeshObject;

public abstract class GraphicFileReader {
	public abstract void Load() throws Exception; // load the reading process
	
	/*protected ArrayList<MeshObject> m_objMesh;
	
	public final ArrayList<MeshObject> getMeshObjects()
	{
		return m_objMesh;
	}*/
	
	protected MeshObject m_objMesh;
	
	public final MeshObject getMeshObject()
	{
		return m_objMesh;
	}
}
