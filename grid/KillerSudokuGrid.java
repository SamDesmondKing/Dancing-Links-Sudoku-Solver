/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {
	private int[][] grid;

	// Key: Cage co-ordinates. Value: Cage total. 
	private HashMap<ArrayList<Integer>, Integer> cages;

	private int gridSize;
	private ArrayList<Integer> values;
	private boolean validity;

	public KillerSudokuGrid() {
		super();
		this.validity = true;
		this.cages = new HashMap<ArrayList<Integer>, Integer>();
	} // end of KillerSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
	
		int cageValue = 0;

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

			// Read number of cages from following line 
			// value is not used in this implimentation, however.
			inputReader.nextLine();

			// Add cages to hashmap
			while (inputReader.hasNextLine()) {

				// Pull and process co-ordinates.
				String line = inputReader.nextLine();
				String[] lineSplit = line.split(" ");
				ArrayList<Integer> cageCoords = new ArrayList<Integer>();

				// Procces cage value and coords from file line.
				for (int i = 0; i < lineSplit.length; i++) {
					if (i == 0) {
						cageValue = Integer.parseInt(lineSplit[i]);
					} else {
						String[] eachCoord = lineSplit[i].split(",");
						for (String j : eachCoord) {
							if (!j.equals("")) {
								cageCoords.add(Integer.parseInt(j));
							}
						}
					}
				}
				// Add cage to hashmap.
				this.cages.put(cageCoords, cageValue);
			}

			// Initialise grid with blank values.
			this.grid = new int[this.gridSize][this.gridSize];

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
	} // end of initBoard()

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
	} // end of outputBoard()

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
		
		//Reset global validity - altered in row/box/column methods to follow. 
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
		
		//Validate all cages are correct
		this.validateCages();
		
		return this.validity;
	} // end of validate()
	
	public boolean isValidCell(int coord1, int coord2, int newValue) {
		
		/*This method differs from that in stdSudoku because we can't just run validate()
		 * as it will consider unfilled cages as invalid. We must seperately validate boxes,
		 * rows and columns and then validate only cages which have been filled completely.
		*/
		
		int cageValue;
		int cageSize;
		int[] cageBoxCoord = new int[2];
		int index = 0;
		boolean fullCage;
		
		//Reset Global validity (used in box/row/column tests)
		this.validity = true;
		//Local validity used for our specific cage checks.
		boolean cellValidity = true;
		
		// Get expected row and box sizes.
		int rowSize = this.values.size();
		int boxWidth = (int) Math.sqrt((double) rowSize);

		//Save current value
		int currentValue = this.getCoord(coord1, coord2);
		//Place new value
		this.setCoord(newValue, coord1, coord2);

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
		
		//Check all completely filled cages
		for (ArrayList<Integer> cage : cages.keySet()) {		
			cageValue = cages.get(cage);
			cageSize = cage.size() / 2;
			fullCage = true;
			int cageRunningTotal = 0;
			index = 0;
		
			//Cycle each individual cage box;
			//Also save running total value for validation.
			for (int i = 0; i < cageSize; i++) {
				cageBoxCoord[0] = cage.get(index);
				index ++;
				cageBoxCoord[1] = cage.get(index);
				index ++;			
				cageRunningTotal += this.grid[cageBoxCoord[0]][cageBoxCoord[1]];
				
				//If the value at any of the cage's co-ordinates is 0, the cage is not full.
				if (this.grid[cageBoxCoord[0]][cageBoxCoord[1]] == 0) {
					fullCage = false;
				}
			}		
			
			//If the cage is full, validate it.
			//System.out.println(fullCage);
			if (fullCage == true) {
				//System.out.println(fullCage);
				if (cageRunningTotal != cageValue) {
					cellValidity = false;
				}
			}
		}
		
		//Reset cell to original value and return result. 
		if (cellValidity && this.validity) {
			this.setCoord(currentValue, coord1, coord2);
			return true;
		} else {
			this.setCoord(currentValue, coord1, coord2);
			return false;
		}
	}

	//Recursively validate that all boxes in grid fit sudoku constraints. 
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

	// Determine if a set of ints is valid (no duplicates)
	public void validateSet(Integer[] row) {

		// Convert row to Arraylist - exclude '0' value
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		for (int i = 0; i < row.length; i++) {
			if (row[i] != 0) {
				rowList.add(row[i]);
			}
		}

		// Convert ArrayList to hashset to check for duplicates.
		int rowListLength = rowList.size();
		HashSet<Integer> hash = new HashSet<Integer>(rowList);
		if (hash.size() != rowListLength) {
			this.validity = false;
		}
	}
	
	//Validate that all cages in this grid's hashmap are correct. 
	public void validateCages() {
		int cageValue;
		int cageRunningTotal;
		int cageSize;
		int[] cageBoxCoord = new int[2];
		int index = 0;
		for (ArrayList<Integer> cage : cages.keySet()) {
			//cage.getKey()
			//cage.getValue()
			cageValue = cages.get(cage);
			cageSize = cage.size() / 2;
			cageRunningTotal = 0;
			index = 0;
			
			//Get co-ordinates for individual cage box;
			//Add value to running total for this cage.
			for (int i = 0; i < cageSize; i++) {
				cageBoxCoord[0] = cage.get(index);
				index ++;
				cageBoxCoord[1] = cage.get(index);
				index ++;
				cageRunningTotal += this.grid[cageBoxCoord[0]][cageBoxCoord[1]];
			}
			
			//Validate cage
			if (cageValue != cageRunningTotal) {
				this.validity = false;
				
			}	
		}
		//Error message.
		if (!this.validity) {
			System.out.println("Error: Cages not correct.");
		}
	}
	
	//Validate that a single given cage is correct
	public boolean validateSingleCage(ArrayList<Integer> cage) {
		
		boolean cageValidity = true;
		int cageValue = this.cages.get(cage);
		int cageSize = cage.size() / 2;
		int[] cageBoxCoord = new int[2];
		int index = 0;
		int cageRunningTotal = 0;
		
		for (int i = 0; i < cageSize; i++) {
			cageBoxCoord[0] = cage.get(index);
			index ++;
			cageBoxCoord[1] = cage.get(index);
			index = 0;
			cageRunningTotal += this.grid[cageBoxCoord[0]][cageBoxCoord[1]];
		}
		
		//Validate cage
		if (cageValue != cageRunningTotal) {
			cageValidity = false;
			
		}
		
		return cageValidity;
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

} // end of class KillerSudokuGrid
