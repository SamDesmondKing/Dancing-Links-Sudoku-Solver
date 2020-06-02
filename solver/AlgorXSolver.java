/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Collections;

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
	private boolean solutionFound;
	private ArrayList<String> partialSolution;

	private ArrayList<ArrayList<String>> binaryMatrix;

	public AlgorXSolver() {
		this.solutionFound = false;
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

		// Initialize partial solution holder.
		this.partialSolution = new ArrayList<String>();

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
		// System.out.println("Initial Print:");
		StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < this.binaryMatrix.size(); i++) {
			for (int j = 0; j < this.binaryMatrix.get(i).size(); j++) {
				builder.append(binaryMatrix.get(i).get(j) + " ");
			}
			builder.append("\n");
		}
		System.out.println(builder);

		// Remove rows that are already solved from binary matrix.
		ArrayList<ArrayList<String>> matrix = this.addPartialSolution(this.binaryMatrix);

		// Execute algorithm X.
		this.algX(matrix, this.partialSolution);

		return this.solutionFound;
	} // end of solve()

	public ArrayList<ArrayList<String>> addPartialSolution(ArrayList<ArrayList<String>> matrix) {

		
		//TODO issue is matrix and matrixCopy - passing a version of the matrix to itself.
		//this is written like a recursive method but it isn't. 
		
		//What if this method takes a matrix and a matrixRow to process, and the looping is done outside the method?
		//Or, we could perform the matrix manipulation on the global binary matrix instead?
		
		// Store row/col indexs to delete
		ArrayList<Integer> colIndexToRemove = new ArrayList<Integer>();
		ArrayList<Integer> rowIndexToRemove = new ArrayList<Integer>();
		// Store rows in matrix to visit which have solved sudoku cells.
		ArrayList<Integer> matrixRowToProcess = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> matrixCopy = new ArrayList<ArrayList<String>>();

		// Create a list of matrix rows to go through (0 indexed).
		for (int i = 0; i < this.gridSize; i++) {
			// For each column in sudoku grid.
			for (int j = 0; j < this.gridSize; j++) {
				// Partial solution cell found.
				if (this.grid.getCoord(i, j) != 0) {
					// System.out.println( "Row: " + (i+1) + " Col: " + (j+1) +" sudoku value: " +
					// this.grid.getCoord(i, j));
					int index = this.getMatrixIndex(i, j, this.grid.getCoord(i, j) - 1);
					// System.out.println("Matrix ind: " + index);
					if (!matrixRowToProcess.contains(index)) {
						matrixRowToProcess.add(index);
					}
				}
			}
		}

		for (int r = 0; r < matrixRowToProcess.size(); r++) {
			int matrixIndex = matrixRowToProcess.get(r);
			// Include row r in the partial solution (key is located at index 0).
			this.partialSolution.add(matrix.get(matrixIndex).get(0));
			// For each column j such that A[r][j] = 1,
			for (int j = 0; j < matrix.get(matrixIndex).size(); j++) {
				if (matrix.get(matrixIndex).get(j).equals("1")) {
					// for each row i such that A[i][j] = 1
					// Go through each row in the matrix, if the value at column i = 1, stage for
					// delete.
					for (int i = 0; i < matrix.size(); i++) {
						if (matrix.get(i).get(j).equals("1")) {
							// stage row for deletion
							if (!rowIndexToRemove.contains(i)) {
								rowIndexToRemove.add(i);
							}
						}
					}
					// stage the col for deletion.
					if (!colIndexToRemove.contains(j)) {
						colIndexToRemove.add(j);
					}
				}
			}
			System.out.println(rowIndexToRemove);
			System.out.println(colIndexToRemove);
			
			// Create new matrix here minus specified rows
			for (int i = 0; i < matrix.size(); i++) {
				if (!rowIndexToRemove.contains(i)) {
					matrixCopy.add(new ArrayList<String>(matrix.get(i)));
				}
			}

			// Sort columns to remove in reverse order so as not to mess up indexing.
			Collections.sort(colIndexToRemove, Collections.reverseOrder());

			// Remove columns from new array
			for (int i = 0; i < colIndexToRemove.size(); i++) {
				for (int k = 0; k < matrixCopy.size(); k++) {
					matrixCopy.get(k).remove(colIndexToRemove.get(i).intValue());
				}
			}

			// Print Matrix for testing
//			StringBuilder builder = new StringBuilder("");
//			for (int i = 0; i < matrixCopy.size(); i++) {
//				for (int j = 0; j < matrixCopy.get(i).size(); j++) {
//					builder.append(matrixCopy.get(i).get(j) + " ");
//				}
//				builder.append("\n");
//			}
//			//System.out.println(builder);

			// Clear row/column removal arrays.
			colIndexToRemove.clear();
			rowIndexToRemove.clear();

		}
		return matrixCopy;
	}

	public void algX(ArrayList<ArrayList<String>> matrix, ArrayList<String> solution) {

		// If the matrix A has no rows, the current partial solution
		// is a valid solution; terminate successfully.
		if (matrix.isEmpty()) {
			if (!this.solutionFound) {
				this.solutionFound(solution);
			}
			return;
		} else {

			// Otherwise, choose a column c (deterministically).
			int columnChoice = 1;

			// Store row/col indexs to delete
			ArrayList<Integer> colIndexToRemove = new ArrayList<Integer>();
			ArrayList<Integer> rowIndexToRemove = new ArrayList<Integer>();

			for (int r = 0; r < matrix.size(); r++) {
				// Choose a row r such that A[r] = 1 (nondeterministically).
				if (matrix.get(r).get(columnChoice).equals("1")) {
					// Include row r in the partial solution (key is located at index 0).
					solution.add(matrix.get(r).get(0));
					// For each column j such that A[r][j] = 1,
					for (int j = 0; j < matrix.get(r).size(); j++) {
						if (matrix.get(r).get(j).equals("1")) {
							// for each row i such that A[i][j] = 1
							// Go through each row in the matrix, if the value at column i = 1, stage for
							// delete.
							for (int i = 0; i < matrix.size(); i++) {
								if (matrix.get(i).get(j).equals("1")) {
									// stage row for deletion
									if (!rowIndexToRemove.contains(i)) {
										rowIndexToRemove.add(i);
									}
								}
							}
							// stage the col for deletion.
							if (!colIndexToRemove.contains(j)) {
								colIndexToRemove.add(j);
							}
						}
					}

					// Create new matrix here minus specified rows
					ArrayList<ArrayList<String>> matrixCopy = new ArrayList<ArrayList<String>>();
					for (int i = 0; i < matrix.size(); i++) {
						if (!rowIndexToRemove.contains(i)) {
							matrixCopy.add(new ArrayList<String>(matrix.get(i)));
						}
					}

					// Sort columns to remove in reverse order so as not to mess up indexing.
					Collections.sort(colIndexToRemove, Collections.reverseOrder());

					// Remove columns from new array
					for (int i = 0; i < colIndexToRemove.size(); i++) {
						for (int k = 0; k < matrixCopy.size(); k++) {
							matrixCopy.get(k).remove(colIndexToRemove.get(i).intValue());
						}
					}

					// Clear row/column removal arrays.
					colIndexToRemove.clear();
					rowIndexToRemove.clear();

					// Print Matrix for testing
					StringBuilder builder2 = new StringBuilder("");
					for (int i = 0; i < matrixCopy.size(); i++) {
						for (int j = 0; j < matrixCopy.get(i).size(); j++) {
							builder2.append(matrixCopy.get(i).get(j) + " ");
						}
						builder2.append("\n");
					}
					// System.out.println(builder);
					// System.out.println(solution.size());
					// System.out.println(solution);

					// Reccur
					algX(matrixCopy, solution);

					if (this.solutionFound == true) {
						break;
					} else {
						solution.remove(solution.size() - 1);
					}
				}
			}
		}
	}

	public void solutionFound(ArrayList<String> solution) {
		this.solutionFound = true;
		for (String i : solution) {
			String[] cell = i.split(",");
			// Method takes order value, Row, Column.
			int value = Integer.parseInt(cell[2]);
			int row = Integer.parseInt(cell[0]) - 1;
			int column = Integer.parseInt(cell[1]) - 1;
			this.grid.setCoord(value, row, column);
		}
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
} // end of class AlgorXSolver
