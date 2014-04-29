package JUST_RUN.RendererEngine.Drawing.Menu;

import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.Resource.ResourceManager;

public class Font
{
	private int m_intBoundX = 40, 
				m_intBoundY = 70, 
				m_intFontSize = 40, // default font size 
				m_intRefFontSize = 40, // reference font size which was originated drawn on the font bitmap
				m_intWcount = 15, // total number of characters per row
				m_intFcount = 95, // total number of characters in bitmap
				m_intBitmapWidth = 600, // m_intWcount * m_intBoundX
				m_intBitmapHeight = 490, // ceiling(m_intFcount / m_intWcount) * m_intBountY
				m_intHeight,
				m_intTexetureID;

	private Vector m_objForeColor, m_objBackColor;
	
	private static Font m_objFont;
	
	private Font() 
	{
		m_intTexetureID = ResourceManager.GAME_FONT; // default setting
	}
	
	public static Font getHandle()
	{
		if(m_objFont == null)
			m_objFont = new Font();
		return m_objFont;
	}
	
	public int TotalSupportedCharacter()
	{
		return m_intFcount;
	}
	
	protected void TotalSupportedCharacter(int count)
	{
		m_intFcount = count;
	}
	
	public int BitmapWidth()
	{
		return m_intBitmapWidth;
	}
	
	protected void BitmapWidth(int width)
	{
		m_intBitmapWidth = width;
	}
	
	public int BitmapHeight()
	{
		return m_intBitmapHeight;
	}
	
	protected void BitmapHeight(int height)
	{
		m_intBitmapHeight = height;
	}
	
	public int TextureID()
	{
		return m_intTexetureID;
	}
	
	public void TextureID(int ID)
	{
		m_intTexetureID = ID;
	}
	
	public Vector ForeColor()
	{
		return m_objForeColor;
	}
	
	public void ForeColor(Vector color)
	{
		m_objForeColor = color;
	}
	
	public Vector BackColor()
	{
		return m_objBackColor;
	}
	
	public void BackColor(Vector color)
	{
		m_objBackColor = color;
	}
	
	public int BoundX()
	{
		return m_intBoundX;
	}
	
	/**
	 * 
	 * @param (integer) value : indicate the bounding width value
	 */
	protected void BoundX(int value)
	{
		m_intBoundX = value;
	}
	
	public int BoundY()
	{
		return m_intBoundY;
	}
	
	/**
	 * 
	 * @param (integer) value : indicate the bounding height value
	 */
	protected void BoundY(int value)
	{
		m_intBoundY = value;
	}
	
	/**
	 * 
	 * @return (integer) Get font size 
	 */
	public int Size()
	{
		return m_intFontSize;
	}
	
	/**
	 * 
	 * @param (integer) value : Set font size value
	 */
	public void Size(int value)
	{
		m_intFontSize = value;
		m_intHeight = (int)((float)m_intFontSize / m_intRefFontSize * m_intBoundY);
	}

	protected void RefSize(int value)
	{
		m_intRefFontSize = value;
	}
	
	protected void Wcount(int value)
	{
		m_intWcount = value;
	}
	
	/**
	 * 
	 * @return (integer) Get the actual height (in terms of pixels) of this font
	 */
	public int Height()
	{
		return m_intHeight;
	}
	
	/**
	 * 
	 * @return (integer) Get the actual width (in terms of pixels) of this font
	 */
	public int Width()
	{
		return (int)(m_intBoundX * ((float)m_intHeight / m_intBoundY));
	}
	
	/**
	 * 
	 * @param (character) c : the character for being draw
	 * @return (integer) X index reference with respect to the font bitmap
	 */
	public int ParentX(char c)
	{
		int value = c - 0x20;
		return (value % m_intWcount) * m_intBoundX;
	}
	
	/**
	 * 
	 * @param (character) c : the character for being draw
	 * @return (integer) Y index reference with respect to the font bitmap
	 */
	public int ParentY(char c)
	{
		int value = c - 0x20;
		return (value / m_intWcount) * m_intBoundY;
	}
	
	/**
	 * 
	 * @param (String) str : Input string to be measured
	 * @param (boolean) isHorizontal : A flag to indicate the orientation of the input string
	 * @return (Vector) The actual width and height of the input string str in pixels
	 */
	public final Vector MeasureString(String str, boolean isHorizontal)
	{
		if(isHorizontal)
			return new Vector(this.Width() * str.length() , m_intHeight);
		else
			return new Vector(this.Width(), m_intHeight * str.length());
	}
}
