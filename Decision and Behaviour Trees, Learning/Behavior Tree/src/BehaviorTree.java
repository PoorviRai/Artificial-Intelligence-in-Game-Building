import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import processing.core.PApplet;
import processing.core.PVector;

public class BehaviorTree extends PApplet 
{
	public static void main(String[] args)
	{
		PApplet.main("BehaviorTree");
	}
	
	Character c;
	Monster m;
	
	boolean isWithinPerception = false;
	
	int currentAction;
	
	String action = "";
	PrintWriter pw;
	
	//Decision Tree for Hero
	void decisionTree()
	{
		//get current X and Y
		int newX = (int)c.position.x/c.graph.tileSize;
		int newY = (int)c.position.y/c.graph.tileSize;
		
		int resultantTile = 10*newY + newX;
		
		//begin tree
		if(c.goal != -1) //root node
		{
			if(c.doneFollowing) 
			{
				c.wanderMode = true;
				c.goal = -1;
				c.start = -1;
				c.wander();
			}
			else
			{
				c.follow(c.objectPath);
			}
			
		}
		else
		{
			if(c.detectProximity(resultantTile))
			{
				c.start = resultantTile;
				
				if(c.start % 10 < 5 && c.start / 10 < 4)
				{
					c.goal = 67;			//going to bottom right
				}
				
				if(c.start % 10 >= 5 && c.start / 10 < 4)
				{
					c.goal = 61;			//going to bottom left
				}
				
				if(c.start % 10 < 5 && c.start / 10 >= 4)
				{
					c.goal = 11;			//going to top right & start
				}
				
				if(c.start % 10 >= 5 && c.start / 10 >= 4)
				{
					c.goal = 27;			//going to top left
				}
					
				c.wanderMode = false;
				profileNewPath(c.graph.tileSize * (c.goal % 10), c.graph.tileSize * (c.goal / 10));
				c.doneFollowing = false;
			}
			else
			{
				c.wanderMode = true;
				c.wander();
			}
		}
		
	}
	
	
	//Behavior Tree for Monster
	void behaviorTree()
	{
		switch(currentAction)
		{
			case 1:
			{
				if(!rangeCheck())
				{
					m.follow(m.objectPath);
				}
				else
				{
					currentAction++;
					action = "chase";
					isWithinPerception = true;
				}
				break;
			}
			
			case 2:
			{
				if(!withinReach() && rangeCheck())
				{
					m.signature = true;
					m.seek(c.position);
				}
				else if(!withinReach() && !rangeCheck())
				{
					currentAction--;
					action = "follow";
					isWithinPerception = false;
					m.signature = false;
				}
				else
				{	
					currentAction++;
					action = "eat";
				}
				
				break;
			}
			
			case 3:
			{
				action = "follow";
				
				reset();
				break;
			}
			
		}
	}
	
	boolean rangeCheck()
	{
		if(m.reverse)
		{
			if((m.position.x - c.position.x) >= 0)
			{
				if(PVector.dist(m.position, c.position) <= 15*m.r)
					return true;
			}
			else
				if(PVector.dist(m.position, c.position) <= 10*m.r)
					return true;
				
		}
		else
		{
			if((m.position.x - c.position.x) <= 0)
			{
				if(PVector.dist(m.position, c.position) <= 15*m.r)
					return true;
			}
			else
				if(PVector.dist(m.position, c.position) <= 10*m.r)
					return true;
		}
		
		return false;
	}
	
	boolean withinReach()
	{
		if(PVector.dist(m.position, c.position) <= 1.5f*m.r)
			return true;
		return false;
	}
	
	
	//initialization
	public void settings()
	{
		size(1000, 800);	
	}
	
