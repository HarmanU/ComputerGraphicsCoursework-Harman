/* CS2150Coursework.java
 * 
 * University Username: uppalhrs
 * Full name: Harman Ryan Singh Uppal
 * Email: uppalhrs@aston.ac.uk
 * Student ID: 159115729
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *
 *  TODO: Provide a scene graph for your submission
 */
package coursework.uppalhrs;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * </ul>
 * TODO: Add any additional controls for your sample to the list above
 *
 */
public class CS2150Coursework extends GraphicsLab
{
	
	private float wingThickness = 0.05f;
	private float wingSpan = 7.0f;
	private float hullThickness = 0.125f;
	private float noseLength = 1.5f;
	private float noseThickness = 0.125f;
	
	private final int spaceshipList = 1;
	private final int moonList = 2;
	
	// used to control animations
	private boolean landShip = false;
	private boolean moveShip = true;
	
	private float currentMoonRotation = 0.0f;
	
	// height above moon ship will fly
	private float shipFlyHeight = 8.0f;
	private float currentShipHeight = shipFlyHeight;
	private float shipLandHeight = 5.0f;
	
	// Angle ship will fly at and rate at which it banks to land on moon
	private float shipFlyAngle = -10.0f;
	private float currentShipAngle = shipFlyAngle;
	private float shipLandAngle = -90.f;
	private float shipRotationSpeed = -(shipLandAngle - shipFlyAngle) / 2.0f;
	
    private Texture moonTex;
    private Texture moonHeightTex;
	
	
	
    //TODO: Feel free to change the window title and default animation scale here
    public static void main(String args[])
    {   
    	new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission - Harman Uppal 159115729",0.01f);
    }

