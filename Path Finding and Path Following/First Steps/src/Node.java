public class Node {
	 final private int id;
	 final private String name;
	 final private int x;
	 final private int y;
	    

	 public Node(int id, String name,int x,int y) {
		 this.id = id;
	     this.name = name;
	     this.x = x;
	     this.y = y;
	 }
	 
	 public int getId() {
	     return id;
	 }
	 
	 public int getX() {
		 return x;
	 }
	 
	 public int getY() {
		 return y;
	 }
	 
	 public String getName() {
		 return name;
	 }

	 @Override
	 public String toString() {
		 return name;
	 }

}
