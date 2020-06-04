package dancingLinks;

public class HeaderNode extends Node {

	private int size;
	private String label;
	
	public HeaderNode(String label) {
		super();
		super.setHeader(this);
		this.label = label;
		this.size = 0;
	}

	/*
	 * 'Covers' the column and associated rows from data structure.
	 *  easy to undo as covered nodes still hold pointers relating to their
	 *  original position - as outlined in Knuth's Dancing Links paper (2000).
	 *  Note that uncovering must be done in 'precisely the reverse order' for this to work. 
	 */
	public void cover() {
		//Cover column header (therefore covering entire column).
		removeLeftRight();
		//For each node in this column
		for (Node i = this.getDown(); i != this; i = i.getDown()) {
			//For each node in the row (able to iterate through due to circularity of list)
			for (Node j = i.getRight(); j != i; j = j.getRight()) {
				//Cover row node
				j.removeUpDown();
				//Decrement size tracker of column that this row node was in.
				j.getHead().decrementSize();
			}
		}
	}
	
	/*
	 * Undoes the cover method as outlined above.
	 */
	public void uncover() {
		for (Node i = this.getUp(); i != this; i = i.getUp()) {
			for (Node j = i.getLeft(); j != i; j = j.getLeft()) {
				//Take advantage of covered node's existing pointers to insert.
				j.insertUpDown();
				//Increment size of column in which node is inserted.
				j.getHead().incrementSize();
			}
		}
		//Done last to maintain strict reverse order.
		insertLeftRight();
	}
	
	/*
	 *  --- Getters & Setters --
	 */
	public void decrementSize() {
		this.size -= 1;
	}
	
	public void incrementSize() {
		this.size += 1;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public String getLabel() {
		return this.label;
	}
}
