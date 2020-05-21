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
	
	private StdSudokuGrid grid;
	private ArrayList<Integer> values;
	private int gridSize;

	public BackTrackingSolver() {
	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		// Convert grid to StdGrid as this class only ever solves Std grids.
		this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();

		boolean result = this.recursiveSolver();

		return result;
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
