/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;

import dancingLinks.DancingLinks;
import dancingLinks.HeaderNode;
import dancingLinks.Node;
import grid.StdSudokuGrid;
import grid.SudokuGrid;

//TODO Note: this implimentation will allow us to keep track of the 
//number of nodes in each column, and select the one with the least nodes,
//unlike the BCM solver which chose the first column each time. 

/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver {
	
	private StdSudokuGrid grid;
	private boolean solutionFound;
	private DancingLinks dancingLinks;

	public DancingLinksSolver() {
		this.solutionFound = false;
    } // end of DancingLinksSolver()

	@Override
	public boolean solve(SudokuGrid grid) {

		// Convert grid to std grid.
		this.grid = (StdSudokuGrid) grid;

		// Use our alg X solver from the previous task to create BCM.
		AlgorXSolver algXSolver = new AlgorXSolver();
		ArrayList<ArrayList<String>> binaryMatrix = algXSolver.createCoverMatrix(this.grid);

		// Create the dancing links structure from BCM.
		this.dancingLinks = new DancingLinks();
		dancingLinks.build(binaryMatrix);
		HeaderNode masterNode = dancingLinks.getMasterNode();
		
		// Invoke algX on dancing links structure.
		ArrayList<String> solution = new ArrayList<String>();
		this.algX(masterNode, solution);

		return this.solutionFound;
	} // end of solve()


	public void algX(HeaderNode masterNode, ArrayList<String> solution) {
		//System.out.println("Master: " + masterNode.getLabel());
		//System.out.println("Check: " + ((HeaderNode) masterNode.getRight()).getLabel());
		//System.out.println("");
		
		//this.dancingLinks.print();
		//System.out.println("---------------------------------------------");
		//System.out.println(solution);
		
		if (masterNode.getRight().equals(masterNode)) {
			if (!this.solutionFound) {
				this.solutionFound(solution);
			}
		} else {
			//Choose a column c (deterministically)
			HeaderNode columnChoice = (HeaderNode) masterNode.getRight();
			columnChoice.cover();
			//For each node in the column
			Node colNode = columnChoice.getDown();
			while (colNode != columnChoice) {
				// Choose a row r such that A[r] = 1 (nondeterministically).
				// Include row r in the partial solution.
				solution.add(colNode.getValue());
				// For each column j such that A[r][j] = 1,
				Node rowNode = colNode.getRight();
				while (rowNode != colNode) {
					// Cover
					rowNode.getHead().cover();
					rowNode = rowNode.getRight();
				}

				//Reccur
				algX(masterNode, solution);
				
				if (this.solutionFound == true) {
					break;
				} else {
					solution.remove(solution.size() - 1);
					columnChoice = colNode.getHead();
			
					//Uncover
					rowNode = colNode.getRight();
					while (rowNode != colNode) {
						rowNode.getHead().uncover();
						rowNode = rowNode.getRight();
					}
					colNode = colNode.getDown();
				}
			}
			columnChoice.uncover();
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
	
	public void selectColumn() {
		
	}

} // end of class DancingLinksSolver
