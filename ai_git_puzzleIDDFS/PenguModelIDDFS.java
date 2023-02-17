import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class PenguModelIDDFS {

	public int pengu_score = 0;
	public static char pengu;
	public String pengu_path = "";
	public ArrayList<String> my_moves = new ArrayList<>();
	public Map<String, Integer> pengu_move_order;
	public static char copy_working_FrozenField[][] = new char[][] {};

	public boolean game_over = false;
	public Map<Character, int[]> pengu_current_position;
	public boolean iceCellPosition = false;
	boolean visited = false;
	private List<String> visitedNode;
	int cost = 0;
	String nodeid = "";
	String nxtRowStr = "";
	String nxtColStr = "";
	int currentRow = 0, currentCol = 0;
	Stack<String[]> stack;
	String[] pop_result = {};
	int[] old_position_pengu;
	int old_score = 0;
	private int depth;
	private boolean goalFound = false;

	public void initialiseVariables() {

		visitedNode = new ArrayList<>();
		stack = new Stack<>();
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
		iterativeDeeping();

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

	// calculating score of pengu. Increments after pengu picks up any fish
	public int calculate_score(String move) {
		int my_pos[] = current_position_pengu('P');
		// check if next cell in the west direction has fish and update the score
		if (move == "W") {
			if (PenguAction.working_FrozenField[my_pos[0]][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0]][my_pos[1] - 1] = ' ';
			}
		}
		// check if next cell in the east direction has fish and update the score
		if (move == "E") {
			if (PenguAction.working_FrozenField[my_pos[0]][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0]][my_pos[1] + 1] = ' ';

			}
		}
		// check if next cell in the north direction has fish and update the score
		if (move == "N") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1]] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1]] = ' ';

			}
		}
		// check if next cell in the south direction has fish and update the score
		if (move == "S") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1]] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1]] = ' ';

			}

		}
		// check if next cell in the northeast direction has fish and update the score
		if (move == "NE") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] + 1] = ' ';
			}
		}
		// check if next cell in the northwest direction has fish and update the score
		if (move == "NW") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] - 1] = ' ';
			}
		}
		// check if next cell in the southeast direction has fish and update the score
		if (move == "SE") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] + 1] = ' ';
			}
		}
		// check if next cell in the southwest direction has fish and update the score
		if (move == "SW") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] - 1] = ' ';
			}
		}

		return pengu_score;
	}

	// This method checks for all the valid moves for pengu
	public static ArrayList<String> valid_moves_for_pengu(char pengu_position) {

		ArrayList<String> valid_moves = new ArrayList<String>(); // Create an ArrayList object
		int position[] = current_position_pengu(pengu_position);
		// check if next cell is not a wall cell
		if (PenguAction.working_FrozenField[position[0] - 1][position[1]] != '#'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1]] != 'S'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1]] != 'U') {

			valid_moves.add("N");

		}
		if (PenguAction.working_FrozenField[position[0] - 1][position[1] - 1] != '#'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1] - 1] != 'U') {
			// System.out.println("NW " + working_FrozenField[position[0] - 1][position[1] -
			// 1]);

			valid_moves.add("NW");

		}
		if (PenguAction.working_FrozenField[position[0]][position[1] - 1] != '#'
				&& PenguAction.working_FrozenField[position[0]][position[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[position[0]][position[1] - 1] != 'U') {
			// System.out.println("W " + working_FrozenField[position[0]][position[1] - 1]);

			valid_moves.add("W");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1] - 1] != '#'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1] - 1] != 'U') {
			// System.out.println("SW " + working_FrozenField[position[0] + 1][position[1] -
			// 1]);

			valid_moves.add("SW");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1]] != '#'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1]] != 'S'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1]] != 'U') {
			// System.out.println("S " + working_FrozenField[position[0] + 1][position[1]]);

			valid_moves.add("S");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1] + 1] != '#'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1] + 1] != 'S'
				&& PenguAction.working_FrozenField[position[0] + 1][position[1] + 1] != 'U') {
			// System.out.println("SE " + working_FrozenField[position[0] + 1][position[1] +
			// 1]);

			valid_moves.add("SE");

		}

		if (PenguAction.working_FrozenField[position[0]][position[1] + 1] != '#'
				&& PenguAction.working_FrozenField[position[0]][position[1] + 1] != 'S'
				&& PenguAction.working_FrozenField[position[0]][position[1] + 1] != 'U') {
			// System.out.println("E " + working_FrozenField[position[0]][position[1] + 1]);

			valid_moves.add("E");

		}

		if (PenguAction.working_FrozenField[position[0] - 1][position[1] + 1] != '#'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1] + 1] != 'S'
				&& PenguAction.working_FrozenField[position[0] - 1][position[1] + 1] != 'U') {
			// System.out.println("NE " + working_FrozenField[position[0] - 1][position[1] +
			// 1]);

			valid_moves.add("NE");

		}

		return valid_moves;

	}

	//this function increments the depth level while goal is found
	public void iterativeDeeping() {

		depth = 0;

		while (!goalFound) {
			penguBoundedDFS(depth);
			depth = depth + 1;
		}
	}

	public void penguBoundedDFS(int depth_limit) {
		int pos[] = current_position_pengu(pengu);

		//push empty sequence into stack
		stack.push(new String[] {});

//		String id = (String.valueOf(pos[0]) + ":" + String.valueOf(pos[1]));
//		//add initial position of pengu in visited nodes list
//		visitedNode.add(id);

		//iterate this while stack is not empty
		while (!stack.isEmpty()) {
			
			//pop element from stack in LIFO manner
			pop_result =  stack.pop();
			
			//transition function
			 transitionFunction(PenguAction.working_FrozenField, pop_result);
			 
			 //check if the list is greater than depth limit, if yes then continue
			 if(pop_result.length > depth_limit)
			 {
				 continue;
			 }
			 
			 //check if the list is equal to depth limit
			 if(pop_result.length == depth_limit)
			 {
  				//if goal found
				 if (pengu_score >= 16) { // find dest
						goalFound = true;
						int pos1[] = current_position_pengu(pengu);
						// print output field
						printFrozenField(pos1);
						break;
					}
			 }
			 
				//get all the valid moves of pengu
				my_moves = valid_moves_for_pengu('P');

				String[] array_moves = new String[my_moves.size()];
				for (int j = 0; j < my_moves.size(); j++) {
					array_moves[j] = my_moves.get(j);
				}

				String[] both = Arrays.copyOf(pop_result, pop_result.length + array_moves.length);
				System.arraycopy(array_moves, 0, both, pop_result.length, array_moves.length);

            		    	//push all the valid moves into the stack
				stack.push(both);
					 
						
					 
		} 
	

	}

	//this transition method keeps track of the valid moves and pengu score
	private void transitionFunction(char[][] working_FrozenField, String[] pop_result2) {

		for (int i = 0; i < pop_result2.length; i++) {

			old_score = calculate_score(pop_result2[i]);
			copy_working_FrozenField = new char[PenguAction.working_FrozenField.length][];

			for (int j = 0; j < PenguAction.working_FrozenField.length; j++) {
				// allocation space to each row of arr2[]
				copy_working_FrozenField[j] = new char[PenguAction.working_FrozenField[j].length];
				for (int k = 0; k < PenguAction.working_FrozenField[j].length; k++) {
					copy_working_FrozenField[j][k] = PenguAction.working_FrozenField[j][k];
				}
			}

			old_position_pengu = current_position_pengu(pengu);
			next_move(pop_result2[i], 'P');
		}

	}

	// This method updates the new position of pengu and act according to given
	// conditions
	public void next_move(String next_move, char pengu) {
		// get the current position of pengu in pos array
		int pos[] = current_position_pengu(pengu);
		my_moves = valid_moves_for_pengu(pengu);


		switch (next_move) {
		case "W":
			// check if pengu slides into any hazards. 
			if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] != '#') {
				if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == 'U'
						|| PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == 'S') {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;

				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {

						if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);
						}

						if (PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("W");

						}

						// assign pengu to current position
						PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						// continues sliding in the same direction
						next_move(next_move, pengu);

					} else {
						// ice cell
						PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("W");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

							// iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "E":
			// check if pengu slides into any hazards. 
			if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] != '#') {
				if ((PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == 'U')
						|| (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}
					pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == ' ')
							|| (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*')) {

						if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*') {
							pengu_score = calculate_score(next_move);
						}

						if (PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("E");

						}

						PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}

						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("E");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "N":
			// check if pengu slides into any hazards.
			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] != '#') {
				if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == ' ')
							|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*')) {

						// System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*') {
							pengu_score = calculate_score(next_move);

						}

						if (PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("N");

						}
						PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("N");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

							// iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;


					}
				}
			}
			break;

		case "S":
			// check if pengu slides into any hazards.
			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] != '#') {
				if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == ' ')
							|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*')) {

						// System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*') {
							pengu_score = calculate_score(next_move);

						}
						if (PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("S");

						}
						PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}

						next_move(next_move, pengu);


					} else {
						PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("S");
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "NE":
			// check if pengu slides into any hazards.
			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] != '#') {
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == 'U'
						|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == 'S')) {

					// check flag if current position is ice cell
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == ' '
							|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*')) {

						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*') {
							pengu_score = calculate_score(next_move);

						}
						if (PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("NE");

						}
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("NE");

						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;


					}
				}
			}
			break;

		case "NW":
			// check if pengu slides into any hazards.
			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] != '#') {
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == 'U'
						|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == 'S') {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;


				} else {
					// if next cell is a sliding cell then pengu will continue sliding in the same
					// direction
					if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {

						// System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);

						}

						if (PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("NW");

						}
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}

						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("NW");

						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "SE":
			// check if pengu slides into any hazards. If yes, then pengu dies and turn the
			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] != '#') {
				if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == 'S')) {

					//if pengu is dying in current move then set pengu position to previous one
					//cancel this move
					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;

				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == ' ')
							|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*')) {

						// System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*') {
							pengu_score = calculate_score(next_move);

						}

						if (PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("SE");

						}

						PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}

						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("SE");
				
						// check flag if current position is ice cell
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

						}
						iceCellPosition = true;

					}
				}
			}
			break;

		case "SW":
			// check if pengu slides into any hazards. If yes, then pengu dies and turn the
			// game_over flag to true.
			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] != '#') {
				if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == 'U'
						|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == 'S') {

					for (int j = 0; j < copy_working_FrozenField.length; j++) {
						// allocation space to each row of arr2[]
						PenguAction.working_FrozenField[j] = new char[copy_working_FrozenField[j].length];
						for (int k = 0; k < copy_working_FrozenField[j].length; k++) {
							PenguAction.working_FrozenField[j][k] = copy_working_FrozenField[j][k];
						}
					}

					pengu_score = old_score;

				} else {
					// if next cell is a sliding cell then pengu will continues sliding in the same
					// direction
					if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {

						if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);

						}

						if (PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == '#') {
							pengu_path = pengu_path + pengu_move_order.get("SW");

						}

						PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						if (iceCellPosition) {
							replace_previous_cell(pos, '0');
							iceCellPosition = false;
						} else {
							replace_previous_cell(pos, pengu);

						}
						next_move(next_move, pengu);

					} else {
						PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						pengu_path = pengu_path + pengu_move_order.get("SW");

						if (iceCellPosition) {
							replace_previous_cell(pos, '0');

						} else {
							replace_previous_cell(pos, pengu);

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
	public void printFrozenField(int[] pos) {

		System.out.println(pengu_path);
		System.out.print(pengu_score + "\n");

        //Filewriter object to write the output into text file
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(PenguAction.output_file.getName());
			myWriter.write(pengu_path + "\n");
			myWriter.write(pengu_score + "\n");
			for (int i = 0; i < PenguAction.working_FrozenField.length; i++) {
				for (int j = 0; j < PenguAction.working_FrozenField[i].length; j++) {
					System.out.print(PenguAction.working_FrozenField[i][j] + "");
					myWriter.write(PenguAction.working_FrozenField[i][j] + "");

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
	public static void replace_previous_cell(int[] pos, Character pengu) {

		if (pengu == 'P') {
			PenguAction.working_FrozenField[pos[0]][pos[1]] = ' ';
		}

		// if previous cell is 0(ice cell) then do not replace it with blank
		if (pengu == '0') {
			PenguAction.working_FrozenField[pos[0]][pos[1]] = '0';
		}


	}

}