	public void setup()
	{
		//UNCOMMENT FOR WRITING DATA
		//try {
		//	pw = new PrintWriter(new File("data.csv"));
		//} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		 
        //StringBuffer header = new StringBuffer("");
        //header.append("FrameCount, Within Perception?, Within Eating Distance?, Action\n");
		
        //pw.write(header.toString());
        
		smooth();
		background(255, 255, 255);
		
		c = new Character(this);
		m = new Monster(this);
		currentAction = 1;
		action = "follow";
		
	}
	
	public void reset()
	{
		c = new Character(this);
		m = new Monster(this);
		currentAction = 1;
	}
	
	public void mouseClicked()
	{
		profileNewPath(mouseX, mouseY);
	}
	
	void profileNewPath(int X, int Y)
	{
		int newX = X/c.graph.tileSize;
		int newY = Y/c.graph.tileSize;

		c.goal = 10*newY + newX;

		int cposX = (int)c.position.x / c.graph.tileSize;
		int cposY = (int)c.position.y / c.graph.tileSize;

		c.start = 10*cposY + cposX;

		c.graph = new Graph(this);
		Edge[] path = c.graph.aStarPathfinder(c.start, c.goal);

		int tempX = c.start % 10;
		int tempY = c.start / 10;
		tempX = tempX * c.graph.tileSize + (c.graph.tileSize/2);
		tempY = tempY * c.graph.tileSize + (c.graph.tileSize/2);
		c.objectPath = new ObjectPath();

		c.currentIndex = 0;
		c.objectPath.add(tempX, tempY);
		for(int i = 0; i < path.length; i++)
		{
			tempX = path[i].getDestination() % 10;
			tempY = path[i].getDestination() / 10;
			tempX = tempX * c.graph.tileSize + (c.graph.tileSize/2);
			tempY = tempY * c.graph.tileSize + (c.graph.tileSize/2);
			c.objectPath.add(tempX, tempY);
		}

		for(int i = 0; i < c.graph.nodeArray.length; i++)
		{
			c.graph.nodeArray[i].setCSF(0);
			c.graph.nodeArray[i].setETC(0);
			c.graph.nodeArray[i].setCategory(0);
			c.graph.nodeArray[i].setParent(null);
		}

		c.wanderMode = false;
		c.doneFollowing = false;
	}

	public void drawTilesAndObstacles()
	{
		//draw tiles
		for(int i = 0; i < c.graph.numNodes; i++)
		{
			int tileX = i % 10;
			int tileY = i / 10;

			pushMatrix();
			translate(tileX * c.graph.tileSize, tileY * c.graph.tileSize);
			if(c.graph.obstacles.contains(i))
			{
				fill(0,0,0);
			}

			rect(0, 0, c.graph.tileSize, c.graph.tileSize);
			fill(255, 255, 255);
			popMatrix();
		}

		if(c.start != -1)
		{
			pushMatrix();
			fill(0, 255, 0);
			translate((c.start % 10)*c.graph.tileSize,(c.start / 10)*c.graph.tileSize);
			rect(0, 0, c.graph.tileSize, c.graph.tileSize);
			popMatrix();
		}

		if(c.goal != -1)
		{
			pushMatrix();
			fill(0, 0, 255);
			translate((c.goal % 10)*c.graph.tileSize,(c.goal / 10)*c.graph.tileSize);
			rect(0, 0, c.graph.tileSize, c.graph.tileSize);
			popMatrix();
		}

	}

	public void draw()
	{
		//UNCOMMENT FOR WRITING DATA
		//{
		//StringBuffer sb = new StringBuffer();
		//sb.append(frameCount);
		//sb.append(",");
		//sb.append(rangeCheck() ? 1 : 0);
		//sb.append(",");
		//sb.append(withinReach() ? 1 : 0);
		//sb.append(",");
		//sb.append(action);
		
		//pw.println(sb.toString());
	//	pw.close();
		//}
		
		rect(0, 0, width, height);
		drawTilesAndObstacles(); 
		
		decisionTree();
		c.runChar();
		
		behaviorTree();
		m.runMonster();
		
	}
}