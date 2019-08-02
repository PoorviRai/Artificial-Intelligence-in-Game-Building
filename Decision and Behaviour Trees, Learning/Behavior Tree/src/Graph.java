import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import processing.core.PApplet;


public class Graph
{
	Node[] nodeArray;
	HashMap<Integer, ArrayList<Edge>> hm;
	HashSet<Integer> obstacles;

	boolean[][] check;

	int numNodes;
	int tileSize;
	
	PApplet pr;
	
	Graph(PApplet parent)
	{
		this.pr = parent;
		this.hm = new HashMap<>();
		this.tileSize = 100;
		
		this.numNodes = pr.width * pr.height / (this.tileSize *  this.tileSize);
		
		nodeArray = new Node[numNodes];
		check = new boolean[pr.height/tileSize][pr.width/tileSize];
		
		//initialization
		for(int i = 0; i < numNodes; i++)
		{
			this.nodeArray[i] = new Node(i);
			this.check[i/10][i%10] = false;
		}

		//set Obstacles
		setObstacles();

	}

	void setObstacles() 
	{
		int obstacleArray[] = {3, 4, 5, 6, 9, 13, 19, 29, 40, 41, 42, 45, 48, 49, 55, 65, 75};
		this.obstacles = new HashSet<>();

		for(int x: obstacleArray)
		{
			this.check[x/10][x%10] = true;
			this.obstacles.add(x);
		}
	}


	Edge[] getEdges(int src)
	{
		int i = src / 10;
		int j = src % 10;
		
		boolean up = false, down = false, right = false, left = false;
		
		ArrayList<Edge> temp = new ArrayList<>();

		if(!this.check[i][j])
		{
			Edge c = new Edge();
			if((i - 1) >= 0)
			{
				if(!this.check[i - 1][j])
				{
					up = true;
					c.setWeight(10);
					c.setSource(src);
					c.setDestination(src - 10);
					temp.add(c);
				}
			}

			if((j + 1) < check[i].length)
			{
				if(!this.check[i][j + 1])
				{
					right = true;
					c = new Edge();
					c.setWeight(10);
					c.setSource(src);
					c.setDestination(src + 1);
					temp.add(c);
				}
			}

			if((i + 1) < check.length)
			{
				if(!this.check[i + 1][j])
				{
					down = true;
					c = new Edge();
					c.setWeight(10);
					c.setSource(src);
					c.setDestination(src + 10);
					temp.add(c);
				}
			}

			if((j - 1) >= 0)
			{
				if(!this.check[i][j - 1])
				{
					left = true;
					c = new Edge();
					c.setWeight(10);
					c.setSource(src);
					c.setDestination(src - 1);
					temp.add(c);
				}
			}

			if(up && right)
			{
				if(!this.check[i - 1][j + 1])
				{
					c = new Edge();
					c.setWeight(14);
					c.setSource(src);
					c.setDestination(src - 9);
					temp.add(c);
				}
			}

			if(up && left)
			{
				if(!this.check[i - 1][j - 1])
				{
					c = new Edge();
					c.setWeight(14);
					c.setSource(src);
					c.setDestination(src - 11);
					temp.add(c);
				}
			}

			if(down && right)
			{
				if(!this.check[i + 1][j + 1])
				{
					c = new Edge();
					c.setWeight(14);
					c.setDestination(src + 11);
					c.setSource(src);
					temp.add(c);
				}
			}

			if(down && left)
			{
				if(!this.check[i + 1][j - 1])
				{
					c = new Edge();
					c.setWeight(14);
					c.setSource(src);
					c.setDestination(src + 9);
					temp.add(c);
				}
			}
		}

		Edge[] result = new Edge[temp.size()];
		result = temp.toArray(result);
		return result;
	}

	
	Edge[] aStarPathfinder(int start, int goal)
	{
		ArrayList<Edge> path = new ArrayList<>();

		//code for A*

		this.nodeArray[start].setETC(this.heuristic(start, goal));
		this.nodeArray[start].setCategory(1);
		int openCount = 1;
		
		Node current = new Node(0);
		int previous = 0;
		while(openCount > 0)
		{
			current = new Node(this.smallestOpen());

			if(current.ID == goal)
			{
				Edge finalEdge = new Edge();
				finalEdge.setSource(previous);
				finalEdge.setDestination(current.ID);
				current.setParent(finalEdge);
				break;
			}

			Edge[] edges = this.getEdges(current.ID);

			for(Edge e: edges)
			{
				int endNode = e.getDestination();
				int endNodeWeight = current.csf + e.getWeight();
				int endNodeHeuristic = 0;

				int nodeFinder = this.nodeArray[endNode].getCategory();
				if(nodeFinder == -1)
				{
					continue;
				}
				else if(nodeFinder == 1)
				{
					if(this.nodeArray[endNode].getCSF() <= endNodeWeight)
						continue;

					endNodeHeuristic = this.nodeArray[endNode].getETC() - this.nodeArray[endNode].getCSF();
				}
				else
				{
					endNodeHeuristic = this.heuristic(endNode, goal);
				}

				this.nodeArray[endNode].setCSF(endNodeWeight);
				this.nodeArray[endNode].setParent(e);
				this.nodeArray[endNode].setETC(endNodeWeight + endNodeHeuristic);
				this.nodeArray[endNode].setCategory(1);
				openCount++;
				
			}

			previous = current.ID;
			this.nodeArray[current.ID].setCategory(-1);
			openCount--;
		}

		if(current.ID != goal)
		{
			return new Edge[0];
		}
		else
		{
			while(current.ID != start)
			{
				Edge c = current.getParent();
				path.add(c);
				int parent = c.getSource();
				current = this.nodeArray[parent];
			}
		}

		Edge[] output = new Edge[path.size()];
		Collections.reverse(path);
		output = path.toArray(output);

		return output;
	}
	
	int heuristic(int start, int goal)
	{
		int currentY = start / 10;
		int currentX = start % 10;
		
		int targetY = goal / 10;
		int targetX = goal % 10;

		int xDistance = Math.abs(currentX - targetX);
		int yDistance = Math.abs(currentY - targetY);
		int heuristic = 0;

		if(xDistance > yDistance)
		{
			heuristic = 14 * yDistance + 10 * (xDistance - yDistance);
		}
		else
		{
			heuristic = 14 * xDistance + 10 * (yDistance - xDistance);
		}

		return heuristic;
	}
	
	
	int smallestOpen()
	{
		int i = 0, mini = 0; 
		int min = Integer.MAX_VALUE;
		while(i < this.nodeArray.length)
		{
			if(nodeArray[i].category == 1)
			{
				int temp = nodeArray[i].getETC();
				if(min > temp)
				{
					min = temp;
					mini = i;
				}

			}
			i++;
		}
		return mini;
	}
	

}