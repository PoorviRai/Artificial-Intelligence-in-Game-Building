import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dijkstra {
	static List<Node> nodes;
    static List<Edge> edges;
    
    public Set<Node> closedList;
    public Set<Node> openList;
    
    private Map<Node, Node> parent;
    private Map<Node, Integer> csf;
    
    int fill = 0;
    int fringe = 0;
    
    Dijkstra(Graph graph) {
    	nodes = new ArrayList<Node>(graph.getVertices());
    	edges = new ArrayList<Edge>(graph.getEdges());
    }

    public LinkedList<Node> findPath(Node source, Node dest){
    	closedList = new HashSet<Node>();
        openList = new HashSet<Node>();
            
        parent = new HashMap<Node, Node>();
        csf = new HashMap<Node, Integer>();
            
        openList.add(source);
        csf.put(source, 0);
        
        fringe++;
        fill++;
             
        while (openList.size() > 0) {
        	fringe = Math.max(fringe,openList.size());
        	
        	Node node = lowestCsf(openList);
        	
        	if(node == dest)
        		break;
            		
        	closedList.add(node);
        	openList.remove(node);
                    
        	AddChildren(node);
        }
           
        return getPath(dest); 
    }

    private void AddChildren(Node node) {
    	List<Node> children = getChildren(node);
        
    	for (Node i : children) {
    		if (getCsf(i) > getCsf(node) + getEdgeWeight(node, i))	//CSF stored only once for each node, not multiple times
    		{
    			csf.put(i, getCsf(node) + getEdgeWeight(node, i));
                parent.put(i, node);
                openList.add(i);
                fill++;
            }
        }

    }

    private int getEdgeWeight(Node node, Node target) {
    	for (Edge edge : edges) {
    		if (edge.getSource().equals(node) && edge.getDestination().equals(target)) 
    			return edge.getWeight();
                
        }
            
    	throw new RuntimeException();
    }

    private List<Node> getChildren(Node node) {
    	List<Node> c = new ArrayList<Node>();
        
    	for (Edge edge : edges) {
    		if (edge.getSource().equals(node) && !closedList.contains(edge.getDestination()))		//not already in path
    			c.add(edge.getDestination());               
    	}
        
    	return c;
    }

    private Node lowestCsf(Set<Node> vertices) {
    	Node minimum = null;
        
    	for (Node vertex : vertices) {
    		if (minimum == null) 
    			minimum = vertex;
            else
            	if (getCsf(vertex) < getCsf(minimum)) 
            		minimum = vertex;                            
        }
    	
    	return minimum;
    }

    private int getCsf(Node dest) {
    	Integer d = csf.get(dest);
        
    	if (d == null) 
    		return Integer.MAX_VALUE;
        else
        	return d;
    }

    public LinkedList<Node> getPath(Node goal) {
    	LinkedList<Node> path = new LinkedList<Node>();
        Node n = goal;
        
        if (parent.get(n) == null) 
        	return null;
            
        path.add(n);
        
        while (parent.get(n) != null) {
        	n = parent.get(n);
        	path.add(n);
        }
        
        Collections.reverse(path);	//start to goal
        return path;
    }
    
    public static void main(String[] args){
    	 

    	// Graph graph = new SmallGraph();
    	Graph graph = new LargeGraph();
                 
    	Dijkstra dijkstra = new Dijkstra(graph);
                 
    	long dij_startTime = System.currentTimeMillis();
    	LinkedList<Node> path = dijkstra.findPath(graph.getVertices().get(0),graph.getVertices().get(9900));
    	long dij_endTime   = System.currentTimeMillis();
    	long dij_totalTime = dij_endTime - dij_startTime;
                 
     	for (Node vertex : path)
          	System.out.println(vertex);

     	//For comparisons
     	int dij_fill = dijkstra.openList.size() + dijkstra.closedList.size();
        System.out.println( "Fill = "+ dij_fill + " Fringe = " + dijkstra.fringe);
        System.out.println( "Run Time = "+ dij_totalTime+" ");
       	
    }
    	
}
