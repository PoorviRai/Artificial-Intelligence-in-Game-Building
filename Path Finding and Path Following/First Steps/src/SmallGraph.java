import java.util.ArrayList;
import java.util.List;

public class SmallGraph implements Graph{
	ArrayList<Integer> obstacles = new ArrayList<Integer>();
	
	static List<Node> nodes;
    static List<Edge> edges;

	SmallGraph(ArrayList<Integer> obstacle_indices){
		obstacles = obstacle_indices;
		
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		
		for (int i = 0; i < 320; i++){
			Node vertex = new Node( i, "Node_" + i, 20+40*(i%20), 20+40*(i/20));
			nodes.add(vertex);
		}

		for(int i = 0; i < 320; i++){
			if(!obstacles.contains(i))
			{
       		//top
				if((nodes.get(i).getY()-40) > 0 && !obstacles.contains(i-20))
					addNewEdge(i, i-20, 10);
       		//bottom
				if((nodes.get(i).getY()+40) < 640 && !obstacles.contains(i+20))
					addNewEdge(i, i+20, 10);
       		//right
				if((nodes.get(i).getX()+40) < 800 && !obstacles.contains(i+1))
					addNewEdge(i, i+1, 10);
       		//left
				if((nodes.get(i).getX()-40) > 0 && !obstacles.contains(i-1))
					addNewEdge(i, i-1, 10);
			}
		}
    }

	private static void addNewEdge(int src, int dest, int weight) {
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
