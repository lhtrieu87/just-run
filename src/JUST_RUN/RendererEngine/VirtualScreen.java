/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.3 -- change to singleton design
 */
package JUST_RUN.RendererEngine;

import JUST_RUN.Core.GraphicsAPI;;

public class VirtualScreen {
	
	private static int m_intScreenWidth = -1, m_intScreenHeight = -1;
	private static VirtualScreen m_objHandle;
	
	private VirtualScreen()
	{}
	
	public static VirtualScreen getHandle()
	{
		if(m_objHandle == null)
			m_objHandle = new VirtualScreen();
		return m_objHandle;
	}
	
	public static void setViewPort(int width, int height)
	{
		m_intScreenHeight = height;
		m_intScreenWidth = width;
		GraphicsAPI.setViewPort(0, 0, width, height);
	}
	
	/**
	 * Get the device screen width
	 * @return (integer) : the device screen width
	 * @throws Exception : defensive programming for this singleton class.
	 * This exception will not thrown since it has been set up internally.
	 */
	public static int getScreenWidth() throws Exception
	{
		if(m_intScreenWidth < 0)
			throw new Exception("Unable to obtain the screen size.");
		return m_intScreenWidth;
	}
	
	/**
	 * Get the device screen height
	 * @return (integer) : the device screen height
	 * @throws Exception : defensive programming for this singleton class.
	 * This exception will not thrown since it has been set up internally.
	 */
	public static int getScreenHeight() throws Exception
	{
		if(m_intScreenHeight < 0)
			throw new Exception("Unable to obtain the screen size.");
		return m_intScreenHeight;
	}
}
