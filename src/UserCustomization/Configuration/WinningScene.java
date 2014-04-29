/**
 * @author Han Lin
 * Created by Han Lin on v0.4
 * Edited :
 */
package UserCustomization.Configuration;

import java.util.ArrayList;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.GameplaySystem.Event;
import JUST_RUN.GameplaySystem.EventManager;
import JUST_RUN.HCI.HCI;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import JUST_RUN.RendererEngine.Drawing.Menu.Component;
import JUST_RUN.RendererEngine.Drawing.Menu.Font;
import JUST_RUN.RendererEngine.Drawing.Menu.Label;
import JUST_RUN.RendererEngine.SceneManagement.Scene;

public class WinningScene extends Scene {
	private Camera m_ortho = new Camera();
	private ArrayList<Component> m_objRenderObject = new ArrayList<Component>();
	private Label m_lblText;
	
	public WinningScene(int width, int height)
	{ 
		initialize(width, height);
	}
	
	private void initialize(int width, int height)
	{
		m_ortho.setOrthographics(0, width, 0, height, 0, 100);
		Font font = Font.getHandle();
		font.Size(50);
		font.ForeColor(SystemColor.White);
		font.BackColor(SystemColor.Transparent);
		Vector temp = font.MeasureString("You Win !", true);
		m_lblText = new Label("You Win !", font, (int)((width - temp.X) * 0.5), (int)((height - temp.Y) * 0.5), 
				true, m_ortho);
		m_objRenderObject.add(m_lblText);
	}
	
	@Override
	public void CreateScene() {
		Refresh(SystemColor.DarkKhaki);
		GraphicsAPI.setDepthTest(false);
		for(Component obj : m_objRenderObject)
			obj.Render();
	}

	@Override
	public void FrameStart() {
		// TODO Auto-generated method stub
	}

	@Override
	public void FrameEnd() {
		// TODO Auto-generated method stub
	}

	@Override
	public void HandleInteraction(Event<Vector> e, long currentTime) {
		if(e.getID() == HCI.Gesture.SingleTap.getID ())
		{
			EventManager.getManager().add2FrontQueue(new Event<Object>(GameManager.GameState.MAIN_GAME_SCENE.getID(), null));
		}
	}

	@Override
	public void Update(long currentTime) { }

}
