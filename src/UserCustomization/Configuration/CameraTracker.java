/**
 * Move the camera along the track and keep the player
 * in view.
 * @author Le Hong Trieu
 */

package UserCustomization.Configuration;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Vector;
import UserCustomization.Character.Character;

public class CameraTracker 
{
	protected final int CENTER_LANE = 1;
	
	/**
	 * The player's character which the camera follows.
	 */
	protected Character m_objCharacter;
	protected Camera m_objCamera;
	protected WorldMap m_objWayPoint;
	
	protected float m_fFrontOffset;
	protected float m_fBackOffset;
	protected float m_fCameraHeight;
	protected float m_flyHeight;
	
	protected Vector m_vecDirection;
	
	public CameraTracker (Character character, Camera camera, WorldMap wayPoint)
	{
		m_objCharacter = character;
		m_objCamera = camera;
		
		m_fFrontOffset = 15.0f;
		m_fBackOffset = -3.0f;
		m_fCameraHeight = 3.0f;
		m_flyHeight = 0.0f;
		
		m_objWayPoint = wayPoint;
		
		m_vecDirection = new Vector (0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * The method is called every frame in the game loop
	 * to update the position and the look at direction 
	 * of the camera.
	 */
	public void UpdateObjState ()
	{
		cameraFollowPlayer ();
	}
	
	/**
	 * Move the camera along the track by following the player's character
	 * by computing the camera's position and look-at direction.
	 */
	protected void cameraFollowPlayer ()
	{
		Vector characterPos = m_objCharacter.getPosition ();
		// The next way point which the character is currently heading to.
		int wayPoint = m_objCharacter.getNextWayPoint ();
		
		Vector p = m_objWayPoint.getPosition(CENTER_LANE, wayPoint);
		Vector n = p;
		
		Vector toWayPoint = Vector.Subtract3v (p, characterPos);
		
		Vector nextWayPoint = m_objWayPoint.getPosition(m_objCharacter.getLane (), m_objCharacter.getNextWayPoint ());
		Vector prevWayPoint = m_objWayPoint.getPosition(m_objCharacter.getLane (), m_objCharacter.getNextWayPoint () - 1);
		Vector forwardVec = Vector.Subtract3v (nextWayPoint, prevWayPoint).getUnitVector3v ();
		float dot = Vector.Dot3v (toWayPoint, forwardVec);
		
		if (dot > 0)
		{
			while (dot > 0)
			{
				p = n;
				wayPoint--;
				n = m_objWayPoint.getPosition(CENTER_LANE, wayPoint);
				toWayPoint = Vector.Subtract3v (n, characterPos);
				dot = Vector.Dot3v (toWayPoint, forwardVec);
			}
		}	
		else
		{
			while (dot <= 0)
			{
				n = p;
				wayPoint++;
				p = m_objWayPoint.getPosition(CENTER_LANE, wayPoint);
				toWayPoint = Vector.Subtract3v (p, characterPos);
				dot = Vector.Dot3v (toWayPoint, forwardVec);
			}
		}
		
		Vector n2p = Vector.Subtract3v (p, n).getUnitVector3v ();
		n2p = Vector.Add3v (n2p, m_vecDirection).getUnitVector3v ();
		m_vecDirection = n2p;
		
		Vector n2Character = Vector.Subtract3v (characterPos, n);
		float profOnN2P = Vector.Dot3v (n2p, n2Character);
		Vector h = Vector.Add3v (n, Vector.Multiply3v (n2p, profOnN2P));
		
		Vector trackTangent = Vector.Cross (new Vector (0.0f, 1.0f, 0.0f), n2p);
		Vector trackNormal = Vector.Cross (n2p, trackTangent);
		
		// Where the camera looks at.
		Vector cameraLookAt = Vector.Add3v (h, Vector.Multiply3v (n2p, m_fFrontOffset)); 
		cameraLookAt = Vector.Add3v (cameraLookAt, Vector.Multiply3v (trackNormal, m_flyHeight));
		m_objCamera.setLookAt(cameraLookAt);
		
		// Where the camera is.
		Vector cameraPosition;
		if (m_flyHeight == 0.0f)
		{
			cameraPosition = Vector.Add3v (h, Vector.Multiply3v (n2p, m_fBackOffset));
			cameraPosition = Vector.Add3v (cameraPosition, Vector.Multiply3v (trackNormal, m_fCameraHeight));
		}
		else
		{
			cameraPosition = h;
			cameraPosition = Vector.Add3v (cameraPosition, Vector.Multiply3v (trackNormal, m_flyHeight));
		}

		m_objCamera.setEyePosition (cameraPosition);
		m_objCamera.setUpVector (trackNormal);
		
		m_objCharacter.ResetTransformation ();
	}
	
	public void SetFlyHeight (float flyHeight)
	{
		m_flyHeight = flyHeight;
	}
	
	public void StopFlying ()
	{
		m_flyHeight = 0.0f;
	}
}
