package adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MyBoundedCubeTest {

	// ################## TEST INDEX OUT OF BOUNDS #################

	@Test(timeout = 500, expected = IllegalArgumentException.class)
	public void testIndexOutOfBounds() {
		@SuppressWarnings("unused")
		Cube<Object> testCube = new BoundedCube<>(-1, -1, -1);
	}

	@Test(timeout = 500, expected = IndexOutOfBoundsException.class)
	public void testIndexOutOfBounds2() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		testCube.add(6, 1, 1, new Object());
		testCube.add(1, 6, 1, new Object());
		testCube.add(1, 1, 6, new Object());
		testCube.add(-1, 1, 1, new Object());
		testCube.add(1, -1, 1, new Object());
		testCube.add(1, 1, -1, new Object());
	}

	// ################### TEST GETTING ELEMENTS #####################

	@Test(timeout = 500)
	public void testGetWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertThat("Only element at a position was not returned.",
				testCube.get(1, 1, 1), is(equalTo(element)));
	}

	@Test(timeout = 500)
	public void testGetWithMultipleElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 1, element2);
		assertThat("First element added at a position was not returned.",
				testCube.get(1, 1, 1), is(equalTo(element1)));
	}

	@Test(timeout = 500)
	public void testGetWithMultipleElements2() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 2, element2);
		assertThat("Element added at a position was not returned.",
				testCube.get(1, 1, 1), is(equalTo(element1)));
	}

	@Test(timeout = 500)
	public void testGetWithMultipleElements3() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 2, element2);
		assertThat("Element added at a position was not returned.",
				testCube.get(1, 1, 2), is(equalTo(element2)));
	}
	
	// #################### TEST REMOVE #############################
	
	@Test(timeout = 500)
	public void testRemoveWithNoElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		assertThat("Something removed that should not have been.",
				testCube.remove(1, 1, 1, element), is(equalTo(false)));
	}
	
	@Test(timeout = 500)
	public void testRemoveWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertThat("Something not removed that should have been.",
				testCube.remove(1, 1, 1, element), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveWithTwoElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 2, element2);
		assertThat("Only element at a position was not returned.",
				testCube.remove(1, 1, 1, element1), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveWithTwoElements2() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 2, element2);
		assertThat("Only element at a position was not returned.",
				testCube.remove(1, 1, 2, element2), is(equalTo(true)));
	}
	
	@Test(timeout = 500)
	public void testRemoveWithThreeElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		Object element3 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 2, element2);
		testCube.add(1, 2, 1, element3);
		assertThat("Only element at a position was not returned.",
				testCube.remove(1, 2, 1, element3), is(equalTo(true)));
	}
	
	

	// ######################## TEST GET ALL #################################

	@Test(timeout = 500)
	public void testGetAllNoElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
		assertThat("Returned queue was wrong size.", queue, is(equalTo(null)));
	}

	@Test(timeout = 500)
	public void testGetAllWithSingleElementSize() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		testCube.add(1, 1, 1, element1);
		IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
		assertThat("Returned queue was wrong size.", queue.size(),
				is(equalTo(1)));
	}

	@Test(timeout = 500)
	public void testGetAllWithMultipleElementsSize() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element1 = new Object();
		Object element2 = new Object();
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 1, element2);
		IterableQueue<Object> queue = testCube.getAll(1, 1, 1);
		assertThat("Returned queue was wrong size.", queue.size(),
				is(equalTo(2)));
	}

	// ################# TEST IS MULTIPLE ELEMENTS AT ##########################

	@Test(timeout = 500)
	public void testIsMultipleElementsAtWithNoElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		assertThat("One element at a position indicates it is multiple.",
				testCube.isMultipleElementsAt(1, 1, 1), is(equalTo(false)));
	}
	
	@Test(timeout = 500)
	public void testIsMultipleElementsAtWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		testCube.add(1, 1, 1, new Object());
		assertThat("One element at a position indicates it is multiple.",
				testCube.isMultipleElementsAt(1, 1, 1), is(equalTo(false)));
	}

	@Test(timeout = 500)
	public void testIsMultipleElementsAtWithMultipleElements() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object obj1 = new Object();
		testCube.add(1, 1, 1, obj1);
		Object obj2 = new Object();
		testCube.add(1, 1, 1, obj2);
		;
		assertThat("Multiple elements at a position indicated as singluar.",
				testCube.isMultipleElementsAt(1, 1, 1), is(equalTo(true)));
	}

	// ########################### TEST CLEAR ############################

	@Test(timeout = 500)
	public void testClearCube() {
		Cube<Object> testCube = new BoundedCube<>(3, 3, 3);
		testCube.add(0, 0, 0, new Object());
		testCube.add(1, 1, 1, new Object());
		testCube.add(1, 1, 1, new Object());
		testCube.add(2, 2, 2, new Object());
		testCube.clear();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					assertThat("", testCube.get(i, j, k), is(equalTo(null)));
	}

}
