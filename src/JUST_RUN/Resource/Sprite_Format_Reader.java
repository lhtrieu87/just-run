package JUST_RUN.Resource;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import JUST_RUN.RendererEngine.Drawing.Keyframe;
import JUST_RUN.RendererEngine.Drawing.SpriteMesh;
import JUST_RUN.RendererEngine.Drawing.SpritePose;

public class Sprite_Format_Reader 
{
	private SpriteMesh m_objMesh;
	private SpritePose[] m_objPoses;
	private float[] m_fVertices, m_fNormal, m_fTexture;
	private short[] m_fIndices;
	private Keyframe[] m_objKeyFrame;
	private int m_intVertices, m_intID;
	private Context m_objContext;
	
	public Sprite_Format_Reader(Context context, int ID)
	{
		m_objContext = context;
		m_intID = ID;
		try
		{
			Load();
			m_objMesh = new SpriteMesh(m_objPoses, m_fTexture, m_fIndices, m_intVertices);
		}catch(Exception e){ System.out.println(e); }
	}
	
	public SpriteMesh getMesh()
	{
		return m_objMesh;
	}
	
	public void Load() throws Exception
	{
		InputStream fstream = m_objContext.getResources().openRawResource(m_intID);
		DataInputStream reader = new DataInputStream(fstream);
        
        m_intVertices = (int)reader.readFloat();
        int numOfPoses = (int)reader.readFloat();
        m_objPoses = new SpritePose[numOfPoses];
        m_fNormal = new float[m_intVertices * 3];
        m_fTexture = new float[m_intVertices * 2];
        m_fVertices = new float[m_intVertices * 3];
        m_fIndices = new short[m_intVertices];
        
        // read UV texture coordinates
        int state  = 0, i1 = 0;
        short i2 = 0;
        while(true)
        {
        	float value = reader.readFloat();
        	if(value == -200) // finished reading UV texture coordinate
        		break;
        	m_fTexture[i1++] = value;
        }
        int numOfKeyframe = (int)reader.readFloat(), keyframe = 0, pose = 0;
        i1 = i2 = 0;
        m_objKeyFrame = new Keyframe[numOfKeyframe];
        // reading for each pose
        try
        {
	        while(true)
	        {
	        	float value = reader.readFloat();
	        	if(value == -200)
	        	{
	        		m_objPoses[pose++] = new SpritePose(m_objKeyFrame);
	        		keyframe = 0;
	        		// read P value
	        		numOfKeyframe = (int)reader.readFloat();
	        	}
	        	else if(value == -300)
	        	{
	        		i1 = 0;
	        		i2 = 0;
	        		// read K value
	        		int index = (int)reader.readFloat();
	        		m_objKeyFrame[keyframe++] = new Keyframe(index, m_fVertices.clone(), m_fNormal.clone());
	        	}
	        	else 
	        	{
	        		state++;
	        		if(i2 < m_intVertices)
        				m_fIndices[i2] = i2;
	        		
	        		switch(state)
	        		{
	        		case 1:
	        			m_fVertices[i1++] = value;
	        			break;
	        		case 2:
	        			m_fVertices[i1++] = value;
	        			break;
	        		case 3:
	        			m_fVertices[i1++] = value;
	        			break;
	        		case 4:
	        			m_fNormal[i2++] = value;
	        			break;
	        		case 5:
	        			m_fNormal[i2++] = value;
	        			break;
	        		case 6:
	        			m_fNormal[i2++] = value;
	        			state = 0;
	        			break;
	        		}
	        	}
	        }
        }catch(Exception e)
        {
        	
        }finally
        {
        	reader.close();
            fstream.close();
        }
	}
	
	public void Load2() throws Exception 
	{
		InputStream input = m_objContext.getResources().openRawResource(m_intID);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        
        m_intVertices = Integer.parseInt(br.readLine());
        int numOfPoses = Integer.parseInt(br.readLine());
        m_objPoses = new SpritePose[numOfPoses];
        m_fNormal = new float[m_intVertices * 3];
        m_fTexture = new float[m_intVertices * 2];
        m_fVertices = new float[m_intVertices * 3];
        m_fIndices = new short[m_intVertices];
        
        // read UV texture coordinates
        int c = -1, state  = 0, i1 = 0;
        short i2 = 0;
        char ch;
        StringBuffer strBuf = new StringBuffer();
        while((c = br.read()) != -1)
        {
        	ch = (char)c;
        	
        	if(ch == 'p' || ch == 'P') // finished reading UV texture coordinate
        		break;
        	
        	if(ch == ' ' || ch == '\n' || ch == '\r') // reached delimiter or reached next line character
        	{
        		if(ch == '\n' || ch == '\r') // reached next line
        		{
        			if(i2 < m_intVertices)
        				m_fIndices[i2] = i2++;
        		}
        		if(strBuf.length() == 0)
            		continue;
        		float value = Float.parseFloat(strBuf.toString());
        		m_fTexture[i1++] = value;
        		strBuf = new StringBuffer();
        	}
        	else if(ch >= '0' && ch <= '9' || ch == '.' || ch == '-' || ch == 'E')
       	 	{
        		/*
				 * Due to precision issue using division, 
				 * default string to float conversion method is used.
				 * Thus, it creates some performance overhead. 
				 */
				 strBuf.append(ch);
       	 	}
        }
        int numOfKeyframe = Integer.parseInt(br.readLine().trim()), keyframe = 0, pose = 0;
        i1 = i2 = 0;
        m_objKeyFrame = new Keyframe[numOfKeyframe];
        strBuf = new StringBuffer();
        
        // reading for each pose
        while((c = br.read()) != -1)
        {
        	ch = (char)c;
        	if(ch == 'p' || ch == 'P')
        	{
        		String str  = br.readLine();
        		m_objPoses[pose++] = new SpritePose(m_objKeyFrame);
        		keyframe = 0;
        		// read P value
        		if(str != null)
        			numOfKeyframe = Integer.parseInt(str.trim());
        	}
        	else if(ch == 'K' || ch == 'k')
        	{
        		i1 = 0;
        		i2 = 0;
        		String str  = br.readLine();
        		if(str == null)
        			continue;
        		// read K value
        		int index = Integer.parseInt(str.trim());
        		// clone is necessary because an array in Java is a reference type
        		m_objKeyFrame[keyframe++] = new Keyframe(index, m_fVertices.clone(), m_fNormal.clone());
        	}
        	else if(ch == ' ')
        	{
        		if(strBuf.length() == 0)
            		continue;
        		float value = Float.parseFloat(strBuf.toString());
        		strBuf = new StringBuffer();
        		state++;
        		switch(state)
        		{
        		case 1:
        			m_fVertices[i1++] = value;
        			break;
        		case 2:
        			m_fVertices[i1++] = value;
        			break;
        		case 3:
        			m_fVertices[i1++] = value;
        			break;
        		case 4:
        			m_fNormal[i2++] = value;
        			break;
        		case 5:
        			m_fNormal[i2++] = value;
        			break;
        		case 6:
        			m_fNormal[i2++] = value;
        			break;
        		}
        	}
        	else if(ch >= '0' && ch <= '9' || ch == '.' || ch == '-' || ch == 'E')
       	 	{
        		/*
				 * Due to precision issue using division, 
				 * default string to float conversion method is used.
				 * Thus, it creates some performance overhead. 
				 */
				 strBuf.append(ch);
       	 	}
        	else if(ch == '\n')
        	{
        		state = 0;
        	}
        }
        
        br.close();
        input.close();
	}
}
