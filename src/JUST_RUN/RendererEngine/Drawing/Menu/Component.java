package JUST_RUN.RendererEngine.Drawing.Menu;

import JUST_RUN.RendererEngine.Camera;
import JUST_RUN.RendererEngine.Shader;
import JUST_RUN.RendererEngine.Drawing.Shape;

public abstract class Component {
	public abstract void setCamera(Camera camera);
	public abstract void setShader(Shader shader);
	public abstract Shape getShape();
	public abstract void Render();
}