    protected void initScene() throws Exception
    {//TODO: Initialise your resources here - might well call other methods you write.
    
    	moonTex = loadTexture("coursework/uppalhrs/textures/moon.bmp");
    	moonHeightTex = loadTexture("coursework/uppalhrs/textures/moonHeight.bmp");
    	
    	System.out.print(shipRotationSpeed);
    	
    	// Global Ambient Light
	    float globalAmbient[]   = {0.1f,  0.1f,  0.1f, 1.0f}; 
	    GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));    // Set global ambient lighting
	    
        // Sun light yellowish
        float diffuse0[]  = { 0.8f,  0.7f, 0.4f, 1.0f};
        // ...with a very dim ambient contribution...
        float ambient0[]  = { 0.05f,  0.05f, 0.05f, 1.0f};
        // ...and is positioned to the left the viewpoint
        float position0[] = { -30.0f, 0.0f, 0.0f, 1.0f};

        // supply OpenGL with the properties for the first light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        
        // enable the first light
        GL11.glEnable(GL11.GL_LIGHT0);
	    

        // enable lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        // ensure that all normals are re-normalised after transformations automatically
        GL11.glEnable(GL11.GL_NORMALIZE);
	    
    }
    protected void checkSceneInput()
    {//TODO: Check for keyboard and mouse input here
    	
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {   landShip = true;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {   landShip = false;
        }
        
    }
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
    	
    	
        if(!landShip && currentShipHeight < shipFlyHeight)
        {   currentShipHeight += 1.0f * getAnimationScale();
        }
        // else if the sun/moon is falling, and it isn't at its lowest,
        // then decrement the sun/moon's Y offset
        else if(landShip && currentShipHeight > shipLandHeight)
        {   currentShipHeight -= 1.0f * getAnimationScale();
        }
        
        if(!landShip && currentShipAngle < shipFlyAngle)
        {
        	currentShipAngle += shipRotationSpeed * getAnimationScale();
        }
        // else if the sun/moon is falling, and it isn't at its lowest,
        // then decrement the sun/moon's Y offset
        else if(landShip && currentShipAngle > shipLandAngle)
        {
        	currentShipAngle -= shipRotationSpeed * getAnimationScale();
        }
        
        if(!landShip)
        {
        	currentMoonRotation += 5.0f * getAnimationScale();
        }
        
    }
    protected void renderScene()
    {//TODO: Render your scene here - remember that a scene graph will help you write this method! 
     //      It will probably call a number of other methods you will write.
    	
        // draw the space ship
        GL11.glPushMatrix();
        {
        	
            // how shiny are the front faces of the moon (specular exponent)
            float shipFrontShininess  = 5.0f;
            // specular reflection of the front faces of the moon
            float shipFrontSpecular[] = {0.6f, 0.6f, 0.6f, 1.0f};
            // diffuse reflection of the front faces of the moon
            float shipFrontDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

            // set the material properties for the sun using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, shipFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(shipFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(shipFrontDiffuse));
        	
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(0f,0f, currentShipHeight);
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glRotatef(currentShipAngle, 0, 0, 1);
            GL11.glScalef(0.125f, 0.125f, 0.125f);
            
            
            drawUnitSpaceship();
        }
        GL11.glPopMatrix();
        
        // draw the moon
        GL11.glPushMatrix();
        {
           // how shiny are the front faces of the moon (specular exponent)
           float moonFrontShininess  = 2.0f;
           // specular reflection of the front faces of the moon
           float moonFrontSpecular[] = {0.6f, 0.6f, 0.6f, 1.0f};
           // diffuse reflection of the front faces of the moon
           float moonFrontDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

           // set the material properties for the sun using OpenGL
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, moonFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(moonFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(moonFrontDiffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,moonTex.getTextureID());
           GL11.glBindTexture(GL11.GL_TEXTURE_HEIGHT,moonHeightTex.getTextureID());

           // enable the texture space S,T
           GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
           GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
           
           // Set The Texture Generation Mode For S To Sphere Mapping 
           GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
           GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                   	
           // position and draw the moon using a sphere quadric object
           GL11.glTranslatef(0f, 0f, 0f);
           GL11.glRotatef(currentMoonRotation, 0, 1, 0);
           new Sphere().draw(5.0f,50,50);
        }
        GL11.glPopMatrix();
    	
    }
    protected void setSceneCamera()
    {
        // call the default behaviour defined in GraphicsLab. This will set a default perspective projection
        // and default camera settings ready for some custom camera positioning below...  
        super.setSceneCamera();

        //TODO: If it is appropriate for your scene, modify the camera's position and orientation here
        //        using a call to GL11.gluLookAt(...)
        
        GLU.gluLookAt(0.0f, 2.0f, 15.0f, 0f, 0f, 0f, 0f, 1f, 0f);
   }

    protected void cleanupScene()
    {//TODO: Clean up your resources here
    }
    
    /** Draw a Spaceship **/
    protected void drawUnitSpaceship()
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
			new Normal(v1.toVector(),v2.toVector(),v3.toVector(),v4.toVector()).submit();
			
			v1.submit();
			v2.submit();
			v3.submit();
			v4.submit();
		}
		GL11.glEnd();
		
		//Nose left side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v5.toVector(),v1.toVector(),v4.toVector(),v6.toVector()).submit();
			
			v5.submit();
			v1.submit();
			v4.submit();
			v6.submit();	
		}
		GL11.glEnd();
		
		//Nose Right side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(),v7.toVector(),v8.toVector(),v3.toVector()).submit();
			
			v2.submit();
			v7.submit();
			v8.submit();
			v3.submit();	
		}
		GL11.glEnd();
		
		//Nose Top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(),v3.toVector(),v10.toVector(),v9.toVector()).submit();
			
			v4.submit();
			v3.submit();
			v10.submit();
			v9.submit();	
		}
		GL11.glEnd();
		
		//Nose Top left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v6.toVector(),v4.toVector(),v9.toVector()).submit();
			
			v6.submit();
			v4.submit();
			v9.submit();	
		}
		GL11.glEnd();
		
		//Nose Top right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(),v8.toVector(),v10.toVector()).submit();
			
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
			new Normal(v11.toVector(),v5.toVector(),v6.toVector(),v12.toVector()).submit();
			
			v11.submit();
			v5.submit();
			v6.submit();
			v12.submit();
		}
		GL11.glEnd();
    	
		//Wing Front
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v13.toVector(),v11.toVector(),v12.toVector(),v14.toVector()).submit();
			
			v13.submit();
			v11.submit();
			v12.submit();
			v14.submit();
		}
		GL11.glEnd();
		
		//Wing Side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			
			new Normal(v15.toVector(),v13.toVector(),v14.toVector(),v16.toVector()).submit();
			
			v15.submit();
			v13.submit();
			v14.submit();
			v16.submit();
		}
		GL11.glEnd();
		
		//Wing Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v17.toVector(),v15.toVector(),v16.toVector(),v18.toVector()).submit();
			
			v17.submit();
			v15.submit();
			v16.submit();
			v18.submit();
		}
		GL11.glEnd();
		
		//Wing top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v18.toVector(),v16.toVector(),v14.toVector(),v12.toVector()).submit();
			
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
			new Normal(v7.toVector(),v19.toVector(),v20.toVector(),v8.toVector()).submit();
			
			v7.submit();
			v19.submit();
			v20.submit();
			v8.submit();
		}
		GL11.glEnd();
    	
		//Wing Front
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v19.toVector(),v21.toVector(),v22.toVector(),v20.toVector()).submit();
			
			v19.submit();
			v21.submit();
			v22.submit();
			v20.submit();
		}
		GL11.glEnd();
		
		//Wing Side
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v21.toVector(),v23.toVector(),v24.toVector(),v22.toVector()).submit();
			
			v21.submit();
			v23.submit();
			v24.submit();
			v22.submit();
		}
		GL11.glEnd();
		
		//Wing Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v23.toVector(),v25.toVector(),v26.toVector(),v24.toVector()).submit();
			
			v23.submit();
			v25.submit();
			v26.submit();
			v24.submit();
		}
		GL11.glEnd();
		
		//Wing top
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v24.toVector(),v26.toVector(),v20.toVector(),v22.toVector()).submit();
			
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
			new Normal(v32.toVector(),v31.toVector(),v27.toVector(),v28.toVector()).submit();
			
			v32.submit();
			v31.submit();
			v27.submit();
			v28.submit();
		}
		GL11.glEnd();
		
		// Read guard Left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v31.toVector(),v29.toVector(),v27.toVector()).submit();
			
			v31.submit();
			v29.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		// Read guard Right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v30.toVector(),v32.toVector(),v28.toVector()).submit();
			
			v30.submit();
			v32.submit();
			v28.submit();
		}
		GL11.glEnd();
		
		
		// -------------- Wing to body Joints
		
		// Wing to body Joint left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v18.toVector(),v6.toVector(),v9.toVector(),v31.toVector()).submit();
			
			v18.submit();
			v6.submit();
			v9.submit();
			v31.submit();
		}
		GL11.glEnd();
		
		// Wing to body Joint left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v8.toVector(),v26.toVector(),v32.toVector(),v10.toVector()).submit();
			
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
			new Normal(v5.toVector(),v1.toVector(),v4.toVector(),v6.toVector()).submit();
			
			v39.submit();
			v40.submit();
			v36.submit();
			v35.submit();
		}
		GL11.glEnd();
		
		// Engine left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v37.toVector(),v33.toVector(),v36.toVector(),v40.toVector()).submit();
			
			v37.submit();
			v33.submit();
			v36.submit();
			v40.submit();
		}
		GL11.glEnd();
		
		// Engine left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v34.toVector(),v38.toVector(),v39.toVector(),v35.toVector()).submit();
			
			v34.submit();
			v38.submit();
			v39.submit();
			v35.submit();
		}
		GL11.glEnd();
		
		// Engine Bottom
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v34.toVector(),v33.toVector(),v37.toVector(),v38.toVector()).submit();
			
			v34.submit();
			v33.submit();
			v37.submit();
			v38.submit();
		}
		GL11.glEnd();
		
		// Engine Back
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v38.toVector(),v37.toVector(),v40.toVector(),v39.toVector()).submit();
			
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
			new Normal(v25.toVector(),v17.toVector(),v18.toVector(),v31.toVector()).submit();
			
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
