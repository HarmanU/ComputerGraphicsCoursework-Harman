package Designer;

import org.lwjgl.opengl.GL11;

import GraphicsLab.Colour;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;

/**
 * The shape designer is a utility class which assits you with the design of 
 * a new 3D object. Replace the content of the drawUnitShape() method with
 * your own code to creates vertices and draw the faces of your object.
 * 
 * You can use the following keys to change the view:
 *   - TAB		switch between vertex, wireframe and full polygon modes
 *   - UP		move the shape away from the viewer
 *   - DOWN     move the shape closer to the viewer
 *   - X        rotate the camera around the x-axis (clockwise)
 *   - Y or C   rotate the camera around the y-axis (clockwise)
 *   - Z        rotate the camera around the z-axis (clockwise)
 *   - SHIFT    keep pressed when rotating to spin anti-clockwise
 *   - A 		Toggle colour (only if using submitNextColour() to specify colour)
 *   - SPACE	reset the view to its initial settings
 *  
 * @author Remi Barillec
 *
 */
public class ShapeDesigner extends AbstractDesigner {
	
	private float wingThickness = 0.05f;
	
	private float wingSpan = 7.0f;
	
	private float hullThickness = 0.125f;
	
	private float noseLength = 1.5f;
	
	private float noseThickness = 0.125f;
	
	/** Main method **/
	public static void main(String args[])
    {   
		new ShapeDesigner().run( WINDOWED, "Designer", 0.01f);
    }
	
	// Vertex's Z reversed in Unity
    
    /** Draw the shape **/
    protected void drawUnitShape()
    {
    	
    	// -------------- Nose ----------------
    	Vertex v1 = new Vertex(-noseThickness, -hullThickness, noseLength); // left bottom
    	Vertex v2 = new Vertex(noseThickness, -hullThickness, noseLength); // right bottom
    	Vertex v3 = new Vertex(noseThickness, noseThickness, noseLength); // right top
    	Vertex v4 = new Vertex(-noseThickness, noseThickness, noseLength); // left top

    	Vertex v5 = new Vertex(-0.5f, -hullThickness, -0.5f); // left bottom and wing front right bottom
    	Vertex v6 = new Vertex(-0.5f, 0.25f, -0.5f); // left top and wing front right top
    	
    	Vertex v7 = new Vertex(0.5f, -hullThickness, -0.5f); // right bottom
    	Vertex v8 = new Vertex(0.5f, 0.25f, -0.5f); // right top
    	
    	Vertex v9 = new Vertex(-0.25f, 0.5f, -0.5f); // right top
    	Vertex v10 = new Vertex(0.25f, 0.5f, -0.5f); // right top
    	
		//Nose face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v1.submit();
			v2.submit();
			v3.submit();
			v4.submit();
		}
		GL11.glEnd();
		
