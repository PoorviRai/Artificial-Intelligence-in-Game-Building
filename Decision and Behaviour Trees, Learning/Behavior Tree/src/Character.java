import java.util.LinkedList;
import java.util.Queue;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

class Character
{
	PShape character;

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
	
	Queue<PVector> breadCrumbs;
	int numCrumbs;
	
	int start;
	int goal;
	
	Graph graph;
	ObjectPath objectPath;
	PApplet pr;

	Character(PApplet parent)
	{
		this.pr = parent;
		this.graph = new Graph(pr);
		this.start = -1;
		this.goal = -1;
		this.objectPath = new ObjectPath();
		this.numCrumbs = 80;
		
		this.target = new PVector(0, 0);
		this.position = new PVector(this.graph.tileSize/2, this.graph.tileSize/2);
		this.maxForce = 0.5f;
		this.maxSpeed = 3f;
		this.acceleration = new PVector(0, 0);
		this.maxAcceleration = 1f;
		this.velocity = new PVector(this.maxSpeed, 0);
		
		this.predictTime = 0.2f;
		this.pathOffset = 50;
		this.currentIndex = 0;
		
		this.r = 7f;
		
		this.ros = 10;
		this.rod = 80;
		
		this.breadCrumbs = new LinkedList<PVector>();
		
	}

	public void runChar()
	{
		updateKinematics();
		updateOrientation();
		breadCrumbs();
		renderCrumbs();
		renderCharacter();
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

		if(PVector.dist(this.position, this.target) <= this.graph.tileSize)
		{
			this.currentIndex = this.currentIndex + 1;
			
		}
		
		if(this.currentIndex == op.objectPath.size())
		{
			if(this.position.dist(this.target) <= this.ros)
				this.doneFollowing = true;
			this.arrive(this.target);
			this.currentIndex -= 1;
		}
		else
		{
			this.seek(this.target);
		}
			
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
		this.rotation = newAngle - this.orientation;
		this.rotation = mapToRange(this.rotation);
		
		arc = (float)this.rotation/((float)0.020*500);
		
		this.orientation += arc;
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

	void renderCharacter()
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
		pr.fill(0, 255, 255);
		pr.ellipse(0, 0, this.r/2, this.r/2);
		pr.fill(255, 255, 255);
		pr.popMatrix();
	}
	
	//For Decision Tree
	
	boolean detectProximity(int resultantTile)
	{
		//Check to see if in proximity to an obstacle
		
		if(PVector.dist(this.position, new PVector(this.position.x, pr.height)) < 2*this.r)
			return true;
		
		if(PVector.dist(this.position, new PVector(this.position.x, 0)) < 2*this.r)
			return true;
		
		if(PVector.dist(this.position, new PVector(pr.width, this.position.y)) < 2.5f*this.r)
			return true;
		
		if(PVector.dist(this.position, new PVector(0, this.position.y)) < 2.5f*this.r)
			return true;
		
		float warning = 1.414f*this.graph.tileSize/2 + this.r;
		int i = resultantTile / 10;
		int j = resultantTile % 10;
		boolean top = false, bottom = false, right = false, left = false;
		
		if(i - 1 >= 0)
		{
			top = true;
			if(this.graph.check[i - 1][j])
			{
				if(distance(this.position, j, i - 1) < warning)
				{
					return true;
				}
			}
			
		}
		
		if(i + 1 <= this.graph.check.length - 1)
		{
			bottom = true;
			if(this.graph.check[i + 1][j])
			{
				if(distance(this.position, j, i + 1) < warning)
				{
					return true;
				}
			}
		}
		
		if(j - 1 >= 0)
		{
			left = true;
			if(this.graph.check[i][j - 1])
			{
				if(distance(this.position, j - 1, i) < warning)
				{
					return true;
				}
			}
		}
		
		if(j + 1 <= this.graph.check[i].length - 1)
		{
			right = true;
			if(this.graph.check[i][j + 1])
			{
				if(distance(this.position, j + 1, i) < warning)
				{
					return true;
				}
			}
		}
		
		if(top && left)
		{
			if(this.graph.check[i - 1][j - 1])
			{
				if(distance(this.position, j - 1, i - 1) < warning)
				{
					return true;
				}
			}
		}
		
		if(top && right)
		{
			if(this.graph.check[i - 1][j + 1])
			{
				if(distance(this.position, j + 1, i - 1) < warning)
				{
					return true;
				}
			}
		}
		
		if(bottom && right)
		{
			if(this.graph.check[i + 1][j + 1])
			{
				if(distance(this.position, j + 1, i + 1) < warning)
				{
					return true;
				}
			}
		}
		
		if(bottom && left)
		{
			if(this.graph.check[i + 1][j - 1])
			{
				if(distance(this.position, j - 1, i + 1) < warning)
				{
					return true;
				}
			}
		}
				
		return false;
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