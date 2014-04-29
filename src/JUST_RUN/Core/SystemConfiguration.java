/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Million on v0.1 -- Plug-in the HCI API
 * 		    by Han Lin on v0.2 -- from interface to abstract class
 */
package JUST_RUN.Core;

import JUST_RUN.HCI.HCI;
import JUST_RUN.RendererEngine.SceneManagement.Scene;

/**
 * Configuration interface for core function components
 */
public abstract class SystemConfiguration
{
	// Edit on v0.3 - v0.4
	private static HCI m_objHCI;
	
	public static final HCI getHCIHandle()
	{
		return m_objHCI;
	}
	
	public static final void setHCI(HCI handle)
	{
		m_objHCI = handle;
	}
	/**
	 * Interface for Resource Manager. This handle will be called once when the 
	 * system have been initialized. Note that this handle runs on separate thread.
	 */
	public abstract void Resource_Task_Load() throws Exception;
	/**
	 * Interface for Resource Manager. This handle will be returned when all the 
	 * resources have been successfully loaded. Note that this handle runs on separate thread.
	 */
	public abstract void Resource_Task_Done() throws Exception;
	
	/**
	 * Interface for system initialzation. Subclass should implement it.
	 * Furthermore, any customized initialization should implement here. 
	 */
	public abstract void StartUpInitialization();
	
	// end edit
	
	public abstract void RenderEngine_onSurfaceCreated();
	public abstract void RenderEngine_onSurfaceChanged(int width, int height);
	
	// Edit on v0.2
	/**
	 * Get the instance of a specific game scene.
	 */
	public abstract Scene getScene();
	// end edit
	
	/**
	 * Call when the rendering thread is paused.
	 */
	public abstract void Exiting();
	/**
	 * Call when the rendering thread is resumed.
	 */
	public abstract void Entering();
}
