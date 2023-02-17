import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;



public class PenguAction {
	
	public static PenguModelGreedyBFS penguModelGreedyBFS;
	public static char working_FrozenField[][];
    public static int noOfRows, noOfColumns;
    public static File output_file;

	public static void main(String[] args) {

	
		try {
		            // command line parameter input file
		            FileInputStream fstream_input = new FileInputStream(args[0]);
	               // FileInputStream fstream_output = new FileInputStream(args[1]);

				   	 // command line parameter output file
	                output_file = new File(args[1]);
		            BufferedReader br = new BufferedReader(new InputStreamReader(fstream_input));

					String strLine = br.readLine();
					//display the 1st line
					// System.out.println(strLine);
				    String[] parts = strLine.split(" ");
		            noOfRows = Integer.parseInt(parts[0]);
		              //  System.out.println("noOfRows: " + noOfRows);
		            noOfColumns = Integer.parseInt(parts[1]);
					   // System.out.println("noOfColumns: " + noOfColumns);
					    
					
					    
		           // getting string array from input txt file and converting to char array
					String[][] array_filed = frozenfield(br, noOfRows, noOfColumns);
					working_FrozenField = new char[array_filed.length][array_filed[0].length];
					for (int i = 0; i < array_filed.length; i++) {
						for (int j = 0; j < array_filed[0].length; j++) {
							working_FrozenField[i][j] = array_filed[i][j].charAt(0);
						}
					}

					if (output_file.createNewFile()) {
		     		  // System.out.println("File created: " + myObj.getName());
		     		}else{
		     		   //System.out.println("File already exists.");
		      		}

				    //Close the input stream
		            fstream_input.close();
		            }catch (Exception e){//Catch exception if any
		              System.err.println("Error: Please specify the correct input and output file names");
		            }
	   	 
	         //create the object of the other class
			 penguModelGreedyBFS = new PenguModelGreedyBFS();
			

			//initiates the game
			if(working_FrozenField != null)
			{
				//calling method of the second class using its object
				((PenguModelGreedyBFS) penguModelGreedyBFS).initialiseVariables();
				
			}


	}
	
	//this method reads the data frozen field data from input text file and store it into 2d string array
	  public static String[][] frozenfield(BufferedReader br, int noOfRows, int noOfColumns) {
	   	    try {
	   	        String[][] array = new String[noOfRows][noOfColumns];
	   	        String line = br.readLine();

	   	        int i = 0, j = 0;
	   	        while(line != null) {
	   	            String strArray[] = line.split("");

	   	            if (!line.isEmpty()) {
	   	                for (String s : strArray) {
	   	                    if (!s.isEmpty()) {
	   	                        array[i][j++] = s;
	   	                    }
	   	                }
	   	                line = br.readLine();
	   	                i++;
	   	                j = 0;
	   	            }
	   	        }

	   	        br.close();
	   	        return array;
	   	    } catch (IOException ex) {
	   	        System.out.println("Problems..");
	   	    }
	   	    return null;
	   	}
	  
	  
		


}