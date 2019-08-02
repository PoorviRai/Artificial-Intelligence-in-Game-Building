import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Follow_Path extends Steering_Variables{
	int r;
	
	Follow_Path(PApplet p,int radius,PVector initial_pos,PVector initial_vel,float initial_orient,float max_rot,float initial_rot,float max_vel,float max_acc,PVector initial_acc,float initial_angular_acc,float max_angular_acc) 
    {
    	parent = p;
    	r = radius;
    	
    	position = initial_pos;
    	orientation = initial_orient;
    	velocity = initial_vel;
    	rotation = initial_rot;
    	max_velocity = max_vel;
    	max_acceleration = max_acc;
    	max_rotation = max_rot;
    	
    	acceleration = initial_acc;
    	angular_acceleration = initial_angular_acc;
    	max_angular_acceleration = max_angular_acc;
    }
	
	void display(int frameCount) {
		  
	    parent.fill(0,0,0);
	    parent.ellipse(position.x,position.y,2*r,2*r);  
	        
	    parent.pushMatrix();
	    parent.translate(position.x,position.y);
	    parent.rotate(PApplet.radians(360-orientation));
	    parent.triangle(r/4,-1*r ,r/4,r,2*r,0);
	    parent.rotate(PApplet.radians(360-orientation));
	    parent.popMatrix();
	  }
	
	public void update(int delta_time){
		
		PVector new_pos = PVector.mult(velocity, delta_time);
		PVector new_vel = PVector.mult(acceleration, delta_time);
		  
		position = PVector.add(position,new_pos);
		
		velocity = PVector.add(velocity,new_vel);
//		  orientation += rotation * delta_time;
		  
//		  if(orientation >= 360)
//			  orientation -=360;
//		  

//		  rotation += angular_acceleration * delta_time;
		  
		if(velocity.mag()> max_velocity)
			{
				PVector unit_vel = PVector.div(velocity,velocity.mag());
				velocity = PVector.mult(unit_vel, max_velocity);
			}
		else if(velocity.mag()<0)
			velocity = new PVector(0,0);
		  
//		  if(java.lang.Math.abs(rotation) > max_rotation)
//		  {
//			  rotation = rotation*max_rotation/java.lang.Math.abs(rotation);
//		  }
	}
	
	public int arrive1(PVector target, int tttV){
		
		float rad_of_dist = 30;
		float rad_of_sat = 20;
		float goal_speed;
		  
		if (position.x == target.x && position.y == target.y)
		{
			acceleration.set(new PVector(0,0));
			return 0;
		}
			 		  
		PVector direction = PVector.sub(target, position);
		float distance = direction.mag();
		
		direction.normalize();
		  
		if(distance < rad_of_sat)
			goal_speed = 0;
		else if (distance > rad_of_dist)
			goal_speed = max_velocity;
		else
			goal_speed = max_velocity * (distance/rad_of_dist);
		  
		PVector goal_velocity = direction;
		goal_velocity.normalize();
		goal_velocity = PVector.mult(goal_velocity, goal_speed);
		  
		acceleration = PVector.sub(goal_velocity, velocity);
		acceleration = PVector.div(acceleration, tttV);
		   		  
		//float target_orient;
		//float rad_of_dec = 10;
		//float rad_of_satangle = 4;  
		
		if(direction.y > 0 && direction.x > 0)
			orientation = 360-rad_to_degree(java.lang.Math.atan(direction.y/direction.x));
		else if(direction.x < 0 && direction.y < 0)
			orientation = 180 - rad_to_degree(java.lang.Math.atan(direction.y/direction.x));
		else if(direction.x > 0 && direction.y < 0)
			orientation = -1 *rad_to_degree(java.lang.Math.atan(direction.y/direction.x));
		else if(direction.x < 0 && direction.y > 0)
			orientation = 180 + -1 *rad_to_degree(java.lang.Math.atan(direction.y/direction.x));
		else 
			orientation = rad_to_degree(java.lang.Math.atan(-direction.y/direction.x));
		 		  
		return 1;
	}
	
	public float rad_to_degree(double d){
		
		return (float) (d*180/Math.PI);
	}
	  
	ArrayList<Float> b_positionx = new ArrayList<Float>();
	ArrayList<Float> b_positiony = new ArrayList<Float>();

	void breadCrumbs(int frameCount){
		
		if(frameCount % 5 ==0)
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
