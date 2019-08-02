import processing.core.PApplet;
import processing.core.PVector;

public class Basic_Motion extends PApplet{

	public static void main(String[] args) {
		PApplet.main("Basic_Motion");
	}
	
	CharK c;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		
		PVector initial_position = new PVector(20, this.height-40);
		PVector initial_velocity = new PVector(2,2);   // Velocity of char
		
		float initial_orientation=0;		  
		float initial_rotation=2;
		
	    c = new CharK(this, initial_position, initial_velocity, initial_orientation, initial_rotation);
	}
	
	 public void draw() 
	  {
	      background(255);
	      c.update();
	      c.display();
	      c.breadCrumbs();
	  }
}
