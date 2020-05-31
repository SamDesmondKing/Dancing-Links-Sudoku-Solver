/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.HashMap;

import grid.StdSudokuGrid;
import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {

	private StdSudokuGrid grid;
	private ArrayList<Integer> values;
	private int gridSize;
	private int matrixRows;
	private int matrixCols;
	private int numCells;
	private boolean solutionFound = false;

	private ArrayList<ArrayList<String>> binaryMatrix;

	public AlgorXSolver() {

	} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
		this.numCells = gridSize * gridSize;

		this.matrixRows = (int) Math.pow(this.gridSize, 3);
		this.matrixCols = (int) Math.pow(this.gridSize, 2) * 4;

		// Initialize and populate the exact cover binary matrix with 0's.
		this.binaryMatrix = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < matrixRows; i++) {
			binaryMatrix.add(new ArrayList<String>());
		}

		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < matrixCols; j++) {
				binaryMatrix.get(i).add("0");
			}
		}

		// Add cell constrains to binary matrix.
		int matrixStartingIndex = 0;
		this.cellConstraints(matrixStartingIndex);

		// Add row constraints to binary matrix.
		matrixStartingIndex += numCells;
		this.rowConstraints(matrixStartingIndex);

		// Add column constraints to binary matrix.
		matrixStartingIndex += numCells;
		columnConstraints(matrixStartingIndex);

		// Add box constraints to binary matrix.
		matrixStartingIndex += numCells;
		int boxWidth = (int) Math.sqrt(gridSize);
		this.boxConstraints(0, 0, boxWidth, 0, matrixStartingIndex);

		// Map row/col/value keys to each row so we can build solution.
		this.mapKeys();

		// Print Matrix for testing
		int count = 0;
		StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < this.binaryMatrix.size(); i++) {
			for (int j = 0; j < this.binaryMatrix.get(i).size(); j++) {
				builder.append(binaryMatrix.get(i).get(j) + " ");
			}
			builder.append("\n");
		}
		System.out.println(builder);
		System.out.println(count);
		
		
		// Execute algorithm X.
		ArrayList<String> solution = new ArrayList<String>();
		this.algX(solution);



		// placeholder
		return false;
	} // end of solve()

	public void algX(ArrayList<String> solution) {

		// If the matrix A has no columns, the current partial solution
		// is a valid solution; terminate successfully.
		if (this.binaryMatrix.isEmpty()) {
			this.solutionFound(solution);
			return;
		} else {

			// Otherwise, choose a column c (deterministically).
			int columnChoice = 1;

			// Store row/col indexs to delete
			ArrayList<Integer> rowIndexToRemove = new ArrayList<Integer>();
			ArrayList<Integer> colIndexToRemove = new ArrayList<Integer>();

			for (int r = 0; r < this.binaryMatrix.size(); r++) {
				//System.out.println("choose a row");
				// Choose a row r such that A[r] = 1 (nondeterministically).
				if (this.binaryMatrix.get(r).get(columnChoice).equals("1")) {
					// Include row r in the partial solution (key is located at index 0).
					solution.add(this.binaryMatrix.get(r).get(0));
					// For each column j such that A[r][j] = 1,
					for (int j = 0; j < this.binaryMatrix.get(r).size(); j++) {
						if (this.binaryMatrix.get(r).get(j).equals("1")) {
							// for each row i such that A[i][j] = 1
							for (int i = 0; i < this.binaryMatrix.size(); i++) {
								if (this.binaryMatrix.get(i).get(j).equals("1")) {
									// stage the row for deletion
									rowIndexToRemove.add(i);
								}
							}
							// stage the col for deletion.
							colIndexToRemove.add(j);
						}
					}
					//Remove cols
					HashMap<Integer, String> removedCols = new HashMap<Integer, String>();
					for (int i : colIndexToRemove) {
						for (int k = 0; k < this.binaryMatrix.size(); k++) {
							removedCols.put(i, this.binaryMatrix.get(k).get(i));
							this.binaryMatrix.get(k).remove(i);
						}
					}
					//Remove rows
					ArrayList<ArrayList<String>> removedRows = new ArrayList<ArrayList<String>>();
					for (int i : rowIndexToRemove) {
						removedRows.add(this.binaryMatrix.get(i));
						this.binaryMatrix.remove(i);
					}
					
//					StringBuilder builder = new StringBuilder("");
//					for (int i = 0; i < this.binaryMatrix.size(); i++) {
//						for (int j = 0; j < this.binaryMatrix.get(i).size(); j++) {
//							builder.append(binaryMatrix.get(i).get(j) + " ");
//						}
//						builder.append("\n");
//					}
//					System.out.println(builder);
					
					//Reccur
					algX(solution);
					
					//If no solution yet, backtrack. 
					if (solutionFound == false) {
						System.out.println("Backtrack");
						//Re-add rows
						int index = 0;
						for (int i : rowIndexToRemove) {
							this.binaryMatrix.add(i, removedRows.get(index));
							index++;
						}
						//Re-add columns
						for (int i : colIndexToRemove) {
							for (int k = 0; k < this.binaryMatrix.size(); k++) {
								this.binaryMatrix.get(k).add(i, removedCols.get(i));
							}
						}
//						StringBuilder builder2 = new StringBuilder("");
//						for (int i = 0; i < this.binaryMatrix.size(); i++) {
//							for (int j = 0; j < this.binaryMatrix.get(i).size(); j++) {
//								builder2.append(binaryMatrix.get(i).get(j) + " ");
//							}
//							builder2.append("\n");
//						}
//						System.out.println(builder2);
					}
				}
			}
		}
	}
	
	public void solutionFound(ArrayList<String> solution) {
		System.out.println("Solution found");
		this.solutionFound = true;
	}

	/*
	 * Adds sudoku row/column/value key to first index of each matrix row.
	 */
	public void mapKeys() {

		int column = 1;
		int row = 1;
		int value = 1;
		String key;

		int columnCount = 1;
		int rowCount = 0;

		for (int i = 0; i < this.binaryMatrix.size(); i++) {

			// Keep track of row
			if (rowCount == gridSize * gridSize) {
				row++;
				rowCount = 0;
			}
			// Keep track of column
			if (columnCount > gridSize) {
				column++;
				columnCount = 1;
				if (column > gridSize) {
					column = 1;
				}
			}
			// Keep track of value
			if (value > gridSize) {
				value = 1;
			}

			key = Integer.toString(row) + "," + Integer.toString(column) + "," + Integer.toString(value);

			this.binaryMatrix.get(i).add(0, key);

			value++;
			columnCount++;
			rowCount++;
		}
	}

	/*
	 * Fills matrix with appropriate column constraints
	 */
	public void columnConstraints(int index) {

		index = gridSize * gridSize * 2;
		int count = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = index; j < gridSize * gridSize * 3; j++) {
				if (j == index) {
					binaryMatrix.get(i).set(j, "1");
					;
					index++;
					count++;
					break;
				}
			}
			if (count == gridSize * gridSize) {
				index = gridSize * gridSize * 2;
				count = 0;
			}
		}
	}

	/*
	 * Fills matrix with appropriate row constraints
	 */
	public void rowConstraints(int index) {

		int count = 0;
		int iteration = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = index; j < index * 2; j++) {
				if (j == index) {
					binaryMatrix.get(i).set(j, "1");
					;
					index++;
					count++;
					if (count == gridSize) {
						index -= gridSize;
						count = 0;
						iteration++;
					}
					break;
				}
			}
			if (iteration == gridSize) {
				index += gridSize;
				iteration = 0;
			}
		}
	}

	/*
	 * Fills matrix with appropriate cell constraints
	 */
	public void cellConstraints(int matrixStartingIndex) {

		int count = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < gridSize * gridSize; j++) {
				if (j == matrixStartingIndex) {
					binaryMatrix.get(i).set(j, "1");
					;
					count++;
					if (count == gridSize) {
						matrixStartingIndex++;
						count = 0;
						break;
					}
				}
			}
		}
	}

	/*
	 * Recursively fills matrix with appropriate box constraints.
	 */
	public void boxConstraints(int coord1, int coord2, int boxWidth, int boxNum, int matrixStartIndex) {

		int numBoxes = boxWidth * boxWidth;
		int boxStartingIndex = boxNum * numBoxes;

		// Stopping condition
		if (coord1 == numBoxes) {
			// terminate quietly.
		} else {
			// Cycle through all box cells.
			for (int i = coord1; i < coord1 + boxWidth; i++) {
				for (int j = coord2; j < coord2 + boxWidth; j++) {
					// For each possible value in each cell.
					for (int h = 0; h < numBoxes; h++) {
						int matrixIndex = this.getMatrixIndex(i, j, h);
						this.binaryMatrix.get(matrixIndex).set(matrixStartIndex + boxStartingIndex + h, "1");
					}
				}
			}
			boxNum++;
			// Adjust starting co-ordinates for next box.
			if (coord2 == numBoxes - boxWidth) {
				coord2 = 0;
				coord1 += boxWidth;
			} else {
				coord2 += boxWidth;
			}
			// Recur.
			boxConstraints(coord1, coord2, boxWidth, boxNum, matrixStartIndex);
		}
	}

	/*
	 * Takes sudoku coords/value and returns that position in the matrix.
	 */
	public int getMatrixIndex(int coord1, int coord2, int value) {
		return (coord1) * gridSize * gridSize + (coord2) * gridSize + (value);
	}

	public int[] getSudokuIndex(int matrixIndex) {

		int[] coords = new int[3];

		return coords;
	}

} // end of class AlgorXSolver
