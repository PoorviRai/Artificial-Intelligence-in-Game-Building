import processing.core.PApplet;
import processing.core.PVector;

public class Flocking extends PApplet {

	CharFlock[] chars = new CharFlock[100];
	
	public static void main(String[] args)
	{
		PApplet.main("Flocking");
	}
	
	
	public void settings()
	{
		size(500,500);
    }

    public void setup()
    {
    	  frameRate(30);
    	  
    	  for(int i = 0; i < chars.length; i++)
    	  {
    		int radius=5;
    		PVector initial_position = new PVector(this.width/2, this.height/2);
		  	PVector initial_velocity = new PVector(0,0); 
		  	PVector initial_acceleration = new PVector(0,0); 
		  	
		  	float initial_orientation=0;		
		  	float initial_rotation=0;
		  	float initial_angular_acceleration = 0;
		   
		  	float max_velocity = (float) 2;
		  	float max_acceleration  = 1;
		  	float max_rotation=5;
		  	float max_angular_acceleration=1;
		  	
		  	chars[i] = new CharFlock(this, radius, initial_position, initial_velocity, initial_orientation, max_rotation, initial_rotation, max_velocity, max_acceleration, initial_acceleration, initial_angular_acceleration, max_angular_acceleration);
    	  }
    	  
    }

    public void draw()
    {
    	  background(255);
    	  
    	  int flock_distance = 20;
    	  for(int i = 2 ; i < chars.length; i++) //char[0] and char[1] are lead chars
    	  {
    		  chars[i].flock(chars,i,flock_distance);
    		  chars[i].align();
    		  chars[i].update(1); 
    		  chars[i].display(frameCount,i);
    	  }
 	      
    	  //Lead Char 1
    	  if(frameCount%4==0)
    		  chars[0].wander1();
		  chars[0].update(1);
		  chars[0].display(frameCount,0);
		  
		  //Lead Char 2
		  if(frameCount%4==0)
			  chars[1].wander1();
		  chars[1].update(1);
		  chars[1].display(frameCount,1);
    	  
    }
    
}
