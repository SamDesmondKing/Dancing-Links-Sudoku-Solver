package dancingLinks;

public class Node {
	
	private Node left;
	private Node right;
	private Node up;
	private Node down;
	private HeaderNode head;
	
	public Node() {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
	}
	
	public Node(HeaderNode columnHead) {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
		this.head = columnHead; 
	}
	
	/*
	 * Takes node and links to the bottom of this node.
	 * used in full structure creation.
	 */
	public Node linkBottom(Node node) {
		node.down = down;
	    node.down.up = node;
	    node.up = this;
	    down = node;
	    return node;
	}
	
	/*
	 * Takes node and links to the left of this node.
	 * used in full structure creation.
	 */
	public Node linkLeft(Node node) {
		node.left = left;
		node.left.right = node;
		node.right = this;
		left = node;
		return node;
	}
	
	/*
	 * Takes node and links to the right of this node.
	 * used in full structure creation.
	 */
	public Node linkRight(Node node) {
		node.right = right;
	    node.right.left = node;
	    node.left = this;
	    right = node;
	    return node;
	} 
	
	/*
	 * Removes this node by stitching together left and right nodes. 
	 */
	public void removeLeftRight() {
		left.right = right;
	    right.left = left;
	}
	
	/*
	 * Inserts this node and updates nodes to left and right to point here.
	 */
	public void insertLeftRight() {
		left.right = this;
	    right.left = this;
	}
	
	/*
	 * Removes this node by stitching together up and down nodes.
	 */
	public void removeUpDown() {
		up.down = down;
		down.up = up;
	}
	
	/*
	 * Inserts this node and updates up and down nodes to point here.
	 */
	public void insertUpDown() {
		up.down = this;
	    down.up = this;
	}
	
	/*
	 * --- Getters & Setters ---
	 */
	public void setHeader(HeaderNode head) {
		this.head = head;
	}
	
	public Node getLeft() {
		return this.left;
	}
	
	public Node getRight() {
		return this.right;
	}
	
	public Node getUp() {
		return this.up;
	}
	
	public Node getDown() {
		return this.down;
	}
	
	public HeaderNode getHead() {
		return this.head;
	}
}