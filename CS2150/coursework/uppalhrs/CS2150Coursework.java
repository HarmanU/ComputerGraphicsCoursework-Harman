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
 *  +-- [] Sun
 *  |   |
 *  |   +-- [T(5,0,0) Ry(360*currentSateliteRotation/365)] Earth
 *  |       |
 *  |       +-- [T(5,0,0) Ry(360*currentSateliteRotation*0.5)] Moon
 *  |			|
 *  |			+-- [T(5,0,0) Ry(360*currentSateliteRotation*0.5)] orbiting Spaceship
 *  |
 *  +-- [T(-180,0,0)] Sun 2
 *  |   |
 *  |   +-- [T(-20,0,0) Ry(360*currentSateliteRotation/150)] Alien Planet
 *  |	|
 *  |	+-- [T(-5,0,0) Ry(360*currentSateliteRotation/10)] Alien Planet
 *  |
 *  +-- [T(20,0,15) Ry(0,0,0) S(0.0625, 0.0625, 0.0625)] Spaceship
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
 *  A spaceship in a Star System with a ship orbiting the moon, and another star system that can be travelled to
 *  
 *  
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * <li> Use 'W D' to move the ship forward and backwards respectively
 * <li> Use 'Q' and 'E' to go up and down respectively
 * <li> Use 'A' and 'D' to rotate ship left and right respectively
 * <li> Use 'F' and either '1' or '2' to a different movement speeds (1 for slow, 2 for fast)
 * <li> Use 'J' and either '1' or '2' to a different star system location
 * <li> Use 'Space' to stop the ship and 'R' to reset to default location
 * </ul>
 *
 */
public class CS2150Coursework extends GraphicsLab
{
	// ANIMATION PARAMETERS
	// Ship parameters
	private float wingThickness = 0.05f;
	private float wingSpan = 7.0f;
	private float hullThickness = 0.125f;
	private float noseLength = 1.5f;
	private float noseThickness = 0.125f;
	
	private float shipSpeedSlow = 1.0f;
	private float shipSpeedFast = 5.0f;
	private float shipSpeed = 1.0f;
	
	private float skyboxSize = 70.0f;
	
	// ANIMATION
	// Animation Control
	public enum Direction {
	    FORWARDS, BACKWARDS, YAWLEFT, YAWRIGHT,
	    UP, DOWN, STOP
	}
	
	Direction direction;
	
	// animation variables
	private float currentSateliteRotation = 0.0f;
	
	//SHIP
	// ships postion information
	private Vector3 shipPos = new Vector3(20.0f, 0.0f, 15.0f, 0.0f, 0.0f, 0.0f);
	
	
	// CAMERA
	// Distance camera is from ship
	private float cameraDistance = 2.0f;
	private float cameraHeight = 0.3f;
	
	// Camera position information
	private Vector3 cameraPos = new Vector3(shipPos.getX() + cameraDistance, shipPos.getY() + cameraHeight, shipPos.getZ(), shipPos.getX(), shipPos.getY(), shipPos.getZ());
	
	
	//WARPJUMP
	private Vector3 System1JumpPoint = new Vector3(-150.0f, 0.0f, 15.0f, 0.0f, 0.0f, 0.0f);
	private Vector3 System0JumpPoint = new Vector3(shipPos.getX(), shipPos.getY(), shipPos.getZ(), shipPos.getRX(), shipPos.getRY(), shipPos.getRZ());
	private boolean inSystem1 = false;
	
	
	// Textures
    private Texture moonTex;
    private Texture sunTex;
    private Texture EarthTex;
    private Texture alienTex;
    private Texture alien2Tex;
    private Texture shipTexture;
    private Texture skyboxTex;
	
	
	
    //TODO: Feel free to change the window title and default animation scale here
    public static void main(String args[])
    {   
    	new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission - Harman Uppal 159115729",0.01f);
    }

