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

		for (int i = 0; i < this.gridSize; i++) {
			for (int j = 0; j < this.gridSize; j++) {
				if (this.grid.getCoord(i,j) == 0) {
					for (int k = 1; k <= this.values.size(); k++) {
						if (this.grid.validCell(i, j, this.values.get(k-1))) {

							if (this.solve(this.grid)) {
								return true;
							} else {
								this.grid.setCoord(i,j,0);
							}
							
						}
					}
					return false;
				}
			}
		}
		return true;
	} // end of solve()

	public void solveNext(int coord1, int coord2, int valueIndex) {

	}

} // end of class BackTrackingSolver()
