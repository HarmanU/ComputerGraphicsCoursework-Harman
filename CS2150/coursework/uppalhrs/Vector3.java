package coursework.uppalhrs;

/**
 * 
 * This class models a 3D vector and contains rotational information
 * 
 * @author Harman Uppal
 * @version 08.12.2017
 */
public class Vector3 {
	
	// positional XYZ values
	private float x;
	private float y;
	private float z;
	
	// Rotational XYZ values
	private float rx;
	private float ry;
	private float rz;
	
	// Values of all variables at start of object creation
	private float[] initalValue;
	
	/**
	 * 
	 * Creates a new Vector3 object
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @param rx X rotation value
	 * @param ry Y rotation value
	 * @param rz Z rotation value
	 */
	public Vector3(float x, float y, float z, float rx, float ry, float rz)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		
		// Sets initial values as ones inputed at objects creation
		float[] temp = {x, y, z, rx, ry, rz};
		initalValue = temp;
	}
	
	// ------------------------------ XYZ Getter Methods ------------------------------
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
	
	// ------------------------------ XYZ Setter Methods ------------------------------
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
	
	/**
	 * Method for retrieving initial XYZ RX RY RZ
	 * 
	 * @return Returns a float[] with initial values
	 */
	public float[] getInitalXYZ()
	{
		return initalValue;
	}
	
	// // ------------------------------ RX RY RZ Getter Methods ------------------------------
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
	
	// ------------------------------ RX RY RZ Setter Methods ------------------------------
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
	
	/**
	 * Reset XYZ to inital values
	 */
	public void resetPosition()
	{
		x = getInitalXYZ()[0];
		y = getInitalXYZ()[1];
		z = getInitalXYZ()[2];
		rx = getInitalXYZ()[3];
		ry = getInitalXYZ()[4];
		rz = getInitalXYZ()[5];
	}
	
	/**
	 * set this objects XYZ RX RY RZ to the XYZ RX RY RZ of a new Vector3 object
	 * @param position New Vector3 to set this objects position to
	 */
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
