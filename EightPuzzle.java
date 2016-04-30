package eightPuzzle;

import java.util.Collections;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class EightPuzzle {

	public static void main(String[] args) {

		Scanner kb = new Scanner(System.in);
		System.out.println("Welcome to Corey's 8 Puzzle Solver!\n-------------------------------");
		int input = 0;
		
		while(input != 4){
			System.out.println("\n1) Randomly generate 8 Puzzle Problems");
			System.out.println("2) Enter your own 8 Puzzle Problem");
			System.out.println("3) Generate 100 Cases with Data"); 
			System.out.println("4) Exit Program"); 
			System.out.print("Enter Choice => " );
			input = kb.nextInt();
			switch(input) {
			case 1: {
				System.out.println("How many puzzles would you like to generate?");
				System.out.print("Enter number => ");
				int puzzles_generated = kb.nextInt();
				System.out.println("Would you like each step from initial state to goal state?");
				System.out.println("1) Show steps");
				System.out.println("2) Show results only");
				System.out.print("Enter Choice => ");
				int show_steps = kb.nextInt();
				solvePuzzle(show_steps, puzzles_generated, false, new int[]{}, false);

				break;
			}
			case 2: {
				System.out.println("Enter the puzzle in array form, separated by spaces");
				System.out.print("Enter puzzle => ");
				kb.nextLine();
				String puzzle_string = kb.nextLine();
				System.out.println("Would you like each step from initial state to goal state?");
				System.out.println("1) Show steps");
				System.out.println("2) Show results only");
				System.out.print("Enter Choice => ");
				int show_steps = kb.nextInt();
				StringTokenizer token = new StringTokenizer(puzzle_string);
				int count = token.countTokens();
				int[] puzzle = new int[count];
				for (int i = 0; i < count; i++) {
					puzzle[i] = Integer.parseInt((String)token.nextElement());
				}
				solvePuzzle(show_steps, 1, false, puzzle, true);
				break;
			}
			case 3: {
				solvePuzzle(2, 100, true, new int[]{}, false);
				break;
			}
			case 4: {
				System.out.println("Thank you for using this 8 Puzzle Program!");
				System.exit(0);
			}
			}
		}
	}
	
	public static void solvePuzzle(int show_steps, int puzzles_generated, boolean data_retrieval, int[] self_puzzle, boolean entered) {
		int count = 0;
		//two dimensional array to hold all the data for 100 test cases
		int[][] data_holder = new int[40][4];
		if (data_retrieval)
			System.out.println("Gathering data from 100 solvable puzzles...");
		while (count < puzzles_generated) {
			int[] puzzle;
			if (!entered)
				puzzle = puzzleGenerator();
			else
				puzzle = self_puzzle;
			boolean solvable = checkIfSolvable(puzzle);
			if (!data_retrieval)
				printPuzzle(puzzle);
			
			//The A* search algorithm is ran twice for each puzzle. One using the h1 heuristic
			//and one using the h2. The data is returned here in array form. These data arrays
			//are then dumped into the two-dimensional data holder array that will be printed out later.
			if (solvable) {
				Graph graphH1 = new Graph(puzzle);
				int[] data1 = graphH1.AStarSearch(1, show_steps, data_retrieval);
				Graph graphH2 = new Graph(puzzle);
				long start = System.currentTimeMillis();
				int[] data2 = graphH2.AStarSearch(2, show_steps + 1, data_retrieval);
				long time = System.currentTimeMillis() - start;
				data_holder[data1[0]][0] += data1[1];
				data_holder[data2[0]][1] += data2[1];
				data_holder[data1[0]][2] += time;
				data_holder[data1[0]][3] += 1;
				count++;
			}
			else 
				if(!data_retrieval)
					System.out.println("This puzzle is unsolvable!");
					if (entered)
						count++;
			if (!data_retrieval)
				System.out.println("---------------------------------------");
		}
		//If you are trying to get the data for 100 test cases, this takes the two dimensional
		//data array and prints out the contents. 
		if (data_retrieval) {
			for (int j = 0; j < 30; j++) {
				for (int k = 0; k < 4; k++) {
					switch (k) {
					case 0: {
						if (data_holder[j][3] != 0) {
							int h1_average = (int)data_holder[j][k] / data_holder[j][3];
							System.out.print("cost: " + j + " steps | h1: " + h1_average + " nodes | ");
						}
						break;
					}
					case 1: {
						if (data_holder[j][3] != 0) {
							int h2_average = (int)data_holder[j][k] / data_holder[j][3];
							System.out.print("h2: " + h2_average + " nodes | ");
						}
						break;
					}
					case 2: {
						if (data_holder[j][3] != 0) {
							long average_time = data_holder[j][k] / data_holder[j][3];
							System.out.print("time: " + average_time + " ms | ");
						}
						break;
					}
					case 3: {
						if (data_holder[j][3] == 0)
							System.out.print("cost: " + j + " ");
						System.out.print("cases: " + data_holder[j][k] + " ");
						break;
					}
					}
				}
				System.out.println();
			}
			
		}
		
	}
	
	/**
	 * Simply prints out the puzzle
	 */
	public static void printPuzzle(int[] puzzle) {
		int square = (int) (Math.sqrt(puzzle.length));
		for (int i = 0; i < puzzle.length; i++) {
			if (i % square == 0)
				System.out.println();
			System.out.print(puzzle[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * This uses the Collections class to shuffle an ArrayList.
	 * The list is then converted in an array to be used
	 */
	public static int[] puzzleGenerator() {
		Integer[] random_puzzle = {0, 1, 2, 3, 4, 5, 6, 7, 8};
		Collections.shuffle(Arrays.asList(random_puzzle));
		int[] shuffled = new int[9];
		for (int i = 0; i < shuffled.length; i++) 
			shuffled[i] = random_puzzle[i];
		return shuffled;
		
	}
	/**
	 * Given the state in array form, checks if the puzzle is solvable
	 * by finding the number of inversions. If the number is even, 
	 * the puzzle is solvable.
	 */
	
	public static boolean checkIfSolvable(int[] array) {
		
		int inversions = 0;
		for (int i = 0; i < array.length; i++) {
			int index = i;
			if (i == (array.length - 1))
				break;
			for (int j = i + 1; j < array.length; j++) {
				if (array[j] == 0)
					continue;
				if (array[index] > array[j]) 
					inversions++;
			}
		}
		if (inversions % 2 == 0)
			return true;
		else
			return false;
	}
}
