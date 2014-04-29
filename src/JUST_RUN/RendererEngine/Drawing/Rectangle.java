package JUST_RUN.RendererEngine.Drawing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.Core.GraphicsAPI;
import JUST_RUN.Core.GraphicsAPI.SupportType;
import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Vector;
import JUST_RUN.Resource.ResourceManager;

public class Rectangle extends Shape{

	private FloatBuffer m_objVertexBuffer, m_objTextureBuffer;
	private ShortBuffer m_objIndexBuffer;
	private short[] m_fIndices = {0, 1, 2, 3};
	private float[] m_fVertices =
			{0,0,0,
			 1,0,0,
			 0,1,0,
			 1,1,0};
	private float[] m_fTexture = 
			{0,1,
			 1,1,
			 0,0,
			 1,0};
	private int m_intFcomponent = 4;
	private boolean m_bTexture;
	private Camera m_objCamera;
	private int m_intTextureID;
	private Shader m_objShader = ResourceManager.SIMPLE_SHADER;
	private Vector m_objForeColor, m_objBackColor;
	
	/**
	 * This constructor is specific to the shader that support single colour manipulation
	 * @param (Vector) color : colour to be drawn
	 */
	public Rectangle(Vector color)
	{
		m_objForeColor = color;
		computeBuffer();
	}
	
	/**
	 * This constructor is specific to the shader that support two colour manipulation
	 * @param vertexCoord
	 * @param textureCoord
	 * @param (Vector) ForeColor : Foreground colour to be drawn
	 * @param  Vector) BackColor : Background colour to be drawn
	 */
	public Rectangle(float[] vertexCoord, float[] textureCoord, Vector ForeColor, Vector BackColor)
	{
		m_fVertices = vertexCoord;
		m_fTexture = textureCoord;
		m_objForeColor = ForeColor;
		m_objBackColor = BackColor;
		computeBuffer();
	}
	
	public Rectangle(float[] textureCoord, Vector ForeColor, Vector BackColor)
	{
		m_fTexture = textureCoord;
		m_objForeColor = ForeColor;
		m_objBackColor = BackColor;
		computeBuffer();
	}
	
	private void computeBuffer()
	{
		// generate vertex buffer for OpenGL rendering
	    m_objVertexBuffer = ByteBuffer.allocateDirect(
	    		m_fVertices.length * 4 // allocate 4 bytes for float data type
	    		).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    m_objVertexBuffer.put(m_fVertices);
	    m_objVertexBuffer.position(0);
	    
	    // generate vertex's texture coordinate buffer for OpenGL rendering
	    m_objTextureBuffer = ByteBuffer.allocateDirect(
	    		m_fTexture.length * 4 // allocate 4 bytes for float data type
	    		).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    m_objTextureBuffer.put(m_fTexture);
	    m_objTextureBuffer.position(0);
	    
	    // generate index buffer for OpenGL indexing
	    m_objIndexBuffer = ByteBuffer.allocateDirect(
	    		m_fIndices.length * 2 // allocate 2 bytes for short data type
	    		).order(ByteOrder.nativeOrder()).asShortBuffer();
	    m_objIndexBuffer.put(m_fIndices);
	    m_objIndexBuffer.position(0);
	    
	    m_fVertices = null;
	    m_fIndices = null;
	}
	
	public final void ResetTransMatrix()
	{
		Camera.setIdentity(m_fVMatrix);
	}
	
	public void Vertex(float[] matrix)
	{
		m_fVertices = matrix;
	}
	
	public float[] Vertex()
	{
		return m_fVertices;
	}
	
	public void TextureCoord(float[] matrix)
	{
		m_fTexture = matrix;
	}
	
	public float[] TextureCoord()
	{
		return m_fTexture;
	}
	
	public void setForegroundColor(Vector color)
	{
		m_objForeColor = color;
	}
	
	public Vector getForegroundColor()
	{
		return m_objForeColor;
	}
	
	public void setBackgroundColor(Vector color)
	{
		m_objBackColor = color;
	}
	
	public Vector getBackgroundColor()
	{
		return m_objBackColor;
	}
	
	@Override
	public void Render()
	{
		if(m_objShader == null)
			return;
		int shader = m_objShader.getShaderProgramHandle();
		GraphicsAPI.setGraphicsShader(shader);
		this.m_objCamera.computeModelViewProjectionMatrix(m_fVMatrix);
		/*
		 * Set up Model-View Projection
		 */
		GraphicsAPI.setShaderModelViewProjection(
				m_objCamera.getModelViewProjectionMatrix(), shader);
		/*
		 * Draw Rectangle
		 */
		GraphicsAPI.renderRectangle(shader, m_intFcomponent, m_objForeColor, m_objBackColor, m_objVertexBuffer,
			m_objTextureBuffer, m_objIndexBuffer, m_intTextureID, m_bTexture);
	}

	@Override
	public Camera getCamera() {
		return m_objCamera;
	}

	@Override
	public Shader getShader() {
		return m_objShader;
	}

	@Override
	public Lightning getLightning() {
		return null;
	}

	@Override
	public Material getMaterial() {
		return null;
	}

	@Override
	public int getTextureID() {
		return m_intTextureID;
	}

	@Override
	public void setCamera(Camera camera) {
		m_objCamera = camera;
	}

	@Override
	public void setShader(Shader shader) 
	{
		m_objShader = shader;
	}

	@Override
	public void setLightning(Lightning lightning) {}

	@Override
	public void setMaterial(Material material) {}

	@Override
	public void setTextureID(int texture) {
		m_intTextureID = texture;
	}

	@Override
	public SupportType getRenderingType() {
		return SupportType.Triangles;
	}

	@Override
	public FloatBuffer getVertexBuffer() {
		return m_objVertexBuffer;
	}

	@Override
	public ShortBuffer getIndexBuffer() {
		return m_objIndexBuffer;
	}

	@Override
	public int getTotal_F_Components() {
		return m_intFcomponent;
	}

	@Override
	public boolean getTextureEnable() {
		return m_bTexture;
	}

	@Override
	public MeshObject getMesh() {
		return null;
	}

	@Override
	public void setMesh(MeshObject mesh) {}

	@Override
	public void setRenderingType(SupportType type) {}

	@Override
	public void setTextureEnable(boolean value) 
	{
		m_bTexture = value;
	}

	@Override
	public boolean getTextureColorOnlyEnable() {
		return false;
	}

	@Override
	public void setTextureColorOnlyEnable(boolean value) {}
	
}
