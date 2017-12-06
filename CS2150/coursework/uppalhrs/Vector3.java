package coursework.uppalhrs;

public class Vector3 {
	
	private float x;
	private float y;
	private float z;
	private float rx;
	private float ry;
	private float rz;
	
	public Vector3(float x, float y, float z, float rx, float ry, float rz)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public void setX(float newX)
	{
		x = newX;
	}
	
	public void setY(float newY)
	{
		y = newY;
	}
	
	public void setZ(float newZ)
	{
		z = newZ;
	}
	
	// Rotation vectors
	
	public float getRX()
	{
		return rx;
	}
	
	public float getRY()
	{
		return ry;
	}
	
	public float getRZ()
	{
		return rz;
	}
	
	public void setRX(float newRX)
	{
		rx = newRX;
	}
	
	public void setRY(float newRY)
	{
		ry = newRY;
	}
	
	public void setRZ(float newRZ)
	{
		rz = newRZ;
	}

}
