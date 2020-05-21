/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
	
	private StdSudokuGrid grid;
	private ArrayList<Integer> values;
	private int gridSize;

    public AlgorXSolver() {
       
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        
    	this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
    	

        // placeholder
        return false;
    } // end of solve()

} // end of class AlgorXSolver
