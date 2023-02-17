import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class PenguModelGreedyBFS {

	public int pengu_score = 0;
	public static char pengu;
	public String pengu_path = "";
	public ArrayList<String> my_moves = new ArrayList<>();
	public Map<String, Integer> pengu_move_order;
	public static char copy_working_FrozenField[][] = new char[][] {};

	public boolean game_over = false;
	public Map<Character, int[]> pengu_current_position;
	public int currentValidMoveCount = 0;
	public boolean iceCellPosition = false;
	public boolean fishCellPosition = false;
	public int noOfRows = 0, noOfColumns = 0;
	boolean visited = false;
	private List<GameBoard> visitedNode;
	int currentRow = 0, currentCol = 0;
	String[] pop_result;

	int[] old_position_pengu;
	int old_score = 0;
	PriorityQueue<Nodes> pQueue;

	private boolean goalFound = false;
	GameBoard gameboard = null;;
	GameBoard gameboard1 = null;
	GameBoard gameboard2 = null;

	public void initialiseVariables() {

		// storing the moves direction according to numeric keypad order.
		pengu_move_order = new HashMap<>();
		pengu_move_order.put("S", 2);
		pengu_move_order.put("W", 4);
		pengu_move_order.put("E", 6);
		pengu_move_order.put("N", 8);
		pengu_move_order.put("NE", 9);
		pengu_move_order.put("NW", 7);
		pengu_move_order.put("SW", 1);
		pengu_move_order.put("SE", 3);

		pengu_current_position = new HashMap<>();
		pengu_current_position.put('P', current_position_pengu('P'));
		pengu_current_position.put('U', current_position_pengu('U'));
		pengu_current_position.put('S', current_position_pengu('S'));

		pengu = 'P';

		//create class Gameboard with required fields
		gameboard = new GameBoard(PenguAction.working_FrozenField, 0, pengu_current_position);
		greedy_BFS(gameboard);
	}

	// This method returns the current position of pengu
	private static int[] current_position_pengu(char pengu_position) {

		for (int i = 0; i < PenguAction.working_FrozenField.length; ++i) {
			for (int j = 0; j < PenguAction.working_FrozenField[i].length; ++j) {
				if (PenguAction.working_FrozenField[i][j] == pengu_position) {
					// System.out.println("start position pengu "+working_FrozenField[i][j]);

					return new int[] { i, j };
				}
			}
		}
		return new int[] { 0, 0 };
	}
	
	// This method returns the current position of pengu
		private static int[] current_position_pengu_gameboard(GameBoard gameboard, char pengu_position) {

			for (int i = 0; i < gameboard.frozen_field.length; ++i) {
				for (int j = 0; j < gameboard.frozen_field[i].length; ++j) {
					if (gameboard.frozen_field[i][j] == pengu_position) {
						// System.out.println("start position pengu "+working_FrozenField[i][j]);

						return new int[] { i, j };
					}
				}
			}
			return new int[] { 0, 0 };
		}

	// calculating score of pengu. Increments after pengu picks up any fish
	public int calculate_score(GameBoard gameboard1, String move) {
		int my_pos[] = gameboard1.pengu_current_position.get('P');

		// check if next cell in the west direction has fish and update the score
		if (move == "W") {
			if (gameboard1.frozen_field[my_pos[0]][my_pos[1] - 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0]][my_pos[1] - 1] = ' ';
			}
		}
		// check if next cell in the east direction has fish and update the score
		if (move == "E") {
			if (gameboard1.frozen_field[my_pos[0]][my_pos[1] + 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0]][my_pos[1] + 1] = ' ';

			}
		}
		// check if next cell in the north direction has fish and update the score
		if (move == "N") {
			if (gameboard1.frozen_field[my_pos[0] - 1][my_pos[1]] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] - 1][my_pos[1]] = ' ';

			}
		}
		// check if next cell in the south direction has fish and update the score
		if (move == "S") {
			if (gameboard1.frozen_field[my_pos[0] + 1][my_pos[1]] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] + 1][my_pos[1]] = ' ';

			}

		}
		// check if next cell in the northeast direction has fish and update the score
		if (move == "NE") {
			if (gameboard1.frozen_field[my_pos[0] - 1][my_pos[1] + 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] - 1][my_pos[1] + 1] = ' ';
			}
		}
		// check if next cell in the northwest direction has fish and update the score
		if (move == "NW") {
			if (gameboard1.frozen_field[my_pos[0] - 1][my_pos[1] - 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] - 1][my_pos[1] - 1] = ' ';
			}
		}
		// check if next cell in the southeast direction has fish and update the score
		if (move == "SE") {
			if (gameboard1.frozen_field[my_pos[0] + 1][my_pos[1] + 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] + 1][my_pos[1] + 1] = ' ';
			}
		}
		// check if next cell in the southwest direction has fish and update the score
		if (move == "SW") {
			if (gameboard1.frozen_field[my_pos[0] + 1][my_pos[1] - 1] == '*') {
				gameboard1.pengu_score++;
				gameboard1.frozen_field[my_pos[0] + 1][my_pos[1] - 1] = ' ';
			}
		}

		return gameboard1.pengu_score;
	}

	// This method checks for all the valid moves for pengu
	public static ArrayList<String> valid_moves_for_pengu(GameBoard gameboard1, char pengu_position) {

		ArrayList<String> valid_moves = new ArrayList<String>(); // Create an ArrayList object
		int position[] = gameboard1.pengu_current_position.get(pengu);
		// check if next cell is not a wall cell
		if (gameboard1.frozen_field[position[0] - 1][position[1]] != '#'
				&& gameboard1.frozen_field[position[0] - 1][position[1]] != 'S'
				&& gameboard1.frozen_field[position[0] - 1][position[1]] != 'U') {

			valid_moves.add("N");

		}
		if (gameboard1.frozen_field[position[0] - 1][position[1] - 1] != '#'
				&& gameboard1.frozen_field[position[0] - 1][position[1] - 1] != 'S'
				&& gameboard1.frozen_field[position[0] - 1][position[1] - 1] != 'U') {
			// System.out.println("NW " + working_FrozenField[position[0] - 1][position[1] -
			// 1]);

			valid_moves.add("NW");

		}
		if (gameboard1.frozen_field[position[0]][position[1] - 1] != '#'
				&& gameboard1.frozen_field[position[0]][position[1] - 1] != 'S'
				&& gameboard1.frozen_field[position[0]][position[1] - 1] != 'U') {
			// System.out.println("W " + working_FrozenField[position[0]][position[1] - 1]);

			valid_moves.add("W");

		}
		if (gameboard1.frozen_field[position[0] + 1][position[1] - 1] != '#'
				&& gameboard1.frozen_field[position[0] + 1][position[1] - 1] != 'S'
				&& gameboard1.frozen_field[position[0] + 1][position[1] - 1] != 'U') {
			// System.out.println("SW " + working_FrozenField[position[0] + 1][position[1] -
			// 1]);

			valid_moves.add("SW");

		}
		if (gameboard1.frozen_field[position[0] + 1][position[1] + 1] != '#'
				&& gameboard1.frozen_field[position[0] + 1][position[1] + 1] != 'S'
				&& gameboard1.frozen_field[position[0] + 1][position[1] + 1] != 'U') {
			// System.out.println("SE " + working_FrozenField[position[0] + 1][position[1] +
			// 1]);

			valid_moves.add("SE");

		}
		
		
		if (gameboard1.frozen_field[position[0] - 1][position[1] + 1] != '#'
				&& gameboard1.frozen_field[position[0] - 1][position[1] + 1] != 'S'
				&& gameboard1.frozen_field[position[0] - 1][position[1] + 1] != 'U') {
			// System.out.println("NE " + working_FrozenField[position[0] - 1][position[1] +
			// 1]);

			valid_moves.add("NE");

		}
		
		if (gameboard1.frozen_field[position[0] + 1][position[1]] != '#'
				&& gameboard1.frozen_field[position[0] + 1][position[1]] != 'S'
				&& gameboard1.frozen_field[position[0] + 1][position[1]] != 'U') {
			// System.out.println("S " + working_FrozenField[position[0] + 1][position[1]]);

			valid_moves.add("S");

		}

		if (gameboard1.frozen_field[position[0]][position[1] + 1] != '#'
				&& gameboard1.frozen_field[position[0]][position[1] + 1] != 'S'
				&& gameboard1.frozen_field[position[0]][position[1] + 1] != 'U') {
			// System.out.println("E " + working_FrozenField[position[0]][position[1] + 1]);

			valid_moves.add("E");

		}

		

		return valid_moves;

	}
	
	//this heuristic function keeps track of the pengu score
	//it calculates and returns the value after subtracting it from 10000.
	public int heuristic_function(int pengu_score) {
		
		
		return 10000 - pengu_score;
		
	}
	
	//this method implements the greedy best first search algorithm
	public void greedy_BFS(GameBoard gameboard) {
		visitedNode = new ArrayList<>();

		//pQueue = new PriorityQueue<Nodes>((x,y) -> x.getScore() - y.getScore());
		//initialize the priority queue
		pQueue = new PriorityQueue<>();
		pQueue.add(new Nodes(1999, new String[]{""}));
	

		//iterate while queue is not empty
		while (!pQueue.isEmpty()) {
			
		
			
			pop_result = pQueue.remove().getNode();
			
		try {
            //make a copy of the original gameboard
			 gameboard1 = (GameBoard) gameboard.clone();
		}
		catch (CloneNotSupportedException c) {
			System.out.println("CloneNotSupportedException ");

		}

	
			transitionFunction(gameboard1, pop_result);

			if (pengu_score >= 20) { // find dest
				goalFound = true;
				
				// print output field
				printFrozenField(gameboard1);
				break;
			}

			//check for valid moves
			my_moves = valid_moves_for_pengu(gameboard1, 'P');
			

			//convert the arraylist to string array
			String[] array_moves = new String[my_moves.size()];
			for (int j = 0; j < my_moves.size(); j++) {
				array_moves[j] = my_moves.get(j);
			}
		
			//combine two string arrays
			String[] both = Arrays.copyOf(pop_result, pop_result.length + array_moves.length);
			System.arraycopy(array_moves, 0, both, pop_result.length, array_moves.length);
			
			
			try {

				//another copy of a original gameboard
				 gameboard2 = (GameBoard) gameboard.clone();
			}
			catch (CloneNotSupportedException c) {
				System.out.println("CloneNotSupportedException ");

			}

	

			
			 transitionFunction(gameboard2, both);

				//check if the given gameboard is visited or not,
				//add to visited list and to the priority queue if yes
			 if(isNotVisited(gameboard2, visitedNode))
			 {
				 pQueue.add(new Nodes(heuristic_function(gameboard2.pengu_score), both));
					//System.out.println("gameboard2.pengu_score  "+gameboard2.pengu_score);

				 visitedNode.add(gameboard2);
			 }
			
			 
			 
		}
	}
	
	//function to check the visited gameboard
	public boolean isNotVisited(GameBoard gameboard2, List<GameBoard> visitedNode2) {
		
		for(int i = 0; i < visitedNode2.size(); i++)
		{
			if(visitedNode2.get(i).pengu_score == gameboard2.pengu_score && visitedNode2.get(i).pengu_current_position.get('P') ==
					gameboard2.pengu_current_position.get('P')) {
				return false;

			}
		}
		
		return true;
		
	}


	//this transition method keeps track of the valid moves and pengu score
	private void transitionFunction(GameBoard gameboard, String[] pop_result2) {
	
		pengu_score += old_score;

		for (int i = 0; i < pop_result2.length; i++) {
			
            

			copy_working_FrozenField = new char[gameboard.frozen_field.length][];

			for (int j = 0; j < gameboard.frozen_field.length; j++) {
				// allocation space to each row of arr2[]
				copy_working_FrozenField[j] = new char[gameboard.frozen_field[j].length];
				for (int k = 0; k < gameboard.frozen_field[j].length; k++) {
					copy_working_FrozenField[j][k] = gameboard.frozen_field[j][k];
				}
			}

			//old_position_pengu = current_position_pengu(pengu);
			next_move(gameboard, pop_result2[i], 'P');
			old_score = calculate_score(gameboard, pop_result2[i]);

		}

	}

	// This method updates the new position of pengu and act according to given
	// conditions
	public void next_move(GameBoard gameboard1, String next_move, char pengu) {

		// get the current position of pengu in pos array
		int pos[] = gameboard1.pengu_current_position.get(pengu);
		

	//	my_moves = valid_moves_for_pengu(gameboard1, pengu);
		switch (next_move) {
		case "W":

			// check if pengu slides into any hazards. 
			if (gameboard1.frozen_field[pos[0]][pos[1] - 1] != '#') {
				if (gameboard1.frozen_field[pos[0]][pos[1] - 1] == 'U'
						|| gameboard1.frozen_field[pos[0]][pos[1] - 1] == 'S') {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					// assign pengu to current position
					
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (gameboard1.frozen_field[pos[0]][pos[1] - 1] == ' '
							|| gameboard1.frozen_field[pos[0]][pos[1] - 1] == '*') {

						if (gameboard1.frozen_field[pos[0]][pos[1] - 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1, next_move);
						}

						if (gameboard1.frozen_field[pos[0]][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("W");

						}

						// assign pengu to current position
						gameboard1.frozen_field[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						// continues sliding in the same direction
						next_move(gameboard1, next_move, pengu);

					} else {
						// ice cell
						gameboard1.frozen_field[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("W");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

							// iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;
					}
				}
			}
			break;

		case "E":

			// check if pengu slides into any hazards. 
			if (gameboard1.frozen_field[pos[0]][pos[1] + 1] != '#') {
				if ((gameboard1.frozen_field[pos[0]][pos[1] + 1] == 'U')
						|| (gameboard1.frozen_field[pos[0]][pos[1] + 1] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);
                    
					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((gameboard1.frozen_field[pos[0]][pos[1] + 1] == ' ')
							|| (gameboard1.frozen_field[pos[0]][pos[1] + 1] == '*')) {

						if (gameboard1.frozen_field[pos[0]][pos[1] + 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1, next_move);
						}

						if (gameboard1.frozen_field[pos[0]][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("E");

						}

						gameboard1.frozen_field[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {
						gameboard1.frozen_field[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("E");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "N":

			// check if pengu slides into any hazards.
			if (gameboard1.frozen_field[pos[0] - 1][pos[1]] != '#') {
				if ((gameboard1.frozen_field[pos[0] - 1][pos[1]] == 'U')
						|| (gameboard1.frozen_field[pos[0] - 1][pos[1]] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((gameboard1.frozen_field[pos[0] - 1][pos[1]] == ' ')
							|| (gameboard1.frozen_field[pos[0] - 1][pos[1]] == '*')) {

						// System.out.println("slide to next cell ");
						if (gameboard1.frozen_field[pos[0] - 1][pos[1]] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}

						if (gameboard1.frozen_field[pos[0] - 2][pos[1]] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("N");

						}
						gameboard1.frozen_field[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {
						gameboard1.frozen_field[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("N");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

							// iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;


					}
				}
			}
			break;

		case "S":

			// check if pengu slides into any hazards.
			if (gameboard1.frozen_field[pos[0] + 1][pos[1]] != '#') {
				if ((gameboard1.frozen_field[pos[0] + 1][pos[1]] == 'U')
						|| (gameboard1.frozen_field[pos[0] + 1][pos[1]] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					
					
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((gameboard1.frozen_field[pos[0] + 1][pos[1]] == ' ')
							|| (gameboard1.frozen_field[pos[0] + 1][pos[1]] == '*')) {

					
						if (gameboard1.frozen_field[pos[0] + 1][pos[1]] == '*') 
						{
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}
						
						
						if (gameboard1.frozen_field[pos[0] + 2][pos[1]] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("S");

						}
						gameboard1.frozen_field[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);


					} else {
						gameboard1.frozen_field[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("S");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "NE":

			// check if pengu slides into any hazards.
			if (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] != '#') {
				if (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] == 'U'
						|| (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] == 'S')) {

					// check flag if current position is ice cell
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] == ' '
							|| (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] == '*')) {

						if (gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}
						if (gameboard1.frozen_field[pos[0] - 2][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("NE");

						}
						gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {
						gameboard1.frozen_field[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("NE");

						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;


					}
				}
			}
			break;

		case "NW":

			// check if pengu slides into any hazards.
			if (gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] != '#') {
				if (gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] == 'U'
						|| gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] == 'S') {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continue sliding in the same
					// direction
					if (gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] == ' '
							|| gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] == '*') {

						// System.out.println("slide to next cell ");
						if (gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}

						if (gameboard1.frozen_field[pos[0] - 2][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("NW");

						}
						gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {
						gameboard1.frozen_field[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("NW");

						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "SE":

			// check if pengu slides into any hazards. If yes, then pengu dies and turn the
			if (gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] != '#') {
				if ((gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] == 'U')
						|| (gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;

				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction

					if ((gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] == ' ')
							|| (gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] == '*')) {

						// System.out.println("slide to next cell ");
						if (gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}

						if (gameboard1.frozen_field[pos[0] + 2][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("SE");

						}

						gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {

						gameboard1.frozen_field[pos[0] + 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] + 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("SE");
				
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "SW":

			// check if pengu slides into any hazards. If yes, then pengu dies and turn the
			// game_over flag to true.
			if (gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] != '#') {
				if (gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] == 'U'
						|| gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] == 'S') {

					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						gameboard1.frozen_field[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							gameboard1.frozen_field[j][k] = copy_working_FrozenField[j][k];
						}
					}
					int pos1[] = current_position_pengu_gameboard(gameboard1, 'P');

					gameboard1.pengu_current_position.put(pengu, pos1);

					gameboard1.pengu_score = old_score;

				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] == ' '
							|| gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] == '*') {

						if (gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] == '*') {
							gameboard1.pengu_score = calculate_score(gameboard1,next_move);

						}

						if (gameboard1.frozen_field[pos[0] + 2][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("SW");

						}

						gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}

						next_move(gameboard1, next_move, pengu);

					} else {
						gameboard1.frozen_field[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						gameboard1.pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("SW");

						if (iceCellPosition) {
							replace_previous_cell(gameboard1, pos, '0');

						} else {
							replace_previous_cell(gameboard1, pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;
		default:
			break;
		}

	}

	// print the position of pengu
	public void printFrozenField(GameBoard gameboard1) {

		System.out.println(pengu_path);
		System.out.print(pengu_score + "\n");

        //Filewriter object to write the output into text file
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(PenguAction.output_file.getName());
			myWriter.write(pengu_path + "\n");
			myWriter.write(pengu_score + "\n");
			for (int i = 0; i < gameboard1.frozen_field.length; i++) {
				for (int j = 0; j < gameboard1.frozen_field[i].length; j++) {
					System.out.print(gameboard1.frozen_field[i][j] + "");
					myWriter.write(gameboard1.frozen_field[i][j] + "");

				}
				System.out.println();
				myWriter.write("\n");
			}

			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// replace previous cells to ""
	public static void replace_previous_cell(GameBoard gameboard1, int[] pos, char pengu) {

		if (pengu == 'P') {
			gameboard1.frozen_field[pos[0]][pos[1]] = ' ';
		}

		// if previous cell is 0(ice cell) then do not replace it with blank
		if (pengu == '0') {
			gameboard1.frozen_field[pos[0]][pos[1]] = '0';
		}


	}

	
	
	class GameBoard implements Cloneable
	{
		char[][] frozen_field;
		int pengu_score;
		Map<Character, int[]> pengu_current_position;
		
		GameBoard(char[][] frozen_field, int pengu_score, Map<Character, int[]> pengu_current_position)
		{
			this.frozen_field = frozen_field;
			this.pengu_score = pengu_score;
			this.pengu_current_position = pengu_current_position;
		}
		 public Object clone() throws CloneNotSupportedException
		    {
		        return super.clone();
		    }
	}

}
