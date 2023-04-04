/**
* Given a pair of coordinates; generate a unique name for it;
* return that point object.
*
* Names go from A..Z..AA..ZZ..AAA...ZZZ  (a name such as ABA does not occur)
*
* <p>Bugs: 
*
* @author Sean Rowland
* @date 4/2/2023
*/

package geometry_objects.points;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PointNamingFactory
{
	// Prefix associated with each generated name so those names are easily distinguishable
	private static final String _PREFIX = "*_";

    // Constants reflecting our naming characters for generated names.
	private static final char START_LETTER = 'A';
	private static final char END_LETTER = 'Z';

    //
    // the number of characters in the generated names:
	// "A" and 1 -> "A"
	// "B" and 3 -> "BBB"
	//
	private String _currentName = "A";
	private int _numLetters = 1;

	//
	// A hashed container for the database of points; this requires the Point
	// class implement equals based solely on the individual coordinates and
	// not a name. We need a get() method; HashSet doesn't offer one.
	// Each entry is a <Key, Value> pair where Key == Value
	//
	protected Map<Point, Point> _database;

	public PointNamingFactory()
	{
		_database = new LinkedHashMap<Point, Point>();
	}

	/**
	 * Initialize the database with points; must call put() to ensure all points are named
	 *
	 * @param points -- a list of points, named or not named
	 */
	public PointNamingFactory(List<Point> points)
	{
		_database = new LinkedHashMap<Point, Point>();
		
		for(Point p : points)
		{
			// Add each point to the database.
			put(p);
		}
	}

	/**
	 * Overloaded add / lookup mechanism for this database.
	 *
	 * @param pt -- a Point object (may or may not be named)
	 
	 * @return THE point object in the database corresponding to its coordinate pair
                    * the object in the database if it already exists or
					* a completely new point that has been added to the database
	 */
	public Point put(Point pt)
	{
		// If the point has a name call put(name, x, y) with its name
		if(!pt.isUnnamed()) return put(pt.getName(), pt.getX(), pt.getY());
	
		// Otherwise call put(x, y) without a name
		return put(pt.getX(), pt.getY());
	}

	/**
	 * Overloaded add / lookup mechanism for this database for an unnamed coordinate pair.
	 *
	 * @param x -- single coordinate
	 * @param y -- single coordinate

	 * @return THE point object in the database corresponding to its coordinate pair
                    * the object in the database if it already exists or
					* a completely new point that has been added to the database (with generated name)
	 */
	public Point put(double x, double y)
	{
		// Call put(name, x, y) with a generated name.
		return put(getCurrentName(), x, y);
	}

	/**
	 * The 'main' overloaded add / lookup mechanism for this database.
	 * 
	 * @param name -- the name of the point 
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * 
	 * @return a point (if it already exists in the database) or a completely new point that
	 *         has been added to the database.
	 *         
	 *         If the point is in the database and the name differs from what
	 *         is given, nothing in the database will be changed; essentially
	 *         this means we use the first name given for a point.
	 *            e.g., a valid name cannot overwrite an existing valid name ;
	 *                  a generated name cannot be overwritten by another generated name
	 *         
	 *         The exception is that a valid name can overwrite an unnamed point.
	 */
	public Point put(String name, double x, double y)
	{
		Point pointToAdd = new Point(name, x, y);
		
		// Check if the point already exists
		if(contains(pointToAdd)) 
		{
			// If it does already exists, get it for future reference.
			Point pointInDatabase = get(pointToAdd);
			
			// If the point is in the database and already has a name, simply return the existing point.
			if(nameIsNotGenerated(pointInDatabase)) return pointInDatabase;
			
			// If its name is generated, remove it and add the new point.
			_database.remove(pointInDatabase);
		}
		

		// Add the new point
		_database.put(pointToAdd, pointToAdd);
		return pointToAdd;
	}    

	private boolean nameIsNotGenerated(Point p)
	{
		return !p._name.substring(0, _PREFIX.length()).equals(_PREFIX);
	}
	/**
	 * Strict access (read-only of the database)
	 * 
	 * @param x
	 * @param y
	 * @return stored database Object corresponding to (x, y) 
	 */
	public Point get(double x, double y) { return get(new Point(x, y)); }	
	
	public Point get(Point pt)
	{
		// Go through the database and find the corresponding point.
		for(Point point : getAllPoints())
		{
			if(point.equals(pt)) return point;
		}
		
		return null;
	}

	/**
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return simple containment; no updating
	 */
	public boolean contains(double x, double y) { return contains(new Point(x, y)); }
	public boolean contains(Point p) { return getAllPoints().contains(p); }

	/**
	 * Constructs the next (complete with prefix) generated name.
	 * Names should be of the form PREFIX + current name
	 *
	 * This method should also invoke updating of the current name
	 * to reflect the 'next' name in the sequence.
     *	 
	 * @return the next complete name in the sequence including prefix.
	 */
	private String getCurrentName()
	{
        String currentName = _PREFIX +_currentName;
		updateName();
		return currentName;
	}

	/**
	 * Advances the current generated name to the next letter in the alphabet:
	 * 'A' -> 'B' -> 'C' -> 'Z' --> 'AA' -> 'BB'
	 */
	private void updateName()
	{
		// Save the current letter associated with the name and then erase the name.
		char letter = _currentName.charAt(0);
		_currentName = "";
		
		// If Z has not been reached, advance to the next letter and add the current number of characters.
        if(letter != END_LETTER) 
        {
        	letter++;
        	
        	for(int i = 1; i <= _numLetters; i++)
        		_currentName += letter;
        	
        }
        
        else
        {
        	// Once Z is reached, reset to letter A and add an extra letter.
        	_numLetters++;
        	
        	for(int i = 1; i <= _numLetters; i++)
        		_currentName += START_LETTER;
        }
	}

	/**
	 * @return The entire database of points.
	 */
	public  Set<Point> getAllPoints() { return _database.keySet(); }

	public void clear() { _database.clear(); }
	public int size() { return _database.size(); }

	@Override
	public String toString()
	{
        String sb = "";
   
        for(Point point : getAllPoints())
        {
        	sb += point.toString() + "\n";
        }
        
        return sb;
	}
}