package adts;

import java.util.Iterator;

/**
 * A three-dimensional data structure that holds items in a positional
 * relationship to each other. Each cell in the data structure can hold multiple
 * items. A bounded cube has a specified maximum size in each dimension. The
 * root of each dimension is indexed from zero.
 * 
 * @author
 *
 * @param <T>
 *            The type of element held in the data structure.
 */
public class BoundedCube<T> implements Cube<T> {

	int length; // x
	int breadth; // y
	int height; // z
	int cellCount;
	private Cell rootCell;

	/**
	 * Constructor for the BoundedCube class. O(1)
	 * 
	 * @param length
	 *            Maximum size in the 'x' dimension.
	 * @param breadth
	 *            Maximum size in the 'y' dimension.
	 * @param height
	 *            Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException
	 *             If provided dimension sizes are not positive.
	 */
	public BoundedCube(int length, int breadth, int height)
			throws IllegalArgumentException {
		if (length < 0 || breadth < 0 || height < 0) {
			throw new IllegalArgumentException();
		}
		this.length = length;
		this.breadth = breadth;
		this.height = height;
		cellCount = 0;
		rootCell = null;
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#add(int, int, int, java.lang.Object)
	 */
	@Override
	public void add(int x, int y, int z, T element)
			throws IndexOutOfBoundsException {
		checkCoords(x, y, z);

		Cell cell = getCell(x, y, z);

		if (cellCount == 0 || cell == null) {
			// Only need one of these, but helps make it clear why both of these
			// are the same thing.
			addNewCell(x, y, z, element);
		} else {
			cell.add(element);
		}

	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#get(int, int, int)
	 */
	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {

		Cell cell = getCell(x, y, z);

		if (cell == null) {
			// Cell not found.
			return null;
		}

		return cell.get();
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#getAll(int, int, int)
	 */
	@Override
	public IterableQueue<T> getAll(int x, int y, int z)
			throws IndexOutOfBoundsException {
		Cell cell = getCell(x, y, z);
		if (cell == null) {
			// Cell not found.
			return null;
		}
		return cell.getAll();
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#isMultipleElementsAt(int, int, int)
	 */
	@Override
	public boolean isMultipleElementsAt(int x, int y, int z)
			throws IndexOutOfBoundsException {
		Cell cell = getCell(x, y, z);
		if (cell == null) {

			// Cell not found - no aircraft in empty cell.
			return false;
		}
		return cell.aircraftCount() > 1;
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#remove(int, int, int, java.lang.Object)
	 */
	@Override
	public boolean remove(int x, int y, int z, T element)
			throws IndexOutOfBoundsException {
		Cell cell = getCell(x, y, z);
		if (cell == null) {
			// Cell not found - provided aircraft not removed.
			return false;
		}
//		if (cell.aircraftCount() > 1) {
//			// remove the first matching aircraft from the cell.
//			return cell.remove(element);
//		} else {
//			// As the cell will be empty once the aircraft is removed, remove
//			// the entire cell from the linked-list: Storing empty cells
//			// wastes memory.
//			removeAll(x, y, z);
//			return true;
//		}
		return cell.remove(element);
	}

	/*
	 * (non-Javadoc) O(n)
	 * 
	 * @see adts.Cube#removeAll(int, int, int)
	 */
	@Override
	public void removeAll(int x, int y, int z)
			throws IndexOutOfBoundsException {
		checkCoords(x, y, z);

		// Search for cell O(n)
		Cell cell = getCell(x, y, z);
		// Cell not found. Nothing to remove
		if (cell == null) {
			return;
		}
		if(cell.nextZ != null){
			cell.nextZ.nextX = cell.nextX;
			cell.nextZ.nextY = cell.nextY;
			cell.parentCell.nextX = cell.nextZ;
			cell.nextZ.parentCell = cell.parentCell;
		}
		if(cell.nextY != null){
			cell.nextY.nextX = cell.nextX;
			cell.parentCell.nextX = cell.nextY;
			cell.nextY.parentCell = cell.parentCell;
		}
		if(cell.nextX != null){
			cell.parentCell.nextX = cell.nextX;
			cell.nextX.parentCell = cell.parentCell;
		}else {
			cell.parentCell.nextX = null;
		}
	}

	/*
	 * (non-Javadoc) O(1)
	 * 
	 * @see adts.Cube#clear()
	 */
	@Override
	public void clear() {
		// Abandon memory to root cell. Garbage collector will free the whole
		// structure.
		rootCell = null;
		cellCount = 0;
	}

	/**
	 * Private method that adds a new cell to the linked-list. O(n)
	 * 
	 * @param x
	 *            X Coordinate of the position of the element.
	 * @param y
	 *            Y Coordinate of the position of the element.
	 * @param z
	 *            Z Coordinate of the position of the element.
	 * @param element
	 *            Object to be stored within cell at provided coordinates.
	 * @throws IndexOutOfBoundsException
	 *             If x, y or z coordinates are out of bounds.
	 */
	private void addNewCell(int x, int y, int z, T element) {
		checkCoords(x, y, z);
		// functionally very similar to getCell(), the only difference is that
		// instead of our spider looking at it's own x, y, and z values, it
		// looks ahead at the appropriate child's x, y, and z values.

		// Check if cube is empty.
		if (rootCell == null) {
			rootCell = new Cell(x, y, z, element);
			++cellCount;
			return;
		}

		Cell spider = rootCell;
		while (spider.getX() != x) {
			// this loop will terminate if we get to a cell with matching x
			// coordinates. It would then be time to start traversing the y
			// branch.
			if (spider.nextX == null) {
				// end of the x branch reached. Cell not found. Create new cell.
				Cell temp = new Cell(x, y, z, element);
				// rearranging cell links
				temp.parentCell = spider;
				spider.nextX = temp;
				// increment cell count
				++cellCount;
				return;
			}
			if (spider.nextX.getX() <= x) {
				// The next cell's x is smaller than the provided x, so keep
				// traversing the x branch.
				spider = spider.nextX;
				continue;
			} else {
				// Spider's x is smaller than provided x, but the next cell's x
				// is greater than the provided x. The new cell must therefore
				// be placed between the two.
				Cell temp = new Cell(x, y, z, element);
				temp.nextX = spider.nextX;
				temp.nextX.parentCell = temp;
				spider.nextX = temp;
				temp.parentCell = spider;
				++cellCount;
				return;
			}
		}
		// rinse and repeat with y branch and z branch.
		while (spider.getY() != y) {
			if (spider.nextY == null) {
				Cell temp = new Cell(x, y, z, element);
				temp.parentCell = spider;
				spider.nextY = temp;
				++cellCount;
				return;
			}
			if (spider.nextY.getY() <= y) {
				spider = spider.nextY;
				continue;
			} else {
				Cell temp = new Cell(x, y, z, element);
				temp.nextY = spider.nextY;
				temp.nextY.parentCell = temp;
				spider.nextY = temp;
				temp.parentCell = spider;
				++cellCount;
				return;
			}
		}

		while (spider.getZ() != z) {
			// could be while(1), because adding a new cell requires the
			// coordinates to not already have a cell.
			if (spider.nextZ == null) {
				Cell temp = new Cell(x, y, z, element);
				temp.parentCell = spider;
				spider.nextZ = temp;
				++cellCount;
				return;
			}
			if (spider.nextZ.getZ() <= z) {
				spider = spider.nextZ;
				continue;
			} else {
				Cell temp = new Cell(x, y, z, element);
				temp.nextZ = spider.nextZ;
				temp.nextZ.parentCell = temp;
				spider.nextZ = temp;
				temp.parentCell = spider;
				++cellCount;
				return;
			}
		}
	}

	/**
	 * Private method that retrieves a cell from the list using provided
	 * coordinates. Throws exception if out of bounds. O(n)
	 * 
	 * @param x
	 *            X Coordinate of the position of the element.
	 * @param y
	 *            Y Coordinate of the position of the element.
	 * @param z
	 *            Z Coordinate of the position of the element.
	 * @return Cell object at given coordinates. Null if not found.
	 * @throws IndexOutOfBoundsException
	 *             If x, y or z coordinates are out of bounds.
	 */
	private Cell getCell(int x, int y, int z) throws IndexOutOfBoundsException {
		checkCoords(x, y, z);
		// important to remember that this method will return null if the cell
		// isn't found.

		// check if the cube is empty
		if (rootCell == null) {
			return null;
		}
		// create variable "spider" that traverses the tree (like a web)
		Cell spider = rootCell;
		while (true) {
			if (x != spider.getX()) {
				// reached the end of the x branch?
				if (spider.nextX == null)
					return null;
				// continue traversing the x branch
				spider = spider.nextX;
				continue;

			}
			// if we've reached here, we have found a cell that matches our x
			// coordinate. Now we want to traverse the y branch.
			if (y != spider.getY()) {
				// reached the end of the y branch?
				if (spider.nextY == null)
					return null;
				// continue traversing the y branch
				spider = spider.nextY;
				continue;

			}
			// if we've reached here, we have found a cell that matches our x
			// AND y coordinates. Now we want to traverse the z branch.
			if (z != spider.getZ()) {
				// reached the end of the z branch?
				if (spider.nextZ == null) {
					return null;
				}
				// continue traversing the z branch
				spider = spider.nextZ;
				continue;

			}
			// If we reach here, our spider has arrived at a cell with matching
			// x, y, and z coordinates. Cell found!
			return spider;
		}
	}

	/**
	 * Private method that checks if provided coordinates are out of bounds.
	 * Throws exception if out of bounds. O(1)
	 * 
	 * @param x
	 *            X Coordinate of the position of the element.
	 * @param y
	 *            Y Coordinate of the position of the element.
	 * @param z
	 *            Z Coordinate of the position of the element.
	 * @throws IndexOutOfBoundsException
	 *             If x, y or z coordinates are out of bounds.
	 */
	private void checkCoords(int x, int y, int z) {
		if (x > length || y > breadth || z > height || x < 0 || y < 0
				|| z < 0) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Private class representing the cells (nodes) in the linked list. Each
	 * cell is 1 square kilometer of airspace and is unique within the list.
	 * Aircraft are stored in the cell as a TraversableQueue
	 * 
	 * @author Angus Trusler
	 *
	 */
	private class Cell {

		// Cell coordinates
		private int x;
		private int y;
		private int z;

		// Parent cell
		public Cell parentCell;

		// Children cells.
		public Cell nextX;
		public Cell nextY;
		public Cell nextZ;

		// Aircraft storage structure
		private TraversableQueue<T> aircraft = null;

		/**
		 * Constructor for cell object. O(1)
		 * 
		 * @param x
		 *            X Coordinate of the position of the element.
		 * @param y
		 *            Y Coordinate of the position of the element.
		 * @param z
		 *            Z Coordinate of the position of the element.
		 */
		Cell(int x, int y, int z, T element) {

			this.x = x;
			this.y = y;
			this.z = z;

			this.nextX = null;
			this.nextY = null;
			this.nextZ = null;

			this.parentCell = null;

			aircraft = new TraversableQueue<T>();
			this.aircraft.enqueue(element);
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public int getZ() {
			return this.z;
		}

		/**
		 * Adds an aircraft to the cell. O(1)
		 *
		 * @param aircraft
		 *            Aircraft to be added to the cell.
		 */
		public void add(T aircraft) {
			this.aircraft.enqueue(aircraft);
		}

		/**
		 * Finds the first added item stored at these coordinates. O(n-1)
		 * 
		 * @return the 'oldest' item in this cell.
		 */
		public T get() {
			Iterator<T> it = this.aircraft.iterator();
			T last = null;
			while (it.hasNext()) {
				last = it.next();
			}
			return last;
		}

		/**
		 * Removes the first found aircraft that equals the provided aircraft.
		 * O(n)
		 *
		 * @param element
		 * @return true if aircraft found and removed, false otherwise.
		 */
		public boolean remove(T element) {
			return this.aircraft.remove(element);
		}

		/**
		 * Returns the number of aircraft stored in this cell. (at the cell's
		 * coordinates) O(1)
		 * 
		 * @return int number of aircraft in this cell.
		 */
		public int aircraftCount() {
			return this.aircraft.size();
		}

		/**
		 * Gets the entire queue of aircraft stored in this cell. O(1)
		 * 
		 * @return IterableQueue<T> the queue of aircraft stored in this cell.
		 */
		public IterableQueue<T> getAll() {
			if (this.aircraft.size() == 0)
				return null;
			return this.aircraft;
		}

	}

}

/*
 * Analysis and Justification of Design Choices for BoundedCube
 * 
 * When designing the BoundedCube, consideration first had to be given to the
 * large scale of the data it may have to store. The number of individual cubic
 * kilometers (cells) of airspace the cube would have to store, using the given
 * spec would be equal to: 5321 * 3428 * 35 which is 638,413,580 unique possible
 * coordinates. To store each and every coordinate - even if each coordinate
 * occupied minimal memory space - would require n^3 slots of memory where n is
 * the average single dimension of the cube (as the array would be 3
 * dimensional).
 * 
 * As the maximum number of aircraft within the OneSKy simulation is no more
 * than 20,000, the number of occupied cells is proportionally tiny compared to
 * the maximum size of the airspace.
 * 
 * Therefore, to be as memory efficient as possible, only coordinates that are
 * in use should be tracked, and as soon as a coordinate is no longer in use, it
 * should be removed from memory.
 * 
 * To this end, a linked-list of only the used coordinates seems like the ideal
 * approach. The biggest problem with the linked-list approach is the large size
 * the cube may end up becoming. As adding a new aircraft to the list requires
 * checking if the cell is already occupied, the cube must first find the cell.
 * To do this, it must iterate over all the existing cells until it either finds
 * the cell, or doesn't find the cell. In either case, the access time is
 * dramatically slower as the number of aircraft increases to a factor of O(n)
 * where n is the number of aircraft stored in the cube which is equal to O(n^3)
 * where n is the average cube dimension is the total number of cells in the
 * airspace.
 * 
 * As this class is scaled up to include more aircraft, it becomes clear the
 * method by which aircraft are retrieved from the cube would not be efficient.
 * In order for an aircraft to be added to the structure, the cube must search
 * through the list of tracked cells to determine if the cell is already being
 * tracked (to check if an aircraft is about to fly into another's airspace). As
 * an aircraft may be found in any cell position, the total search space, as
 * detailed above, is O(n^3).
 * 
 * For each aircraft, the program must search this space, and iterating through
 * the entire list of aircraft each time an aircraft is added means that to add
 * a list of aircraft of size n, means that the total time taken to add the list
 * of aircraft becomes O(n^2).
 * 
 * Therefore, in this current implementation to add a list of aircraft to a cube
 * of dimension n, is O(n^2) * O(n^3). This run time efficiency is excessive.
 * 
 * To maintain the memory efficient qualities a linked-list provides, but cut
 * down on the excessive access time, a different approach is needed. Instead of
 * linking cells in a standard one dimensional strategy, utilizing the cell's 3
 * different coordinate types to our advantage may yield faster run times.
 * 
 * An incomplete sorted ternary tree was implemented using a linked list and the
 * cell's three coordinate types. Cells each had a parent, and three children -
 * an x, y, and z child. A cell's child cells all have coordinates greater than
 * the parent's coordinates. Eg coordinate x5y5z5 may have an x child x6y2z8, a
 * y child x5y9z1, and a z child x5y5z6. (x children must have xP < xC - y
 * children must have xP==xC, yP < yC - z children must have xP==xC, yP==yC, zP
 * < zC)
 * 
 * A cell was found by starting at the root node, and traversing down the tree
 * checking first, the input coordinates, against only the x children. If an x
 * child with matching x value was found, continue traversing down the y
 * children, comparing y coordinates. If a child was found with a coordinate
 * value greater than the provided coordinate, it is clear the cell is not
 * currently being tracked and therefore is not in the tree, and is then added
 * between the lesser parent and the greater child. Links to nodes are updated.
 * 
 * As the structure is now sorted, this new structure reduces the search time
 * (where n is the cube's average dimension) from O(n^3) to O(3n). This is
 * because the worst case search time is: a cube that is full, finding the cell
 * located at max length, breadth, and width from x0y0z0, requires iteratively
 * adding +1 to x, then y, then z, until cell is reached - as opposed to
 * searching each and every possible location.
 * 
 * An array based structure would have been inefficient compared to a linked
 * list structure when one considers the same memory management problems
 * presented in the TraversableQueue. While access time would have greatly
 * exceeded the capabilities of the linked list, the use of memory would have
 * been very inefficient. However, even allowing the limitations provided in the
 * spec it may still have been *practical*. The implementation would still need
 * to discard unoccupied cells, but by keeping the list sorted, memory usage
 * would not have exceeded the spec's limitations, and access time would have
 * been greatly increased.
 * 
 * In summary, this implementation of the BoundedCube:
 * 
 * - Memory usage: O(n)
 * 
 * - Access time: O(n)
 */
