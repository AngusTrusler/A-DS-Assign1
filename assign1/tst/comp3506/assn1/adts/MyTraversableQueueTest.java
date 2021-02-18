package adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTraversableQueueTest {

	@SuppressWarnings("unused")
	static private class dummy {
		int dummyInt = 0;
		int dummyint2 = 0;
		int dummyint3 = 0;
		int dummyint4 = 0;
		double dummydouble = 2.321;
		String dummystring = "dummystring";
	}
	
	// #################### INDEX OUT OF BOUNDS TESTS #####################

	@Test(timeout = 500, expected = IndexOutOfBoundsException.class)
	public void testDequeueEmptyQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.dequeue(); // Nothing to dequeue.
	}

	@Test(timeout = 500, expected = IndexOutOfBoundsException.class)
	public void testDequeueOneElementQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		testQueue.dequeue();
		testQueue.dequeue(); // Nothing to dequeue.
	}

	@Test(timeout = 500, expected = IndexOutOfBoundsException.class)
	public void testDequeueManyElementsQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		testQueue.enqueue(new Object());
		testQueue.dequeue();
		testQueue.dequeue();
		testQueue.dequeue(); // Nothing to dequeue.
	}

	// ##################### QUEUE SIZE TESTS ######################

	@Test(timeout = 500)
	public void testNewQueueIsEmpty() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		assertThat("A newly created queue does not have a size of 0.",
				testQueue.size(), is(equalTo(0)));
	}

	@Test(timeout = 500)
	public void testSingleElementQueueSize() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		assertThat("A queue with one element does not have a size of 1.",
				testQueue.size(), is(equalTo(1)));
	}

	@Test(timeout = 500)
	public void testTwoElementQueueSize() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		testQueue.enqueue(new Object());
		assertThat("A queue with one element does not have a size of 1.",
				testQueue.size(), is(equalTo(2)));
	}

	@Test(timeout = 500)
	public void testManyElementQueueSize() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		for (int i = 0; i < 20000; i++) {
			testQueue.enqueue(new dummy());
		}
		assertThat("A queue with x elements does not have a size of x.",
				testQueue.size(), is(equalTo(20000)));
	}

	// ################# ENQUEUE AND DEQUEUE TESTS ##########################

	@Test(timeout = 500)
	public void testSingleElementQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		testQueue.enqueue(element);
		assertThat(
				"Enqueing and Dequeing one element does not return that element.",
				testQueue.dequeue(), is(equalTo(element)));
	}

	@Test(timeout = 500)
	public void testTwoElementQueueFirst() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		testQueue.dequeue();
		assertThat(
				"Enqueing and Dequeing one element does not return that element.",
				testQueue.dequeue(), is(equalTo(element)));
	}

	public void testTwoElementQueueLast() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		assertThat(
				"Enqueing and Dequeing element does not return that element.",
				testQueue.dequeue(), is(equalTo(element2)));
	}
	
	public void testManyElementQueueMiddle() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		Object element3 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		testQueue.enqueue(element3);
		testQueue.dequeue();
		assertThat(
				"Enqueing and Dequeing element does not return that element.",
				testQueue.dequeue(), is(equalTo(element2)));
	}
	
	//#################### ITERATOR TESTS ###########################
	
	@Test(timeout = 500)
	public void testIteratorHasNextOnEmptyQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Iterator<Object> it = testQueue.iterator();
		assertThat(
				"Iterator before first position on a queue of one element does not have a next.",
				it.hasNext(), is(equalTo(false)));
	}
	
	@Test(timeout = 500, expected = NoSuchElementException.class)
	public void testIteratorNextOnEmptyQueueException() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Iterator<Object> it = testQueue.iterator();
		it.next(); // no next, should throw NoSuchElementException
	}
	
	@Test(timeout = 500)
	public void testIteratorHasNextOnSingleEntityQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.enqueue(new Object());
		Iterator<Object> it = testQueue.iterator();
		assertThat(
				"Iterator before first position on a queue of one element does not have a next.",
				it.hasNext(), is(equalTo(true)));
		it.next();
		assertThat(
				"Iterator before second position on a queue of one element has a next.",
				it.hasNext(), is(equalTo(false)));
	}
	
	// ######################## REMOVE TESTS ###########################
	// 					   (NON STANDARD METHOD)
	
	@Test(timeout = 500)
	public void testRemoveFromEmptyQueue() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		assertThat(
				"Removed non-existant element",
				testQueue.remove(element), is(equalTo(false)));
	}
	
	@Test(timeout = 500)
	public void testRemoveFromSingleElementQueue() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		testQueue.enqueue(element);
		assertThat(
				"Didn't remove element",
				testQueue.remove(element), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveFromTwoElementQueue() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		assertThat(
				"Didn't remove element",
				testQueue.remove(element), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveFromTwoElementQueueRootNode() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		assertThat(
				"Didn't remove element",
				testQueue.remove(element2), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveFromTwoElementQueueQueueSize() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		testQueue.remove(element);
		assertThat(
				"Didn't remove element",
				testQueue.size(), is(equalTo(1)));
	}
	
	@Test(timeout = 500)
	public void testRemoveFromTwoElementQueueRemovedRightElement() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		testQueue.remove(element);
		assertThat(
				"Didn't remove correct element",
				testQueue.dequeue(), is(equalTo(element2)));
	}
	
	@Test(timeout = 500)
	public void testRemoveNonExistantElementFromTwoElementQueue() {
		TraversableQueue<Object> testQueue = new TraversableQueue<>();
		Object element = new Object();
		Object element2 = new Object();
		Object element3 = new Object();
		testQueue.enqueue(element);
		testQueue.enqueue(element2);
		assertThat(
				"Didn't remove correct element",
				testQueue.remove(element3), is(equalTo(false)));
	}
	
	
	
	
	
	
	
	
	
}
