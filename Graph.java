package eightPuzzle;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Stack;

/**
 * 
 * This is the Graph class. This class holds the frontier, which is made up
 * of a priority queue, the explored set, which is made up of a HashMap, 
 * and also holds the A* search algorithm. There is also a stack to hold
 * all the states in their solution order if the user requires it.
 */

public class Graph {

	private Node root;
	private int[] puzzle;
	private PriorityQueue<Node> frontier;
	private HashMap<List<Integer>, Node> explored;
	private int steps;
	
	public Graph(int[] puzzle){
		this.puzzle = puzzle;
		this.root = new Node(new State(this.puzzle));
		this.frontier = new PriorityQueue<Node>();
		this.frontier.add(this.root);
		this.steps = 1;
		this.explored = new HashMap<List<Integer>, Node>();
	}
	
	/**
	 * A* function that takes the following:
	 * heuristic: lets the function know to use either the h1 or h2 heuristic for this search
	 * solution: whether or not to print the entire solution
	 * data_retrieval: if this is true, do not print out any solution or information. Simply return an 
	 * 	array that holds the data to be documented and printed later for the 100 case test.
	 */
	
	public int[] AStarSearch(int heuristic, int solution, boolean data_retrieval) {
		
		while(!this.frontier.isEmpty()) {
			//Pops the node out of the frontier (priority queue) that has the lowest f cost
			Node current_node = (Node) this.frontier.poll();
			//Since we are making this node the current node, we add it to the explored set. The key 
			//for this hash is the current state of the puzzle (array of the puzzle)
			this.explored.put(Collections.unmodifiableList(this.createList(current_node.getState().getPuzzle())), current_node);
			
			//checks if the current state of this current node is the goal state
			if (!current_node.getState().isGoal()) {
				//Gets all the successor states of the current state in ArrayList form
				ArrayList<State> current_successors  = current_node.getState().generateSuccessors();
				
				for (int i = 0; i < current_successors.size(); i++) {
					Node checked_node;
					
					//Creates a new node using the new successor state created. 
					//If heuristic=1, use the h1 value, if heuristic=2, use the h2 value
					if (heuristic == 1)
						checked_node = new Node(current_node, current_successors.get(i), current_node.getGCost() + current_successors.get(i).getStepCost(), current_successors.get(i).getH1());
					else
						checked_node = new Node(current_node, current_successors.get(i), current_node.getGCost() + current_successors.get(i).getStepCost(), current_successors.get(i).getH2());
					
					//Checks if the new successor state is already in the explored set.
					//If it isn't add it to the frontier
					if (!this.isExplored(checked_node)) {
						this.frontier.add(checked_node);
					}
				}
				
				if (this.explored.isEmpty())
					continue;
				//Increment the cost to move to the next state
				this.steps++;
			}
			//The goal state is found
			else {
				//Using a stack, it adds all the solution states in reverse order by
				//calling the parent node and adding it to the stack.
				//Then, it pops the stack back in the right order to show the solution.
				//This solution is only showed if the solution parameter=1
				if (solution == 1) {
					Stack<Node> solution_path = new Stack<Node>();
					Node temp_node = current_node;
					solution_path.push(temp_node);
					temp_node = temp_node.getParent();
					while (temp_node.getParent() != null) {
						solution_path.push(temp_node);
						temp_node = temp_node.getParent();
					}
					this.printSolution(solution_path);
				}				
				//This is the data array to be returned to the caller function to be used for the 
				//100 test cases. It returns the number of nodes required to find the solution using that
				//heuristic value, and also the number of steps to find the solution
				int[] data = {current_node.getGCost(), this.steps};
				
				//If you are not using this algorithm to find the data for 100 test cases, 
				//there is no need to explicitly state the data for each test.
				if (!data_retrieval) {
					if (heuristic == 1) {
						System.out.println("Total cost: " + current_node.getGCost());
						System.out.println("Total number of nodes using h1: " + this.steps);
					}
					else 
						System.out.println("Total number of nodes using h2: " + this.steps);
				}
				return data;
			}
		}
		return new int[] {};
	}
	
	//This converts the state array to an ArrayList to be used as the key for the Hashmap
	public List<Integer> createList(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	//This checks if the current state is in the explored set.
	public boolean isExplored(Node current_node) {
		if (this.explored.containsKey(Collections.unmodifiableList(this.createList(current_node.getState().getPuzzle())))) {
			return true;
		}
		else
			return false;
	}
	
	
	//Prints out the entire solution from initial state to goal state
	public void printSolution(Stack<Node> solution) {
		
		int solution_size = solution.size();
		for (int i = 0; i < solution_size; i++) {
			Node temp = solution.pop();
			int[] array = temp.getState().getPuzzle();
			int square = (int) (Math.sqrt(array.length));
			for (int j = 0; j < array.length; j++) {
				if (j % square == 0)
					System.out.println();
				System.out.print(array[j] + " ");
			}
			System.out.println();
		}
		

	}
}
