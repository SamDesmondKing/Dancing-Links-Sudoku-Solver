package solver;

import java.util.ArrayList;

import dancingLinks.DancingLinks;
import dancingLinks.HeaderNode;
import dancingLinks.Node;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;


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
		this.algX(masterNode);

		return this.solutionFound;
	} // end of solve()

	/*
	 * Algorithm X for Killer DLX.
	 */
	public void algX(HeaderNode masterNode) {
		if (masterNode.getRight().equals(masterNode)) {
			if (!this.solutionFound) {
				this.solutionFound = true;
			}
		} else {
			//Choose a column c (deterministically).
			HeaderNode columnChoice = this.chooseColumn(masterNode);
			columnChoice.cover();
			//For each node in the column
			Node colNode = columnChoice.getDown();
			while (colNode != columnChoice) {
				// Choose a row r such that A[r] = 1 (nondeterministically).
				String[] splitString = colNode.getValue().split(",");
				int coord1 = Integer.parseInt(splitString[0]) - 1;
				int coord2 = Integer.parseInt(splitString[1]) - 1;
				int value = Integer.parseInt(splitString[2]);
				
				if (this.grid.isValidDancingCell(coord1, coord2, value)) {
					// Include row r in the partial solution.
					this.grid.setCoord(value, coord1, coord2);
				} else {
					//If invalid, break and try another value.
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
				algX(masterNode);
				if (this.solutionFound == true) {
					break;
				} else {
					//Reset cell value
					this.grid.setCoord(0, coord1, coord2);
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
} // end of class DancingLinksSolver

