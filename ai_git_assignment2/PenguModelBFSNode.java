import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PenguModelBFSNode {

	public int pengu_score = 0;
	public static char pengu;
	public String pengu_path = "";
	public ArrayList<String> my_moves = new ArrayList<>();
	public Map<String, Integer> pengu_move_order;

	public boolean game_over = false;
	public Map<Character, int[]> pengu_current_position;
	public int currentValidMoveCount = 0;
	public boolean iceCellPosition = false;
	public int noOfRows = 0, noOfColumns = 0;
	boolean visited = false;
	private List<String> visitedNode = new ArrayList<String>();
	int cost = 0;
	BFSNode next;
	String nodeid = "";
	String nxtRowStr = "";
	String nxtColStr = "";
	int currentRow = 0, currentCol = 0;
	BFSNode currentNode;
	LinkedList<BFSNode> queue;

	public void initialiseVariables() {

		// storing the moves direction according to numeric keypad order.
		pengu_move_order = new HashMap<>();
		pengu_move_order.put("S",2);
		pengu_move_order.put("W",4);
		pengu_move_order.put("E",6);
		pengu_move_order.put("N",8);
		pengu_move_order.put("NE",9);
		pengu_move_order.put("NW",7);
		pengu_move_order.put("SW",1);
		pengu_move_order.put("SE",3);

		pengu_current_position = new HashMap<>();
		pengu_current_position.put('P', current_position_pengu('P'));
		pengu_current_position.put('U', current_position_pengu('U'));
		pengu_current_position.put('S', current_position_pengu('S'));

		pengu = 'P';
		next_move(pengu);

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

	// This method updates the new position of pengu and act according to given
	// conditions
	public void next_move(char pengu) {

		// get the current position of pengu in pos array
		int pos[] = current_position_pengu(pengu);

		//place the current position of pengu in the BFS node
		currentNode = new BFSNode(pos[0], pos[1], null);
		queue = new LinkedList<BFSNode>();
		//add initial position of pengu in queue
		queue.add(currentNode);

		String id = (String.valueOf(pos[0]) + ":" + String.valueOf(pos[1]));
		//add initial position of pengu in visited nodes list
		visitedNode.add(id);

		boolean findDest = false;

		//iterate while queue is not empty
		while (!queue.isEmpty()) {

			//remove the first added position of pengu from queue(FIFO manner)
			currentNode = queue.remove();
			currentRow = currentNode.getRow();
			currentCol = currentNode.getCol();
			

			//check if pengu reaches at score 8
			if (pengu_score >= 8) { // find dest
				findDest = true;
				int pos1[] = current_position_pengu(pengu);
				//print output field
				printFrozenField(pos1);
				break;
			}
			
			moveToSouth(pengu);
			moveToSouthEast(pengu);
			moveToWest(pengu);
			moveToNorthEast(pengu);

			moveToEast(pengu);

			moveToNorth(pengu);

			moveToNorthWest(pengu);
			moveToSouthWest(pengu);
			

		}

	}

	// move southwest
	private void moveToSouthWest(char pengu) {
		// TODO Auto-generated method stub
		int pos[] = current_position_pengu(pengu);

		nxtRowStr = String.valueOf(pos[0] + 1);
		nxtColStr = String.valueOf(pos[1] - 1);
		nodeid = nxtRowStr + ":" + nxtColStr;
		
        //check if pengu's next move has hazards or wall
		if ((PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] != 'U'
				&& PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] != '#')
				&& (!visitedNode.contains(nxtRowStr + ":" + nxtColStr))) {

			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == ' '
					|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {

				if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '*') {
					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == '#') {
						//add next move to the queue and to the visited node list
						//and update the path and calculate the score
						pengu_score = calculate_score("SW");
						next = new BFSNode(pos[0] + 1, pos[1] - 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("SW");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == 'U' || 
							PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == 'S') {
						return;
					}
					else
					{
						pengu_score = calculate_score("SW");
						
					}
					
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == '#') {
						next = new BFSNode(pos[0] + 1, pos[1] - 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("SW");

					}
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == 'U' || 
							PenguAction.working_FrozenField[pos[0] + 2][pos[1] - 2] == 'S') {
						return;
					}
				}


				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
				int[] arr = { pos[0] + 1, pos[1] - 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// continues sliding in the same direction
				moveToSouthWest(pengu);

			} else if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] == '0') {
				// ice cell

				//add next move to the queue and to the visited node list
				//and update the path
				next = new BFSNode(pos[0] + 1, pos[1] - 1, currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("SW");

				PenguAction.working_FrozenField[pos[0] + 1][pos[1] - 1] = pengu;
				int[] arr = { pos[0] + 1, pos[1] - 1 };
				pengu_current_position.put(pengu, arr);
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');

					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;

			}

		} else {
		

		}
	}

	private void moveToWest(char pengu) {
		// TODO Auto-generated method stub
		int pos[] = current_position_pengu(pengu);

		// move west
		nxtRowStr = String.valueOf(pos[0]);
		nxtColStr = String.valueOf(pos[1] - 1);
		nodeid = nxtRowStr + ":" + nxtColStr;
	

		//check if pengu's next move has hazards or wall
		if ((PenguAction.working_FrozenField[pos[0]][pos[1] - 1] != 'U'
				&& PenguAction.working_FrozenField[pos[0]][pos[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[pos[0]][pos[1] - 1] != '#')
				&& (!visitedNode.contains(nxtRowStr + ":" + nxtColStr))) {

			if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == ' '
					|| PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '*') {
					if(PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == '#') {
						//add next move to the queue and to the visited node list
						//and update the path  and calculate the score
						pengu_score = calculate_score("W");
						next = new BFSNode(pos[0], pos[1] - 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("W");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == 'S') {
						return;
					}
					else
					{
						pengu_score = calculate_score("W");

					}
					
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == '#') {
						//add next move to the queue and to the visited node list
						//and update the path
					next = new BFSNode(pos[0], pos[1] - 1, currentNode);
					next.setCol(Integer.valueOf(nxtColStr));
					next.setRow(Integer.valueOf(nxtRowStr));
					visitedNode.add(nodeid);
					queue.add(next);
					pengu_path = pengu_path + pengu_move_order.get("W");

					}
					else if(PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0]][pos[1] - 2] == 'S') {
						return;
					}
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
				moveToWest(pengu);


			} else if (PenguAction.working_FrozenField[pos[0]][pos[1] - 1] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//and update the path
				next = new BFSNode(pos[0], pos[1] - 1, currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("W");

				PenguAction.working_FrozenField[pos[0]][pos[1] - 1] = pengu;
				int[] arr = { pos[0], pos[1] - 1 };
				pengu_current_position.put(pengu, arr);
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');

					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;
		

			}
		} else {
			

		}

	}

	private void moveToNorthWest(char pengu) {
		// TODO Auto-generated method stub
		int pos[] = current_position_pengu(pengu);

		// move Northwest
		nxtRowStr = String.valueOf(pos[0] - 1);
		nxtColStr = String.valueOf(pos[1] - 1);
		nodeid = nxtRowStr + ":" + nxtColStr;
		
		//check if pengu's next move has hazards or wall
		if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] != 'U'
				&& PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] != 'S'
				&& PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] != '#')
				&& (!visitedNode.contains(nxtRowStr + ":" + nxtColStr))) {

			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == ' '
					|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '*') {
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("NW");
						next = new BFSNode(pos[0] - 1, pos[1] - 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("NW");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == 'S') {
						
						return;
					}
					else
					{
						//calculate the score of pengu
						pengu_score = calculate_score("NW");

					}
					
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == '#') {
						//add next move to the queue and to the visited node list
						//and update the path
						next = new BFSNode(pos[0] - 1, pos[1] - 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("NW");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1] - 2] == 'S') {
						
						return;
					}
				}

				

				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
				int[] arr = { pos[0] - 1, pos[1] - 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}

				moveToNorthWest(pengu);


			} else if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] == '0') {
				// ice cell

				next = new BFSNode(pos[0] - 1, pos[1] - 1, currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("NW");

				PenguAction.working_FrozenField[pos[0] - 1][pos[1] - 1] = pengu;
				int[] arr = { pos[0] - 1, pos[1] - 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;

			}

		} else {
		

		}
	}

	private void moveToNorth(char pengu) {
		// TODO Auto-generated method stub
		// move North
		int pos[] = current_position_pengu(pengu);

		nxtRowStr = String.valueOf(pos[0] - 1);
		nxtColStr = String.valueOf(pos[1]);
		nodeid = nxtRowStr + ":" + nxtColStr;
		
		//check if pengu's next move has hazards or wall
		if ((PenguAction.working_FrozenField[pos[0] - 1][pos[1]] != 'U'
				&& PenguAction.working_FrozenField[pos[0] - 1][pos[1]] != 'S'
				&& PenguAction.working_FrozenField[pos[0] - 1][pos[1]] != '#')
				&& (!visitedNode.contains(nxtRowStr + ":" + nxtColStr))) {

			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == ' '
					|| PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '*') {
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("N");
						next = new BFSNode(pos[0] - 1, pos[1], currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("N");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == 'S') {
						
						return;
					}
					else
					{
						pengu_score = calculate_score("N");

					}
					
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						next = new BFSNode(pos[0] - 1, pos[1], currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("N");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1]] == 'S') {
						
						return;
					}
				}

				
			//	pengu_path = pengu_path + pengu_move_order.get("N");

				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
				int[] arr = { pos[0] - 1, pos[1] };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				moveToNorth(pengu);


			} else if (PenguAction.working_FrozenField[pos[0] - 1][pos[1]] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//update the path and calculate the score
				next = new BFSNode(pos[0] - 1, pos[1], currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("N");

				PenguAction.working_FrozenField[pos[0] - 1][pos[1]] = pengu;
				int[] arr = { pos[0] - 1, pos[1] };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');

					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;

			}

		} else {
		

		}
	}

	private void moveToNorthEast(char pengu) {
		// TODO Auto-generated method stub
		// move northeast
		int pos[] = current_position_pengu(pengu);

		nxtRowStr = String.valueOf(pos[0] - 1);
		nxtColStr = String.valueOf(pos[1] + 1);
		nodeid = nxtRowStr + ":" + nxtColStr;
		

		if ((!visitedNode.contains(nxtRowStr + ":" + nxtColStr))
				&& (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] != 'U'
						&& PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] != 'S'
						&& PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] != '#')) {

			if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == ' '
					|| PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '*') {
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("NE");
						next = new BFSNode(pos[0] - 1, pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("NE");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == 'S') {
						
						return;
					}
					else
					{
						pengu_score = calculate_score("NE");

					}
					
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						next = new BFSNode(pos[0] - 1, pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("NE");

					}
					//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] - 2][pos[1] + 2] == 'S') {
						
						return;
					}
				}

			
				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
				int[] arr = { pos[0] - 1, pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}

				moveToNorthEast(pengu);

			} else if (PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//update the path and calculate the score
				next = new BFSNode(pos[0] - 1, pos[1] + 1, currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("NE");

				PenguAction.working_FrozenField[pos[0] - 1][pos[1] + 1] = pengu;
				int[] arr = { pos[0] - 1, pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');

					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;
			
			}

		} else {
		

		}
	}

	private void moveToEast(char pengu) {
		// TODO Auto-generated method stub
		int pos[] = current_position_pengu(pengu);

		// move east
		nxtRowStr = String.valueOf(pos[0]);
		nxtColStr = String.valueOf(pos[1] + 1);
		nodeid = nxtRowStr + ":" + nxtColStr;
		
		//check if pengu's next move has hazards or wall
		if ((PenguAction.working_FrozenField[pos[0]][pos[1] + 1] != 'U'
				&& PenguAction.working_FrozenField[pos[0]][pos[1] + 1] != 'S'
				&& PenguAction.working_FrozenField[pos[0]][pos[1] + 1] != '#')
				&& (!visitedNode.contains(nxtRowStr + ":" + nxtColStr))) {

			if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == ' '
					|| PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*') {
				

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '*') {
					
					if (PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("E");
						next = new BFSNode(pos[0], pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("E");

					}//check if pengu's next move has hazards
 
					else if (PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == 'U'
							|| PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == 'S') {

						return;
					} else {
						pengu_score = calculate_score("E");

					}
				
				}
				else
				{
					if (PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						next = new BFSNode(pos[0], pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("E");

					}//check if pengu's next move has hazards
					else if (PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == 'U'
							|| PenguAction.working_FrozenField[pos[0]][pos[1] + 2] == 'S') {

						return;
					}
				}

			

				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
				int[] arr = { pos[0], pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// continues sliding in the same direction
				moveToEast(pengu);

			} else if (PenguAction.working_FrozenField[pos[0]][pos[1] + 1] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//update the path and calculate the score
				next = new BFSNode(pos[0], (pos[1] + 1), currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("E");

				PenguAction.working_FrozenField[pos[0]][pos[1] + 1] = pengu;
				int[] arr = { pos[0], pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;

			}

		} else {
			

		}
	}

	public void moveToSouth(char pengu) {
		
		int pos[] = current_position_pengu(pengu);

		nxtRowStr = String.valueOf(pos[0] + 1);
		nxtColStr = String.valueOf(pos[1]);
		nodeid = nxtRowStr + ":" + nxtColStr;

	
		if ((!visitedNode.contains(nxtRowStr + ":" + nxtColStr))
				&& (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] != 'U'
						&& PenguAction.working_FrozenField[pos[0] + 1][pos[1]] != 'S'
						&& PenguAction.working_FrozenField[pos[0] + 1][pos[1]] != '#')) {

			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == ' '
					|| PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '*') {
					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("S");
						next = new BFSNode(pos[0] + 1, (pos[1]), currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("S");

					}//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == 'U' ||
							PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == 'S' ) 
					{
						return;
					}
						
					else {
						pengu_score = calculate_score("S");

					}
				
				}
				else
				{
					System.out.println("south blank "+PenguAction.working_FrozenField[pos[0] + 2][pos[1]]);

					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						next = new BFSNode(pos[0] + 1, (pos[1]), currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("S");

					}//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == 'U' ||
							PenguAction.working_FrozenField[pos[0] + 2][pos[1]] == 'S' ) 
					{
						return;
					}
				}

				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
				int[] arr = { pos[0] + 1, pos[1] };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// continues sliding in the same direction
				moveToSouth(pengu);

			} else if (PenguAction.working_FrozenField[pos[0] + 1][pos[1]] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//update the path and calculate the score
				next = new BFSNode(pos[0] + 1, (pos[1]), currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("S");

				PenguAction.working_FrozenField[pos[0] + 1][pos[1]] = pengu;
				int[] arr = { pos[0] + 1, pos[1] };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;


			}

		} else {
			

		}
	}

	private void moveToSouthEast(char pengu) {

		int pos[] = current_position_pengu(pengu);
		// move southeast
		nxtRowStr = String.valueOf(pos[0] + 1);
		nxtColStr = String.valueOf(pos[1] + 1);
		nodeid = nxtRowStr + ":" + nxtColStr;

		if ((!visitedNode.contains(nxtRowStr + ":" + nxtColStr))
				&& (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] != 'U'
						&& PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] != 'S'
						&& PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] != '#')) {

			if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == ' '
					|| PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*') {

				// System.out.println("slide to next cell ");
				if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '*') {
					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						pengu_score = calculate_score("SE");
						next = new BFSNode(pos[0] + 1, pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("SE");

					}//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == 'S') {
						return;
					}
					else
					{
						pengu_score = calculate_score("SE");

					}
				
				}
				else
				{
					if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == '#') {
						//add next move to the queue and to the visited node list
						//update the path and calculate the score
						next = new BFSNode(pos[0] + 1, pos[1] + 1, currentNode);
						next.setCol(Integer.valueOf(nxtColStr));
						next.setRow(Integer.valueOf(nxtRowStr));
						visitedNode.add(nodeid);
						queue.add(next);
						pengu_path = pengu_path + pengu_move_order.get("SE");

					}//check if pengu's next move has hazards
					else if(PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == 'U' ||
							PenguAction.working_FrozenField[pos[0] + 2][pos[1] + 2] == 'S') {
						return;
					}
				}


				// assign pengu to current position
				PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
				int[] arr = { pos[0] + 1, pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				
				// continues sliding in the same direction
				moveToSouthEast(pengu);

			} else if (PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] == '0') {
				// ice cell
				//add next move to the queue and to the visited node list
				//update the path and calculate the score
				next = new BFSNode(pos[0] + 1, (pos[1] + 1), currentNode);
				next.setCol(Integer.valueOf(nxtColStr));
				next.setRow(Integer.valueOf(nxtRowStr));
				visitedNode.add(nodeid);
				queue.add(next);
				pengu_path = pengu_path + pengu_move_order.get("SE");

				PenguAction.working_FrozenField[pos[0] + 1][pos[1] + 1] = pengu;
				int[] arr = { pos[0] + 1, pos[1] + 1 };
				pengu_current_position.put(pengu, arr);
				//if previous cell was ice cell then update the cell with 0
				if (iceCellPosition) {
					replace_previous_cell(pos, '0');
					//iceCellPosition = false;
				} else {
					replace_previous_cell(pos, pengu);

				}
				// check flag if current position is ice cell
				iceCellPosition = true;
		
			}

		} else {
		

		}

	}

	// print the position of pengu
	public void printFrozenField(int[] pos) {

		System.out.println(pengu_path);
		System.out.print(pengu_score + "\n");
		
		
		//System.out.println("Queue "+queue.size());//print it in a single line

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
