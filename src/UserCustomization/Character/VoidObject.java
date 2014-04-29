package UserCustomization.Character;

import JUST_RUN.GameSubSystem.GameObject;

public class VoidObject extends GameObject{

	@Override
	public Type getType() {
		return Type.UserDefined;
	}

	@Override
	public void setType(Type type) {}

	@Override
	public void Update(long currentTime) {}
	
	@Override
	public void Render(){ }
}
