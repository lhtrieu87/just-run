/**
 * @author Han Lin
 * Created by Han Lin on v0.5
 */
package JUST_RUN.RendererEngine;

import JUST_RUN.RendererEngine.Drawing.SpriteMesh;

public class SpriteAnimator 
{
	private SpriteMesh m_objMesh;
	private int m_intCurrentPose, // current pose index
				m_intCurrentKeyframe, // current keyframe index
				m_intNextKeyframe, // next keyframe index
				m_intFrameCount; // frame count for advancing to next frame
	
	public SpriteAnimator(SpriteMesh mesh)
	{ 
		m_objMesh = mesh;
		setPose(0); // default
	}
	
	public void Interpolate()
	{
		// special case when the pose contains only frame
		if(m_intCurrentKeyframe == m_intNextKeyframe)
			return;
		
		int i1 = m_objMesh.getKeyframe(m_intCurrentPose, m_intCurrentKeyframe).Index();
		int i2 = m_objMesh.getKeyframe(m_intCurrentPose, m_intNextKeyframe).Index();
		
		if(m_intFrameCount == i1) // at initial frame
		{
			m_objMesh.setCurrentFrameData(m_intCurrentPose, m_intCurrentKeyframe);
		}
		else if(m_intFrameCount == i2) // reached next keyframe
		{
			m_objMesh.setCurrentFrameData(m_intCurrentPose, m_intNextKeyframe);
			m_intCurrentKeyframe = m_intNextKeyframe;
			m_intNextKeyframe++;
			if(m_intNextKeyframe == m_objMesh.getNumOfKeyframe(m_intCurrentPose)) // reached last frame
			{
				m_intCurrentKeyframe = m_intFrameCount = 0;
				m_intNextKeyframe = 1;
			}
		}
		
		m_intFrameCount++;
	}
	
	public void setPose(int i)
	{
		m_intCurrentPose = i; // update pose index
		m_intCurrentKeyframe = 0; // default
		// determine next keyframe index
		if(m_objMesh.getNumOfKeyframe(i) == 1)
			m_intNextKeyframe = 0;
		else
			m_intNextKeyframe = 1;
	}
}
