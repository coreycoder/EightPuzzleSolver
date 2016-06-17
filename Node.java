package eightPuzzle;

/**
 * This is the Node class that holds the State object of the node,
 * the parent node of the current node, and the evaluation functions of
 * A*. This class implements the Comparable interface to allow the 
 * Priority Queue to pop the Nodes in proper order.
 *
 */

public class Node implements Comparable<Node>{
	
	private State current_state;
	private Node parent;
	private int g_cost;
	private int h_cost;
	private int f_cost;
	
	public Node(State state) {
		this.current_state = state;
		this.parent = null;
		this.g_cost = 0;
		this.h_cost = 0;
		this.f_cost = 0;
	}
	
	public Node(Node parent, State state, int g, int h) {
		this.parent = parent;
		this.current_state = state;
		this.g_cost = g;
		this.h_cost = h;
		this.f_cost = this.g_cost + this.h_cost;
	}
	
	public State getState() {
		return this.current_state;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public int getGCost() {
		return this.g_cost;
	}
	
	public int getHCost() {
		return this.h_cost;
	}
	
	public int getFCost() {
		return this.f_cost;
	}
	
	/**
	 * 
	 * The next two functions are used to let the priority queue
	 * know that it should pop out the Nodes in order of the lowest
	 * f cost.
	 */
	
	public boolean equals(Node other) {
		return this.getFCost() == other.getFCost();
	}
	
	public int compareTo(Node other) {
		if (this.equals(other))
			return 0;
		else if (this.getFCost() > other.getFCost())
			return 1;
		else
			return -1;
	}
}
