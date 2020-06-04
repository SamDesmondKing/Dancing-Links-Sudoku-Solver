package dancingLinks;

import java.util.ArrayList;

public class DancingLinks {

	private HeaderNode masterNode;
	ArrayList<HeaderNode> columns;

	public DancingLinks() {
	}

	/*
	 * Create the dancing links data structure from the BCM and save the master node
	 * as a private member variable of this class so that we can access the data
	 * structure from outside.
	 */
	public void build(ArrayList<ArrayList<String>> matrix) {

		// Create master node
		this.masterNode = new HeaderNode("master");

		// Minus one because the keys are stored in the matrix as a column.
		int numCols = matrix.get(0).size() - 1;

		// Build the columns
		columns = new ArrayList<HeaderNode>();
		for (int i = 0; i < numCols; i++) {
			HeaderNode headerNode = new HeaderNode(Integer.toString(i));
			columns.add(headerNode);
			this.masterNode = (HeaderNode) this.masterNode.linkRight(headerNode);

		}
		// Circle back around to master node.
		// (Will have been maintained as the 'right' node of the rightmost column)
		this.masterNode = (HeaderNode) this.masterNode.getRight();
		
		// Build rows
		// For each row in the matrix
		for (int i = 0; i < matrix.size(); i++) {
			Node previousNode = null;
			// For each column
			for (int j = 1; j <= numCols; j++) {
				// If we get a hit
				if (matrix.get(i).get(j) == "1") {
					// Get HeaderNode from column array
					HeaderNode columnHead = columns.get(j - 1);
					// Get cell value from first column of matrix
					String value = matrix.get(i).get(0);
					Node newNode = new Node(columnHead, value);

					// If it's the first hit in the column
					if (previousNode == null) {
						previousNode = newNode;
					}
					
					//Link the new node into the structure.
					columnHead.incrementSize();
					columnHead.getUp().linkBottom(newNode);
					previousNode = previousNode.linkRight(newNode);
				}
			}
		}
		this.masterNode.setSize(numCols);
	}
	
	/*
	 * Returns master node to provide entry to this data structure.
	 */
	public HeaderNode getMasterNode() {
		return this.masterNode;
	}
	
	/*
	 * Print struct for testing.
	 */
	public void print() {
			//Print for testing.
			StringBuilder b = new StringBuilder();
			//Print for testing.
			for (HeaderNode j = (HeaderNode) this.masterNode.getRight(); j != this.masterNode; j = (HeaderNode) j.getRight()) {
				System.out.println(j.getValue());
				Node temp = j.getDown();
				while (temp != j) {
					System.out.println(temp.getValue());
					temp = temp.getDown();
				}
				System.out.println("");
			}
	}
}
