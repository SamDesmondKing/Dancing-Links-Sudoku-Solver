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
	
	private int[][] binaryMatrix;

    public AlgorXSolver() {
       
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        
    	this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
		
		int matrixRows = (int) Math.pow(this.gridSize, 3);
		int matrixCols = (int) Math.pow(this.gridSize, 2) * 4;
		
		//Initialize the exact cover binary matrix. 
		this.binaryMatrix = new int[matrixRows][matrixCols];
		
		//Cell constraints
		int index = 0;
		int count = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < gridSize * gridSize; j++) {
				if (j == index) {
					binaryMatrix[i][j] = 1;
					count++;
					if (count == gridSize) {
						index++;
						count = 0;
						break;
					}
				}
			}
		}
		
		//Row Constraints
		index = gridSize * gridSize;
		count = 0;
		int iteration = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = index; j < index * 2; j++) {
				if (j == index) {
					binaryMatrix[i][j] = 1;
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
		
		//Column Constraints
		index = gridSize * gridSize * 2;
		count = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = index; j < gridSize * gridSize * 3; j++) {
				if (j == index) {
					binaryMatrix[i][j] = 1;
					index++;
					count++;
					break;
				}
			}
			System.out.println(count);
			if (count == gridSize * gridSize) {
				index = gridSize * gridSize * 2;
				count = 0;
			}
		}
		
		
		
		
		//Box Constraints
		index = gridSize * gridSize * 3;
		count = 0;
		
		
		
		
		
		
		
		
		
		
		
		
		String returnString = "";
		int h = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < matrixCols; j++) {
				returnString += binaryMatrix[i][j] + " ";
			}
			returnString += "\n";
			h++;
			if (h == 4) {
				//returnString += "\n";
				h = 0;
			}
		}
		System.out.println(returnString);
		
		
		
		
		
		
		
    	

        // placeholder
        return false;
    } // end of solve()

} // end of class AlgorXSolver
