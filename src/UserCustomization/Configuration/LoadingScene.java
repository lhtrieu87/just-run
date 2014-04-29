package UserCustomization.Configuration;

import java.util.ArrayList;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameplaySystem.Clock;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import JUST_RUN.RendererEngine.Drawing.Menu.Component;
import JUST_RUN.RendererEngine.Drawing.Menu.Font;
import JUST_RUN.RendererEngine.Drawing.Menu.Label;
import JUST_RUN.RendererEngine.SceneManagement.Scene;

public class LoadingScene extends Scene{
	private Camera m_ortho = new Camera();
	private ArrayList<Component> m_objRenderObject = new ArrayList<Component>();
	private Label m_lblText1;
	private long m_startTime;
	private int count = 0, m_width, m_height;
	private Font m_font;
	
	public LoadingScene(int width, int height)
	{
		initialize(width, height);
	}
	
	private void initialize(int width, int height)
	{
		m_width = width;
		m_height = height;
		m_ortho.setOrthographics(0, width, 0, height, 0, 100);
		m_font = Font.getHandle();
		m_font.Size(30);
		m_font.ForeColor(SystemColor.White);
		m_font.BackColor(SystemColor.Transparent);
		Vector temp = m_font.MeasureString("Loading    ", true);
		m_lblText1 = new Label("Loading", m_font, (int)((width - temp.X) * 0.5), (int)((height - temp.Y) * 0.5), 
				true, m_ortho);
		m_objRenderObject.add(m_lblText1);
		m_startTime = Clock.GetCurrentTime_Milli();
	}
	
	@Override
	public void CreateScene() {
		Refresh(SystemColor.Black);
		GraphicsAPI.setDepthTest(false);
		for(Component obj : m_objRenderObject)
			obj.Render();
	}

	@Override
	public void FrameStart() {
		long currentTime = Clock.GetCurrentTime_Milli();
		long elapsed = currentTime - m_startTime;
		if(elapsed > 700)
		{
			m_startTime = currentTime;
			String str = "Loading    ";
			Vector temp = m_font.MeasureString(str, true);
			switch(count)
			{
				case 1:
					str = "Loading .  ";
					temp = m_font.MeasureString(str, true);
					break;
				case 2:
					str = "Loading .. ";
					temp = m_font.MeasureString(str, true);
					break;
				case 3:
					str = "Loading ...";
					temp = m_font.MeasureString(str, true);
					count = -1;
					break;
			}
			m_objRenderObject.set(0, new Label(str, m_font, 
					(int)((m_width - temp.X) * 0.5), (int)((m_height - temp.Y) * 0.5), 
					true, m_ortho));
			count++;
		}
	}

	@Override
	public void FrameEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HandleInteraction(Event<Vector> e, long currentTime) {}

	@Override
	public void Update(long currentTime) { }

}
