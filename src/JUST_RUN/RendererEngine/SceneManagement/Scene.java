/**
 * Created by Han Lin on v0.1
 * Edited : Han Lin on v0.3 -- added Refresh function
 */
package JUST_RUN.RendererEngine.SceneManagement;

import java.util.ArrayList;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameSubSystem.GameObject;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.RendererEngine.Vector;

public abstract class Scene {
	protected ArrayList<GameObject> m_objGameObject = new ArrayList<GameObject>();
	
	public abstract void CreateScene();
	
	public abstract void FrameStart(); // do things that before rendering scene
	public abstract void FrameEnd(); // do things that after rendering scene
	
	public final void addGameObject(GameObject object)
	{
		m_objGameObject.add(object);
	}
	
	public final ArrayList<GameObject> getGameObjects()
	{
		return m_objGameObject;
	}
	
	protected static void Refresh(Vector color)
	{
		GraphicsAPI.clearBuffer(color);
	}
	
	public abstract void HandleInteraction (Event<Vector> e, long currentTime);
	
	public abstract void Update (long currentTime);
}
