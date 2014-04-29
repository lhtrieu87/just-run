/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.Core;

// Android API

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

// our system package
import JUST_RUN.RendererEngine.RenderEngine;
import JUST_RUN.Resource.ResourceManager;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.HCI.*;
import UserCustomization.Configuration.GameManager;

/*
 * PRECAUTION: DO NOT PERFORM RUN-TIME CONFIGURATION BECAUSE ANY RUN-TIME CHANGE(S)
 * WILL CAUSE ANDROID ACTIVITY TO RESTART !!
 */
public class JUST_RUN extends Activity {
	
	private JUST_RUN_GraphicsSurfaceView m_objGLSurfaceView; // OpenGL rendering screen handler
	private SystemConfiguration m_objSystem;
	//private UnitTesting m_objUnitTesting;
	
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // checking the availability of openGL ES 2.0
        if(isOpenGLES_2_Exist())
        {
        	//alertbox("debug", UnitTesting.raw_folder_reflection());
        	// initialization
        	init();
        	// plug-in the surface view control
        	setContentView(m_objGLSurfaceView);
        }
        else
        {
        	GraphicsAPI.AlertBox(this, "Error","Incompatible Error !");
        	this.finish();
        }
    }
    
    private void init()
    {
    	// Activity initialisation
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	/* DO NOT DO THIS; OTHERWISE, SYSTEM ERROR ! (It caused multiple initialisation)*/
    	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    	
    	// system objects declaration
    	m_objSystem = new GameManager(this);
    	m_objGLSurfaceView = new JUST_RUN_GraphicsSurfaceView(this, m_objSystem);
    	// plug-in the HCI controller
		SystemConfiguration.setHCI(new HCI(this));
		
		// plug-in the Resource Manager
		ResourceManager resource = new ResourceManager(this, m_objSystem);
		resource.Task_Execute();

    	// start up event manager
    	EventManager.getManager();

    	// calling child initialization function
    	m_objSystem.StartUpInitialization();
    }
    
    // getting the availability of openGL ES 2.0
    private boolean isOpenGLES_2_Exist()
    {
    	ActivityManager objActivity = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    	ConfigurationInfo info = objActivity.getDeviceConfigurationInfo();
    	return (info.reqGlEsVersion >= 0x20000);
    }
    
    // The following call resumes a paused rendering thread.
    @Override
    protected void onResume() {
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
    	super.onResume();
    	m_objGLSurfaceView.onResume();
        // clearing event queue whenever the Apps is running
        EventManager.getManager().clearBackQueue();
        EventManager.getManager().clearFrontQueue();
        m_objSystem.Entering(); // allocate any resource under system configuration
        SystemConfiguration.getHCIHandle().HCI_registerSensor(); // allocate HCI sensor listener
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    // The following call whenever this activity has been killed.
    @Override
    protected void onStop()
    {
    	super.onStop();
    	m_objGLSurfaceView.onPause();
    	m_objSystem.Exiting(); // release any resource under system configuration
    	ResourceManager.KillTask(); // release Resource manager
    	SystemConfiguration.getHCIHandle().HCI_unregisterSensor(); // release HCI sensor listener
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    // When power button is pressed, the destroy event will call once.
    // Edit on V0.4 by Han Lin
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	// Bypass the bugs found during re-initialisation (Forced the activity to close).
		System.exit(0); // invoke JVM to end this Java program
    }
    
    // Edit on V0.4 by Han Lin
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	switch(keyCode)
    	{
	    	/*
	    	 * Handling the back button is necessary
	    	 */
    		case KeyEvent.KEYCODE_BACK:
    			this.onStop(); // invoke activity onStop() to clear resources. It is unnecessary, just for safe. 
    			this.finish(); // invoke Android OS to end this activity
    			System.exit(0); // invoke JVM to end this Java program
    			break;
    		default:
    			// Other key events should handle in HCI API
    			break;
    	}
    	return true;
    }
    
    // OpenGL Surface View handler
    private class JUST_RUN_GraphicsSurfaceView extends GLSurfaceView
	{
    	private RenderEngine m_objRenderer;  // system rendering engine
		public JUST_RUN_GraphicsSurfaceView(Context context, SystemConfiguration sysMgr) {
			super(context);
			// plug-in the render engine
			setEGLContextClientVersion(2);
			m_objRenderer = new RenderEngine(sysMgr);
			setRenderer(m_objRenderer);
		}
		
		// Android Gesture Event Interface
	    @Override 
	    public boolean onTouchEvent(MotionEvent e) 
	    {
	    	// Touch event is handle by HCI API
	    	SystemConfiguration.getHCIHandle().HCI_processMotionEvent(e);
	    	return true;
	    }
	}
}