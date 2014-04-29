/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited : by Han Lin on v0.3 --  adding actual implementation
 */
package JUST_RUN.GameplaySystem;

public class Event<T>{
	private int m_intID;
	private T m_objArg;
	
	public Event(int ID, T argument)
	{
		m_intID = ID;
		m_objArg = argument;
	}
	
	public int getID()
	{
		return m_intID;
	}
	
	public T getArgument()
	{
		return m_objArg;
	}
	
	@Override
	public int hashCode() {
		return this.m_intID;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.m_intID == ((Event<?>)o).getID();
	}
}
