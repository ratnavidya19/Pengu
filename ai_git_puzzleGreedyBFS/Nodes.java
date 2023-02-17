import java.util.Arrays;

//this class is a user defined class to store score and moves 
public class Nodes implements Comparable<Nodes>{
	
	private int score;
	private String[] node;

	public Nodes(int score, String[] node) {
		this.score = score;
		this.node = node;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String[] getNode() {
		return node;
	}

	public void setNode(String[] node) {
		this.node = node;
	}
	
	

	   @Override
	public String toString() {
		return "Nodes [score=" + score + ", node=" + Arrays.toString(node) + "]";
	}

	   //inbuilt function for priority queue
	public int compareTo(Nodes nodes) {
	      
		   if(this.score < nodes.score) {
	            return 1;
	        } else if (this.score > nodes.score) {
	            return -1;
	        } else {
	            return 0;
	        }
	    }


}
