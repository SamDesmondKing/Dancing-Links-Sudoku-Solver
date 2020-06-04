package dancingLinks;

import java.util.ArrayList;

public class DancingLinks {

	private HeaderNode masterNode;
	
	public DancingLinks(ArrayList<ArrayList<String>> matrix) {
		this.buildDancingLinks(matrix);
	}
	
	/*
	 * Create the dancing links data structure and save the 
	 * master node as a private member variable of this class
	 * so that we can access the data structure from outside. 
	 */
	public void buildDancingLinks(ArrayList<ArrayList<String>> matrix) {
	
		//Create master node
		this.masterNode = new HeaderNode("master");
		
		//Minus one because the keys are stored in the matrix as a column. 
		int numCols = matrix.get(0).size() - 1;
		
		//Build the columns
		ArrayList<HeaderNode> columns = new ArrayList<HeaderNode>();

		
		
		
	}
}
