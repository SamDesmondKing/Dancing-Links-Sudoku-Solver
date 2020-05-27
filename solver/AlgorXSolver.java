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
	
	private ArrayList<ArrayList<String>> binaryMatrix;

    public AlgorXSolver() {
       
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        
    	this.grid = (StdSudokuGrid) grid;
		this.gridSize = this.grid.getGridSize();
		this.values = this.grid.getValues();
		
		int matrixRows = (int) Math.pow(this.gridSize, 3);
		int matrixCols = (int) Math.pow(this.gridSize, 2) * 4;
		
		//Initialize and populate the exact cover binary matrix. 
		this.binaryMatrix = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < matrixRows; i++) {
			binaryMatrix.add(new ArrayList<String>());
		}
		
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < matrixCols; j++) {
				binaryMatrix.get(i).add("0");
			}
		}
		
		//Cell constraints
		int index = 0;
		int count = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < gridSize * gridSize; j++) {
				if (j == index) {
					binaryMatrix.get(i).set(j, "1");;
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
					binaryMatrix.get(i).set(j, "1");;
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
					binaryMatrix.get(i).set(j, "1");;
					index++;
					count++;
					break;
				}
			}
			if (count == gridSize * gridSize) {
				index = gridSize * gridSize * 2;
				count = 0;
			}
		}
		
		//Box Constraints
		int boxWidth = (int) Math.sqrt(gridSize);
		this.boxConstraints(0,0,boxWidth,0);
		
		
		
		
		StringBuilder builder = new StringBuilder("");
		int h = 0;
		for (int i = 0; i < matrixRows; i++) {
			for (int j = 0; j < matrixCols; j++) {
				builder.append(binaryMatrix.get(i).get(j) + " ");

			}
			builder.append("\n");
			h++;
			if (h == 4) {
				//returnString += "\n";
				h = 0;
			}
		}
		System.out.println(builder);
		
		
		
        // placeholder
        return false;
    } // end of solve()
    
    
    public void boxConstraints(int coord1, int coord2, int boxWidth, int boxNum) {

		int numBoxes = boxWidth * boxWidth;
		int matrixStartIndex = numBoxes * numBoxes * 3;
		int boxStartingIndex = boxNum * numBoxes;
		
		// Stopping condition
		if (coord1 == numBoxes) {
			// terminate quietly.
		} else {
			//System.out.println(coord1 + " " + coord2);
			// Get all box values.
			//System.out.println(boxStartingIndex);
			for (int i = coord1; i < coord1 + boxWidth; i++) {
				for (int j = coord2; j < coord2 + boxWidth; j++) {
					
					for (int h = 0; h < numBoxes; h++) {
						int matrixIndex = this.getMatrixIndex(i + 1, j + 1, h + 1);
						//System.out.println(matrixIndex);
						this.binaryMatrix.get(matrixIndex).set(matrixStartIndex + boxStartingIndex + h, "1");
					}
				}
			}
			boxNum++;

			// Adjust starting co-ordinates for next box.
			if (coord2 == numBoxes - boxWidth) {
				coord2 = 0;
				coord1 += boxWidth;
			} else {
				coord2 += boxWidth;
			}
			
			// Recur.
			boxConstraints(coord1, coord2, boxWidth, boxNum);
		}
	}
    
    //Takes sudoku coords/value and returns that position in the matrix.
    public int getMatrixIndex(int coord1, int coord2, int value) {
    	
    	return (coord1 - 1) * gridSize * gridSize 
    		      + (coord2 - 1) * gridSize + (value - 1);	
    }
    

} // end of class AlgorXSolver
