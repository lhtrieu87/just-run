package JUST_RUN.RendererEngine.Drawing.Menu;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.RendererEngine.Drawing.Rectangle;
import JUST_RUN.RendererEngine.Drawing.Shape;
import JUST_RUN.Resource.ResourceManager;

public class Canvas extends Component {
	private Rectangle m_objCanvas;
	
	private int m_intX, m_intY, m_intWidth, m_intHeight;
	private Vector m_objAxis;
	private float m_fAngle;
	
	public Canvas(int x, int y, int width, int height, Vector backgroundcolor)
	{
		m_intX = x;
		m_intY = y;
		m_intWidth = width;
		m_intHeight = height;
		m_objCanvas = new Rectangle(backgroundcolor);
		// default setting
		m_objCanvas.setShader(ResourceManager.SIMPLE_SHADER);
	}
	
	public Canvas(int x, int y, int width, int height)
	{
		m_intX = x;
		m_intY = y;
		m_intWidth = width;
		m_intHeight = height;
	}
	
	public void setBackgroundColor(Vector color)
	{
		m_objCanvas.setBackgroundColor(color);
	}
	
	public Vector getBackgroundColor()
	{
		return m_objCanvas.getBackgroundColor();
	}
	
	public void setForegroundColor(Vector color)
	{
		m_objCanvas.setForegroundColor(color);
	}
	
	public Vector getForegroundColor()
	{
		return m_objCanvas.getForegroundColor();
	}
	
	@Override
	public void setCamera(Camera camera)
	{
		m_objCanvas.setCamera(camera);
	}
	
	@Override
	public void setShader(Shader shader)
	{
		m_objCanvas.setShader(shader);
	}
	
	@Override
	public Shape getShape()
	{
		return m_objCanvas;
	}
	
	public void SetImage(int ID)
	{
		m_objCanvas.setTextureID(ID);
		m_objCanvas.setTextureEnable(true);
	}

	@Override
	public void Render()
	{
		m_objCanvas.ResetTransMatrix();
		m_objCanvas.Translate(new Vector(m_intX, m_intY));
		m_objCanvas.Scale(new Vector(m_intWidth, m_intHeight));
		if(m_fAngle != 0)
			m_objCanvas.Rotate(m_fAngle, m_objAxis);
		m_objCanvas.Render();
	}
	
	public final void Rotate(float angle_inDegree, Vector axis)
	{
		m_fAngle = angle_inDegree;
		m_objAxis = axis;
	}
	
	public final void setRectangle(Rectangle rect)
	{
		m_objCanvas = rect;
	}
	
	public void SetLayout(int x, int y, int width, int height)
	{
		m_intX = x;
		m_intY = y;
		m_intWidth = width;
		m_intHeight = height;
	}
	
	public boolean Intersect(Vector coord)
	{
		if(coord.X >= m_intX && coord.X <= m_intX + m_intWidth &&
		   coord.Y >= m_intY  && coord.Y <= m_intY + m_intHeight)
			return true;
		return false;
	}
	
	public int X()
	{
		return m_intX;
	}
	
	public void X(int value)
	{
		m_intX = value;
	}
	
	public int Y()
	{
		return m_intY;
	}
	
	public void Y(int value)
	{
		m_intY = value;
	}
	
	public int Width()
	{
		return m_intWidth;
	}
	
	public void Width(int value)
	{
		m_intWidth = value;
	}
	
	public int Height()
	{
		return m_intHeight;
	}
	
	public void Height(int value)
	{
		m_intHeight = value;
	}
}
