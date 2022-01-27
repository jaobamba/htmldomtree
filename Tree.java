package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
			/** COMPLETE THIS METHOD **/
		root = build2();
	}
	public TagNode build2(){
		String data = "";
		TagNode tagNode = null;
		if (sc.hasNextLine()){
			String line = sc.nextLine();
			if (line.charAt(0) == '<'){
				// tag element
				data = line.substring(1, line.length()-1);
				if (data.charAt(0) == '/'){
					return null;
				}
				tagNode = new TagNode(data.substring(0, data.length()), null, null);
				tagNode.firstChild = build2();
			}
			else{
				tagNode = new TagNode(line, null, null);
				tagNode.toString();
			}
			tagNode.sibling = build2();
			tagNode.toString();
		}
		return tagNode;
	}


	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replaceTag2(oldTag, newTag, root);
	}
	
	private void replaceTag2(String oldTag, String newTag, TagNode tagNode)
	{
		if (tagNode != null) {
			if (tagNode.tag.equals(oldTag))
				tagNode.tag = newTag;
			if (tagNode.firstChild != null)
				replaceTag2(oldTag, newTag, tagNode.firstChild);
			if (tagNode.sibling != null)
				replaceTag2(oldTag, newTag, tagNode.sibling);
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/

		TagNode current = new TagNode(null, null, null);
		TagNode temp;
	
		current = boldRow2(root);
		if (current == null) {
			return;
		}
		
		current = current.firstChild;
		for(int i = 1; i < row; i++) {
			current = current.sibling;
		} 
		
		for (temp = current.firstChild; temp != null; temp = temp.sibling) {
			temp.firstChild = new TagNode("b", temp.firstChild, null);
			
		}
	}
	
	private TagNode boldRow2(TagNode node) { 
		if (node == null) {
			return null; 
			}
		
		TagNode nodeT = null;
		String strT = node.tag;
		if(strT.equals("table")) { 
			nodeT = node; 
			return nodeT;
		} 
		if(nodeT == null) {
			nodeT = boldRow2(node.firstChild);
		}
		if(nodeT == null) { 
			nodeT = boldRow2(node.sibling);
		} 
		return nodeT;
	}
	
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if (root == null)
			return;
		else 
			while (this.cTag(tag, root))
				removeTag2(tag, root, root.firstChild);
	}
	
	private void removeTag2(String tag, TagNode pNode, TagNode node) {
		if (node == null || pNode == null){
			return;
		} else if (node.tag.equals(tag)){

			if (tag.equals("ul") || tag.equals("ol"))
				this.removeTag3(node.firstChild); 

			if (pNode.firstChild == node) {
				pNode.firstChild = node.firstChild;
				this.addLastSibling(node.firstChild, node.sibling);
			} else if (pNode.sibling == node) {
				this.addLastSibling(node.firstChild, node.sibling);
				pNode.sibling = node.firstChild;
			}

			return;
		}

		pNode = node;
		removeTag2(tag, pNode, node.firstChild);
		removeTag2(tag, pNode, node.sibling);
	}

	private void removeTag3(TagNode node) {
		if (node == null)
			return;
		else if (node.tag.compareTo("li") == 0)
			node.tag = "p";

		this.removeTag3(node.sibling);
	}
	
	private TagNode getLastSibling (TagNode node) {
		while (node.sibling != null)
			node = node.sibling;

		return node;
	}
	
	private void addLastSibling (TagNode node, TagNode newSibling) {
		node = this.getLastSibling(node);
		node.sibling = newSibling;
	}
	
	private boolean cTag(String tag, TagNode node) {
		if (node == null)
			return false;
		else if (node.tag.compareTo(tag) == 0)
			return true;

		return this.cTag(tag, node.firstChild) || this.cTag(tag, node.sibling);
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if (tag.equals("b") || tag.equals("em"))
		{
			this.root = this.addTag2(word, tag, this.root);
		}
	}
	
	private TagNode addTag2(String word, String tag, TagNode node)
	{
		if (node.sibling != null) {
			node.sibling = this.addTag2(word, tag, node.sibling);
		}
		if (node.firstChild != null) {
			node.firstChild = this.addTag2(word, tag, node.firstChild);
		}
		if (node.tag.contains(word))
		{
			TagNode newTag = new TagNode(tag, node, node.sibling);
			node.sibling = null;
			return newTag;
		}
		
		return node;
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