		//Nose left side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v5.submit();
			v1.submit();
			v4.submit();
			v6.submit();	
		}
		GL11.glEnd();
		
		//Nose Right side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v2.submit();
			v7.submit();
			v8.submit();
			v3.submit();	
		}
		GL11.glEnd();
		
		//Nose Top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v4.submit();
			v3.submit();
			v10.submit();
			v9.submit();	
		}
		GL11.glEnd();
		
		//Nose Top left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.BLUE.submit();
			v6.submit();
			v4.submit();
			v9.submit();	
		}
		GL11.glEnd();
		
		//Nose Top right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.BLUE.submit();
			v3.submit();
			v8.submit();
			v10.submit();	
		}
		GL11.glEnd();
    	
    	
    	// -------------- Left Wing --------------
    	
		Vertex v11 = new Vertex(-0.5f, -hullThickness, -1.5f); // left Body bottom
    	Vertex v12 = new Vertex(-0.5f, 0.25f, -1.5f); // left body top
		
    	Vertex v13 = new Vertex(-wingSpan, -wingThickness, -5f); // wing front bottom left
    	Vertex v14 = new Vertex(-wingSpan, wingThickness, -5f); // wing front top left
    	
    	Vertex v15 = new Vertex(-wingSpan, -wingThickness, -6f); // wing back right bottom
    	Vertex v16 = new Vertex(-wingSpan, wingThickness, -6f); // wing back right top
    	
    	Vertex v17 = new Vertex(-0.5f, -hullThickness, -6f); // wing back left bottom
    	Vertex v18 = new Vertex(-0.5f, 0.25f, -6f); // wing back left top
    	
		//Wing Front Body
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v11.submit();
			v5.submit();
			v6.submit();
			v12.submit();
		}
		GL11.glEnd();
    	
		//Wing Front
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v13.submit();
			v11.submit();
			v12.submit();
			v14.submit();
		}
		GL11.glEnd();
		
		//Wing Side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v15.submit();
			v13.submit();
			v14.submit();
			v16.submit();
		}
		GL11.glEnd();
		
		//Wing Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v17.submit();
			v15.submit();
			v16.submit();
			v18.submit();
		}
		GL11.glEnd();
		
		//Wing top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.BLUE.submit();
			v18.submit();
			v16.submit();
			v14.submit();
			v12.submit();
		}
		GL11.glEnd();
		
		
		// -------------- Right Wing --------------
    	
		Vertex v19 = new Vertex(0.5f, -hullThickness, -1.5f); // left Body bottom
    	Vertex v20 = new Vertex(0.5f, 0.25f, -1.5f); // left body top
		
    	Vertex v21 = new Vertex(wingSpan, -wingThickness, -5f); // wing front bottom left
    	Vertex v22 = new Vertex(wingSpan, wingThickness, -5f); // wing front top left
    	
    	Vertex v23 = new Vertex(wingSpan, -wingThickness, -6f); // wing back right bottom
    	Vertex v24 = new Vertex(wingSpan, wingThickness, -6f); // wing back right top
    	
    	Vertex v25 = new Vertex(0.5f, -hullThickness, -6f); // wing back left bottom
    	Vertex v26 = new Vertex(0.5f, 0.25f, -6f); // wing back left top
    	
		//Wing Front Body
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v7.submit();
			v19.submit();
			v20.submit();
			v8.submit();
		}
		GL11.glEnd();
    	
		//Wing Front
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v19.submit();
			v21.submit();
			v22.submit();
			v20.submit();
		}
		GL11.glEnd();
		
		//Wing Side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v21.submit();
			v23.submit();
			v24.submit();
			v22.submit();
		}
		GL11.glEnd();
		
		//Wing Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v23.submit();
			v25.submit();
			v26.submit();
			v24.submit();
		}
		GL11.glEnd();
		
		//Wing top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.BLUE.submit();
			v24.submit();
			v26.submit();
			v20.submit();
			v22.submit();
		}
		GL11.glEnd();
		
		// ----------------- Cockpit
		
    	Vertex v27 = new Vertex(-0.25f, 1f, -2f); // top left
    	Vertex v28 = new Vertex(0.25f, 1f, -2f); //  top right
    	
    	Vertex v29 = new Vertex(-0.25f, 0.5f, -2f); // right top
    	Vertex v30 = new Vertex(0.25f, 0.5f, -2f); // right top
    	
		//Cockpit Front to top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.CYAN.submit();
			v9.submit();
			v10.submit();
			v28.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		//Cockpit left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v29.submit();
			v9.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		//Cockpit right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v10.submit();
			v30.submit();
			v28.submit();
		}
		GL11.glEnd();
		
		// --------------- Rear Guard
		
    	Vertex v31 = new Vertex(-0.25f, 0.5f, -6f); // right top
    	Vertex v32 = new Vertex(0.25f, 0.5f, -6f); // right top
		
		// Read guard top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.BLUE.submit();
			v32.submit();
			v31.submit();
			v27.submit();
			v28.submit();
		}
		GL11.glEnd();
		
		// Read guard Left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v31.submit();
			v29.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		// Read guard Right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.RED.submit();
			v30.submit();
			v32.submit();
			v28.submit();
		}
		GL11.glEnd();
		
		
		// -------------- Wing to body Joints
		
		// Wing to body Joint left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v18.submit();
			v6.submit();
			v9.submit();
			v31.submit();
		}
		GL11.glEnd();
		
		// Wing to body Joint left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v8.submit();
			v26.submit();
			v32.submit();
			v10.submit();
		}
		GL11.glEnd();
		
		// -------- Engine
		
    	Vertex v33 = new Vertex(-0.25f, -hullThickness, -6f); // left bottom
    	Vertex v34 = new Vertex(0.25f, -hullThickness, -6f); // right bottom
    	Vertex v35 = new Vertex(0.25f, 0.25f, -6f); // right top
    	Vertex v36 = new Vertex(-0.25f, 0.25f, -6f); // left top
    	
    	Vertex v37 = new Vertex(-0.125f, -hullThickness/2, -7f); // left bottom
    	Vertex v38 = new Vertex(0.125f, -hullThickness/2, -7f); // right bottom
    	Vertex v39 = new Vertex(0.125f, 0.125f, -7f); // right top
    	Vertex v40 = new Vertex(-0.125f, 0.125f, -7f); // left top
    	
		// Engine top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v39.submit();
			v40.submit();
			v36.submit();
			v35.submit();
		}
		GL11.glEnd();
		
		// Engine left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v37.submit();
			v33.submit();
			v36.submit();
			v40.submit();
		}
		GL11.glEnd();
		
		// Engine left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.YELLOW.submit();
			v34.submit();
			v38.submit();
			v39.submit();
			v35.submit();
		}
		GL11.glEnd();
		
		// Engine Bottom
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.GREEN.submit();
			v34.submit();
			v33.submit();
			v37.submit();
			v38.submit();
		}
		GL11.glEnd();
		
		// Engine Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.CYAN.submit();
			v38.submit();
			v37.submit();
			v40.submit();
			v39.submit();
		}
		GL11.glEnd();
		
		// ----------- Back
		
		// Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.CYAN.submit();
			v25.submit();
			v17.submit();
			v18.submit();
			v31.submit();
			v32.submit();
			v26.submit();
		}
		GL11.glEnd();
		
		// ----------- Bottom
		
		// Bottom
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.CYAN.submit();
			v15.submit();
			v17.submit();
			v25.submit();
			v23.submit();
			v21.submit();
			v19.submit();
			v11.submit();
			v13.submit();
		}
		GL11.glEnd();
		
		// Bottom nose
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Colour.CYAN.submit();
			v11.submit();
			v19.submit();
			v7.submit();
			v2.submit();
			v1.submit();
			v5.submit();
		}
		GL11.glEnd();
    }
}
