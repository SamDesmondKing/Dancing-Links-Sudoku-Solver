package solver;

import java.util.ArrayList;

import dancingLinks.DancingLinks;
import dancingLinks.HeaderNode;
import dancingLinks.Node;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;

//TODO Note: this implementation will allow us to keep track of the 
//number of nodes in each column, and select the one with the least nodes,
//unlike the BCM solver which chose the first column each time. 

/**
 * Dancing links solver for standard Sudoku.
 */
public class KillerAdvancedSolver extends StdSudokuSolver {
	
	private KillerSudokuGrid grid;
	private boolean solutionFound;
	private DancingLinks dancingLinks;

	public KillerAdvancedSolver() {
		this.solutionFound = false;
    } // end of DancingLinksSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// Convert grid to killer grid.
		this.grid = (KillerSudokuGrid) grid;

		// Use our alg X solver from AlgorXSolver to create BCM.
		AlgorXSolver algXSolver = new AlgorXSolver();
		ArrayList<ArrayList<String>> binaryMatrix = algXSolver.createCoverMatrix(grid);

		// Create the dancing links structure from BCM.
		this.dancingLinks = new DancingLinks();
		dancingLinks.build(binaryMatrix);
		HeaderNode masterNode = dancingLinks.getMasterNode();
		
		// Invoke algX on dancing links structure.
		ArrayList<String> solution = new ArrayList<String>();
		this.algX(masterNode, solution);

		return this.solutionFound;
	} // end of solve()

	/*
	 * Algorithm X for Killer DLX.
	 */
	public void algX(HeaderNode masterNode, ArrayList<String> solution) {
		if (masterNode.getRight().equals(masterNode)) {
			if (!this.solutionFound) {
				this.solutionFound(solution);
			}
		} else {
			//Choose a column c (deterministically).
			HeaderNode columnChoice = this.chooseColumn(masterNode);
			columnChoice.cover();
			//For each node in the column
			Node colNode = columnChoice.getDown();
			while (colNode != columnChoice) {
				// Choose a row r such that A[r] = 1 (nondeterministically).
				// Include row r in the partial solution.
				
				//Validate solution
				solution.add(colNode.getValue());
				if (!this.checkSolution(solution)) {
					//Try different value if this one won't work.
					solution.remove(solution.size() - 1);
					colNode = colNode.getDown();
					continue;
				} 
				
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
	
	/*
	 * Find the HeaderNode with the least number of Nodes,
	 * equivalent to finding the column with the least number
	 * of '1's as per Knuth's suggested column choice heuristic. 
	 */
	public HeaderNode chooseColumn(HeaderNode masterNode) {
		HeaderNode choice = null;
		int min = 0;
		
		HeaderNode nextNode = (HeaderNode) masterNode.getRight();
		while (!nextNode.equals(masterNode)) {
			if (min == 0) {
				min =  nextNode.getSize();
				choice = nextNode;
			} else if (nextNode.getSize() < min) {
				min = nextNode.getSize();
				choice = nextNode;
			}
			nextNode = (HeaderNode) nextNode.getRight();
		}
		return choice;
	}
	
	/*
	 * Checks solution to ensure complete cages are correct
	 */
	public boolean checkSolution(ArrayList<String> solution) {
		
		//isValidCell(int coord1, int coord2, int newValue) 
		boolean result = true;
		
		//Fill the grid
		for (String i : solution) {
			String[] splitString = i.split(",");
			int coord1 = Integer.parseInt(splitString[0]) - 1;
			int coord2 = Integer.parseInt(splitString[1]) - 1;
			int value = Integer.parseInt(splitString[2]);
			
			if (this.grid.isValidDancingCell(coord1, coord2, value)) {
				//place it
				this.grid.setCoord(value, coord1, coord2);
			} else {
				result = false;
				break;
			}
		}
		
		//Clear the grid.
		this.grid.clearGrid();

		return result;
	}
	
	/*
	 * Called when alg X finds a solution.
	 * Allows us to break out of recursion early. 
	 */
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

} // end of class DancingLinksSolver

