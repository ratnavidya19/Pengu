import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//This class is the main class 
public class PenguModelAI {
	
	public int pengu_score = 0;
	public char pengu;
	public String pengu_path = "";

	public ArrayList<String> my_moves = new ArrayList<>();
	public Map<Integer, String> pengu_move_order;

	public boolean game_over = false;
	public Map<Character, int[]> pengu_current_position;
	public int currentValidMoveCount = 0;
	public boolean iceCellPosition = false;
	public int noOfRows = 0, noOfColumns=0;
	
	//initialising the variables
	public void initialiseVariables() {
		// TODO Auto-generated method stub
		
		  //storing the moves direction according to numeric keypad order.
		pengu_move_order = new HashMap<>();
		pengu_move_order.put(2, "S");
		pengu_move_order.put(4, "W");
		pengu_move_order.put(6, "E");
		pengu_move_order.put(8, "N");
		pengu_move_order.put(9, "NE");
		pengu_move_order.put(7, "NW");
		pengu_move_order.put(1, "SW");
		pengu_move_order.put(3, "SE");

		pengu_current_position = new HashMap<>();
		pengu_current_position.put('P', current_position_pengu('P'));
		pengu_current_position.put('U', current_position_pengu('U'));
		pengu_current_position.put('S', current_position_pengu('S'));		
		
		
		playGame();
		
	}
	
	//Start the game
	public void playGame() {

		// Dont check the count if game is already over
		
			// considering max valid moves allowed are 6
			while (currentValidMoveCount < 6) {
				if (!game_over) {

				currentValidMoveCount++;
				// starting position of pengu
				pengu_move();
				}

			}
			
		
	}
	
	//This method returns the current position of pengu
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

