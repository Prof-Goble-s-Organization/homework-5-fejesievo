package hw5;

/**
 * Linked implementation of a binary search tree. The binary search tree
 * inherits the methods from the binary tree. The add and remove methods must
 * must be overridden so that they maintain the BST Property. The contains, get
 * and set methods are overridden to improve their performance by taking
 * advantage of the BST property. The inherited size and traversal methods work
 * well as they are.
 * 
 * @author William Goble
 * @author Dickinson College
 *
 */
public class COMP232LinkedBinarySearchTree<K extends Comparable<K>, V> extends COMP232LinkedBinaryTree<K, V> {

	/*
	 * NOTE: We inherit the size and root fields, and the BTNode class from the
	 * LinkedBinaryTree class because they were declared as protected in that
	 * class.
	 */

	/**
	 * Construct an empty binary search tree.
	 */
	public COMP232LinkedBinarySearchTree() {
		super();
	}

	/**
	 * Construct a binary search tree with a single node at the root.
	 * 
	 * @param key
	 *            the key for the root.
	 * @param value
	 *            the value for the root.
	 */
	public COMP232LinkedBinarySearchTree(K key, V value) {
		super(key, value);
	}

	/**
	 * Construct a binary search tree using the provided keys and values. The
	 * key,value pairs are inserted into the tree in level order. If the
	 * resulting tree does not satisfy the BST Property, then an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param keys
	 *            the keys.
	 * @param values
	 *            the values.
	 * @throws IllegalArgumentException
	 *             if the keys and values do not have the same length, the keys
	 *             or the values are empty, or the keys are not specified in an
	 *             order that results in a valid binary search tree.
	 */
	public COMP232LinkedBinarySearchTree(K[] keys, V[] values) {
		super(keys, values);

		if (!checkBSTProperty()) {
			throw new IllegalArgumentException(
					"Key, Value pairs did not satisfy BST property.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(K key) {
		return containsHelper(root, key);
	}

	/*
	 * Recursive helper method that checks if the key can be found in the
	 * subtree rooted at subTreeRoot.
	 */
	private boolean containsHelper(BTNode<K, V> subTreeRoot, K key) {
		if (subTreeRoot == null) {
			return false; // off the tree.
		} else if (subTreeRoot.key.equals(key)) {
			return true; // found it.
		} else if (key.compareTo(subTreeRoot.key) < 0) {
			/*
			 * The key we are looking for is less than the key at the
			 * subTreeRoot so if it is in the tree it will be in the left
			 * subtree.
			 */
			return containsHelper(subTreeRoot.left, key);
		} else {
			/*
			 * The key we are looking for is greater than or equal to the key at
			 * the subTreeRoot so if it is in the tree it will be in the right
			 * subtree.
			 */
			return containsHelper(subTreeRoot.right, key);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public V get(K key) {
		// Intentionally not implemented - see homework assignment.
		return getNode(root, key);
	}

	private V getNode(BTNode<K,V> subTreeRoot, K key) {
		if (subTreeRoot == null){
			return null;
		} else if (subTreeRoot.key.equals(key)){
			return subTreeRoot.value;
		} else if (key.compareTo(subTreeRoot.key) < 0) {
			return getNode(subTreeRoot.left, key);
		} else {
			return getNode(subTreeRoot.right, key);
		}
	}

	/**
	 * {@inheritDoc}
	 * @return 
	 * @return 
	 */
	public void set(K key, V value) {
		BTNode<K, V> current = root;
	    BTNode<K, V> parent = null;

	    while (current != null) {
	        int cmp = key.compareTo(current.key);
	        if (cmp == 0) {
	            V oldValue = current.value;
	            current.value = value;
	            return; 
	        }
	        parent = current;
	        if (cmp < 0) {
	            current = current.left;
	        } else {
	            current = current.right;
	        }
	    }

	    BTNode<K, V> newNode = new BTNode<>(key, value);
	    if (parent == null) {
	        
	        root = newNode;
	    } else if (key.compareTo(parent.key) < 0) {
	        parent.left = newNode;
	    } else {
	        parent.right = newNode;
	    }
	    newNode.parent = parent;
	    size++; 

	    return; 
	}
	
	
	


	/**
	 * {@inheritDoc}
	 */
	public void add(K key, V value) {
		BTNode<K, V> nodeToAdd = new BTNode<K, V>(key, value);

		if (size == 0) { // tree is empty!
			root = nodeToAdd;
		} else {
			addNodeToSubTree(root, nodeToAdd);
		}
		size++;
	}

	/*
	 * Add the nodeToAdd to the subtree rooted at subTreeRoot.
	 */
	private void addNodeToSubTree(BTNode<K, V> subTreeRoot,
			BTNode<K, V> nodeToAdd) {
		if (nodeToAdd.key.compareTo(subTreeRoot.key) < 0) {
			/*
			 * Key of the new node is less than the key at subTreeRoot so we are
			 * going to add the new node into the left sub tree.
			 */
			if (subTreeRoot.left == null) {
				/*
				 * The left subtree is empty, so make the new node the left
				 * child of the subtree root.
				 */
				subTreeRoot.left = nodeToAdd;
				nodeToAdd.parent = subTreeRoot;
			} else {
				/*
				 * The left subtree is not empty, so add the new node to that
				 * sub tree.
				 */
				addNodeToSubTree(subTreeRoot.left, nodeToAdd);
			}
		} else {
			/*
			 * The key of the new node is greater than or equal to the key at
			 * the subTreeRoot so we are going to add the new node to the right
			 * sub tree. This is exactly the complement of the approach used
			 * above.
			 */
			if (subTreeRoot.right == null) {
				subTreeRoot.right = nodeToAdd;
				nodeToAdd.parent = subTreeRoot;
			} else {
				addNodeToSubTree(subTreeRoot.right, nodeToAdd);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public V remove(K key) {
	    BTNode<K, V> current = root;
	    BTNode<K, V> parent = null;

	    // to find node to remove
	    while (current != null) {
	        int cmp = key.compareTo(current.key);
	        if (cmp == 0) {
	            
	            V oldValue = current.value;

	            // for no children
	            if (current.left == null && current.right == null) {
	                if (parent == null) {
	                    root = null;
	                } else if (parent.left == current) {
	                    parent.left = null; 
	                } else {
	                    parent.right = null; 
	                }
	            }
	            //  for 1 child
	            else if (current.left == null || current.right == null) {
	                BTNode<K, V> child = (current.left != null) ? current.left : current.right;

	                if (parent == null) {
	                    root = child; 
	                } else if (parent.left == current) {
	                    parent.left = child; 
	                } else {
	                    parent.right = child; 
	                }
	            }
	            // for 2 children
	            else {
	                
	                BTNode<K, V> successor = getMinNode(current.right);
	               
	                current.key = successor.key;
	                current.value = successor.value;

	               
	                remove(successor.key);
	            }

	            size--; 
	            return oldValue; 
	        } else {
	            parent = current;
	            current = (cmp < 0) ? current.left : current.right;
	        }
	    }
	    
	    return null; 
	}

	/**
	 * Helper method to find the node with the smallest key in the subtree.
	 */
	private BTNode<K, V> getMinNode(BTNode<K, V> subTreeRoot) {
	    if (subTreeRoot == null) {
	        return null;
	    }
	    while (subTreeRoot.left != null) {
	        subTreeRoot = subTreeRoot.left; 
	    }
	    return subTreeRoot; 
	}
	/*
	 * Helper method that verifies the BST property of this tree by traversing
	 * the tree and verifying that at each node the key of the left child is <
	 * the key of the node and that the key of the right child is >= the key of
	 * the node.  This is used by the 
	 */
	boolean checkBSTProperty() {
		return checkBSTPropertyHelper(root);
	}

	private boolean checkBSTPropertyHelper(BTNode<K, V> subTreeRoot) {
		if (subTreeRoot == null) {
			return true; // off tree.
		} else {
			if (leftChildOK(subTreeRoot) && rightChildOK(subTreeRoot)) {
				return checkBSTPropertyHelper(subTreeRoot.left)
						&& checkBSTPropertyHelper(subTreeRoot.right);
			} else {
				return false;
			}
		}
	}

	private boolean leftChildOK(BTNode<K, V> node) {
		if (node.left == null) {
			return true;
		} else {
			// true if key at node is > key at left child.
			return node.key.compareTo(node.left.key) > 0;
		}
	}

	private boolean rightChildOK(BTNode<K, V> node) {
		if (node.right == null) {
			return true;
		} else {
			// true if key at node is <= key at right child.
			return node.key.compareTo(node.right.key) <= 0;
		}
	}
}

