/**
 * @author Han Lin
 * Created by Han Lin on v0.5
 */
package JUST_RUN.RendererEngine.Drawing;

public class SpriteMesh extends MeshObject
{
	private float[] m_fTexture, m_fCurrentVertices, m_fCurrentNormal;
	private short[] m_fIndices;
	private SpritePose[] m_objPose;
	
	public SpriteMesh(SpritePose[] pose, float[] texture, short[] indices, int numOfVertices) throws Exception
	{
		if(pose.length < 1)
			throw new Exception("Error at SpriteMesh.class: Pose cannot be empty.");
		m_objPose = pose;
		m_fTexture = texture;
		m_fIndices = indices;
		m_intFcomponent = numOfVertices;
		m_fCurrentVertices = new float[m_intFcomponent];
		m_fCurrentNormal = new float[m_intFcomponent];
		this.m_bHasNormal = true;
		this.m_bHasTexture = true;
		preCompute();
	}
	
	public SpritePose getPose(int i)
	{
		if(i < 0 || i >= m_objPose.length)
			return null;
		return m_objPose[i];
	}
	
	public float[] getTexture()
	{
		return m_fTexture;
	}
	
	public float[] getVertices()
	{
		return m_fCurrentVertices;
	}
	
	public float[] getNormal()
	{
		return m_fCurrentNormal;
	}
	
	public short[] getIndices()
	{
		return m_fIndices;
	}
	
	public Keyframe getKeyframe(int pose, int keyframe)
	{
		if(pose < 0 || pose >= m_objPose.length)
			return null;
		if(keyframe < 0 || keyframe >= m_objPose[pose].getNumOfKeyframe())
			return null;
		return m_objPose[pose].getKeyFrame(keyframe);
	}
	
	public int getNumOfKeyframe(int pose)
	{
		if(pose < 0 || pose >= m_objPose.length)
			return -1;
		return m_objPose[pose].getNumOfKeyframe();
	}
	
	public int getNumOfVertices()
	{
		return m_intFcomponent;
	}
	
	public int getNumOfPoses()
	{
		return m_objPose.length;
	}
	
	public void setCurrentFrameData(int pose, int keyframe)
	{
		if(pose < 0 || pose >= m_objPose.length)
			return;
		if(keyframe < 0 || keyframe >= m_objPose[pose].getNumOfKeyframe())
			return;
		
		//m_fCurrentNormal = m_objPose[pose].getKeyFrame(keyframe).getNormals().clone();
		//m_fCurrentVertices = m_objPose[pose].getKeyFrame(keyframe).getVertices().clone();
		this.setBuffer(m_objPose[pose].getKeyFrame(keyframe).getVertexBuffer(), 
				m_objPose[pose].getKeyFrame(keyframe).getNormalBuffer());
	}
	
	public void setVertex(int i, float value)
	{
		if(i < 0 || i >= m_intFcomponent)
			return;
		m_fCurrentVertices[i] = value;
	}
	
	public void setNormal(int i, float value)
	{
		if(i < 0 || i >= m_intFcomponent)
			return;
		m_fCurrentNormal[i] = value;
	}
	
	public final void Compute()
	{
		//try
		//{
			//this.ComputeBuffer(m_fCurrentVertices, m_fCurrentNormal, m_fTexture, m_fIndices);
		//}catch(Exception e){}
	}

	private void preCompute()
	{
		m_fCurrentVertices = m_objPose[0].getKeyFrame(0).getVertices().clone();
		m_fCurrentNormal = m_objPose[0].getKeyFrame(0).getNormals().clone();
		//Compute();
		this.ComputeBuffer(null, null, m_fTexture, m_fIndices);
	}
}