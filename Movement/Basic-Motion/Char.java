import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Char extends Kinematic_Variables {
	Char(PApplet p, PVector initial_pos, PVector initial_vel, float initial_orient, float initial_rot){
		parent = p;
		
		position = initial_pos;
		orientation = initial_orient;
		
		velocity = initial_vel;
		rotation = initial_rot;
	}
	
	void display() {
		//parent.background(255);
		
	    parent.ellipse(position.x,position.y,40,40);
	    parent.pushMatrix();
	    parent.translate(position.x,position.y);
	    parent.rotate(PApplet.radians(orientation));
	    parent.ellipse(position.x,position.y,40,40);
	    parent.triangle(5,-20,5,20,40,0);
	    parent.fill(0,0,0);
	    parent.popMatrix();
	}
	
	void update() {
		if(position.y == (parent.height-40) && position.x != (parent.width-40) && orientation == 0)
  		{
  			position.x += velocity.x;
  		}
  		else if(position.x == (parent.width-40) && orientation > -90)
  		{
  			orientation -= rotation;	//moving anti-clockwise
  		}
  		else if(position.x == (parent.width-40) && orientation == -90 && position.y != 40)
  		{
  			position.y -= velocity.y;	//moving upward = negative y-axis
  		}
  		else if(position.y == (40) && orientation > -180)
  		{
  			orientation -= rotation;
  		}
  		else if(position.y == (40) && orientation == -180 && position.x != (40))
  		{
  			position.x -= velocity.x;	//moving left = negative x-axis
  		}
  		else if(position.x == (40) && orientation > -270)
  		{
  			orientation -= rotation;
  		}
  		else if(position.x == (40) && orientation == -270 && (position.y != parent.height-40))
  		{
  			position.y += velocity.y;	//moving down = positive y-axis
  		}
  		else if(position.y == (parent.height-40) && orientation > -360)
  		{
  			orientation -= rotation;
  			if(orientation == -360)		//for continuous motion
  				orientation =0;
  		}
	}
	
	ArrayList<Float> b_positionx = new ArrayList<Float>();
	ArrayList<Float> b_positiony = new ArrayList<Float>();

	
	void breadCrumbs()
	  {
		
		  if(position.x%50==0)
		  {
			  PVector temp = new PVector(position.x, position.y);
			  b_positionx.add(temp.x);
			  b_positiony.add(temp.y);
		  }
		  if(position.y%50==0)
		  {
			  PVector temp = new PVector(position.x, position.y);
			  b_positionx.add(temp.x);
			  b_positiony.add(temp.y);
		  }
		  
		  for(int i=0 ; i < b_positionx.size(); i++)
		  {
	    	  parent.fill(0,0,0);
			  parent.ellipse(b_positionx.get(i), b_positiony.get(i), 4, 4);
		  }
	  }
}
