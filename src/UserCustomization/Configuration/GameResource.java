/**
 * @author Han Lin
 * Created by Han Lin on v0.4
 * Edited :
 */
package UserCustomization.Configuration;

import JUST_RUN.Core.R;
import JUST_RUN.RendererEngine.Drawing.MeshObject;
import JUST_RUN.RendererEngine.Drawing.TriangleMesh;
import JUST_RUN.Resource.Sprite_Format_Reader;
import JUST_RUN.Resource.XOBJ_FORMAT_READER;
import JUST_RUN.Resource.ResourceManager;

public class GameResource {
	// Texture resource
	public static int SKY_TEXTURE;
	public static int LANDSCAPE_TEXTURE;
	public static int VAMPIRE_TEXTURE;
	public static int ANCIENTGUY_SKILL_TEXTURE;
	public static int VAMPIRE_SKILL_TEXTURE;
	public static int DIAMOND_TEXTURE;
	public static int HUD_ROCK_TEXTURE;
	public static int ROCK_TEXTURE;
	
	// Geometry Mesh
	public static TriangleMesh ENVIRONMENT_MESH;
	public static TriangleMesh LANDSCAPE1_MESH;
	public static TriangleMesh SKY_MESH;
	public static MeshObject LINE1_1;
	public static MeshObject LINE1_2;
	public static MeshObject LINE1_3;
	
	public static TriangleMesh GOLD_MESH;
	public static TriangleMesh ROCK_MESH;
	public static TriangleMesh POISON_MESH;
	public static TriangleMesh CONFUSION_MESH;
	public static TriangleMesh MINE_MESH;
	
	public static TriangleMesh VAMPIRE_MESH;
	public static TriangleMesh ANCIENT_GUY_MESH;
	public static TriangleMesh MAGICIAN_MESH;
	
	// Game static Parameter
	public static final int GOLD_VALUE_1 = 3;
	public static final int GOLD_VALUE_2 = 8;
	public static final int ROCK_PRICE = 15;
	public static final int ROCK_DAMAGE_POINT = 5;
	public static final int ROCK_CHEAPER_PRICE = 10;
	public static final int ROCK_MORE_DAMAGE_POINT = 10;
	public static final float INITIAL_CHARACTER_SPEED = 0.3f;
	public static final float CHANGING_LANE_SPEED = 0.8f;
	public static final int INITIAL_PLAYER_GOLD = 10;
	// there still have more but I lazy to change liao ...
	
	public static final int POISON_PRICE = 0;
	public static final int POISON_CHEAPER_PRICE = 0;
	public static final int POISON_DAMAGE_POINT = 0;
	public static final int POISON_MORE_DAMAGE_POINT = 0;
	public static final long POISON_ACTIVE_TIME = 5000;    // In millisecond.
	
	public static final int CONFUSION_PRICE = 0;
	public static final int CONFUSION_CHEAPER_PRICE = 0;
	public static final int CONFUSION_DAMAGE_POINT = 0;
	public static final int CONFUSION_MORE_DAMAGE_POINT = 0;
	public static final long CONFUSION_ACTIVE_TIME = 2500; // In millisecond.
	
	public static final int MINE_PRICE = 20;
	public static final int MINE_DAMAGE_POINT = 20;
	public static final int MINE_CHEAPER_PRICE = 20;
	public static final int MINE_MORE_DAMAGE_POINT = 25;
	
	// Static Functions
	public static void LoadSky() throws Exception
	{
		XOBJ_FORMAT_READER reader;
		if(SKY_MESH == null)
		{
			//reader = new OBJ_Format_Reader(ResourceManager.getContext(), R.raw.sky);
			//SKY_MESH = new TriangleMesh(reader.getMeshObjects().get(0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.sky);
			SKY_MESH = new TriangleMesh(reader.getMeshObject());
		}
	}
	
	public static void LoadLandscape1 () throws Exception
	{
		XOBJ_FORMAT_READER reader;
		if (LANDSCAPE1_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.landscape);
			//reader.Load ();
			//LANDSCAPE1_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.landscape);
			LANDSCAPE1_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (LINE1_1 == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.demo_waypoint);
			//reader.Load ();
			//LINE1_1 = reader.getMeshObjects ().get (0);
			//LINE1_2 = reader.getMeshObjects ().get (1);
			//LINE1_3 = reader.getMeshObjects ().get (2);
			reader = new XOBJ_FORMAT_READER (ResourceManager.getContext(), R.raw.waypoint1);
			LINE1_1 = reader.getMeshObject();
			reader = new XOBJ_FORMAT_READER (ResourceManager.getContext(), R.raw.waypoint2);
			LINE1_2 = reader.getMeshObject();
			reader = new XOBJ_FORMAT_READER (ResourceManager.getContext(), R.raw.waypoint3);
			LINE1_3 = reader.getMeshObject();
		}
	}
	
	public static void LoadItems () throws Exception
	{
		XOBJ_FORMAT_READER reader;
		
		if (ROCK_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.rock);
			//reader.Load ();
			//ROCK_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.rock);
			ROCK_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (POISON_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.rock);
			//reader.Load ();
			//POISON_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.rock);
			POISON_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (CONFUSION_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.rock);
			//reader.Load ();
			//CONFUSION_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.rock);
			CONFUSION_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (GOLD_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.diamond);
			//reader.Load ();
			//GOLD_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.diamond);
			GOLD_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (MINE_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.rock);
			//reader.Load ();
			//MINE_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.rock);
			MINE_MESH = new TriangleMesh(reader.getMeshObject());
		}
	}
	
	public static void LoadCharacters () throws Exception
	{
		XOBJ_FORMAT_READER reader;
		
		if (VAMPIRE_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.vampire);
			//reader.Load ();
			//VAMPIRE_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			//reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.vampire);
			//VAMPIRE_MESH = new TriangleMesh(reader.getMeshObject());
			Sprite_Format_Reader r = new Sprite_Format_Reader(ResourceManager.getContext(), R.raw.vampire_1);
			VAMPIRE_MESH = new TriangleMesh(r.getMesh());
		}
		
		if (ANCIENT_GUY_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.vampire);
			//reader.Load ();
			//ANCIENT_GUY_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.vampire);
			ANCIENT_GUY_MESH = new TriangleMesh(reader.getMeshObject());
		}
		
		if (MAGICIAN_MESH == null)
		{
			//reader = new OBJ_Format_Reader (ResourceManager.getContext(), R.raw.vampire);
			//reader.Load ();
			//MAGICIAN_MESH = new TriangleMesh (reader.getMeshObjects ().get (0));
			reader = new XOBJ_FORMAT_READER(ResourceManager.getContext(), R.raw.vampire);
			MAGICIAN_MESH = new TriangleMesh(reader.getMeshObject());
		}
	}
}