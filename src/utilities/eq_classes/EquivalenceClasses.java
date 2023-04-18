package utilities.eq_classes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
* The EquivalenceClasses class provides a way to store many LinkedEquivalenceClasses. 
*
* <p>Bugs: None
*
* @author Sean Rowland, Mason Taylor, Alex 
* @date 02/11/2023
*/

public class EquivalenceClasses<T> 
{
	
	protected Comparator<T> _comparator;
	
	protected List<LinkedEquivalenceClass<T>> _classes;
	
	public EquivalenceClasses(Comparator<T> comparator) {
		
		_comparator = comparator;
		
		_classes = new ArrayList<LinkedEquivalenceClass<T>>();
		
	}
	
	/**
	 *	Add a given element to an existing class if it fits in one,
	 * otherwise create a new LinkedEquivalenceClass for the element.
	 */
	public boolean add(T element) {
		
		int eqIndex = indexOfClass(element);
		
		if(eqIndex != -1) {
			
			return _classes.get(eqIndex).add(element);
			
		}
		
		LinkedEquivalenceClass<T> list = new LinkedEquivalenceClass<T>(_comparator);
		
		_classes.add(list);
		
		return list.add(element);
		
	}
	
	/**
	 *	Check if a given element is contained one of the equivalence classes.
	 */
	public boolean contains(T element) {
		
		for(LinkedEquivalenceClass<T> list : _classes) {
			
			if(list.contains(element)) return true;
			
		}
		
		return false;
		
	}
	
	/**
	 *	Returns the collective size of all equivalence classes.
	 */
	public int size() {
		
		int size = 0;
		
		for(LinkedEquivalenceClass<T> list : _classes) {
			
			size = size + list.size();
			
		}
		
		return size;
		
	}
	
	/**
	 *	Returns the number of equivalence classes.
	 */
	public int numClasses() {
		
		return _classes.size();
		
	}
	
	/**
	 *	Returns the index of a given equivalence class.
	 */
	protected int indexOfClass(T element) {
		
		int index = 0;
		
		for(; index < _classes.size(); index++) {
			
			if(_classes.get(index).belongs(element)) return index;
			
		}
	
		return -1;
		
	}
	
	/**
	 *	Returns returns a string representation of the list of equivalence classes.
	 */
	public String toString() {
		
		String str = "";
		
		for(LinkedEquivalenceClass<T> list : _classes) {
			
			str = str + ", " + list.toString();
			
		}
		
		return str;
		
	}

}
