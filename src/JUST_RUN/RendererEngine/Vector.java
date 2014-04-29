/**
 * @author Han Lin
 * Created by Han Lin on v0.1
 * Edited :
 */
package JUST_RUN.RendererEngine;

public class Vector {
	
	public float X, Y, Z, A;
	
	public Vector(float x, float y, float z)
	{
		this(x, y, z, 1);
	}
	
	public Vector(float x, float y, float z, float a)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
		this.A = a;
	}

	public Vector(Vector vector, float value)
	{
		this(vector.X, vector.Y, vector.Z, value);
	}
	
	public Vector() 
	{
		this(0, 0, 0);
	}
	
	public Vector(float x, float y)
	{
		this(x, y, 0);
	}
	
	public static Vector getTriangleNormal(Vector a, Vector b, Vector c)
	{
		return Vector.Cross(Vector.Subtract3v(b, a), Vector.Subtract3v(c, a));
	}
	
	public static boolean Equals3v(Vector a, Vector b)
	{
		return a.equals3v(b);
	}
	
	public static Vector Add3v(Vector a, Vector b)
	{
		return new Vector(a.X + b.X, a.Y + b.Y, a.Z + b.Z);
	}
	
	public static Vector Add3v(Vector a, float value)
	{
		return new Vector(a.X + value, a.Y + value, a.Z + value);
	}
	
	public static Vector Subtract3v(Vector a, Vector b)
	{
		return new Vector(a.X - b.X, a.Y - b.Y, a.Z - b.Z);
	}
	
	public static Vector Subtract3v(Vector a, float value)
	{
		return new Vector(a.X - value, a.Y - value, a.Z - value);
	}
	
	public static Vector Multiply3v(Vector a, Vector b)
	{
		return new Vector(a.X * b.X, a.Y * b.Y, a.Z * b.Z);
	}
	
	public static Vector Multiply3v(Vector a, float value)
	{
		return new Vector(a.X * value, a.Y * value, a.Z * value);
	}
	
	public static Vector Divide3v(Vector a, Vector b)
	{
		return new Vector(a.X / b.X, a.Y / b.Y, a.Z / b.Z);
	}
	
	public static Vector Divide3v(Vector a, float value)
	{
		return new Vector(a.X / value, a.Y / value, a.Z / value);
	}
	
	public static Vector Cross(Vector a, Vector b)
	{
		return new Vector(
				a.Y * b.Z - a.Z * b.Y,
				a.Z * b.X - a.X * b.Z,
				a.X * b.Y - a.Y * b.X);
	}
	
	public static float Dot2v(Vector a, Vector b)
	{
		return a.X * b.X + a.Y * b.Y;
	}
	
	public static float Dot3v(Vector a, Vector b)
	{
		return a.X * b.X + a.Y * b.Y + a.Z * b.Z;
	}
	
	public static float Dot4v(Vector a, Vector b)
	{
		return a.X * b.X + a.Y * b.Y + a.Z * b.Z + a.A * b.A;
	}
	
	public void Negate3v()
	{
		X -= X;
		Y -= Y;
		Z -= Z;
	}
	
	public float getLength3v()
	{
		return  android.util.FloatMath.sqrt(getSquareLength3v());
	}
	
	public float getSquareLength3v()
	{
		return X*X + Y*Y + Z*Z;
	}
	
	public Vector getUnitVector3v()
	{
		float length = getLength3v();
		return Vector.Divide3v(this, length);
	}
	
	public void setAsUnitVector3v()
	{
		double length = getLength3v();
		this.X /= length;
		this.Y /= length;
		this.Z /= length;
	}
	
	public boolean equals3v(Vector v)
	{
		return this.X - v.X < 0.001 && this.Y - v.Y < 0.001 && this.Z - v.Z < 0.001;
	}
	
	public float[] getArray3v()
	{
		return new float[]{this.X, this.Y, this.Z};
	}
	
	public float[] getArray4v()
	{
		return new float[]{this.X, this.Y, this.Z, this.A};
	}
	
	public String toString()
	{
		return X + " , " + Y + ", " + Z + ", " + A;
	}
}
