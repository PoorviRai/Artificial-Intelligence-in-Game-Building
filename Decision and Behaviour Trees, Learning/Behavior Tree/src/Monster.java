import java.util.LinkedList;
import java.util.Queue;

import processing.core.*;

public class Monster
{
	PShape monster;

	PVector position;
	PVector velocity;
	PVector acceleration;
	PVector target, linearSteer, targetVelocity;
	
	float rotation;
	float orientation;
	float wanderangle;
	float newAngle;
	float ros;
	float rod;
	float targetSpeed;
	float maxAcceleration;

	float scaleF;
	float maxSpeed;
	float maxForce;
	float predictTime;
	float pathOffset;

	float r;
	
	int currentIndex;

	boolean wanderMode;
	boolean doneFollowing;
	boolean reverse;
	boolean signature;
	
	Queue<PVector> breadCrumbs;
	int numCrumbs;
	
	int start;
	int goal;
	
	Graph graph;
	ObjectPath objectPath;	
	PApplet pr;

	Monster(PApplet parent)
	{
		this.pr = parent;
		this.graph = new Graph(parent);
		this.start = -1;
		this.goal = -1;
		this.objectPath = new ObjectPath();
		this.objectPath = this.populate(this.objectPath);
		this.numCrumbs = 80;
		
		this.target = new PVector(0, 0);
		this.position = new PVector(this.objectPath.getStart().x, this.objectPath.getStart().y);
		this.maxForce = 0.5f;
		this.maxSpeed = 2.5f;
		this.acceleration = new PVector(0, 0);
		this.maxAcceleration = 1f;
		this.velocity = new PVector(this.maxSpeed, 0);
		
		this.pathOffset = 50;
		this.predictTime = 0.2f;
		this.currentIndex = 0;
		
		this.r = 10f;
		
		this.ros = 10;
		this.rod = 80;
		
		this.breadCrumbs = new LinkedList<PVector>();
		
	}

	public void runMonster()
	{
		updateKinematics();
		updateOrientation();			
		breadCrumbs();
		renderCrumbs();
		renderMonster();
	}

	public void breadCrumbs()
	{
		if(pr.frameCount % this.maxSpeed == 0)
		{
			if(breadCrumbs.size() == this.numCrumbs)
			{
				this.breadCrumbs.remove();
			}
			this.breadCrumbs.add(new PVector(this.position.x, this.position.y));

		}
	}

	public void renderCrumbs()
	{

		for(PVector temp: this.breadCrumbs)
		{
			pr.pushMatrix();
			pr.translate(temp.x, temp.y);
			pr.fill(255, 0, 0);
			pr.ellipseMode(PConstants.RADIUS);
			pr.ellipse(0, 0, 1, 1);
			pr.fill(255);
			pr.popMatrix();

		}
	}

	void follow(ObjectPath op)
	{
		this.target = op.objectPath.get(this.currentIndex);

		if(PVector.dist(this.position, this.target) <= this.graph.tileSize/2)
		{
			if(this.reverse)
				this.currentIndex = this.currentIndex - 1;
			else
				this.currentIndex = this.currentIndex + 1;		
		}
		
		if(this.currentIndex == op.objectPath.size() || this.currentIndex == -1)
		{
			if(this.reverse)
				this.currentIndex += 1;
			else
				this.currentIndex -= 1;
			
			if(this.position.dist(this.target) <= this.ros)
				this.reverse = !this.reverse;		
		}
		
		this.seek(this.target);
			
	}
	
	ObjectPath populate(ObjectPath op)
	{
		int[] monsterPath = {30, 31, 32, 33, 43, 44, 34, 35, 36, 46, 47, 37, 38, 39};
		for(int i = 0; i < monsterPath.length; i++)
		{
			int tempX = monsterPath[i] % 10;
			int tempY = monsterPath[i] / 10;
			tempX = tempX * this.graph.tileSize + this.graph.tileSize/2;
			tempY = tempY * this.graph.tileSize + this.graph.tileSize/2;
			
			op.add(tempX, tempY);
		}
		return op;
	}

	void updateKinematics()
	{
		this.velocity.add(this.acceleration);
		this.velocity.limit(this.maxSpeed);
		this.position.add(this.velocity);
		this.acceleration.mult(0);
	}

	void applyForce(PVector force)
	{
		this.acceleration.add(force);
	}

