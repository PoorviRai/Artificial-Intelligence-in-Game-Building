import processing.core.PApplet;

public class IndoorEnv {
	PApplet parent;
	
	IndoorEnv(PApplet p)
	{
		parent = p;
	}
	
	void displayObstacles() {
		  
	    parent.fill(0,0,0);
	    
	    //creating tiles
	    for(int i = 0; i < parent.width; i+=40)
	    {
	    	parent.rect(i, 0 ,2, parent.height);
	    }
	    
	    for(int i = 0; i < parent.height; i+=40)
	    {
	    	parent.rect(0, i ,parent.width, 2);
	    }
	    
	    //obstacles
	    parent.rect(0, 120, 160, 40);
	    parent.rect(200, 120, 160, 40);
	    parent.rect(320, 0, 40, 120);
	    parent.rect(200, 0, 40, 80);
	    
	    parent.rect(0, 280, 240, 40);
	    parent.rect(200, 280, 40, 120);
	    parent.rect(80, 480, 120, 160);
	    parent.rect(320, 280, 40, 360); 
	    
	    parent.rect(400, 40, 120, 120);
	    
	    parent.rect(560, 0, 80, 40);
	    parent.rect(640, 0, 40, 280);
	    
	    parent.rect(520, 440, 80, 160);
	    parent.rect(440, 320, 80, 80);
	    
	    parent.rect(640, 400, 40, 240);
	    parent.rect(640, 320, 160, 40);
	    
	    
	  }

}
