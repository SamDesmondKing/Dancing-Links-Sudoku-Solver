/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


//TODO Note: this implimentation will allow us to keep track of the 
//number of nodes in each column, and select the one with the least nodes,
//unlike the BCM solver which chose the first column each time. 


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{

    private StdSudokuGrid grid;

	public DancingLinksSolver() {
       
    	
    } // end of DancingLinksSolver()

    @Override
    public boolean solve(SudokuGrid grid) {
       
    	//Convert grid to std grid.
    	this.grid = (StdSudokuGrid) grid;
    	
    	//Use our alg X solver from the previous task to create BCM.
    	AlgorXSolver algXSolver = new AlgorXSolver();
    	ArrayList<ArrayList<String>> binaryMatrix = algXSolver.createCoverMatrix(this.grid);
    	
    	//Create the dancing links structure from BCM.
    	this.createDancingLinks(binaryMatrix);
    	
    	//Invoke algX on dancing links structure.
    	this.algX();
    	
    	//Convert result list of nodes into sudoku grid for display.
    	
    	
        return false;
    } // end of solve()
    
    public void createDancingLinks(ArrayList<ArrayList<String>> binaryMatrix) {
    	
    	//TODO create the dancing links structure using nodes (also todo)
    	
    }
    
    public void algX() {
    	
    }

} // end of class DancingLinksSolver
