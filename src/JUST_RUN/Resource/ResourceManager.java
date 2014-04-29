/**
 * @author Han Lin
 * Created by Han Lin by migrating from v0.3 to v0.4
 * Edited :
 */
package JUST_RUN.Resource;

import JUST_RUN.Core.R;
import JUST_RUN.Core.SystemConfiguration;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Texture;
import android.content.Context;

public class ResourceManager{
	public static int GAME_FONT;
	public static int GAME_ENGINE_LOGO;
	public static Shader PHONG_SHADER;
	public static Shader GOURAUD_SHADER;
	public static Shader SIMPLE_SHADER;
	public static Shader FONT_SHADER;
	
	public static enum ResourceEvent
	{
		THREAD_ERROR(EventManager.getNewErrorID());
		
		private final int ID;
		
		private ResourceEvent(int ID)
		{
			this.ID = ID;
		}
		
		public int getID() {
			return ID;
		}
	}
	
	private static Context m_objContext;
	private static Thread m_objThread;
	private SystemConfiguration m_objSystem;
	private static Texture m_objTexture;
	private long m_StartTime;
	
	public ResourceManager(Context context, SystemConfiguration sysConfig)
	{
		m_objSystem = sysConfig;
		m_objContext = context;
		m_objTexture = getTextureHandle();
		GAME_FONT = m_objTexture.addTexture(R.raw.font);
		GAME_ENGINE_LOGO = m_objTexture.addTexture(R.raw.just_run_logo);
	}
	
	public static final Context getContext()
	{
		return m_objContext;
	}
	
	public static final Texture getTextureHandle()
	{
		if(m_objTexture == null)
			m_objTexture = Texture.getHandle(m_objContext);
		return m_objTexture;
	}
	
	public static final void KillTask()
	{
		if(m_objThread != null)
			m_objThread = null;
	}
	
	public final void Task_Execute()
	{
		m_StartTime = System.nanoTime() / 1000000;
		m_objThread = new Thread(backgroundProcess);
		m_objThread.start();
	}

	private Runnable backgroundProcess = new Runnable() {
		@Override
		public void run() {
			long elapsed = 0;
			try
			{
				// loading user defined resource
				m_objSystem.Resource_Task_Load();
				elapsed = (System.nanoTime() / 1000000) - m_StartTime;
				/*
				 * This thread should run at least 1 second to synchronise the
				 * rendering thread and game update thread. 
				 */
				if(elapsed < 1000)
				{
					// have not reach 1 second, then go sleep
					Thread.sleep(1000 - elapsed);
				}
				// return handle to user when process finish
				m_objSystem.Resource_Task_Done();
			}catch(Exception e)
			{
				EventManager.getManager().add1FrontQueue(new Event<Exception>(ResourceEvent.THREAD_ERROR.getID(), e));
			}
			finally
			{
				m_objThread = null; // kills current thread immediately
			}
		}
	};
}
