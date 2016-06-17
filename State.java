package eightPuzzle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * This is the state class that holds the current state of the puzzle. 
 * The state of the puzzle is held in a single dimension array.
 * The heuristic functions are also held in this class. 
 *
 */

public class State {

	private int[] current_puzzle;
	private int[] goal;
	private int h1;
	private int h2;
	
	public State(int[] puzzle) {
		this.current_puzzle = puzzle;
		this.goal = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
		this.setH1();
		this.setH2();
	}	
	
	/**
	 * Heuristic function to find the value of h1. Compares
	 * the current state of the puzzle to the goal state and keeps
	 * track of the number of tiles that don't match.
	 * 
	 */
	public void setH1() {
		int misplaced_tiles = 0;
		for (int i = 0; i < this.current_puzzle.length; i++) {
				if (this.current_puzzle[i] != this.goal[i]) {
					misplaced_tiles++;
				}
		}
		this.h1 = misplaced_tiles;
	}
	
	/**
	 * Heuristic function to find the value of h2 or the manhattan distance.
	 * To make my algorithm work, I needed turn the array into a two dimensional
	 * array. Taking the absolute values of the difference between the current
	 * and goal indices, and then adding them gets me the manhattan distance for
	 * that number.
	 */
	
	public void setH2() {
		int[][] current = new int[3][3];
		int[][] goal = new int[3][3];
		int count = 0;
		for (int i = 0; i < current.length; i++) {
			for (int j = 0; j < current.length; j++) {
				current[i][j] = this.current_puzzle[count];
				goal[i][j] = count;
				count++;
			}
		}
		int manhattan_distance = 0;
		for (int i = 0; i < current.length; i++) {
			for (int j = 0; j < current.length; j++) {
				if (current[i][j] != goal[i][j]) {
					int[] goal_position = findGoalPosition(current[i][j], goal);
					int steps_to_goal = Math.abs(i - goal_position[0]) + Math.abs(j - goal_position[1]);
					manhattan_distance += steps_to_goal;
				}
			}
		}
		this.h2 = manhattan_distance;
	}
	
	/**
	 * This is a helper function to find the goal position in a two
	 * dimensional array.
	 */ 
	public int[] findGoalPosition(int value, int[][] goal) {
		int[] position = new int[2];
		for (int i = 0; i < goal.length; i++) {
			for (int j = 0; j < goal.length; j++) {
				if (goal[i][j] == value) {
					position[0] = i;
					position[1] = j;
					return position;
				}
			}
		}
		return position;		
	}
	
	public int[] getPuzzle() {
		return this.current_puzzle;
	}
	
	public int getStepCost() {
		return 1;
	}
	
	public int getH1() {
		return this.h1;
	}
	
	public int getH2() {
		return this.h2;
	}
	
	public int findZero() {
		for (int i = 0; i < this.current_puzzle.length; i++ ) {
			if (this.current_puzzle[i] == 0) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This is the function that creates all possible successor states to the current state.
	 * It locates the current position of the empty square, and goes in all the possible directions
	 * that it is allowed to go. These new states are then put into an ArrayList and sent back to the
	 * caller function. 
	 */
	public ArrayList<State> generateSuccessors() {
		ArrayList<State> successors = new ArrayList<State>();
		int zero_position = this.findZero();
		int visual_size = (int)Math.sqrt(this.current_puzzle.length);
		
		if (zero_position != 2 && zero_position != 5 && zero_position != 8) {
			this.createNewState(zero_position, zero_position + 1, successors);
		}
		if (zero_position != 0 && zero_position != 3 && zero_position != 6) {
			this.createNewState(zero_position, zero_position - 1, successors);
		}
		if (zero_position != 6 && zero_position != 7 && zero_position != 8) {
			this.createNewState(zero_position, zero_position + visual_size, successors);
		}
		if (zero_position != 0 && zero_position !=1 && zero_position != 2) {
			this.createNewState(zero_position, zero_position - visual_size, successors);
		}
		return successors;
	}
	
	/**
	 * This is a helper function that switches the positions of the empty square
	 * and the square that it is legally allowed to switch with.
	 */
	public void createNewState(int old_position, int new_position, ArrayList<State> list) {
		
		int[] copy = this.generatePuzzleCopy();
		int temp = copy[old_position];
		copy[old_position] = copy[new_position];
		copy[new_position] = temp;
		list.add(new State(copy));
	}
	
	public int[] generatePuzzleCopy() {
		int[] copy = Arrays.copyOf(this.current_puzzle, this.current_puzzle.length);
		return copy;
	}
	
	public boolean isGoal() {
		if (Arrays.equals(this.current_puzzle, this.goal))
			return true;
		else
			return false;
	}
}
