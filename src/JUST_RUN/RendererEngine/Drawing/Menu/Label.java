/**
 * @author Chan Han Lin
 * A label is a special type of canvas that contain several small rectangle to draw a string of characters.
 */
package JUST_RUN.RendererEngine.Drawing.Menu;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Drawing.Rectangle;
import JUST_RUN.RendererEngine.Drawing.Shape;
import JUST_RUN.Resource.ResourceManager;

public class Label extends Component{
	
	private int m_intX, m_intY, m_intLength;
	private String m_strText;
	private Camera m_objCamera;
	private Shader m_objShader = ResourceManager.FONT_SHADER; // default setting
	private Canvas[] m_objCanvas;
	private boolean m_bHorizontal;
	private Font m_objFont;
	
	/**
	 * Label constructor.
	 * @param str (String) : The text to be drawn.
	 * @param font (Font) : The font class that defined all the font properties on how the text to be drawn
	 * @param x (integer) : Initial X position 
	 * @param y (integer) : Initial Y position
	 * @param isHorizontal (boolean) : This flag used to determine the text orientation. 
	 * True if text is horizontal oriented. Otherwise, false. 
	 * @param camera (camera) : The camera handle on where this label to be drawn.
	 * @param shader (Shader) : The openGL programmable shader handle on how this label to be drawn.
	 */
	public Label(String str,Font font, int x, int y, boolean isHorizontal, Camera camera)
	{
		m_intX = x;
		m_intY = y;
		m_bHorizontal = isHorizontal;
		m_objFont = font;
		m_objCamera = camera;
		m_strText = str;
		m_intLength = m_strText.length();
		setUp();
	}
	
	/**
	 * Setting new font to the label. This function is expensive since all the drawing pattern and size need
	 * to recalculate.
	 * @param font (Font) : The font class that defined all the font properties on how the text to be drawn
	 */
	public void setFont(Font font)
	{
		m_objFont = font;
		setUp();
	}
	
	/**
	 * Setting new text orientation. This function is expensive since all the drawing pattern and size need
	 * to recalculate except that the input flag value is the same as current flag value.
	 * @param isHorizontal (boolean) : This flag used to determine the text orientation. 
	 * True if text is horizontal oriented. Otherwise, false.
	 */
	public void setTextOrientation(boolean isHorizontal)
	{
		if(m_bHorizontal != isHorizontal)
		{
			m_bHorizontal = isHorizontal;
			setUp();
		}
	}
	
	/**
	 * Setting new text to be drawn. This function is expensive since all the drawing pattern and size need
	 * to recalculate except that the input text is the same as current text.
	 * @param str (String) : The text to be drawn.
	 */
	public void setText(String str)
	{
		if(!m_strText.equals(str))
		{
			m_strText = str;
			m_intLength = m_strText.length();
			setUp();
		}
	}
	
	/**
	 * Setting new position for the label object. This function is not expensive.
	 * @param x (integer) : Initial X position 
	 * @param y (integer) : Initial Y position 
	 */
	public void setLocation(int x, int y)
	{
		m_intX = x;
		m_intY = y;
		propagate();
	}
	
	/*
	 * Set up the drawing rectangle to draw each character on screen
	 */
	private void setUp()
	{
		//m_objRect = new Rectangle[m_intLength];
		m_objCanvas = new Canvas[m_intLength];
		
		int width = m_objFont.Width();
		int height = m_objFont.Height();
		int bwidth = m_objFont.BitmapWidth();
		int bheight = m_objFont.BitmapHeight();
		
		for(int i=0; i<m_intLength;++i)
		{
			
			char c;
			if(m_bHorizontal)
				c = m_strText.charAt(i);
			else
				c = m_strText.charAt(m_intLength - i - 1);
			float tx = m_objFont.ParentX(c),
				ty = m_objFont.ParentY(c);
			int x1 = m_intX, y1 = m_intY;
			
			if(m_bHorizontal)
			{
				// set horizontal oriented vertex coordinate
				x1 = i*width + m_intX;
			}
			else
			{
				// set vertical oriented vertex coordinate
				y1 = i*height + m_intY;
			}

			Rectangle r = new Rectangle(
				new float[]{
					tx / bwidth, (ty + m_objFont.BoundY()) / bheight,
					(tx + m_objFont.BoundX()) / bwidth, (ty + m_objFont.BoundY()) / bheight,
					tx / bwidth, ty / bheight,
					(tx + m_objFont.BoundX()) / bwidth, ty / bheight
				}, m_objFont.ForeColor(), m_objFont.BackColor());
			m_objCanvas[i] = new Canvas(x1, y1, width, height);
			m_objCanvas[i].setRectangle(r);
			m_objCanvas[i].setCamera(m_objCamera);
			m_objCanvas[i].setShader(m_objShader);
			m_objCanvas[i].SetImage(m_objFont.TextureID());
		}
	}
	
	/*
	 * The length of the string and the size of the label don't change but the starting drawing 
	 * coordinate has changed. Thus, it just needs to update the 
	 * coordinate for each drawing rectangle.
	 */
	private void propagate()
	{
		if(m_intLength > 0)
		{
			int offsetX = m_objCanvas[0].X() - m_intX, offsetY = m_objCanvas[0].Y() - m_intY;
			for(int i=0; i<m_intLength; ++i)
			{
				m_objCanvas[i].SetLayout(m_objCanvas[i].X() - offsetX, 
						m_objCanvas[i].Y() - offsetY, 
						m_objCanvas[i].Width(),
						m_objCanvas[i].Height());
			}
		}
	}
	
	@Override
	public void Render() {

		for(int i = 0; i < m_intLength; ++i)
		{
			if(m_objCanvas[i] != null)
				m_objCanvas[i].Render();
		}
	}

	@Override
	public void setCamera(Camera camera) {
		m_objCamera = camera;
	}

	@Override
	public void setShader(Shader shader) {
		m_objShader = shader;
	}

	@Override
	public Shape getShape() {
		return null; // this is null since this class doesn't contain single shape but a complex shape.
	}
}
