/**
 * @author Han Lin on v0.4
 */
package JUST_RUN.Resource;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import JUST_RUN.RendererEngine.Drawing.MeshObject;
import android.content.Context;
import android.util.Log;

/*
 * Enhanced OBJ Format reader.
 */
public class XOBJ_FORMAT_READER extends GraphicFileReader {
	private int m_intID, m_intFComponents;
	private Context m_objContext;
	private float[] m_fVertices, m_fNormal, m_fTexture;
	private short[] m_fIndices;
	
	public XOBJ_FORMAT_READER(Context context, int ID)
	{
		m_objContext = context;
		m_intID = ID;
		try
		{
			Load();
		}catch(Exception e)
		{}
		finally
		{
			m_objMesh = new MeshObject(m_fVertices, m_fNormal, m_fTexture, m_fIndices, m_intFComponents);
		}
	}
	
	@Override
	public void Load() throws Exception 
	{
		InputStream fstream = m_objContext.getResources().openRawResource(m_intID);
		DataInputStream reader = new DataInputStream(fstream);
        m_intFComponents = (int)reader.readFloat(); // read total Faces
        int type = (int)reader.readFloat(); // read format type
        int index = 3;
        Log.e("Log Reader","" + type);
        m_fVertices = new float[m_intFComponents * 3];
        m_fIndices = new short[m_intFComponents];
        if((type >> 2 & 0x1) == 1) // has texture
        {
        	m_fTexture = new float[m_intFComponents * 2];
        	index += 2;
        }
        if((type >> 1 & 0x1) == 1) // has normal
        {
        	m_fNormal = new float[m_intFComponents * 3];
        	index += 3;
        }
        // run state machine
        int state = 0, i1 = 0, i2 = 0, i3 = 0;
        short i4 = 0;
        try
        {
	        while(true)
	        {
	        	float value = reader.readFloat();
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
        		case 7:
        			m_fTexture[i3++] = value;
        			break;
        		case 8:
        			m_fTexture[i3++] = value;
        			break;
        		}
        		
        		if(state == index)
        			state = 0;
        		
        		if(i4 < m_intFComponents)
        			m_fIndices[i4] = i4++;
	        }
        }catch(Exception e)
        { }
        finally
        {
        	fstream.close();
        	reader.close();
        }
	}
	public void Load2() throws Exception 
	{
		InputStream input = m_objContext.getResources().openRawResource(m_intID);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        m_intFComponents = Integer.parseInt(br.readLine()); // read total Faces
        int type = Integer.parseInt(br.readLine()); // read format type
        m_fVertices = new float[m_intFComponents * 3];
        m_fIndices = new short[m_intFComponents];
        if((type >> 2 & 0x1) == 1) // has texture
        {
        	m_fTexture = new float[m_intFComponents * 2];
        }
        if((type >> 1 & 0x1) == 1) // has normal
        {
        	m_fNormal = new float[m_intFComponents * 3];
        }
        // run state machine
        StringBuffer strBuf = new StringBuffer();
        int c = -1, state = 0, i1 = 0, i2 = 0, i3 = 0;
        short i4 = 0;
        
        while((c = br.read()) != -1)
        {
        	char ch = (char)c;
        	
        	if(ch == ' ')
        	{
        		if(strBuf.length() == 0)
        			continue;
        		float value = Float.parseFloat(strBuf.toString());
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
        		case 7:
        			m_fTexture[i3++] = value;
        			break;
        		case 8:
        			m_fTexture[i3++] = value;
        			break;
        		}
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
        	else if(ch == '\n' || ch == '\r')
        	{
        		/*
        		 * Safe to remove if the delimiter is a space
        		 */
        		/*
        		if(strBuf.length() > 0)
        		{
	        		float value = Float.parseFloat(strBuf.toString());
	        		state++;
	        		switch(state)
	        		{
	        		case 3:
	        			m_fVertices[i1++] = value;
	        			break;
	        		case 6:
	        			m_fNormal[i2++] = value;
	        			break;
	        		case 8:
	        			m_fTexture[i3++] = value;
	        			break;
	        		}
        		}
        		*/
        		state = 0;
        		m_fIndices[i4] = i4++;
        		strBuf = new StringBuffer();
        	}
        }
       
        /*
		 * Safe to remove if the delimiter is a space
		 */
		/*
		if(strBuf.length() > 0)
		{
    		float value = Float.parseFloat(strBuf.toString());
    		state++;
    		switch(state)
    		{
    		case 3:
    			m_fVertices[i1++] = value;
    			break;
    		case 6:
    			m_fNormal[i2++] = value;
    			break;
    		case 8:
    			m_fTexture[i3++] = value;
    			break;
    		}
		}
		*/
        
        m_fIndices[i4] = i4++;
        
        br.close();
        input.close();
	}

}
