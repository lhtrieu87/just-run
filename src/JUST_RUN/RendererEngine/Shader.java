/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.2 -- no longer read shader code from the resource file
 *          by Han Lin on v0.3 -- added Uber Lightning shader 
 */
package JUST_RUN.RendererEngine;

import android.opengl.GLES20;
import android.util.Log;

public class Shader {
	
	private String m_strVertexShader_Code, m_strFragmentShader_code;
	private int m_intVertexHandle, m_intFragmentHandle, m_intShaderProgramHandle;
	
	public int getVertexHandle()
	{
		return m_intVertexHandle;
	}
	
	public int getFragmentHandle()
	{
		return m_intFragmentHandle;
	}
	
	public int getShaderProgramHandle()
	{
		return m_intShaderProgramHandle;
	}
	
	public void setVertexHandle(int handle)
	{
		m_intVertexHandle = handle;
	}
	
	public void setFragmentHandle(int handle)
	{
		m_intFragmentHandle = handle;
	}
	
	public void setShaderProgramHandle(int handle)
	{
		m_intShaderProgramHandle = handle;
	}
	
	public static enum Type
	{
		Font,
		Phong,
		Gouraud,
		Simple,
		UberLight, 
		Custom
	}
	
	public void setVertexShader(String code)
	{
		this.m_strVertexShader_Code = code;
	}
	
	public void setFragmentShader(String code)
	{
		this.m_strFragmentShader_code = code;
	}
	
	/* Class Constructors */
	public Shader()
	{ }
	
	public Shader(Type shaderType)
	{
		Setup(shaderType);
	}
	
	public void Setup(Type shaderType)
	{
		try
		{
			switch(shaderType)
			{
				default:
				case Font:
					m_strVertexShader_Code = ShaderLanguage.Font_Vertex_Shader;
					m_strFragmentShader_code = ShaderLanguage.Font_Fragment_Shader;
					break;
				case Phong:
					m_strVertexShader_Code = ShaderLanguage.Phong_Vertex_Shader;
					m_strFragmentShader_code = ShaderLanguage.Phong_Fragment_Shader;
					break;
				case Gouraud:
					m_strVertexShader_Code = ShaderLanguage.Gouraud_Vertex_Shader;
					m_strFragmentShader_code = ShaderLanguage.Gouraud_Fragment_Shader;
					break;
				case Simple:
					m_strVertexShader_Code = ShaderLanguage.Simple_Vertex_Shader;
					m_strFragmentShader_code = ShaderLanguage.Simple_Fragment_Shader;
					break;
				case UberLight:
					//m_strVertexShader_Code = ShaderLanguage.UberLight_Vertex_Shader;
					//m_strFragmentShader_code = ShaderLanguage.UberLight_Fragment_Shader;
					break;
				case Custom:
					// do nothing since user has specified their shader code
					break;
			}
		}
		catch(Exception e)
		{
			Log.d("Reading Shader Code", e.toString());
		}
		finally
		{
			try
			{
				createShader(); // create OpenGL Shader
			}catch(Exception e)
			{
				Log.d("Create Shader Program", e.toString());
			}
		}
	}
	
