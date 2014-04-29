/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.RendererEngine;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLSurfaceView;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.Core.SystemConfiguration;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import JUST_RUN.RendererEngine.Drawing.Menu.Canvas;
import JUST_RUN.Resource.ResourceManager;


public final class RenderEngine implements GLSurfaceView.Renderer {
	private SystemConfiguration m_objSysMgr;
	private LogoDisplayScene m_logo;
	private boolean m_bIsDisplayLogo = true;
	
    public RenderEngine(SystemConfiguration sysMgr)
    {
		m_objSysMgr = sysMgr;
    }
    
    private static void LoadShaders()
	{
		if (ResourceManager.PHONG_SHADER == null)
			ResourceManager.PHONG_SHADER = new Shader (Shader.Type.Phong);
		if (ResourceManager.SIMPLE_SHADER == null)
			ResourceManager.SIMPLE_SHADER = new Shader (Shader.Type.Simple);
		if(ResourceManager.FONT_SHADER == null)
			ResourceManager.FONT_SHADER = new Shader(Shader.Type.Font);
		//if(ResourceManager.GOURAUD_SHADER == null)
			//ResourceManager.GOURAUD_SHADER = new Shader(Shader.Type.Gouraud);
	}
    
	/*
	 * Initialization function
	 */
	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
	{
		// initialization
		GraphicsAPI.standardInit();
		// Edit on v0.4 -- automatic calling texture setup
		ResourceManager.getTextureHandle().SetupAll();
		// Edit on v0.4 -- loading default engine shader
		LoadShaders();
		// Edit on v0.4
		int size = ResourceManager.getTextureHandle().getCount();
		// calling customization subclass function
		m_objSysMgr.RenderEngine_onSurfaceCreated();
		
		// Edit on v0.4 -- loading user texture unit
		/* 
		 * There is a slightly performance loss with a tradeoff between
		 * reinitialized array, function call and looping.
		 */
		if(size != ResourceManager.getTextureHandle().getCount())
		{
			// Edit on v0.4 -- automatic calling texture setup
			ResourceManager.getTextureHandle().SetupAll();
		}
	}
	
	/*
	 * Called when viewport is changed
	 */
	public void onSurfaceChanged(GL10 unused, int width, int height) 
	{	
		// setting up virtual screen properties
		VirtualScreen.setViewPort(width, height);
		// Edit on v0.4
		Camera camera = new Camera();
		camera.setOrthographics(0, width, 0, height, 0, 1000);
		m_logo = new LogoDisplayScene(camera);
		// calling customization subclass function
		m_objSysMgr.RenderEngine_onSurfaceChanged(width, height);
	}
	
	/*
	 * OpenGL Standard Drawing Function
	 * It will automatically called on every frame
	 */
	public void onDrawFrame(GL10 unused)
	{
		// OpenGL ES 1.0 is ignore. 
		// Instead, OpenGL ES 2.0 static method is used here.
		// Edit on v0.4 -- display game engine logo
		if(m_bIsDisplayLogo)
		{
			GraphicsAPI.setBackfaceCulling(false);
			GraphicsAPI.setDepthTest(false);
			m_logo.Update(System.nanoTime() / 1000000);
			m_logo.Render();
			GraphicsAPI.setDepthTest(true);
			GraphicsAPI.setBackfaceCulling(true);
		}
		else
		{
			if(m_objSysMgr.getScene() == null)
				return;
			m_objSysMgr.getScene().FrameStart();
			// Edit on v0.2
			m_objSysMgr.getScene().CreateScene(); // draw scene
			//End edit
			m_objSysMgr.getScene().FrameEnd();
		}
	}
	
	/*
	 * Display Game Engine logo
	 * This logo will always be displayed as first scene.
	 */
	private class LogoDisplayScene {
		private Canvas m_canvas;
		private final int m_duration = 3000, m_fadeTime = 21;
		private long m_startTime;
		private boolean m_bIsFade = false;
		private int m_count, m_size = 220;
		
		public LogoDisplayScene(Camera camera)
		{ 
			try
			{
				// calculate logo size
				int temp = Math.min(VirtualScreen.getScreenWidth(), VirtualScreen.getScreenHeight());
				m_size = Math.max((int)((double)temp * 0.6), 300);
				// locate the logo into screen center
				int x1 = (VirtualScreen.getScreenWidth() - m_size) / 2;
				int y1 = (VirtualScreen.getScreenHeight() - m_size) / 2;
				m_canvas = new Canvas(x1, y1, m_size, m_size, SystemColor.White);
				m_canvas.setCamera(camera);
				m_canvas.SetImage(ResourceManager.GAME_ENGINE_LOGO);
				// recorded current time stamp
				m_startTime = System.nanoTime() / 1000000;
			}catch(Exception e)
			{/* never happened */}
		}
		
		public void Render()
		{
			GraphicsAPI.clearBuffer(SystemColor.Black);
			m_canvas.Render();
		}
		
		public void Update(long ctime)
		{
			long elapsed = ctime - m_startTime;
			if(elapsed >= m_duration && !m_bIsFade)
			{
				m_startTime = ctime;
				m_bIsFade = true;
				m_count = 0;
			}
			
			// fading animation, total 20 steps to fade out
			if(m_bIsFade)
			{
				m_count++;
				float alpha = 1.05f - 0.05f * m_count;
				m_canvas.setForegroundColor(new Vector(1, 1, 1, alpha));
			}

			if(m_count == m_fadeTime)
				m_bIsDisplayLogo = false;
		}
	}
}
