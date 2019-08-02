import processing.core.*;

public class DecisionTree extends PApplet 
{

	public static void main(String[] args)  
	{
		PApplet.main("DecisionTree");
	}


	Character c;
	
	
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
					c.goal = 67;			//going bottom right
				}
				
				if(c.start % 10 >= 5 && c.start / 10 < 4)
				{
					c.goal = 61;			//going bottom left
				}
				
				if(c.start % 10 >= 5 && c.start / 10 >= 4)
				{
					c.goal = 11;			//going top right & start
				}
				
				if(c.start % 10 < 5 && c.start / 10 >= 4)
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

	public void settings()
	{
		size(1000, 800);	
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
	

	public void setup()
	{
		smooth();
		background(255, 255, 255);
		
		c = new Character(this);
		
	}

	public void drawTilesAndObstacles()
	{
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
		rect(0, 0, width, height); 
		drawTilesAndObstacles();
		
		decisionTree();
		
		c.runChar();
		
	}

}