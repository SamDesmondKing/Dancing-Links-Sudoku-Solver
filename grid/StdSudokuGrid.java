/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
	// TODO: Add your own attributes
	private int[][] grid;
	private int gridSize;
	private int[] values;
	private boolean validity;

	public StdSudokuGrid() {
		super();
		validity = true;

	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		int coord1;
		int coord2;
		int value;

		// Read in from file and initialise grid, place all values where they should be.
		// Save to private member variables.
		try {
			File inputFile = new File(filename);
			Scanner inputReader = new Scanner(inputFile);

			// First two lines of file store valid values.
			this.gridSize = Integer.parseInt(inputReader.nextLine());
			String valueString = inputReader.nextLine();
			
			// Convert valid values into int array.
			String[] splitValues = valueString.split(" ");
			this.values = new int[splitValues.length];
			for (int i = 0; i < values.length; i++) {
				this.values[i] = Integer.parseInt(splitValues[i]);
			}

			// Initialise grid with blank values
			this.grid = new int[this.gridSize][this.gridSize];

			// Place appropriate starter values in the grid.
			while (inputReader.hasNextLine()) {

				// Pull and process co-ordinates.
				String coords = inputReader.next();
				String[] coordSplit = coords.split(",");
				coord1 = Integer.parseInt(coordSplit[0]);
				coord2 = Integer.parseInt(coordSplit[1]);

				// Pull value.
				value = inputReader.nextInt();

				// Place value at co-ordinates.
				this.grid[coord1][coord2] = value;
			}
			inputReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException.");
			e.printStackTrace();
		} catch (NoSuchElementException f) {
			// Blank line at end of file, don't worry about it.
		} catch (Exception g) {
			System.out.println("Other exception in Std InitGrid");
			g.printStackTrace();
		}
	} // end of initGrid()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {

		String returnString = "";
		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				returnString += this.grid[i][j] + " ";
			}
			returnString += "\n";
		}
		try {
			FileWriter outputFile = new FileWriter(filename);
			outputFile.write(returnString);
			outputFile.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	} // end of outputGrid()

	@Override
	public String toString() {
		String returnString = "";
		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				returnString += this.grid[i][j] + " ";
			}
			returnString += "\n";
		}
		return returnString;
	} // end of toString()

	@Override
	public boolean validate() {

		// Get expected row and box sizes.
		int rowSize = this.values.length;
		int numBoxes = rowSize;
		int boxSize = (int) Math.sqrt((double) rowSize);

		//Validate all boxes in grid are correct
		validateBoxes(0, 0, boxSize);
		
		//Validate all rows are correct
		
		//Validate all columns are correct

		return this.validity;
	} // end of validate()

	public void validateBoxes(int coord1, int coord2, int boxSize) {

		int numBoxes = boxSize * boxSize;
		Integer[] boxValues = new Integer[numBoxes];

		// Stopping condition
		if (coord2 == numBoxes || this.validity == false) {
			//terminate quietly.
		} else {
			// Get all box values.
			int k = 0;
			for (int i = coord1; i < coord1 + boxSize; i++) {
				for (int j = coord2; j < coord2 + boxSize; j++) {
					boxValues[k] = this.grid[i][j];
					k++;
				}
			}
			//Validate
			this.validity = this.validateRow(boxValues);
			// Adjust starting co-ordinates for next validation.
			if (coord1 == numBoxes - boxSize) {
				coord1 = 0;
				coord2 += boxSize;
			} else {
				coord1 += boxSize;
			}
			// Recur.
			validateBoxes(coord1, coord2, boxSize);
		}
	}
	
	public boolean validateRow(Integer[] row) {
		
		//Check row is correct length
		if (this.values.length != row.length) {
			return false;
		}
		
		//Copy Integer array to hashset to check for duplicate values.
		HashSet<Integer> hash =  new HashSet<Integer>(Arrays.asList(row)); 
		if (hash.size() != row.length) {
			return false;
		}
		
		//Use hashset to check that each value in this.values appears. 
		for (int i = 0; i < this.values.length; i++) {
			if (!hash.contains(this.values[i])) {
				return false;
			}
		}
		
		//These two checks determine if a row of sudoku ints is valid (no duplicates, same length, all values appear).
		return true;
		
	}


} // end of class StdSudokuGrid
