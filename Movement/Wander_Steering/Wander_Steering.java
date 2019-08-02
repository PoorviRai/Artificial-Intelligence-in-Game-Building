import processing.core.PApplet;
import processing.core.PVector;

public class Wander_Steering extends PApplet{
	public static void main(String[] args) 
	{	
		PApplet.main("Wander_Steering");
	}
	
	CharWander c;

	public void settings()
	{
		size(500,500);
    }
	
	public void setup()
    {
    	  frameRate(30);
    	  int radius=20;
		  PVector initial_position = new PVector(this.width/2, this.height/2);
		  PVector initial_velocity = new PVector(0,0);   // Velocity of player
		  PVector initial_acceleration = new PVector(0,0);   // Velocity of player
		  
		  float initial_orientation=0;		  
		  float initial_rotation=0;
		  float initial_angular_acceleration = 0;
		  
		  float max_velocity = (float) 5;
		  float max_acceleration  = 1;
		  float max_rotation=5;
		  float max_angular_acceleration= 1;

	      c = new CharWander(this, radius, initial_position, initial_velocity, initial_orientation, max_rotation, initial_rotation, max_velocity,  max_acceleration, initial_acceleration,  initial_angular_acceleration, max_angular_acceleration);
    }
	
	public void draw()
    {
    	  background(255);
    	  
    	  if(frameCount%10==0)
    		  c.wander1();
    		  //c.wander2();
 	      c.update(1);
 	      c.display(frameCount);
 	      c.breadCrumbs(frameCount); 	      
    }
	
}
