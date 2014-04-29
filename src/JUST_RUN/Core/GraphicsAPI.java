/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.Core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import JUST_RUN.RendererEngine.Lightning;
import JUST_RUN.RendererEngine.Material;
import JUST_RUN.RendererEngine.Texture;
import JUST_RUN.RendererEngine.Vector;

public class GraphicsAPI {

	public static enum SupportType
	{
		Triangle_Fan,
		Triangle_Strip,
		Triangles,
		Mesh,
		Lines
	}
	
	public static void renderLines(int shader, int faces, ShortBuffer indexBuffer, FloatBuffer vertices, Vector color)
	{
		/*
		 * Set up vertex coordinate
		 */
		GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "vertexPosition"), 
			3, GLES20.GL_FLOAT, false, 0, vertices);
		GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "vertexPosition"));
		// Shader : Texture Enabling
		GLES20.glUniform1f(GLES20.glGetUniformLocation(shader, "hasTexture"), 0.0f);
		/*
		 * Set vertex color
		 */
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "color"), 1, color.getArray4v(), 0);
		/*
		 * Invoke OpenGL to draw
		 */
		GLES20.glDrawElements(GLES20.GL_LINE_LOOP, faces, 
				GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
	
	public static void renderRectangle(int shader, int total_f_component, Vector fcolor, Vector bcolor,  FloatBuffer vertices, 
			FloatBuffer textures, ShortBuffer indexBuffer,
			int textureID, boolean hasTexture)
	{
		/*
		 * Set up vertex coordinate
		 */
		GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "vertexPosition"));
		GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "vertexPosition"), 
			3, GLES20.GL_FLOAT, false, 0, vertices);
		// Shader : Texture Enabling
		GLES20.glUniform1f(GLES20.glGetUniformLocation(shader, "hasTexture"),
			(hasTexture ? 1.0f : 0.0f));
		
		/*
		 * Set vertex color
		 */
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "color"), 1, fcolor.getArray4v(), 0);
		
		if(bcolor != null)
		{
			GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "fcolor"), 1, fcolor.getArray4v(), 0);
			GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "bcolor"), 1, bcolor.getArray4v(), 0);
		}
		
		/*
		 * Texture Binding
		 */
		if (hasTexture) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Texture.getHandle().getTextureID()[textureID]);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(shader, "texture0"), 0);
			
			/*
			 * Set up Texture Coordinate
			 */
			vertices.position(0);
			GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "textureCoord"), 
				2, GLES20.GL_FLOAT, false, 0, textures);
			GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "textureCoord"));
		}

		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, total_f_component, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
	
	public static void renderTriangle(SupportType type, int shader, int total_f_component, ShortBuffer indexBuffer,
			FloatBuffer vertices, FloatBuffer textures, FloatBuffer normals, int textureID, 
			boolean hasTexture, boolean useTextureColor)
	{
		setUpTriangle(shader, vertices, textures, normals, textureID, hasTexture, useTextureColor);
		/*
		 * Invoke OpenGL to draw
		 */
		switch(type)
		{
			case Triangle_Fan:
				GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, total_f_component, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
				break;
			case Triangle_Strip:
				GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, total_f_component, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
				break;
			case Triangles:
				GLES20.glDrawElements(GLES20.GL_TRIANGLES, total_f_component, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
				break;
			case Mesh:
				GLES20.glDrawElements(GLES20.GL_LINES, total_f_component, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
				break;
		}
	}
	
	private static void setUpTriangle(int shader,  FloatBuffer vertices, 
			FloatBuffer textures, FloatBuffer normals, int textureID, 
			boolean hasTexture, boolean useTextureColor)
	{
		/*
		 * Set up vertex coordinate
		 */
		GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "vertexPosition"), 
			3, GLES20.GL_FLOAT, false, 0, vertices);
		GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "vertexPosition"));
		
		/*
		 * Set up normal vector
		 */
		GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "vertexNormal"), 
			3, GLES20.GL_FLOAT, false, 0, normals);
		GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "vertexNormal"));
		
		// Shader : Texture Enabling
		GLES20.glUniform1f(GLES20.glGetUniformLocation(shader, "hasTexture"),
			(hasTexture ? 1.0f : 0.0f));
		
		GLES20.glUniform1f(GLES20.glGetUniformLocation(shader, "useTextureColor"),
				(useTextureColor ? 1.0f : 0.0f));
		
		/*
		 * Texture Binding
		 */
		if (hasTexture) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Texture.getHandle().getTextureID()[textureID]);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(shader, "texture0"), 0);

			/*
			 * Set up Texture Coordinate
			 */
			GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(shader, "textureCoord"), 
				2, GLES20.GL_FLOAT, false, 0, textures);
			GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(shader, "textureCoord"));
		}
	}
	
	public static void setBackfaceCulling(boolean value)
	{
		if(value)
		{
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			GLES20.glCullFace(GLES20.GL_BACK);
		}
		else
			GLES20.glDisable(GLES20.GL_CULL_FACE);
	}
	
	public static void setDepthTest(boolean value)
	{
		if(value)
		{
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		}
		else
		{
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		}
		
		GraphicsAPI.setBackfaceCulling(value);
		GLES20.glDepthMask(value);
	}
	
	public static void standardInit()
	{
		GLES20.glClearDepthf(1.0f);
		GraphicsAPI.setBackfaceCulling(true);
		GraphicsAPI.setDepthTest(true);
		GraphicsAPI.setBlendFunc(true);
		GLES20.glFrontFace(GLES20.GL_CCW);
		
	}
	
	public static void setBlendFunc(boolean value)
	{
		if(value)
		{
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
		else
		{
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}
	
	public static void onDrawInit(int shader)
	{
		GraphicsAPI.clearBuffer(new Vector());
		GLES20.glUseProgram(shader);
	}
	
	public static void clearBuffer(Vector color)
	{
		GLES20.glClearColor(color.X, color.Y, color.Z, color.A);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void setGraphicsShader(int shader)
	{
		GLES20.glUseProgram(shader);
	}
	
	public static void setShaderModelView(float[] matrix, int shader)
	{
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shader, "uMVMatrix"),
				1, false, matrix, 0);
		
		// Create the normal of model-view matrix
		float[] normalMatrix = new float[16];
		Matrix.invertM(normalMatrix, 0, matrix, 0);
		Matrix.transposeM(normalMatrix, 0, normalMatrix, 0);

		// send to the shader
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shader, "normalMatrix"), 
				1, false, matrix, 0);
	}
	
	public static void setShaderModelViewProjection(float[] matrix, int shader)
	{
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(shader, "uMVPMatrix"), 
			1, false, matrix, 0);
	}
	
	public static void setEyePosition(Vector position, int shader)
	{
		GLES20.glUniform3fv(
			GLES20.glGetUniformLocation(shader, "eyePos"),
			1, position.getArray3v(), 0);
	}
	
	public static void setViewPort(int x, int y, int width, int height)
	{
		GLES20.glViewport(x, y, width, height);
	}
	
	public static void setLightning(Lightning light, int shader)
	{
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "lightPos"), 1, light.getPosition(), 0);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "lAmbient"), 1, light.getAmbient(), 0);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "lDiffuse"), 1, light.getDiffuse(), 0);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "lSpecular"), 1, light.getSpecular(), 0);
	}
	
	public static void setMaterial(Material material, int shader)
	{
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "matAmbient"), 1, material.getAmbient().getArray4v(), 0);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "matDiffuse"), 1, material.getDiffuse().getArray4v(), 0);
		GLES20.glUniform4fv(GLES20.glGetUniformLocation(shader, "matSpecular"), 1, material.getSpecular().getArray4v(), 0);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(shader, "matShininess"), material.getShineness());
	}
	
	public static void textureInit(int ID)
	{
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ID);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
	}
	
	public static void generateTexture(int length, int[] storeIn)
	{
		GLES20.glGenTextures(length, storeIn, 0);
	}
	
	public static void createTexture(Bitmap bitmap)
	{
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	}
	
	 public static void AlertBox(Context context, String title, String msg)   {  
		new AlertDialog.Builder(context)
			.setMessage(msg)
			.setTitle(title)
			.setCancelable(true)
			.setNeutralButton(android.R.string.cancel,
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton)
						{}
					})
			.show();   
			
	}
}
