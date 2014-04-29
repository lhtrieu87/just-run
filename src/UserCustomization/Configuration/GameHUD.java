package UserCustomization.Configuration;

import java.util.ArrayList;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.SystemColor;
import JUST_RUN.RendererEngine.Drawing.Menu.*;

public class GameHUD {
	public static enum CanvasLabel
	{
		SelectableItem_1 (0),
		SelectableItem_2 (1),
		SelectableItem_3 (2),
		SelectableItem_4 (3),
		SelectableItem_5 (4),
		ActivateButton (5),
		PauseMenu (6),
		GoldDisplay (7),
		HealthDisplay (9);
		
		private int index;
		
		private CanvasLabel(int value)
		{
			index = value;
		}
		
		public int getIndex()
		{
			return index;
		}
	}
	
	private ArrayList<Component> m_objComponents = new ArrayList<Component>();
	
	private static final int NUM_ITEMS = 6; // 5 number of items + 1 activation skill
	private int m_intHealth = 100; //default HP 
	private Camera m_camera;
	private int m_intWidth, m_intHeight, m_intPrevHealth, m_intPrevGold, m_intGold;
	private Label m_lblGold;
	
	public GameHUD(int screenWidth, int screenHeight, Camera camera)
	{
		m_intWidth = screenWidth;
		m_intHeight = screenHeight;
		m_camera = camera;
		initialize_components();
	}
	
	private void initialize_components()
	{
		int buttonSize = (int) (m_intHeight/(NUM_ITEMS));
		
		// Selectable Item 1
		Canvas canvas = new Canvas(m_intWidth-buttonSize, 5*buttonSize, 
				buttonSize, buttonSize, new Vector(SystemColor.Black,0.8f));
		canvas.setCamera(m_camera);
		canvas.SetImage(GameResource.HUD_ROCK_TEXTURE);
		m_objComponents.add(canvas);
		
		// Selectable Item 2
		canvas = new Canvas(m_intWidth-buttonSize, 4*buttonSize, 
				buttonSize, buttonSize, new Vector(SystemColor.BlueViolet,0.8f));
		canvas.setCamera(m_camera);
		m_objComponents.add(canvas);
		
		// Selectable Item 3
		canvas = new Canvas(m_intWidth-buttonSize, 3*buttonSize, 
				buttonSize, buttonSize, new Vector(SystemColor.BlanchedAlmond,0.8f));
		canvas.setCamera(m_camera);

		m_objComponents.add(canvas);

		// Selectable Item 4
		canvas = new Canvas(m_intWidth-buttonSize, 2*buttonSize, 
				buttonSize, buttonSize, new Vector(SystemColor.LimeGreen,0.8f));
		canvas.setCamera(m_camera);

		m_objComponents.add(canvas);
		
		// Selectable Item 5
		canvas = new Canvas(m_intWidth-buttonSize, buttonSize, 
				buttonSize, buttonSize, new Vector(SystemColor.Turquoise,0.8f));
		canvas.setCamera(m_camera);
		m_objComponents.add(canvas);
		
		// Activate Player skill button
		canvas = new Canvas(m_intWidth-buttonSize, 0, 
				buttonSize, buttonSize, new Vector(SystemColor.AliceBlue,0.8f));
		canvas.setCamera(m_camera);
		canvas.SetImage(GameResource.VAMPIRE_SKILL_TEXTURE);
		m_objComponents.add(canvas);
		
		int hoffset = 6;
		
		// Pause button
		canvas = new Canvas(m_intWidth/20,  m_intHeight - m_intHeight / 10 - 2*hoffset, 
				m_intHeight/20, m_intHeight/20, new Vector(SystemColor.Maroon,0.5f));
		canvas.setCamera(m_camera);
		m_objComponents.add(canvas);
		
		// Gold icon display
		canvas = new Canvas(m_intWidth/2, m_intHeight - m_intHeight/20 - hoffset, 
				m_intHeight/20, m_intHeight/20, new Vector(SystemColor.Gold,0.5f));
		canvas.setCamera(m_camera);
		canvas.Rotate(45, new Vector(0,0,1));
		m_objComponents.add(canvas);
		
		// Gold value display
		Font font = Font.getHandle();
		font.Size(20);
		font.ForeColor(SystemColor.Goldenrod);
		font.BackColor(SystemColor.Transparent);
		Vector temp = font.MeasureString("20", true);
		m_lblGold = new Label(Integer.toString(m_intGold), font, (int)(m_intWidth * 0.5 + canvas.Width()), 
				(int)(m_intHeight - temp.Y), true, m_camera);
		m_objComponents.add(m_lblGold);
		
		// Health display bar
		canvas = new Canvas(m_intWidth/20, m_intHeight - m_intHeight/20 - hoffset, 
				m_intWidth/3, m_intHeight/20, 
				new Vector(SystemColor.Red,1));
		canvas.setCamera(m_camera);
		m_objComponents.add(canvas);
	}
	
	public void setHealth(int value)
	{
		m_intPrevHealth = m_intHealth = value;
	}
	
	public void DisplayGold(int value)
	{
		if(m_intPrevGold != value)
		{
			m_intPrevGold = value;
			m_lblGold.setText(Integer.toString(value));
		}
	}
	
	public void DisplayHealth(double value)
	{
		if(m_intPrevHealth != (int)value)
		{
			m_intPrevHealth = (int)value;
			if(value < 0)
				value = 0;
			((Canvas)m_objComponents.get(GameHUD.CanvasLabel.HealthDisplay.getIndex())).
				Width((int)(value/m_intHealth*m_intWidth/3));
		}
	}
	
	public ArrayList<Component> getComponents()
	{
		return m_objComponents;
	}
}
