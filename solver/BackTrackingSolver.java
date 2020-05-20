/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.LinkedList;

import grid.StdSudokuGrid;
import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
	StdSudokuGrid grid;
	ArrayList<Integer> values;
	int valueIndex;
	int gridSize;
	int numCells;
	boolean complete;

	boolean[] visited;
	int count;

	int[] lastSuccessfulCell;
	int cellIndex;

	public BackTrackingSolver() {

		this.valueIndex = 0;
		this.complete = false;
		this.cellIndex = 0;

	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		// Convert grid to StdGrid as this class only ever solves Std grids.
		this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
		this.numCells = this.gridSize * this.gridSize;

		// Holds last successfull co-ordinate, and the index of the value placed there.
		this.lastSuccessfulCell = new int[] { 0, 0, 0 };
		// this.solveNext(0,0,0);

		// TODO we need to enumerate the co-ordinates of every blank cell to visit, then
		// go through those co-ordinates one by one
		// Instead of only changing blank co-ordinates in solveNext().

		/*
		 * global var lastSuccessfulCellIndex[][]
		 * 
		 * sudokuSolver(cell) { if (isCellValid(cell)) { lasSuccessfulCellIndex = cell
		 * foreach (cell c : cellsRemaining) { sudokuSolver(c); } } else {
		 * 
		 * sudokuSolver(lastSuccessfulCellIndex); }
		 */

		this.recursiveSolver();

		return true;
	} // end of solve()

	public boolean recursiveSolver() {

		// For each row
		for (int i = 0; i < this.gridSize; i++) {
			// For each column
			for (int j = 0; j < this.gridSize; j++) {
				// If square is unassigned (0)
				if (this.grid.getCoord(i, j) == 0) {
					// Check every possible value
					for (int k = 0; k < this.values.size(); k++) {
						// If a value is valid
						if (this.grid.validCell(i, j, this.values.get(k))) {
							this.grid.setCoord(this.values.get(k), i, j);
							// Recursive call in an if statement to allow us to backtrack up through it.
							// If any future recursion returns false it will be caught here.
							if (this.recursiveSolver()) {
								// If this method returns true, recursion is complete.
								return true;
							} else {
								// Bad value found, reset and try a new value.
								this.grid.setCoord(0, i, j);
							}
						} 
					}
					// No available value - backtrack.
					return false;
				}
			}
		}
		return true;
	}

} // end of class BackTrackingSolver()
