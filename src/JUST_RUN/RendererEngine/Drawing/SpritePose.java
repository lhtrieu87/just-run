/**
 * @author Han Lin
 * Created by Han Lin on v0.5
 */
package JUST_RUN.RendererEngine.Drawing;

public class SpritePose 
{
	/*
	 * Store each keyframe of a specific pose
	 */
	private Keyframe[] m_objKeyframe;
	
	public SpritePose(Keyframe[] frames) throws Exception
	{
		if(frames.length < 1)
			throw new Exception("Error at SpritePose.class: Keyframe cannot be empty. ");
		m_objKeyframe = frames;
	}
	
	public Keyframe getKeyFrame(int i)
	{
		if(i >= m_objKeyframe.length)
			return null;
		return m_objKeyframe[i];
	}
	
	public int getNumOfKeyframe()
	{
		return m_objKeyframe.length;
	}
}