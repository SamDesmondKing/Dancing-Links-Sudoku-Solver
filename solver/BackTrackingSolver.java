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

	public BackTrackingSolver() {

		this.valueIndex = 0;
		this.complete = false;

	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		// Convert grid to StdGrid as this class only ever solves Std grids.
		this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
		this.numCells = this.gridSize * this.gridSize;

		//Holds last successfull co-ordinate, and the index of the value placed there.
		//TODO right now this will only go back up one level. Needs to be able to go all the way.
		this.lastSuccessfulCell = new int[] { 0, 0, 0 };

		//this.solveNext(0,0,0);
		
//		while (!complete) {
//			System.out.println(this.lastSuccessfulCell[0] + " " + this.lastSuccessfulCell[1] + " " + this.lastSuccessfulCell[2]);
//			solveNext(this.lastSuccessfulCell[0], this.lastSuccessfulCell[1], this.lastSuccessfulCell[2]);
//		}
		
		solveNext(this.lastSuccessfulCell[0], this.lastSuccessfulCell[1], this.lastSuccessfulCell[2]);
		
		// placeholder
		return false;
	} // end of solve()

	public void solveNext(int coord1, int coord2, int valueIndex) {
		
		if (coord1 == this.gridSize) {
			this.complete = true;
			return;
		}
		
		while (valueIndex < this.values.size()) {
			
			//If cell is blank, set to value(i)
			if (this.grid.getCoord(coord1, coord2) == 0) {
				this.grid.setCoord(this.values.get(valueIndex), coord1, coord2);
				
				//Validate grid
				this.grid.validate();
				
				//If grid is valid, save as last successful cell and break. 
				if (this.grid.getValidity()) {
					this.lastSuccessfulCell[0] = coord1;
					this.lastSuccessfulCell[1] = coord2;
					this.lastSuccessfulCell[2] = valueIndex;
					break;
				} else {
					//If invalid, reset cell to 0.
					this.grid.setCoord(0, coord1, coord2);
				}
			}
			valueIndex++;
		}
		
		//If the cell is still blank, break to last successfull coordinate and try next value.
		if (this.grid.getCoord(coord1, coord2) == 0) {
			return;
			//solveNext(this.lastSuccessfulCell[0], this.lastSuccessfulCell[1], this.lastSuccessfulCell[2]);
			//System.out.println(this.lastSuccessfulCell[0] + " "+ this.lastSuccessfulCell[1] + " " + this.lastSuccessfulCell[2]);
		}
		
		//Else, determine next linear coordinate
		if (coord2 == this.gridSize - 1) {
			coord1++;
			coord2 = 0;
		} else {
			coord2++;
		}
		solveNext(coord1, coord2, 0);
	}
	

} // end of class BackTrackingSolver()
