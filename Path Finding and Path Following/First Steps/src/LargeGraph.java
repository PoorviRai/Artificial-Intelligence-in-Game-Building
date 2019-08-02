import java.util.ArrayList;
import java.util.List;

public class LargeGraph implements Graph{
	static List<Node> nodes;
    static List<Edge> edges;

	LargeGraph(){
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		
		for (int i = 0; i < 10000; i++) {
			Node vertex = new Node( i, "Node_" + i, 10*(i%100), 10*(i/100));
			nodes.add(vertex);
		}

		float randNum1,randNum2 ,randNum3,randNum4 ;       
        
		for(int i = 0; i < 10000; i++){
       	  	randNum1 = (float)(Math.random()); 
            randNum2 = (float)(Math.random()); 
            randNum3 =  (float)(Math.random()); 
            randNum4 =  (float)(Math.random()); 
         	// top
         	if(i-100 > 0 && randNum1 > 0.2)
         		addNewEdge(i, i-100, 10);
         	// bottom
         	if(i+100 < 10000 && randNum2 > 0.2)
         		addNewEdge(i, i+100, 10);
         	//right
         	if(i%100 > 0 && randNum3 > 0.2)
         		addNewEdge(i, i-1, 10);
         	//left
         	if(i%100 < 99 && randNum4 > 0.2)
         		addNewEdge(i, i+1, 10);
		}
	}
	
	private static void addNewEdge(int src, int dest,int weight) {
		Edge link = new Edge(nodes.get(src), nodes.get(dest), weight);
		edges.add(link);
	}
	
	public List<Node> getVertices() {
		return nodes;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}

}