	void seek(PVector target)
	{
		if(target.x >= pr.width)
			  target.x = 0;
		  else if(target.x < 0)
			  target.x = pr.width;
		  
		  if(target.y >= 700)
			  target.y = 0;
		  else if(target.y < 0)
			  target.y = 700;
		  
		PVector desired = PVector.sub(target, this.position);
		this.newAngle = getNewOrientation(desired, this.orientation);

		desired.normalize();
		float tempSpeed = this.maxSpeed;
		if(this.wanderMode)
			tempSpeed = 0.5f*tempSpeed;
		desired.mult(tempSpeed);

		PVector steering = PVector.sub(desired, this.velocity);
		float tempForce = this.maxForce;
		if(this.wanderMode)
			tempForce = 0.5f*tempForce;
		steering.limit(tempForce);
		this.applyForce(steering);
	}

	public void arrive(PVector target)
	{
		this.targetVelocity = PVector.sub(this.target, this.position);
		float distance = this.targetVelocity.mag();
		if(distance < this.ros)
		{
			this.acceleration.mult(0);
			this.targetVelocity.mult((float)0.0);			
		}
		else if(distance < this.rod)
		{
			this.targetSpeed = this.maxSpeed * distance / this.rod;
		}
		else
		{
			this.targetSpeed = this.maxSpeed;
		}
				
		this.targetVelocity.normalize();
		this.targetVelocity.mult(this.targetSpeed);
		
		this.linearSteer = PVector.sub(this.targetVelocity, this.velocity);
		
		this.applyForce(this.linearSteer);
		
	}
	
	public float getNewOrientation(PVector velocity, float orient)
	{
		if(velocity.mag() > 0)
		{
			return PApplet.atan2(velocity.y, velocity.x);
		}
		return orient;
	}

	public void updateOrientation()
	{
		float arc;
		if(!this.signature)
		{
			this.rotation = newAngle - this.orientation;
			this.rotation = mapToRange(this.rotation);

			arc = (float)this.rotation/((float)0.020*500);
		}
		else
			arc = PApplet.radians(30);
		
		this.orientation += arc;
	}
	
	public void signatureMove()
	{
		this.orientation += PApplet.radians(30);
	}

	public float mapToRange(float rotation)
	{
		rotation = rotation % PConstants.TWO_PI;
		if(PApplet.abs(rotation) <= PConstants.PI)
		{
			return rotation;
		}
		else if(rotation > PConstants.PI)
			return rotation - PConstants.TWO_PI;
		else
			return rotation + PConstants.TWO_PI;
	}

	void renderMonster()
	{
		pr.pushMatrix();
		pr.translate(this.position.x, this.position.y);
		pr.rotate(this.orientation + PApplet.radians(90));
		pr.fill(0);
		pr.ellipse(0, 0, r, r);
		pr.fill(0);
		pr.beginShape();
		pr.vertex(0, 0);
		pr.vertex(-r*1.732f/2, -r/2);
		pr.vertex(0,-2*r);
		pr.vertex(r*1.732f/2, -r/2);
		pr.endShape();
		pr.fill(255, 0, 0);
		pr.ellipse(0, 0, this.r/2, this.r/2);
		pr.fill(255, 255, 255);
		pr.popMatrix();
	}
		
	float distance(PVector pos, int tileMidX, int tileMidY)
	{
		
		tileMidX *= this.graph.tileSize;
		tileMidY *= this.graph.tileSize;
		
		tileMidX += this.graph.tileSize/2;
		tileMidY += this.graph.tileSize/2;
		
		float xDist = pos.x - tileMidX;
		float yDist = pos.y - tileMidY;
		return (float)Math.sqrt(xDist*xDist + yDist*yDist);
	}
	
	void wander() 
	{
		float wanderR = 10;         					// Radius for our "wander circle"
		float wanderD = 120;         					// Distance for our "wander circle"
		float change = 1f;
		wanderangle += pr.random(-change,change);     	// Randomly change wander angle

		// Now we have to calculate the new position to steer towards on the wander circle
		PVector circlepos = velocity.get();    			// Start with velocity
		circlepos.normalize();            				// Normalize
		circlepos.mult(wanderD);          				// Multiply by distance
		circlepos.add(position);               			// Make it relative to char's position

		float h = velocity.heading();        			// To offset wander angle

		PVector circleOffSet = new PVector(wanderR*PApplet.cos(wanderangle+h),wanderR*PApplet.sin(wanderangle+h));
		PVector target = PVector.add(circlepos,circleOffSet);
		target = check(target);
		seek(target);

	}
	
	
	PVector check(PVector p)
	{
        PVector check = p.copy();
        if(p.x > pr.width - this.graph.tileSize || p.x < 0 + this.graph.tileSize)  
        	check.x = pr.random(p.x - 250*(p.x)/pr.abs(p.x), p.y);
        if(p.y > pr.height - this.graph.tileSize || p.y < 0 + this.graph.tileSize)  
        	check.y = pr.random(p.x, p.y - 250*(p.y)/pr.abs(p.y));
        return check;
    }
	
}