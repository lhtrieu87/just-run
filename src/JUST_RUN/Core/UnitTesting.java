/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.Core;

import java.lang.reflect.Field;

import android.content.Context;

public class UnitTesting {
	public UnitTesting()
	{ 

	}
	
	public UnitTesting(Context context)
	{ }
	
	// make a reflection to show the file(s) in res\raw folder
	public static String raw_folder_reflection()
	{
		String str="";
    	Field[] fields = R.raw.class.getFields(); 
    	for(Field f : fields) 
    	try 
    	{ 
    		str += f.getName() + "\n";
    	} catch (IllegalArgumentException e) 
    	{ } 
    	
    	return str;
	}
}
