import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Astar {
	static List<Node> nodes;
    static List<Edge> edges;
    
    public Set<Node> closedList;
    public Set<Node> openList;
    
    private Map<Node, Node> parent;
    private Map<Node, Integer> csf;
    private Map<Node, Float> etc;
    
    int fill=0;
    int fringe=0;
    
    public  Astar(Graph graph) {
            nodes = new ArrayList<Node>(graph.getVertices());
            edges = new ArrayList<Edge>(graph.getEdges());
    }

    public LinkedList<Node> findPath(Node source, Node dest){
    	closedList = new HashSet<Node>();
    	openList = new HashSet<Node>();
        
    	parent = new HashMap<Node, Node>();
    	csf = new HashMap<Node, Integer>();
    	etc = new HashMap<Node, Float>();
            
    	csf.put(source, 0);
    	etc.put(source, 0 + manhattan(source,dest));
    	openList.add(source);
        
    	fill++;
        
    	while (openList.size() > 0){
    		fringe=Math.max(fringe, openList.size());
    		
    		Node node = lowestEtc(openList);
            
    		if(node==dest)
    			break;
    		
    		closedList.add(node);
    		openList.remove(node);
            
    		AddChildren(node,dest);
        }
        
    	return getPath(dest); 
    }

    private void AddChildren(Node node, Node dest){
    	List<Node> children = getChildren(node);
        
    	for (Node i : children){
    		if (getEtc(i) > getEtc(node) + getEdgeWeight(node, i))	//ETC stored only once for each node, not multiple times
    		{
    			csf.put(i, (int)(getEtc(node) + getEdgeWeight(node, i)));
    			etc.put(i, csf.get(i) + manhattan(i, dest));
    			parent.put(i, node);
    			openList.add(i);
    			fill++;
            }
        }

    }

    private int getEdgeWeight(Node node, Node target){
    	for (Edge edge : edges) {
    		if (edge.getSource().equals(node) && edge.getDestination().equals(target))
    			return edge.getWeight();
                    
        }
        
    	throw new RuntimeException();
    }

    private List<Node> getChildren(Node node){
    	List<Node> c = new ArrayList<Node>();
            
    	for (Edge edge : edges){
    		if (edge.getSource().equals(node) && !closedList.contains(edge.getDestination()))	// not already in path
    			c.add(edge.getDestination());
        }
        
    	return c;
    }

    private Node lowestEtc(Set<Node> vertexes){
    	Node minimum = null;
        
    	for (Node vertex : vertexes){
    		if (minimum == null)
    			minimum = vertex;
            else 
            	if (getEtc(vertex) < getEtc(minimum))
            		minimum = vertex;
        }
        
    	return minimum;
    }

    private Float getEtc(Node dest){
    	Float d = etc.get(dest);
        
    	if (d == null)
    		return Float.MAX_VALUE;
    	else
    		return d;
    }

    public LinkedList<Node> getPath(Node goal){
    	LinkedList<Node> path = new LinkedList<Node>();
        
    	Node n = goal;
    	
    	if (parent.get(n) == null)
    		return null;
            
        path.add(n);
            
        while (parent.get(n) != null){
        	n = parent.get(n);
        	path.add(n);
        }
        
        Collections.reverse(path);	//start to goal
        
        return path;
    }
    
    // Heuristic1 = Euclidean Distance
    private float euclidean(Node source,Node dest){
    	return (float) Math.sqrt(Math.pow((Math.abs(source.getX()-dest.getX())), 2) + Math.pow((Math.abs(source.getY()-dest.getY())), 2));
    }

    // Heuristic2 = Manhattan Distance
    private float manhattan(Node source, Node dest){
		return Math.abs(source.getX()-dest.getX()) + Math.abs(source.getY()-dest.getY());
    }
    
    public static void main(String[] args){
    	Graph graph = new LargeGraph();
    	Astar astar = new Astar(graph);
                 
    	long ast_startTime = System.currentTimeMillis();
    	LinkedList<Node> path = astar.findPath(graph.getVertices().get(0),graph.getVertices().get(9900));
    	long ast_endTime   = System.currentTimeMillis();
    	long ast_totalTime = ast_endTime - ast_startTime;
                             
    	for (Node vertex : path) {
    		System.out.println(vertex);
    	}

    	// For Comparisons
    	int ast_fill = astar.openList.size()+astar.closedList.size();
    	System.out.println( "Fill = "+ ast_fill+" Fringe = "+astar.fringe);
    	System.out.println( "Run Time = "+ast_totalTime);
        
    }

}
