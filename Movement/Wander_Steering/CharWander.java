import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import processing.core.PApplet;
import processing.core.PVector;

public class CharWander extends Steering_Variables{
	int r;
	
	CharWander(PApplet p,int radius,PVector initial_pos,PVector initial_vel,float initial_orient,float max_rot,float initial_rot,float max_vel,float max_acc,PVector initial_acc,float initial_angular_acc,float max_angular_acc) 
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
  
  public void update(int delta_time) 
  {
	  PVector new_pos = PVector.mult(velocity, delta_time);
	  PVector new_vel = PVector.mult(acceleration, delta_time);
	  
	  position = PVector.add(position,new_pos);
	  orientation += rotation * delta_time;
	  
	  if(orientation >= 360)
		  orientation -=360;
	   
	  velocity = PVector.add(velocity,new_vel);
	  rotation += angular_acceleration * delta_time;
	  
	  if(velocity.mag()> max_velocity)
	  {
		  PVector unit_vel = PVector.div(velocity,velocity.mag());
		  velocity = PVector.mult(unit_vel, max_velocity);
		  
	  }
	  else if(velocity.mag()<0)
		  velocity = new PVector(0,0);
	  
	  
	  if(Math.abs(rotation) > max_rotation)
	  {
		  rotation = rotation*max_rotation/Math.abs(rotation);
	  }
	  
	  //Boundary Conditions
	  if(position.x >= parent.width)
		  position.x = 0;
	  else if(position.x < 0)
		  position.x = parent.width;
	  
	  if(position.y >= parent.height)
		  position.y = 0;
	  else if(position.y < 0)
		  position.y = parent.height;
	  
  }
  
  public int arrive1(PVector target, int tttV)
  {
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
	   		  
	  float target_orient;
	  float rad_of_dec = 20;
	  float rad_of_satangle = 4;  
	  
	  if(direction.x>0)
	  {
		  target_orient = rad_to_degree(Math.atan(-direction.y/direction.x));
		  if(target_orient < 0)
			  target_orient = target_orient + 360;
	  }
	  else
		  target_orient = 180 + rad_to_degree(Math.atan(-direction.y/direction.x));

	  float rot = target_orient - orientation;
	  if(rot > 180)
		  rot -= 360;
	  else if(rot < -180)
		  rot += 360;
	  
	  float rotation_dir = rot/Math.abs(rot);
	  
	  if(Math.abs(rot) > rad_of_dec)
	  {
		  angular_acceleration = rotation_dir * max_angular_acceleration;
	  }
	  else if(Math.abs(rot) > rad_of_satangle)
	  {
		  angular_acceleration = max_angular_acceleration * rot / rad_of_dec;
	  }
	  else
	  {
		  rotation = 0;
		  angular_acceleration = 0;
	  }
	  
	  return 1;
  }
  
  public float rad_to_degree(double d)
  {
	  return (float) (d*180/Math.PI);
  }
    
  public void wander1() {
	  Random rn = new Random();
	  float rand1 = rn.nextFloat();
	  float rand2 = rn.nextFloat();

	  float rand_num = rand1-rand2;
	  float wander_orient, target_orient;
	  
	  wander_orient = rand_num * rad_to_degree(180); //180 = wanderRate
	  target_orient = wander_orient + orientation;
	  
	  PVector char_orient = new PVector((float)Math.cos(orientation), (float) Math.sin(orientation));
	  PVector tar_orient = new PVector((float)Math.cos(target_orient), (float) Math.sin(target_orient));
	  	  
	  PVector target = PVector.add(position, PVector.mult(char_orient, 200)); //200 = wanderOffset
	  target = PVector.add(target, PVector.mult(tar_orient, 100));	//100 = wanderRadius
	  	  
	  velocity.x = (float) (max_velocity*Math.cos(PApplet.radians(360-orientation)));
	  velocity.y = (float) (max_velocity*Math.sin(PApplet.radians(360-orientation)));
	  
	  arrive1(target,100);
  }
  
  public void wander2()
  {
	  //wander without arrive
	  Random rn = new Random();
	  float rand1 = rn.nextFloat();
	  float rand2 = rn.nextFloat();

	  double rand_num = rand1-rand2;
	  rotation = (float) (max_rotation*rand_num);
	  
	  velocity.x = (float) (max_velocity*Math.cos(PApplet.radians(360-orientation)));
	  velocity.y = (float) (max_velocity*Math.sin(PApplet.radians(360-orientation)));

  }
  
  ArrayList<Float> b_positionx = new ArrayList<Float>();
  ArrayList<Float> b_positiony = new ArrayList<Float>();

  void breadCrumbs(int frameCount)
  {
	
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
