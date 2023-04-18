package geometry_objects.angle;

import geometry_objects.angle.comparators.AngleStructureComparator;
import utilities.eq_classes.LinkedEquivalenceClass;

/**
 * This implementation requires greater knowledge of the implementing Comparator.
 * 
 * According to our specifications for the AngleStructureComparator, we have
 * the following cases:
 *
 *    Consider Angles A and B
 *    * Integer.MAX_VALUE -- indicates that A and B are completely incomparable
                             STRUCTURALLY (have different measure, don't share sides, etc. )
 *    * 0 -- The result is indeterminate:
 *           A and B are structurally the same, but it is not clear one is structurally
 *           smaller (or larger) than another
 *    * 1 -- A > B structurally
 *    * -1 -- A < B structurally
 *    
 *    We want the 'smallest' angle structurally to be the canonical element of an
 *    equivalence class.
 * 
 * @author XXX
 */
public class AngleLinkedEquivalenceClass extends LinkedEquivalenceClass<Angle>
{
    public AngleLinkedEquivalenceClass()
    {
    	super(new AngleStructureComparator());
    }
    
    @Override
    public boolean belongs(Angle angle)
    { 
    	// Check if the angle is structurally comparable to the canonical
    	return _comparator.compare(angle, _canonical) != AngleStructureComparator.STRUCTURALLY_INCOMPARABLE;
    }
    /**
	 *	Adds an element to the equivalence class if it belongs. 
	 *	Returns whether is was successfully added.
	 */
    @Override
	public boolean add(Angle angle)
	{
    	// If there is 
    	if(_canonical ==  null) 
    	{
    		_canonical = angle;
    		return true;
    	}
    		
    	
    	if(!belongs(angle)) return false;
    	
    	//check if angle is structuraclly smaller then the canonical
    	if (_comparator.compare(angle, _canonical) == -1)
    	{
    		_rest.addToFront(_canonical);
    		_canonical = angle;
    	}

		
	}
}