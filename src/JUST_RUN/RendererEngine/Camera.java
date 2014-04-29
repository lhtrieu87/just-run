/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.RendererEngine;

import android.opengl.Matrix;

public class Camera {
	private Vector m_objEyePosition, m_objLookAt, m_objUp;
	private float[] m_fMVPMatrix = new float[16];
	private float[] m_fProjMatrix = new float[16]; // projection matrix
	
	public Camera()
	{ 
		setIdentity(m_fMVPMatrix);
		setIdentity(m_fProjMatrix);
	}
	
	public static void setIdentity(float[] matrix)
	{
		Matrix.setIdentityM(matrix, 0);
	}
	
	public void initCameraPosition(float[] m_fVMatrix)
	{

		Matrix.setLookAtM(m_fVMatrix, 0, 
			m_objEyePosition.X, m_objEyePosition.Y, m_objEyePosition.Z, 
			m_objLookAt.X, m_objLookAt.Y, m_objLookAt.Z, 
			m_objUp.X, m_objUp.Y, m_objUp.Z);
	}
	
	public void setEyePosition(Vector position)
	{
		this.m_objEyePosition = position;
	}
	
	public Vector getEyePosition()
	{
		return m_objEyePosition;
	}
	
	public float[] getProjectionMatrix()
	{
		return m_fProjMatrix;
	}
	
	public void setLookAt(Vector position)
	{
		this.m_objLookAt = position;
	}
	
	public void setUpVector(Vector position)
	{
		this.m_objUp = position;
	}
	
	/**
	 * This function is used to set up model-view transformation matrix
	 * @param m_fVMatrix (float[]) : one 16-columns elements of float array containing the 4x4 model-view matrix
	 * @param m_fTransform (float[]): one 16-columns elements of float array containing the 4x4 affine transformation matrix
	 */
	public static void setViewTransform(float[] m_fVMatrix, float[] m_fTransform)
	{
		float[] temp = new float[16];
		float[] temp2 = new float[16];
		Matrix.multiplyMM( temp2, 0, m_fVMatrix, 0, m_fTransform, 0);
		setIdentity(temp);
		Matrix.multiplyMM( m_fVMatrix, 0, temp2, 0, temp, 0);
	}
	
	public static void setViewRotation(float[] m_fVMatrix, float angle_inDegree, Vector axis)
	{
		float[] temp = new float[16];
		float[] temp2 = new float[16];
		Matrix.setRotateM(temp, 0, angle_inDegree, 
				axis.X, axis.Y, axis.Z);
		Matrix.multiplyMM(temp2, 0, m_fVMatrix, 0, temp, 0);
		setIdentity(temp);
		Matrix.multiplyMM(m_fVMatrix, 0, temp2, 0, temp, 0);
		temp = temp2 = null;
	}
	
	public static void setViewTranslation(float[] m_fVMatrix, Vector position)
	{
		float[] temp = new float[16];
		float[] temp2 = new float[16];
		setIdentity(temp);
		Matrix.translateM(temp, 0, 
				position.X, position.Y, position.Z);
		Matrix.multiplyMM(temp2, 0, m_fVMatrix, 0, temp, 0);
		setIdentity(temp);
		Matrix.multiplyMM(m_fVMatrix, 0, temp2, 0, temp, 0);
		temp = temp2 = null;
	}
	
	public static void setViewScaling(float[] m_fVMatrix, Vector scale)
	{
		float[] temp = new float[16];
		float[] temp2 = new float[16];
		setIdentity(temp);
		Matrix.scaleM(temp, 0, scale.X, scale.Y, scale.Z);
		Matrix.multiplyMM(temp2, 0, m_fVMatrix, 0, temp, 0);
		setIdentity(temp);
		Matrix.multiplyMM(m_fVMatrix, 0, temp2, 0, temp, 0);
		temp = temp2 = null;
	}

	public void computeModelViewProjectionMatrix(float[] m_fVMatrix)
	{
		Matrix.multiplyMM(m_fMVPMatrix, 0, m_fProjMatrix, 0, m_fVMatrix, 0);
	}
	
	public float[] getModelViewProjectionMatrix()
	{
		return m_fMVPMatrix;
	}
	
	public void setProjectionMatrix(float[] matrix)
	{
		m_fProjMatrix = matrix;
	}
	
	public void setPerspective(float fov, float aspectratio, float near, float far)
	{
		float halfsize = (float)(near * Math.tan(Math.toRadians(fov) / 2.0));
		setProjectionFrustum(-halfsize, halfsize, -halfsize / aspectratio, halfsize / aspectratio, near, far);
		
	}
	
	public void setProjectionFrustum(float left, float right, float bottom, float top, float near, float far)
	{
		// Note: The result will be stored in m_fProjMatrix
		Matrix.frustumM(m_fProjMatrix, 0, left, right, bottom, top, near, far);
	}
	
	public void setOrthographics(float left, float right, float bottom, float top, float near, float far)
	{
		Matrix.orthoM(m_fProjMatrix, 0, left, right, bottom, top, near, far);
	}
}
