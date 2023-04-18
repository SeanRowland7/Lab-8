
package utilities.eq_classes;

/**
* A LinkedList provides a linear data structure with constant time access to the first element.
*
* <p>Bugs: None
*
* @author Sean Rowland, Mason Taylor, Alex 
* @date 02/11/2023
*/

public class LinkedList <T>
{
	private int _size;
	protected Node _head, _tail;

	protected class Node
	{
		protected T _data;
		protected Node _next;

		public Node() {this(null, null);}

		public Node(T data, Node next) {
			_data = data; 
			_next = next;
		}
	}

	public LinkedList(){

		_head = new Node();
		
		_tail = new Node(null, null);

		_head._next = _tail;

		_size = 0;
		
	}

	/**
	 *	Returns whether the list is empty.
	 */
	public boolean isEmpty()
	{
		return _head._next == _tail;
	}

	/**
	 *	Clears the list.
	 */
	public void clear()
	{
		_head._next = _tail;
		
		_size = 0;
		
	}

	/**
	 *	Returns the size of the list.
	 */
	public int size()
	{
		return _size;
	}

	/**
	 *	Adds an element to the front of the list.
	 */
	public void addToFront(T element) {
		
		insert(element, _head);
		
	}
	
	/**
	 *	Adds an element to the back of the list.
	 */
	public void addToBack(T target) {
			
		if(_size == 0) this.addToFront(target);
		
		else {
			
			insert(target, lastValid());
			
		}
		
	}

	/**
	 *	Allows for easy insertion into the list.
	 */
	private void insert(T element, Node left) {
		
		Node node = new Node(element, left._next);
		
		left._next = node;
		
		_size++;
		
	}

	/**
	 *	Check is a given element is contained in the list.
	 */
	public boolean contains(T target)
	{
		return contains(target, _head._next);
	}

	private boolean contains(T target, Node n)
	{
		if(n == _tail)
		{
			return false;
		}

		if(n._data.equals(target))
		{
			return true;
		}

		return contains(target, n._next);
	}

	private Node previous(T target)
	{
		return previous(target,_head);
	}

	private Node previous(T target, Node n)
	{
		if(n._next == _tail)
		{
			return null;
		}
		if(n._next._data.equals(target))
		{
			return n;
		}

		return previous(target, n._next);

	}

	/**
	 *	Removes a given element from the list.
	 */
	public boolean remove(T target)
	{
		Node previous = _head;
		while(previous._next != _tail)
		{
			if(previous._next._data.equals(target))
			{
				previous._next = previous._next._next;
				
				_size--;
				
				return true;
			}

			previous = previous._next;

		}
		return false;
	}
	
	private Node lastValid() {
		
		if(isEmpty()) return _head;
		
		Node current;
		
		for(current = _head._next; current._next != _tail; current = current._next) ;
		
		return current;
		
	}
	
	/**
	 *	Reverses the linked list.
	 */
	public void reverse()
	{
	    Node current = _head;
	    Node prev = null;
	    Node next = null;

	    while (current != null) {
	        next = current._next;
	        current._next = prev;
	        prev = current;
	        current = next;
	    }

	    _tail = _head;
	    _head = prev;
	}

	/**
	 *	Gets the first element in the list.
	 */
	public T getFirst() { return _head._next._data; }
	
	/**
	 *	Returns a string representation of the list.
	 */
	public String toString()
	{
		String result = "";

		for(Node n = _head._next; n != _tail; n = n._next)
		{
			result += n._data;
			if(n._next != _tail) result +=  ", ";
		}
		return result;	
	}
}