	private void createShader() throws Exception
	{
		// loading and compile Vertex Shader
		m_intVertexHandle = loadShader(GLES20.GL_VERTEX_SHADER, m_strVertexShader_Code);
		if(m_intVertexHandle == 0)
		{
			throw new Exception("Unable to load Vertex Shader");
		}		
		// loading and compile Fragment Shader
		m_intFragmentHandle = loadShader(GLES20.GL_FRAGMENT_SHADER, m_strFragmentShader_code);
		if(m_intFragmentHandle == 0)
		{
			throw new Exception("Unable to load Fragment Shader");
		}
		// creating OpenGL Shader Program
		m_intShaderProgramHandle = GLES20.glCreateProgram();
		if(m_intShaderProgramHandle != 0)
		{
			GLES20.glAttachShader(m_intShaderProgramHandle, m_intVertexHandle);
			GLES20.glAttachShader(m_intShaderProgramHandle, m_intFragmentHandle);
			GLES20.glLinkProgram(m_intShaderProgramHandle);
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(m_intShaderProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if(linkStatus[0] == 0)
			{
				GLES20.glDeleteProgram(m_intShaderProgramHandle);
				m_intShaderProgramHandle = 0;
				throw new Exception("Unable to link shader program.");
			}
		}
		else
		{
			throw new Exception("Unable to create shader program.");
		}
		
		// release resources
		m_strFragmentShader_code = m_strVertexShader_Code = null;
	}
	
	private int loadShader(int type, String code)
	{
		int handle = GLES20.glCreateShader(type);
		if(handle != 0)
		{
			GLES20.glShaderSource(handle, code);
			GLES20.glCompileShader(handle);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if(compiled[0] == 0)
			{
				Log.e("Shader", "Could not compile shader " + type + ":");
				Log.e("Shader", GLES20.glGetShaderInfoLog(handle));
				GLES20.glDeleteShader(handle);
				handle = 0;
			}
		}
		
		return handle;
	}
	
	/*
	 * No longer used -- discarded on v0.2
	 */
	/*
	private void convertRaw2VertexShader(int ID) throws Exception
	{
		try
		{
			m_strVertexShader_Code = readRaw(ID);
		}catch(Exception e)
		{
			throw new Exception("Create Vertex Shader:\n" + e);
		}
	}
	
	private void convertRaw2FragmentShader(int ID) throws Exception
	{
		try
		{
			m_strFragmentShader_code = readRaw(ID);
		}catch(Exception e)
		{
			throw new Exception("Create Fragment Shader:\n" + e);
		}
	}
	
	private String readRaw(int ID) throws Exception
	{
		StringBuffer str = new StringBuffer();
		
		try
		{
			InputStream inputStream = m_objContext.getResources().openRawResource(ID);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String read = in.readLine();
			while (read != null) {
				str.append(read + "\n");
				read = in.readLine();
			}
			str.deleteCharAt(str.length() - 1);
			in.close();
			inputStream.close();
		}catch(Exception e)
		{
			throw new Exception(e);
		}
		
		return str.toString();
	}
	*/
	/*
	 * Edit on v0.2
	 */
	private static class ShaderLanguage
	{
		public static String Gouraud_Vertex_Shader = 
				"precision mediump float;\n" +
				"uniform mat4 uMVPMatrix;\n" +
				"uniform mat4 uMVMatrix;\n" +
				"uniform mat4 normalMatrix;\n" +
				"uniform bool inverseNormal;\n" +
				"uniform vec4 lightPos;\n" +
				"uniform vec4 lAmbient;\n" +
				"uniform vec4 lDiffuse;\n" +
				"uniform vec4 lSpecular;\n" +
				"uniform vec4 matAmbient;\n" +
				"uniform vec4 matDiffuse;\n" +
				"uniform vec4 matSpecular;\n" +
				"uniform float matShininess;\n" +
				"attribute vec2 textureCoord;\n" +
				"attribute vec4 vertexPosition;\n" +
				"attribute vec3 vertexNormal;\n" +
				"varying vec4 color;\n" +
				"varying vec2 tCoord;\n" +
				"void main() {\n" +
				"	vec4 ecPosition = uMVMatrix * vertexPosition;\n" +
				"	tCoord = textureCoord;\n" +
				"	gl_Position = uMVPMatrix * vertexPosition;\n" +
				"	vec3 N = normalize(vec3(normalMatrix * vec4(vertexNormal, 0.0)));\n" +
				"	vec3 E = normalize(-ecPosition.xyz);\n" +
				"	vec3 L = normalize(lightPos.xyz - ecPosition.xyz);\n" +
				"	vec3 R = reflect(-L, N);\n" +
				"	vec4 ambientTerm = lAmbient * matAmbient;\n" +
				"	vec4 diffuseTerm = lDiffuse * matDiffuse * max(dot(N, L), 0.0);\n" +
				"	vec4 specularTerm = lSpecular * matSpecular * pow(max(dot(R, E), 0.0), matShininess);\n" +
				"	color = ambientTerm + diffuseTerm + specularTerm;\n" +
				"}\n";
		
		public static String Gouraud_Fragment_Shader = 
				"precision mediump float;\n" +
				"uniform sampler2D texture0;\n" +
				"uniform bool hasTexture;\n" +
				"uniform bool useTextureColor;\n" +
				"varying vec2 tCoord;\n" +
				"varying vec4 color;\n" +
				"void main () {\n" +
				"	if(hasTexture)\n" +
				"	{\n" +
				"		vec4 textureColor = texture2D(texture0, tCoord);\n" +
				"		if(useTextureColor)\n" +
				"			gl_FragColor = textureColor;	\n" +
				"		else\n" +
				"			gl_FragColor = textureColor + color;\n" +
				"	}\n" +
				"	else\n" +
				"	{\n" +
				"		gl_FragColor = color;\n" +
				"	}\n" + 
				"}";
		
		public static String Phong_Vertex_Shader = 
			"// Vertex shader Phong Shading - Per-pixel lighting\n" +
			"uniform mat4 uMVPMatrix;\n" +
			"uniform mat4 uMVMatrix;\n" +
			"uniform mat4 normalMatrix;\n" +
			"uniform bool inverseNormal;\n" +
			"attribute vec2 textureCoord;\n" +
			"attribute vec4 vertexPosition;\n" +
			"attribute vec3 vertexNormal;\n" +
			"varying vec3 ecNormal;\n" +
			"varying vec4 ecPosition;\n" +
			"varying vec2 tCoord;\n" +
			"void main() {\n" +
			"	ecNormal = vec3(normalMatrix * vec4(vertexNormal, 0.0));\n" +
			"	ecPosition = uMVMatrix * vertexPosition;\n" +
			"	tCoord = textureCoord;\n" +
			"	gl_Position = uMVPMatrix * vertexPosition;\n" +
			"}\n";
		public static String Phong_Fragment_Shader = 
			"// Fragment shader Phong Shading - Per-pixel lighting\n" +
			"precision mediump float;\n" +
			"uniform vec4 lightPos;\n" +
			"uniform vec4 lAmbient;\n" +
			"uniform vec4 lDiffuse;\n" +
			"uniform vec4 lSpecular;\n" +
			"uniform vec4 matAmbient;\n" +
			"uniform vec4 matDiffuse;\n" +
			"uniform vec4 matSpecular;\n" +
			"uniform float matShininess;\n" +
			"uniform bool hasTexture;\n" +
			"uniform bool useTextureColor;\n" +
			"uniform sampler2D texture0;\n" +
			"varying vec3 ecNormal;\n" +
			"varying vec4 ecPosition;\n" +
			"varying vec2 tCoord;\n" +
			"void main() {\n" +
			"	vec3 N = normalize(ecNormal);\n" +
			"	vec3 E = normalize(-ecPosition.xyz);\n" +
			"	vec3 L = normalize(lightPos.xyz - ecPosition.xyz);\n" +
			"	vec3 R = reflect(-L, N);\n" +
			"	vec4 ambientTerm = lAmbient * matAmbient;\n" +
			"	vec4 diffuseTerm = lDiffuse * matDiffuse * max(dot(N, L), 0.0);\n" +
			"	vec4 specularTerm = lSpecular * matSpecular * pow(max(dot(R, E), 0.0), matShininess);\n" +
			"	if(hasTexture)\n" +
			"	{\n" +
			"		vec4 textureColor = texture2D(texture0, tCoord);\n" +
			"		if(useTextureColor)\n" +
			"			gl_FragColor = textureColor;	\n" +
			"		else\n" +
			"			gl_FragColor = textureColor + diffuseTerm + specularTerm;\n" +
			"	}\n" +
			"	else\n" +
			"	{\n" +
			"		gl_FragColor = ambientTerm + diffuseTerm + specularTerm;\n" +
			"	}\n" +
			"}\n";
		public static String Simple_Vertex_Shader = 
			"uniform mat4 uMVPMatrix;\n" + 
			"attribute vec4 vertexPosition;\n" +
			"attribute vec2 textureCoord;\n" +
			"varying vec2 tCoord;\n" +
			"void main() {\n" + 
			"	tCoord = textureCoord;\n" +
			"	gl_Position = uMVPMatrix * vertexPosition;\n" +
			"}\n";
		public static String Simple_Fragment_Shader = 
			"precision mediump float;\n" +
			"uniform vec4 color;\n" +
			"uniform bool hasTexture;\n" +
			"uniform sampler2D texture0;\n" +
			"varying vec2 tCoord;\n" +
			"void main() {\n" +
			"	if(hasTexture)\n" +
			"	{\n" +
			"		vec4 texColor = texture2D(texture0, tCoord);\n" +
			"		if(texColor.a < 1.0)\n" +
			"			gl_FragColor = vec4(0.0,0.0,0.0,0.0);\n" +
			"		else\n" +
			"			gl_FragColor = vec4(vec3(texColor), color.a);\n" +
			"	}\n" +
			"	else\n" +
			"		gl_FragColor = color;\n" +
			"}\n";
		public static String Font_Vertex_Shader = 
			"uniform mat4 uMVPMatrix;\n" +
			"attribute vec4 vertexPosition;\n" +
			"attribute vec2 textureCoord;\n" +
			"varying vec2 tCoord;\n" +
			"void main(){\n" +
			"	tCoord = textureCoord;\n" + 
			"	gl_Position = uMVPMatrix * vertexPosition;\n" +
			"}\n";
		public static String Font_Fragment_Shader =
			"precision mediump float;\n" +
			"uniform vec4 fcolor;\n" +
			"uniform vec4 bcolor;\n" +
			"uniform sampler2D texture0;\n" +
			"varying vec2 tCoord;\n" + 
			"void main(){\n" +
			"	vec4 texColor = texture2D(texture0, tCoord);\n" +
			"	if(texColor.r < 0.5)\n" +
			"		gl_FragColor = vec4(vec3(texColor + bcolor), bcolor.a);\n" +
			"	else\n" +
			"		gl_FragColor = vec4(vec3(texColor * fcolor), fcolor.a);\n" +
			"}\n";
	}
	/*
	 * End edit
	 */
}