	//This method checks for all the valid moves for pengu
	public static ArrayList<String> valid_moves_for_pengu(char pengu_position) {
      
		ArrayList<String> valid_moves = new ArrayList<String>(); // Create an ArrayList object
		int position[] = current_position_pengu(pengu_position);
		//check if next cell is not a wall cell
		if (PenguAction.working_FrozenField[position[0] - 1][position[1]] != '#') {

			valid_moves.add("N");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1]] != '#') {
		//	System.out.println("S  " + working_FrozenField[position[0] + 1][position[1]]);

			valid_moves.add("S");

		}
		if (PenguAction.working_FrozenField[position[0]][position[1] + 1] != '#') {
			//System.out.println("E  " + working_FrozenField[position[0]][position[1] + 1]);

			valid_moves.add("E");

		}
		if (PenguAction.working_FrozenField[position[0]][position[1] - 1] != '#') {
			//System.out.println("W  " + working_FrozenField[position[0]][position[1] - 1]);

			valid_moves.add("W");

		}

		if (PenguAction.working_FrozenField[position[0] - 1][position[1] + 1] != '#') {
		//	System.out.println("NE  " + working_FrozenField[position[0] - 1][position[1] + 1]);

			valid_moves.add("NE");

		}
		if (PenguAction.working_FrozenField[position[0] - 1][position[1] - 1] != '#') {
			//System.out.println("NW  " + working_FrozenField[position[0] - 1][position[1] - 1]);

			valid_moves.add("NW");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1] + 1] != '#') {
			//System.out.println("SE  " + working_FrozenField[position[0] + 1][position[1] + 1]);

			valid_moves.add("SE");

		}
		if (PenguAction.working_FrozenField[position[0] + 1][position[1] - 1] != '#') {
			//System.out.println("SW  " + working_FrozenField[position[0] + 1][position[1] - 1]);

			valid_moves.add("SW");

		}

		return valid_moves;

	}

	// This function creates random number for pengu to make a move in that
	// direction
	public void pengu_move() {
		
		Random r = new Random();
		int low = 1;
		int high = 9;
		int random_number1 = r.nextInt(high - low) + low;
		pengu = 'P';

		//store all valid moves in my_moves array
		my_moves = valid_moves_for_pengu(pengu);
		String move = pengu_move_order.get(random_number1);
	
		if (!my_moves.contains(move)) {
			//System.out.println("invalid move  ");
            //if move is invalid, again call the function to generate new random number
			pengu_move();
		} else {
			//storing the path of the pengu
			pengu_path = pengu_path + random_number1;
		    //change the position of the pengu according to given direction
			next_move(move, pengu);

		}

	}

	// calculating score of pengu. Increments after pengu picks up any fish 
	public int calculate_score(String move) {
		int my_pos[] = current_position_pengu('P');
		//check if next cell in the west direction has fish and update the score
		if (move == "W") {
			if (PenguAction.working_FrozenField[my_pos[0]][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0]][my_pos[1] - 1] = ' ';
			}
		}
		//check if next cell in the east direction has fish and update the score
		if (move == "E") {
			if (PenguAction.working_FrozenField[my_pos[0]][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0]][my_pos[1] + 1] = ' ';

			}
		}
		//check if next cell in the north direction has fish and update the score
		if (move == "N") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1]] == '*') {
				pengu_score++; 
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1]] = ' ';

			}
		}
		//check if next cell in the south direction has fish and update the score
		if (move == "S") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1]] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1]] = ' ';

			}

		}
		//check if next cell in the northeast direction has fish and update the score
		if (move == "NE") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] + 1] = ' ';
			}
		}
		//check if next cell in the northwest direction has fish and update the score
		if (move == "NW") {
			if (PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] - 1][my_pos[1] - 1] = ' ';
			}
		}
		//check if next cell in the southeast direction has fish and update the score
		if (move == "SE") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] + 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] + 1] = ' ';
			}
		}
		//check if next cell in the southwest direction has fish and update the score
		if (move == "SW") {
			if (PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] - 1] == '*') {
				pengu_score++;
				PenguAction.working_FrozenField[my_pos[0] + 1][my_pos[1] - 1] = ' ';
			}
		}

		return pengu_score;
	}

	//This method updates the new position of pengu and act according to given conditions
	public void next_move(String next_move, char pengu) {
        //get the current position of pengu in pos array
		int pos[] = current_position_pengu(pengu);
    	my_moves = valid_moves_for_pengu(pengu);
     
    	//check if move is a valid move
		if (my_moves.contains(next_move)) {
			
			switch (next_move) {
			case "W":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == 'U' || PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == 'S') {
					game_over = true;
					PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = 'X';
					int[] arr = { pos[0], pos[1] - 1 };
					pengu_current_position.put(pengu, arr);
					//check if previous cell was 0
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					  printFrozenField(pos);
					  return;
				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {

						//System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);
						}
						
						//assign pengu to current position
						PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }
						  //continues sliding in the same direction
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}

					} else {
						//ice cell
						PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = pengu;
						int[] arr = { pos[0], pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
					    //check flag if current position is ice cell
						iceCellPosition = true;
						replace_previous_cell(pos, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						} 
						//printFrozenField(pos);

					}
				}
				break;
				
			case "E":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if ((PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == 'U')
						|| (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == 'S')) {
					game_over = true;
					PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = 'X';
					int[] arr = { pos[0], pos[1] + 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }		
					  printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if ((PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == ' ')
							|| (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*')) {

						//System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*') {
							pengu_score = calculate_score(next_move);
						}

						PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }

						//printFrozenField(pos);
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
				
					} else {
						PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
						int[] arr = { pos[0], pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						//working_maze[pos[0]][pos[1] + 1] = "0";
						//working_FrozenField[pos[0]][pos[1]] = "0";
						iceCellPosition = true;
						replace_previous_cell(pos, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
						//printFrozenField(pos);
					}
				}
				break;
				
			case "N":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == 'S')) {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = 'X';
					int[] arr = { pos[0] - 1, pos[1] };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					  printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == ' ')
							|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*')) {

					//	System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*') {
							pengu_score = calculate_score(next_move);

						}

						PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
						// slide to next
					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
						int[] arr = { pos[0] - 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						//working_maze[pos[0] - 1][pos[1]] = "0";
						//working_FrozenField[pos[0]][pos[1]] = "0";
						iceCellPosition = true;
						replace_previous_cell(pos, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
				//		printFrozenField(pos);

					}
				}
				break;
				
			case "S":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == 'S')) {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = 'X';
					int[] arr = { pos[0] + 1, pos[1] };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == ' ')
							|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*')) 
					{

						//System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*') {
							pengu_score = calculate_score(next_move);

						}
						PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						pengu_current_position.put(pengu, arr);
                        if(iceCellPosition)
                        {
                        	replace_previous_cell(pos, '0');
                        	iceCellPosition = false;
                        }
                        else {
                        	replace_previous_cell(pos, pengu);

                        }

						//printFrozenField(pos);
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
					
					} else {
						PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
						int[] arr = { pos[0] + 1, pos[1] };
						pengu_current_position.put(pengu, arr);
						//working_maze[pos[0] + 1][pos[1]] = "0";
						//working_FrozenField[pos[0]][pos[1]] = "0";
						iceCellPosition = true;
						replace_previous_cell(pos,pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
					//	printFrozenField(pos);
					}
				}
				break;
				
			case "NE":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == 'U'
						|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == 'S')) {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = 'X';
					int[] arr = { pos[0] - 1, pos[1] + 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					  printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == ' '
							|| (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*')) {

						//System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*') {
							pengu_score = calculate_score(next_move);

						}
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }
						//printFrozenField(pos);
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}

						// next_move("NE", pengu);

						// slide to next
					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] + 1 };
						pengu_current_position.put(pengu, arr);
						//working_maze[pos[0] - 1][pos[1] + 1] = "0";
						//working_FrozenField[pos[0]][pos[1]] = "0";
						iceCellPosition = true;
						replace_previous_cell(pos, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
						//printFrozenField(pos);

					}
				}

				break;
				
			case "NW":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == 'U'
						|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == 'S') {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = 'X';
					int[] arr = { pos[0] - 1, pos[1] - 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					  printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {

					//	System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);

						}
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }

						//printFrozenField(pos);
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
						// slide to next
					} else {
						PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] - 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
					//	working_maze[pos[0] - 1][pos[1] - 1] = "0";
						//working_FrozenField[pos[0]][pos[1]] = "0";
						iceCellPosition = true;
						replace_previous_cell(pos,pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
						//printFrozenField(pos);
					}
				}
				break;
				
			case "SE":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == 'U')
						|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == 'S')) {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = 'X';
					int[] arr = { pos[0] + 1, pos[1] + 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					  printFrozenField(pos);
					  return;

				}else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
				if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == ' ')
						|| (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*')) {

					//System.out.println("slide to next cell ");
					if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*') {
						pengu_score = calculate_score(next_move);

					}

					PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
					int[] arr = { pos[0] + 1, pos[1] + 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }

					//printFrozenField(pos);
					next_move(next_move, pengu);
					if(currentValidMoveCount == 6)
					{
						printFrozenField(pos);
					}

					// next_move("SE", pengu);

					// slide to next
				} else {
					PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
					int[] arr = { pos[0] + 1, pos[1] + 1 };
					pengu_current_position.put(pengu, arr);
					//working_maze[pos[0] + 1][pos[1] + 1] = "0";
					//working_FrozenField[pos[0]][pos[1]] = "0";
					iceCellPosition = true;
					replace_previous_cell(pos, pengu);
					if(currentValidMoveCount == 6)
					{
						printFrozenField(pos);
					}
					//printFrozenField(pos);

				}
			}
				break;
				
			case "SW":
				//check if pengu slides into any hazards. If yes, then pengu dies and turn the game_over flag to true.
				if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == 'U'
						|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == 'S') {
					game_over = true;
					PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = 'X';
					int[] arr = { pos[0] + 1, pos[1] - 1 };
					pengu_current_position.put(pengu, arr);
					  if(iceCellPosition)
                      {
                      	replace_previous_cell(pos, '0');
                      	iceCellPosition = false;
                      }
                      else {
                      	replace_previous_cell(pos, pengu);

                      }
					printFrozenField(pos);
					  return;

				} else {
					//if next cell is a sliding cell then pengu will continues sliding in the same direction
					if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == ' '
							|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {

					//	System.out.println("slide to next cell ");
						if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {
							pengu_score = calculate_score(next_move);

						}

						PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
						  if(iceCellPosition)
	                        {
	                        	replace_previous_cell(pos, '0');
	                        	iceCellPosition = false;
	                        }
	                        else {
	                        	replace_previous_cell(pos, pengu);

	                        }
						//printFrozenField(pos);
						next_move(next_move, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}

						// slide to next
					} else {
						PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
						int[] arr = { pos[0] + 1, pos[1] - 1 };
						pengu_current_position.put(pengu, arr);
					//	working_maze[pos[0] + 1][pos[1] - 1] = "0;
						//working_FrozenField[pos[0]][pos[1]] = "0";
                        iceCellPosition = true;
						replace_previous_cell(pos, pengu);
						if(currentValidMoveCount == 6)
						{
							printFrozenField(pos);
						}
					    //printFrozenField(pos);
					}
				}
				break;
			default:
				break;
			}
			
		} else {
		//	System.out.println("Invalid move");

		}  

	}

	
	//print the position of pengu 
	public void printFrozenField(int[] pos) {
		
		System.out.println(pengu_path);
		System.out.print(pengu_score+"\n");
		FileWriter myWriter;
		try {
			myWriter = new FileWriter(PenguAction.output_file.getName());
			 myWriter.write(pengu_path+"\n");
			 myWriter.write(pengu_score+"\n");
				for (int i = 0; i < PenguAction.working_FrozenField.length; i++) {
					for (int j = 0; j < PenguAction.working_FrozenField[i].length; j++) {
						System.out.print(PenguAction.working_FrozenField[i][j] + " ");
						 myWriter.write(PenguAction.working_FrozenField[i][j] + " ");

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

	//replace previous cells to ""
	public static void replace_previous_cell(int[] pos, Character pengu) {

		
		if (pengu == 'P') {
			PenguAction.working_FrozenField[pos[0]][pos[1]] = ' ';
		}
		
		//if previous cell is 0(ice cell) then do not replace it with blank 
		if (pengu == '0') {
			PenguAction.working_FrozenField[pos[0]][pos[1]] = '0';
		}
	
	}

	

	

}
