import processing.core.PApplet;
import processing.core.PVector;

public class Steering_Variables {
	  PVector position;
	  float orientation; 
	  PVector velocity;
	  float rotation;
	  float max_velocity;
	  float max_acceleration;
	  float max_rotation;
	  
	  PVector acceleration;
	  float angular_acceleration;
	  float max_angular_acceleration;

	  PApplet parent;	// The parent PApplet that we will render ourselves onto
}
