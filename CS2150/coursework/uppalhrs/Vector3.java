package coursework.uppalhrs;

import com.sun.javafx.scene.paint.GradientUtils.Point;

public class Vector3 {
	
	private float x;
	private float y;
	private float z;
	private float rx;
	private float ry;
	private float rz;
	
	private float[] initalValue;
	
	public Vector3(float x, float y, float z, float rx, float ry, float rz)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		
		float[] temp = {x, y, z, rx, ry, rz};
		initalValue = temp;
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
	
	public float[] getInitalXYZ()
	{
		return initalValue;
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
	
	//Reset XYZ
	public void resetPosition()
	{
		x = getInitalXYZ()[0];
		y = getInitalXYZ()[1];
		z = getInitalXYZ()[2];
		rx = getInitalXYZ()[3];
		ry = getInitalXYZ()[4];
		rz = getInitalXYZ()[5];
	}
	
	//set XYZ
	public void setPosition(Vector3 position)
	{
		x = position.getX();
		y = position.getY();
		z = position.getZ();
		rx = position.getRX();
		ry = position.getRY();
		rz = position.getRZ();
	}

}