    protected void initScene() throws Exception
    {//TODO: Initialise your resources here - might well call other methods you write.
    	
    	direction = Direction.STOP;
    
    	moonTex = loadTexture("coursework/uppalhrs/textures/moon.png");
    	sunTex = loadTexture("coursework/uppalhrs/textures/sun.png");
    	EarthTex = loadTexture("coursework/uppalhrs/textures/earth.png");
    	shipTexture = loadTexture("coursework/uppalhrs/textures/shipHull.png");
    	skyboxTex = loadTexture("coursework/uppalhrs/textures/skybox.png");
    	alienTex = loadTexture("coursework/uppalhrs/textures/alienPlanet.png");
    	alien2Tex = loadTexture("coursework/uppalhrs/textures/alienPlanet2.png");
    	
    	// Global Ambient Light
	    float globalAmbient[]   = {0.2f,  0.2f,  0.2f, 1.0f}; 
	    GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));
	    
        // Sun light yellowish
        float diffuse0[]  = { 0.8f,  0.7f, 0.4f, 1.0f};
        // with a very dim ambient contribution
        float ambient0[]  = { 0.05f,  0.05f, 0.05f, 1.0f};
        // and is positioned at the center of the scene
        float position0[] = { 0.0f, 0.0f, 0.0f, 1.0f};

        // supply OpenGL with the properties for the first light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        
        // enable the sun light
        GL11.glEnable(GL11.GL_LIGHT0);
	    

        // enable lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        // ensure that all normals are re-normalised after transformations automatically
        GL11.glEnable(GL11.GL_NORMALIZE);
	    
    }
    protected void checkSceneInput()
    {//TODO: Check for keyboard and mouse input here
    	
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {   
        	direction = Direction.STOP;
        	direction = Direction.FORWARDS;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {   
        	direction = Direction.STOP;
        	direction = Direction.BACKWARDS;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {   
        	direction = Direction.STOP;
        	direction = Direction.YAWLEFT;
        }  
        else if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {   
        	direction = Direction.STOP;
        	direction = Direction.YAWRIGHT;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_Q))
        {   
        	direction = Direction.STOP;
        	direction = Direction.UP;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_E))
        {   
        	direction = Direction.STOP;
        	direction = Direction.DOWN;
        }
        // Speed controls
        else if(Keyboard.isKeyDown(Keyboard.KEY_F) && Keyboard.isKeyDown(Keyboard.KEY_1))
        {   
        	shipSpeed = shipSpeedSlow;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_F) && Keyboard.isKeyDown(Keyboard.KEY_2))
        {   
        	shipSpeed = shipSpeedFast;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {   
        	direction = Direction.STOP;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_R))
        {   
        	shipPos.resetPosition();
        	direction = Direction.STOP;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_J) && Keyboard.isKeyDown(Keyboard.KEY_1) && !inSystem1)
        {   
        	shipPos.setY(0.0f);
        	shipPos.setPosition(System1JumpPoint);
        	inSystem1 = true;
        	direction = Direction.STOP;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_J) && Keyboard.isKeyDown(Keyboard.KEY_2) && inSystem1)
        {   	
    		shipPos.setY(180.0f);
        	shipPos.setPosition(System0JumpPoint);
        	inSystem1 = false;
        	direction = Direction.STOP;
        }
        
        
    }
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
    	
    	// move ship depending on what direction is selected
        if(direction == Direction.FORWARDS)
        {
        	moveShip(shipSpeed);
        }
        else if(direction == Direction.BACKWARDS)
        {   
        	moveShip(-shipSpeed);
        }
        else if(direction == Direction.UP)
        {   
        	// Move ship in y-axis
        	shipPos.setY(shipPos.getY() + shipSpeed * getAnimationScale());
        }
        else if(direction == Direction.DOWN)
        {   
        	// Move ship in y-axis
        	shipPos.setY(shipPos.getY() - shipSpeed * getAnimationScale());
        }
        else if(direction == Direction.YAWLEFT)
        {   
        	// Rotate ship in y-axis
        	shipPos.setRY(shipPos.getRY() + (5.0f * shipSpeed) * getAnimationScale());
        }
        else if(direction == Direction.YAWRIGHT)
        {   
        	// Rotate ship in y-axis
        	shipPos.setRY(shipPos.getRY() - (5.0f * shipSpeed) * getAnimationScale());
        }
        
    	// ensures rotation is kept within 360 degrees
    	if (shipPos.getRY() > 360.0f )
    	{
    		shipPos.setRY(0.0f);
    	}
    	else if (shipPos.getRY() < 0.0f)
    	{
    		shipPos.setRY(360.0f);
    	}
    	
    	// Updates cameras position relative to the ship
        updateCameraPos();
        
        // move the planets
        currentSateliteRotation += 1.0f * getAnimationScale();
        
    }
    protected void renderScene()
    {//TODO: Render your scene here - remember that a scene graph will help you write this method! 
     //      It will probably call a number of other methods you will write.
    	
    	drawSkybox();
    	
    	drawStarSystem0();
    	
    	drawStarSystem1();
        
        drawShip();
    }
    
    protected void setSceneCamera()
    {
        // call the default behaviour defined in GraphicsLab. This will set a default perspective projection
        // and default camera settings ready for some custom camera positioning below...  
        super.setSceneCamera();
        //super.setViewingAxis(true);
        
        GLU.gluLookAt(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(), shipPos.getX(), shipPos.getY(), shipPos.getZ(), 0f, 1f, 0f);
        
        
   }

    protected void cleanupScene()
    {//TODO: Clean up your resources here
    	
    }
    
    /**
     * Will move ship depending on which way it is facing
     * @param shipMoveDistance Distance to move the ship
     */
    protected void moveShip(float shipMoveDistance)
    {
    	/**
    	 * 	   (45)	      (0' / 360')	(315)
    	 * 					  |
    	 * 					  |
    	 * 					  |
    	 * 	 Z (90') <--------+-------- (270')
    	 * 					  |
    	 * 					  |
    	 * 					  V
    	 * 					  X
    	 * 	   (135) 	    (180')		(225)
    	 */
    	
    	// Ships current rotation
    	float shipR = shipPos.getRY();
    	
    	// used for ratio calculations - returns the lowest rounded number of 45 degree turns in the ships current Y rotation
    	int times45 = (int) Math.floor((shipR / 45));
    	
    	float shipNewX = 0.0f;
    	float shipNewZ = 0.0f;
    	
        // Determining ship's orientation in Y-axis
    	if (shipR >= 0 && shipR < 45)
    	{
    		shipNewZ = ((shipR / 45) * shipMoveDistance);
    		shipNewX = -shipMoveDistance;
    	}
    	else if (shipR > 45 && shipR < 90)
    	{
    		shipNewZ = shipMoveDistance;
    		shipNewX = (-shipMoveDistance + ((shipR - (45 * times45)) / 45) * shipMoveDistance);
    	}
    	else if (shipR > 90 && shipR < 135)
    	{
    		shipNewZ = shipMoveDistance;
    		shipNewX = (((shipR - (45 * times45)) / 45) * shipMoveDistance);
    	}
    	else if (shipR > 135 && shipR < 180)
    	{
    		shipNewZ = (shipMoveDistance - ((shipR - (45 * times45)) / 45) * shipMoveDistance);
    		shipNewX = shipMoveDistance;
    	}
    	else if (shipR > 180 && shipR < 225) // half-way
    	{
    		shipNewZ = (-((shipR - (45 * times45)) / 45) * shipMoveDistance);
    		shipNewX = shipMoveDistance;
    	}
    	else if (shipR > 225 && shipR < 270)
    	{
    		shipNewZ = -shipMoveDistance;
    		shipNewX = (shipMoveDistance - ((shipR - (45 * times45)) / 45) * shipMoveDistance);
    	}
    	else if (shipR > 270 && shipR < 315)
    	{
    		shipNewZ = -shipMoveDistance;
    		shipNewX = (- ((shipR - (45 * times45)) / 45) * shipMoveDistance);
    	}
    	else if (shipR > 315 && shipR < 360)
    	{
    		shipNewZ = (-shipMoveDistance  + ((shipR - (45 * times45)) / 45) * shipMoveDistance);
    		shipNewX = -shipMoveDistance;
    	}
    	
    	// Update ships X and Z axis
    	shipPos.setX((shipPos.getX() + shipNewX * getAnimationScale() ));
    	shipPos.setZ((shipPos.getZ() + shipNewZ * getAnimationScale() ));
    }
    
    /**
     * Will update camera position depending on the locations and rotation of the spaceship
     */
    protected void updateCameraPos()
    {
    	
    	// Ships current rotation
    	float shipR = shipPos.getRY();
    	
    	// used for ratio calculations - returns the lowest rounded number of 45 degree turns in the ships current Y rotation
    	int times45 = (int) Math.floor((shipR / 45));
    	
    	float cameraNewX = 0.0f;
    	float cameraNewZ = 0.0f;
    	
    	System.out.println(shipR);
    	
    	if (shipR >= 0 && shipR < 45)
    	{
        	cameraNewZ = (-(shipR / 45) * cameraDistance);
        	cameraNewX = cameraDistance;
    	}
    	else if (shipR > 45 && shipR < 90)
    	{
        	cameraNewZ = -cameraDistance;
        	cameraNewX = (cameraDistance - ((shipR - (45 * times45)) / 45) * cameraDistance);
    	}
    	else if (shipR > 90 && shipR < 135)
    	{
        	cameraNewZ = -cameraDistance;
        	cameraNewX = (- ((shipR - (45 * times45)) / 45) * cameraDistance);
    	}
    	else if (shipR > 135 && shipR < 180)
    	{
        	cameraNewZ = (-cameraDistance + ((shipR - (45 * times45)) / 45) * cameraDistance);
        	cameraNewX = -cameraDistance;
    	}
    	else if (shipR > 180 && shipR < 225) // half-way
    	{
        	cameraNewZ = (+ ((shipR - (45 * times45)) / 45) * cameraDistance);
        	cameraNewX = -cameraDistance;
    	}
    	else if (shipR > 225 && shipR < 270)
    	{
        	cameraNewZ = cameraDistance;
        	cameraNewX = (-cameraDistance + ((shipR - (45 * times45)) / 45) * cameraDistance);
    	}
    	else if (shipR > 270 && shipR < 315)
    	{
        	cameraNewZ = cameraDistance;
        	cameraNewX = (+ ((shipR - (45 * times45)) / 45) * cameraDistance);
    	}
    	else if (shipR > 315 && shipR < 360)
    	{
        	cameraNewZ = (cameraDistance -((shipR - (45 * times45)) / 45) * cameraDistance);
        	cameraNewX = cameraDistance;
    	}
    	
    	cameraPos.setX((shipPos.getX() + cameraNewX ));
    	cameraPos.setZ((shipPos.getZ() + cameraNewZ ));
    	cameraPos.setY(shipPos.getY() + cameraHeight);
    	
    }
    
    protected void drawSkybox()
    {
    	GL11.glPushMatrix();
    	{
    		
       	 // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            
            float skyBoxDiffuse[]  = {1.0f, 1.0f, 1.0f, 1.0f};

            // set the material properties for the moon
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(skyBoxDiffuse));

            // Enable Textures
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,skyboxTex.getTextureID());
            
            Colour.WHITE.submit();

            // enable the texture space S,T
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            
            // Set The Texture Generation Mode For S To Sphere Mapping 
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
    		
            GL11.glTranslatef(shipPos.getX(), shipPos.getY(), shipPos.getZ());
            
    		Sphere skyboxTest = new Sphere();
    		skyboxTest.setOrientation(GLU.GLU_INSIDE);
    		skyboxTest.draw(skyboxSize, 50,50);
    		
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
    	}
    	GL11.glPopMatrix();
    }
    
    protected void drawMoon()
    {
        // draw the moon
        GL11.glPushMatrix();
        {
           float moonFrontShininess  = 2.0f;
           float moonFrontSpecular[] = {0.6f, 0.6f, 0.6f, 1.0f};
           float moonFrontDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

           // set the material properties for the moon
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, moonFrontShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(moonFrontSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(moonFrontDiffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,moonTex.getTextureID());

           // enable the texture space S,T
           GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
           GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
           
           // Set The Texture Generation Mode For S To Sphere Mapping 
           GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
           GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                   	
           // position and draw the moon using a sphere quadric object
           new Sphere().draw(0.25f,50,50);
        }
        GL11.glPopMatrix();
    }
    
    protected void drawStarSystem0()
    {
        // draw the Sun
        GL11.glPushMatrix();
        {
           // diffuse reflection of faces of the sun
           float sunDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};
           
           float sunEmmision[]  = { 0.8f,  0.7f, 0.4f, 1.0f };
           float sunResetEmmision[]  = { 0.0f,  0.0f, 0.0f, 1.0f };

           // set the material properties for the sun using OpenGL
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(sunEmmision));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(sunDiffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,sunTex.getTextureID());
           GL11.glBindTexture(GL11.GL_TEXTURE_HEIGHT,sunTex.getTextureID());

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
           new Sphere().draw(5.0f,50,50);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(sunResetEmmision));
           
           // rotate the Earth around the Sun
           GL11.glRotatef((360.0f*currentSateliteRotation/365.0f),0.0f,1.0f,0.0f);
           // the Earth is 5 units from the Sun
           GL11.glTranslatef(-20.0f,0.0f,0.0f);
           // draw the Earth
           drawEarth();
           
           // rotate the Moon around the Earth
           GL11.glRotatef((360.0f*currentSateliteRotation*0.1f),0.0f,1.0f,0.0f);
           // the Moon is .5 units from the Earth
           GL11.glTranslatef(-5.0f,0.0f,0.0f);
           // draw the moon
           drawMoon();
           
           // rotate the Moon around the Earth
           GL11.glRotatef((360.0f*currentSateliteRotation*0.2f),0.0f,1.0f,0.0f);
           // the Moon is .5 units from the Earth
           GL11.glTranslatef(-1.0f,0.0f,0.0f);
           // draw a spaceship
           GL11.glScalef(0.04f, 0.04f, 0.04f);
           drawUnitSpaceship();
           
        }
        GL11.glPopMatrix();
    }
    
    protected void drawStarSystem1()
    {
    	GL11.glPushMatrix();
    	{
    		// draw the Sun
            GL11.glPushMatrix();
            {
               // diffuse reflection of the front faces of the sun
                float sunDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};
                
                float sunEmmision[]  = { 0.8f,  0.7f, 0.4f, 1.0f };
                float sunResetEmmision[]  = { 0.0f,  0.0f, 0.0f, 1.0f };

               // set the material properties for the sun using OpenGL
               GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(sunEmmision));
               GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(sunDiffuse));

               // Enable Textures
               GL11.glEnable(GL11.GL_TEXTURE_2D);
               GL11.glBindTexture(GL11.GL_TEXTURE_2D,sunTex.getTextureID());
               GL11.glBindTexture(GL11.GL_TEXTURE_HEIGHT,sunTex.getTextureID());

               // enable the texture space S,T
               GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
               GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
               GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
               GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
               
               // Set The Texture Generation Mode For S To Sphere Mapping 
               GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
               GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                       	
               // position and draw the moon using a sphere quadric object
               GL11.glTranslatef(-180f, 0f, 0f);
               new Sphere().draw(2.0f,50,50);
               GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(sunResetEmmision));
               
               GL11.glPushMatrix();
               {
                   // rotate the Alien Planet around the Sun
                   GL11.glRotatef((360.0f*currentSateliteRotation/150.0f),0.0f,1.0f,0.0f);
                   // the planet is -20 units from the Sun
                   GL11.glTranslatef(-20.0f,0.0f,0.0f);
                   // draw the planet
                   drawAlienPlanet();
               }
               GL11.glPopMatrix();
               
               // rotate the Alien Planet 2 around the Sun
               GL11.glRotatef((360.0f*currentSateliteRotation/10.0f),0.0f,1.0f,0.0f);
               // the planet is -5 units from the Sun
               GL11.glTranslatef(-5.0f,0.0f,0.0f);
               // draw the planet
               drawAlienPlanet2(); 
 
            }
            GL11.glPopMatrix();
    	}
    	GL11.glPopMatrix();
    }
    
    protected void drawShip()
    {
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
            

            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,shipTexture.getTextureID());
        	
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(shipPos.getX(), shipPos.getY(), shipPos.getZ());
            GL11.glRotatef(shipPos.getRY() + -90, 0, 1, 0);
            //GL11.glRotatef(currentShipAngle, 0, 0, 1);
            GL11.glScalef(0.0625f, 0.0625f, 0.0625f);
            
            
            drawUnitSpaceship();
            
        }
        GL11.glPopMatrix();
    }
    
    protected void drawEarth()
    {
        // draw the earth
        GL11.glPushMatrix();
        {
           float earthShininess  = 2.0f;
           float earthSpecular[] = {0.8f, 0.8f, 0.8f, 1.0f};
           float earthDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

           // set the material properties for the planet
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, earthShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(earthSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(earthDiffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,EarthTex.getTextureID());

           // enable the texture space S,T
           GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
           GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
           
           // Set The Texture Generation Mode For S To Sphere Mapping 
           GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
           GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                   	
           // position and draw the moon using a sphere quadric object
           new Sphere().draw(1.5f,50,50);
        }
        GL11.glPopMatrix();
    }
    
    protected void drawAlienPlanet()
    {
        // draw the earth
        GL11.glPushMatrix();
        {
           float alienPlanetShininess  = 2.0f;
           float alienPlanetSpecular[] = {0.6f, 0.6f, 0.6f, 1.0f};
           float alienPlanetDiffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

           // set the material properties for the planet
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, alienPlanetShininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(alienPlanetSpecular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(alienPlanetDiffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,alienTex.getTextureID());

           // enable the texture space S,T
           GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
           GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
           
           // Set The Texture Generation Mode For S To Sphere Mapping 
           GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
           GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                   	
           // position and draw the moon using a sphere quadric object
           new Sphere().draw(1.0f,50,50);
        }
        GL11.glPopMatrix();
    }
    
    protected void drawAlienPlanet2()
    {
        // draw the earth
        GL11.glPushMatrix();
        {
           float alienPlanet2Shininess  = 1.0f;
           float alienPlanet2Specular[] = {0.6f, 0.6f, 0.6f, 1.0f};
           float alienPlanet2Diffuse[]  = {0.6f, 0.6f, 0.6f, 1.0f};

           // set the material properties for the planet
           GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, alienPlanet2Shininess);
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(alienPlanet2Specular));
           GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(alienPlanet2Diffuse));

           // Enable Textures
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glBindTexture(GL11.GL_TEXTURE_2D,alien2Tex.getTextureID());

           // enable the texture space S,T
           GL11.glEnable(GL11.GL_TEXTURE_GEN_S); 
           GL11.glEnable(GL11.GL_TEXTURE_GEN_T);            
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
           GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
           
           // Set The Texture Generation Mode For S To Sphere Mapping 
           GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);           
           GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
                   	
           // position and draw the moon using a sphere quadric object
           new Sphere().draw(0.75f,50,50);
        }
        GL11.glPopMatrix();
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
			new Normal(v9.toVector(),v10.toVector(),v28.toVector(),v27.toVector()).submit();
			
			v9.submit();
			v10.submit();
			v28.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		//Cockpit left
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v29.toVector(),v9.toVector(),v27.toVector()).submit();
			
			v29.submit();
			v9.submit();
			v27.submit();
		}
		GL11.glEnd();
		
		//Cockpit right
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v10.toVector(),v30.toVector(),v28.toVector()).submit();
			
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
