/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
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

	private int[][] grid;

	private int gridSize;
	private ArrayList<Integer> values;
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
			this.values = new ArrayList<Integer>();
			for (int i = 0; i < splitValues.length; i++) {
				this.values.add(Integer.parseInt(splitValues[i]));
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
	
	//Check if a given value would be valid if placed at a given co-ordinate
	//Does not actually change the state of the grid. 
	public boolean isValidCell(int coord1, int coord2, int value) {
	
		int currentValue = this.getCoord(coord1, coord2);
		this.setCoord(value, coord1, coord2);
		
		if (this.validate()) {
			this.setCoord(currentValue, coord1, coord2);
			return true;
		} else {
			this.setCoord(currentValue, coord1, coord2);
			return false;
		}
	}

	@Override
	public boolean validate() {

		this.validity = true;
		
		// Get expected row and box sizes.
		int rowSize = this.values.size();
		int boxWidth = (int) Math.sqrt((double) rowSize);

		// Validate all boxes in grid are correct
		validateBoxes(0, 0, boxWidth);

		// Validate all rows & columns are correct
		for (int i = 0; i < rowSize; i++) {
			Integer[] row = new Integer[rowSize];
			Integer[] column = new Integer[rowSize];
			for (int j = 0; j < rowSize; j++) {
				row[j] = this.grid[i][j];
				column[j] = this.grid[j][i];
			}
			this.validateSet(row);
			this.validateSet(column);
		}

		return this.validity;
	} // end of validate()

	public void validateBoxes(int coord1, int coord2, int boxWidth) {

		int numBoxes = boxWidth * boxWidth;
		Integer[] boxValues = new Integer[numBoxes];

		// Stopping condition
		if (coord2 == numBoxes || this.validity == false) {
			// terminate quietly.
		} else {
			// Get all box values.
			int k = 0;
			for (int i = coord1; i < coord1 + boxWidth; i++) {
				for (int j = coord2; j < coord2 + boxWidth; j++) {
					boxValues[k] = this.grid[i][j];
					k++;
				}
			}
			// Validate
			//TODO Here
			this.validateSet(boxValues);
			// Adjust starting co-ordinates for next validation.
			if (coord1 == numBoxes - boxWidth) {
				coord1 = 0;
				coord2 += boxWidth;
			} else {
				coord1 += boxWidth;
			}
			// Recur.
			validateBoxes(coord1, coord2, boxWidth);
		}
	}

	//Determine if a set of ints is valid (no duplicates)
	public void validateSet(Integer[] row) {
		
		//Convert row to Arraylist - exclude '0' value
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		for (int i = 0; i < row.length; i++) {
			if (row[i] != 0) {
				rowList.add(row[i]);
			}
		}
		
		//Convert ArrayList to hashset to check for duplicates. 
		int rowListLength = rowList.size(); 
		HashSet<Integer> hash = new HashSet<Integer>(rowList);
		if (hash.size() != rowListLength) {
			this.validity = false;
		}
	}
	

	public void setCoord(int value, int coord1, int coord2) {
		this.grid[coord1][coord2] = value;
	}
	
	public int getCoord(int coord1, int coord2) {
		return this.grid[coord1][coord2];
	}
	
	public int getGridSize() {
		return this.gridSize;
	}
	
	public ArrayList<Integer> getValues() {
		return this.values;
	}
	
	public boolean getValidity() {
		return this.validity;
	}

} // end of class StdSudokuGrid
