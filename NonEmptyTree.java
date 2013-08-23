package searchTree;

import java.util.Collection;

/* Author: Alexander Bezobchuk */
/* Please do not reproduce     */

/**
 * This class represents a non-empty search tree. An instance of this class
 * should contain:
 * <ul>
 * <li>A key
 * <li>A value (that the key maps to)
 * <li>A reference to a left Tree that contains key:value pairs such that the
 * keys in the left Tree are less than the key stored in this tree node.
 * <li>A reference to a right Tree that contains key:value pairs such that the
 * keys in the right Tree are greater than the key stored in this tree node.
 * </ul>
 *  
 */
public class NonEmptyTree<K extends Comparable<K>, V> implements Tree<K, V> {

	K key;
	V value;
	public Tree<K,V> left, right;

	public NonEmptyTree(K key, V value, Tree<K,V> left, Tree<K,V> right){
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}

	public V search(K key) {
		if(key.compareTo(this.key) > 0){
			return this.right.search(key);
		}if(key.compareTo(this.key) < 0){
			return this.left.search(key);
		}
		return this.value; // The search key and the current Tree's key are the same.
	}

	public NonEmptyTree<K, V> insert(K key, V value) {
		// If the keys are the same (i.e. duplicates), the the value is just updated.
		if(key.compareTo(this.key) == 0){
			this.value = value;
		}if(key.compareTo(this.key) > 0){
			right = this.right.insert(key, value);
		}if(key.compareTo(this.key) < 0){
			left = this.left.insert(key, value);
		}
		// Insert will be recursively called until a Empty Tree is reached where a new Tree will be created.
		return this;
	}

	public Tree<K, V> delete(K key) {
		if(key.compareTo(this.key) > 0){
			right = right.delete(key);
		}if(key.compareTo(this.key) < 0){
			left = left.delete(key);
		}if(key.compareTo(this.key) == 0){
			try{
				// Find smallest right sub-tree key. Update the value and key of the replaced tree
				this.value = this.search(right.min());
				this.key = right.min();
				// Delete the duplicate
				right = right.delete(right.min());
			} catch(TreeIsEmptyException e){
				// In case if Tree is the root, else if it was a leaf it would not matter if left or right was returned
				return this.left;
			}
		}
		// If empty tree is encountered, that means the key was not found and becomes a no-op.
		return this;
	}

	public K max() {
		try{
			// The larger values of the tree are on right sub-trees
			// Keep traversing right tree until an EmptyTree is reached
			K tmp = this.right.max();
			return tmp;
		}catch(TreeIsEmptyException e){
			// Return the last key before exception thrown
			return key;
		}
	}

	public K min() {
		try{
			// The smaller values of the tree are on left sub-trees
			// Keep traversing left tree until an EmptyTree is reached
			K tmp = this.left.min();
			return tmp;
		}catch (TreeIsEmptyException e) {
			// Return the last key before exception thrown
			return this.key;
		}
	}

	public int size() {
		// In-Order Traversal
		// If EmptyTree is encountered then 0 is returned and we add 1 for NonEmptyTrees.
		return left.size() + 1 + right.size();
	}

	public void addKeysToCollection(Collection<K> c) {

		// Use In-Order traversal of BST in order to receive the keys in ascending order.
		this.left.addKeysToCollection(c);
		c.add(this.key);
		this.right.addKeysToCollection(c);
	}

	public Tree<K,V> subTree(K fromKey, K toKey) {

		if(key.compareTo(toKey) > 0){
			return this.left.subTree(fromKey, toKey);
		}if(key.compareTo(fromKey) < 0){
			return this.right.subTree(fromKey, toKey);
		}
		// Tree must be in range of fromKey and toKey, the Trees are recursively referenced to each other by a constructor
		return new NonEmptyTree<K,V>(this.key, this.value, left.subTree(fromKey, toKey), right.subTree(fromKey, toKey));
	}
}