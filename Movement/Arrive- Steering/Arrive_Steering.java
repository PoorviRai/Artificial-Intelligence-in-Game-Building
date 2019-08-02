import processing.core.PApplet;
import processing.core.PVector;

public class Arrive_Steering extends PApplet{
	
	public static void main(String[] args) 
	{	
		PApplet.main("Arrive_Steering");
	}
	
	CharS c;

	public void settings()
	{
		size(500,500);
    }
	
	public void setup()
    {
    	  frameRate(30);
    	  int radius=20;
		  PVector initial_position = new PVector(20, this.height-20);
		  PVector initial_velocity = new PVector(0,0);   // Velocity of player
		  PVector initial_acceleration = new PVector(0,0);   // Velocity of player
		  
		  float initial_orientation=0;		  
		  float initial_rotation=0;
		  float initial_angular_acceleration = 0;
		  
		  float max_velocity = (float) 8;
		  float max_acceleration  = 10;
		  float max_rotation=10;
		  float max_angular_acceleration= 3;

	      c = new CharS(this, radius, initial_position, initial_velocity, initial_orientation, max_rotation, initial_rotation, max_velocity,  max_acceleration, initial_acceleration,  initial_angular_acceleration, max_angular_acceleration);
    }
	
	public void draw()
    {
    	  background(255);
    	  
    	  if(flag == 1)
 	    	  flag = c.arrive1(target, 1);
    	  	  //flag = c.arrive2(target);
    	  
 	      c.update(1);
 	      c.display(frameCount);
 	      c.breadCrumbs(frameCount);
 	      
    }
    
    int flag = 0; 
    PVector target = new PVector(0,0);
    int mouse_clicked = 0;
    public void mousePressed() 
    {
    	target.x = mouseX;
    	target.y = mouseY;
    	flag=1;
    }
}
