package adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TraversableQueue<T> implements IterableQueue<T> {

	private Node<T> first;
	int size = 0;

	public TraversableQueue() {
		this.first = null;
	}

	public int size() {
		return size;
	}

	/*
	 * (non-Javadoc) O(1)
	 * 
	 * @see adts.IterableQueue#enqueue(java.lang.Object)
	 */
	@Override
	public void enqueue(T element) throws IllegalStateException {
		// create a new node using the provided element
		Node<T> newFirst = new Node<T>(element);
		// if the list is empty, make new node the root node
		if (size == 0) {
			this.first = newFirst;
		} else {
			// if the list isn't empty, make the current root node the new
			// node's next node.
			newFirst.setNext(this.first);
			// make the new node the root node.
			this.first = newFirst;
		}
		size++;
	}

	/*
	 * (non-Javadoc) O(1)
	 * 
	 * @see adts.IterableQueue#dequeue()
	 */
	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (size == 0) {
			throw new IndexOutOfBoundsException();
		}
		// get the item stored at the root node.
		T item = this.first.getItem();
		// remove root node, by making the next node the new root node.
		this.first = this.first.getNext();
		size--;
		return item;
	}

	/**
	 * Remove first node found containing a matching element. Implemented
	 * specifically for use in the BoundedCube. O(n)
	 * 
	 * @param element
	 *            - element to be removed
	 * @return True if element found and removed, false otherwise.
	 */
	public boolean remove(T element) {
		// Check root node
		if (this.first == null) {
			return false;
		}
		//Check root node's element
		if (this.first.getItem().equals(element)) {
			if(this.first.getNext() == null){
				this.first = null;
			}else {
				this.first = this.first.getNext();
			}
			return true;
		}

		Node<T> spider = this.first;
		// Check remaining queued nodes
		while (true) {
			if (spider.getNext() == null)
				break;

			if (element.equals(spider.getNext().getItem())) {
				// remove element by removing all reference to its node. Garbage
				// collector will clean it up.
				spider.setNext(spider.getNext().getNext());
				--this.size;
				return true;
			} else {
				spider = spider.getNext();
			}
		}
		return false;
	}

	/**
	 * Private class representing the nodes that make up the linked list.
	 * 
	 * @author Angus Trusler
	 *
	 * @param <R>
	 *            - Typed parameter.
	 */
	private class Node<R> {
		// next node in the linked list
		private Node<R> next = null;
		// item stored at each node
		private R item;

		Node(R item) {
			this.item = item;
		}

		public Node<R> getNext() {
			return this.next;
		}

		public R getItem() {
			return this.item;
		}

		public void setNext(Node<R> next) {
			this.next = next;
		}
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {
			// variable node that will iterate over the linked list
			private Node<T> next = first;
			// int representing the count of remaining nodes in the linked list.
			private int remaining = size;

			public boolean hasNext() {
				return (remaining != 0);
			}

			public T next() {
				if (remaining == 0)
					// should never get here, but just in case.
					throw new NoSuchElementException();

				T item = next.getItem();
				next = next.getNext();
				--remaining;
				return item;
			}
		};
		return it;
	}
}
/*
 * Analysis and Justification of Design Choices for TraversableQueue
 * 
 * When designing the TraversableQueue, primary consideration was given to
 * memory usage over access time, as a constraint of 8GB of memory was imposed,
 * but no constraint on access time was provided in the spec. Additionally, with
 * only 20,000 potential aircraft occupying a queue in a worst case scenario,
 * being able to iterate over 20,000 elements with even a lower end processor
 * shouldn't take greater than 0.5 seconds.
 * 
 * Because memory is at a premium, efficient use of space was necessary. To
 * store n items in any structure requires at least O(n) n-sized memory
 * positions.
 * 
 * Initially, I attempted an array based implementation of a one dimensional
 * list. Naturally, this made access time O(1), as a given index correspond's to
 * its position of memory. However, as the primary method of access for the
 * TraversableQueue will be through the enqueue and dequeue, the access time
 * will only depend on its ability to access the *first* index, which can be
 * functionally different to its ability to access *any* index.
 * 
 * Moreover, memory management with this structure is more complex, especially
 * when considering what behavior the queue should display when the queue
 * completely filled. Does it refuse and throw an exception, or should it
 * attempt to resize, and by how much? At what point should it release memory
 * the queue isn't occupying? Worse case scenario:, a queue with 19,999
 * elements, with a size in memory of exactly 19,999 needing to add 1 more
 * element, would need to increase to a size of 20,000. To do this, it would
 * need to allocate a second array (of the new size), and copy into it from the
 * old array. This uses a total of 39,999 n-sized portions of memory. If the
 * size of a single n is large enough, to require access to nearly twice the
 * amount of available memory may not be practical. To require even 20,000 n
 * sized chunks of memory (that the queue may not necessarily be using), given
 * the other memory requirements of the overall program may not be practical.
 * Clearly then, a new design was required.
 * 
 * Instead of an array based structure, a linked-list structure may be
 * preferable, as no additional memory is required to add/remove elements, and
 * resizing is never required. The amount of memory required to store n elements
 * is therefore *always* exactly n. (As the size of pointers to neighboring
 * linked items is constant (and normally quite small), the size of the pointers
 * is simply added to its element and considered negligible in calculations.)
 * 
 * The biggest tradeoff when using a linked-list is the slower access time. In
 * order to find a specific item in the list, the class must traverse the list
 * sequentially to find the desired element. Therefore access time is O(n) where
 * n is the list size. For a queue however, this isn't a problem. The spec only
 * requires access to the first element in the queue, and never actually needs
 * to traverse the whole thing. So for the purposes of the TraversableQueue, as
 * the first element is always available, access is O(1).
 * 
 * Because I intended to use my traversable queue in my BoundedCube, I've added
 * two additional methods to the class:
 * 
 * - Remove: traaversing the entire queue, it finds the highest priority (first
 * found) node in the list with a matching element, and removes it from the
 * linked list. O(n). If the element is found returns true, returns false
 * otherwise. Implemented specifically for use in the BoundedCube.
 * 
 * 
 * In summary, this implementation of the TraversableQueue:
 * 
 * - Memory usage: O(n)
 * 
 * - Access time (dequeue and enqueue): O(1)
 * 
 * - Access time (remove and iterate): O(n)
 */
