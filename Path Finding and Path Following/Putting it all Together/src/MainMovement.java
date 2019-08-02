import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;

public class MainMovement extends PApplet{
	
	public static void main(String[] args) 
	{	
		PApplet.main("MainMovement");
	}
	
	Follow_Path p;
	ArrayList<Integer> obstacle_indices = new ArrayList<Integer>();
	IndoorEnv env ;
	Graph small;
	Dijkstra dj;
    Astar ast ;
    LinkedList<Node> path1, path2;

	public void settings()
	{
		size(800,640);
    }
	
	public void setup()
    {
    	  frameRate(30);
    	  int radius=20;
		  PVector initial_position = new PVector(20, this.height-20);
		  PVector initial_velocity = new PVector(0,0);   // Velocity of player
		  PVector initial_acceleration = new PVector(0,0);   // Velocity of player
		  
		  float initial_orientation=0;		  
		  float initial_rotation=0;
		  float initial_angular_acceleration = 0;
		  
		  float max_velocity = (float) 4;
		  float max_acceleration  = 2;
		  float max_rotation=10;
		  float max_angular_acceleration= 3;

	      p = new Follow_Path(this, radius, initial_position, initial_velocity, initial_orientation, max_rotation, initial_rotation, max_velocity,  max_acceleration, initial_acceleration,  initial_angular_acceleration, max_angular_acceleration);
	      env = new IndoorEnv(this);
	      
	      obstacle_indices.add(5);
	      obstacle_indices.add(8);
	      obstacle_indices.add(14);
	      obstacle_indices.add(15);
	      obstacle_indices.add(16);
	      
	      obstacle_indices.add(25);
	      obstacle_indices.add(28);
	      obstacle_indices.add(30);
	      obstacle_indices.add(31);
	      obstacle_indices.add(32);
	      obstacle_indices.add(36);
	      
	      obstacle_indices.add(48);
	      obstacle_indices.add(50);
	      obstacle_indices.add(51);
	      obstacle_indices.add(52);
	      obstacle_indices.add(56);
	      
	      obstacle_indices.add(60);
	      obstacle_indices.add(61);
	      obstacle_indices.add(62);
	      obstacle_indices.add(63);
	      obstacle_indices.add(65);
	      obstacle_indices.add(66);
	      obstacle_indices.add(67);
	      obstacle_indices.add(68);
	      obstacle_indices.add(70);
	      obstacle_indices.add(71);
	      obstacle_indices.add(72);
	      obstacle_indices.add(76);
	      
	      obstacle_indices.add(96);
	      
	      obstacle_indices.add(116);
	      
	      obstacle_indices.add(136);
	      
	      obstacle_indices.add(140);
	      obstacle_indices.add(141);
	      obstacle_indices.add(142);
	      obstacle_indices.add(143);
	      obstacle_indices.add(144);
	      obstacle_indices.add(145);
	      obstacle_indices.add(148);
	      
	      obstacle_indices.add(165);
	      obstacle_indices.add(168);	      
	      obstacle_indices.add(171);
	      obstacle_indices.add(172);
	      obstacle_indices.add(176);
	      obstacle_indices.add(177);	      
	      obstacle_indices.add(178);
	      obstacle_indices.add(179);
	      
	      obstacle_indices.add(185);
	      obstacle_indices.add(188);	      
	      obstacle_indices.add(191);
	      obstacle_indices.add(192);
	      
	      obstacle_indices.add(208);      
	      obstacle_indices.add(216);
	      
	      obstacle_indices.add(228);
	      obstacle_indices.add(233);
	      obstacle_indices.add(234);
	      obstacle_indices.add(236);
	      
	      obstacle_indices.add(242);
	      obstacle_indices.add(243);
	      obstacle_indices.add(244);      
	      obstacle_indices.add(248);
	      obstacle_indices.add(253);
	      obstacle_indices.add(254);
	      obstacle_indices.add(256);
	      
	      obstacle_indices.add(262);
	      obstacle_indices.add(263);
	      obstacle_indices.add(264);
	      obstacle_indices.add(268);
	      obstacle_indices.add(273);      
	      obstacle_indices.add(274);
	      obstacle_indices.add(276);
	      
	      obstacle_indices.add(282);	      
	      obstacle_indices.add(283);
	      obstacle_indices.add(284);
	      obstacle_indices.add(288);
	      obstacle_indices.add(293);
	      obstacle_indices.add(294);
	      obstacle_indices.add(296);
	      	      
	      obstacle_indices.add(302);
	      obstacle_indices.add(303);
	      obstacle_indices.add(304);	      
	      obstacle_indices.add(308);
	      obstacle_indices.add(316);
	      
	        
	      small = new SmallGraph(obstacle_indices);
    	  dj = new Dijkstra(small);
          ast = new Astar(small);
    }
	
	public void draw() {
		
		background(255);
		env.displayObstacles();
  	  
  	  	if(flag == 1)
  	  	{  
  	  		for (Node vertex : path1)
  	  		{
  	  			this.ellipse(vertex.getX(), vertex.getY(), 10, 10);
  	  			this.fill(100);
  	  		}

  	  		if(pointer_pos < path1.size())
  	  		{
  	  			PVector intrm_dest = new PVector((float)path1.get(pointer_pos).getX(),(float)path1.get(pointer_pos).getY());
  	  			flag = p.arrive1(intrm_dest, 1);
 
  	  			if(path1.get(path1.size()-1).getId() == get_player_index(p.position.x, p.position.y))
  	  			{
  	  				p.acceleration = new PVector(0,0);
  	  				p.velocity = new PVector(0,0);
  	  				flag = 0;
  	  				target_index = -1;
  	  				source_index = -1;
  	  			}
  	  			
  	  			if( path1.get(pointer_pos).getId()==get_player_index(p.position.x, p.position.y))
  	  			{
  	  				pointer_pos++;
  	  				flag = 1;
  	  			}

  	  		}

  	  	}
  	  
  	  	p.update(1);
  	  	p.display(frameCount);
  	  	p.breadCrumbs(frameCount);
  	  
	}
	
	public int get_player_index(float x, float y){
		return ((int)y/40)*20+((int)x/40);
	}
	
	int flag = 0;
	int pointer_pos = -1;
	int target_index = -1;
    int source_index = -1;
    PVector target = new PVector(0,0);
    
    int mouse_clicked = 0;
    public void mousePressed() 
    {
    	target.x = mouseX;
    	target.y = mouseY;
    	
    	source_index = ((int)p.position.y/40)*20+((int)p.position.x/40);
    	target_index = (mouseY/40)*20+(mouseX/40);
    	
    	if(!obstacle_indices.contains(target_index))
    	{
        	path1 = dj.findPath(small.getVertices().get(source_index),small.getVertices().get(target_index));
//        	path2 = ast.findPath(small.getVertices().get(source_index),small.getVertices().get(target_index));
     
        	int dij_fill = dj.openList.size()+dj.closedList.size();
            //int astar_fill = ast.openList.size()+ast.closedList.size();
            System.out.println( dij_fill+" "+dj.fringe);
            //System.out.println( astar_fill+" "+ast.fringe);
//          System.out.println( dij_totalTime+" "+astar_totalTime);
    	
            flag = 1;
            pointer_pos = 1;
    	}
    }
}
