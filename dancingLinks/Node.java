package dancingLinks;

public class Node {
	
	private Node left;
	private Node right;
	private Node up;
	private Node down;
	private HeaderNode head;
	private String value;
	
	public Node() {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
		this.value = "header";
	}
	
	public Node(HeaderNode columnHead, String value) {
		this.left = this;
		this.right = this;
		this.up = this;
		this.down = this;
		this.head = columnHead; 
		this.value = value;
	}
	
	/*
	 * Takes node and links to the bottom of this node.
	 * Ensures circularity (Nodes created without arguments 
	 * have all directions pointing to itself).
	 * Used in full structure creation.
	 */
	public Node linkBottom(Node node) {
		node.down = this.down;
	    node.down.up = node;
	    node.up = this;
	    this.down = node;
	    return node;
	}
	
	/*
	 * Takes node and links it to the left of this node.
	 * Ensures circularity (Nodes created without arguments 
	 * have all directions pointing to itself).
	 * Used in full structure creation.
	 */
	public Node linkLeft(Node node) {
		node.left = this.left;
		node.left.right = node;
		node.right = this;
		this.left = node;
		return node;
	}
	
	/*
	 * Takes node and links it to the right of this node.
	 * Ensures circularity (Nodes created without arguments 
	 * have all directions pointing to itself).
	 * Used in full structure creation.
	 * 
	 */
	public Node linkRight(Node node) {
		node.right = this.right;
	    node.right.left = node;
	    node.left = this;
	    this.right = node;
	    return node;
	} 
	
	/*
	 * Removes this node by stitching together left and right nodes.
	 * used when covering col/rows. 
	 */
	public void removeLeftRight() {
		this.left.right = this.right;
	    this.right.left = this.left;
	}
	
	/*
	 * Inserts this node and updates nodes to left and right to point here.
	 * used when uncovering col/rows.
	 */
	public void insertLeftRight() {
		this.left.right = this;
	    this.right.left = this;
	}
	
	/*
	 * Removes this node by stitching together up and down nodes.
	 * used when covering col/rows.
	 */
	public void removeUpDown() {
		this.up.down = this.down;
		this.down.up = this.up;
	}
	
	/*
	 * Inserts this node and updates up and down nodes to point here.
	 * used when uncovering col/rows.
	 */
	public void insertUpDown() {
		this.up.down = this;
	    this.down.up = this;
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
	
	public String getValue() {
		return this.value;
	}
}