package geometry_objects.angle.comparators;

import java.util.Comparator;

import geometry_objects.angle.Angle;

/**
 * The AngleStructureComparator class provides a comparator for comparing the structure
 * of angles.
 *
 * <p>Bugs: None
 *
 * @author Sean Rowland, Caden Parry
 * @date   25 April 2023
 */
public class AngleStructureComparator implements Comparator<Angle>
{
	public static final int STRUCTURALLY_INCOMPARABLE = Integer.MAX_VALUE;
	
	/**
	 * Given the figure below:
	 * 
	 *    A-------B----C-----------D
	 *     \
	 *      \
	 *       \
	 *        E
	 *         \
	 *          \
	 *           F
	 * 
	 * What we care about is the fact that angle BAE is the smallest angle (structurally)
	 * and DAF is the largest angle (structurally). 
	 * 
	 * If one angle X has both rays (segments) that are subsegments of an angle Y, then X < Y.
	 * 
	 * If only one segment of an angle is a subsegment, then no conclusion can be made.
	 * 
	 * So:
	 *     BAE < CAE
   	 *     BAE < DAF
   	 *     CAF < DAF

   	 *     CAE inconclusive BAF
	 * 
	 * @param left -- an angle
	 * @param right -- an angle
	 * @return -- according to the algorithm above:
 	 *              Integer.MAX_VALUE will refer to our error result
 	 *              0 indicates an inconclusive result
	 *              -1 for less than
	 *              1 for greater than
	 */
	@Override
	public int compare(Angle left, Angle right)
	{
		// no overlay -> not structurally comparable
		if (!left.overlays(right)) return STRUCTURALLY_INCOMPARABLE;
		
		// both left rays >= right rights -> 1
		if (left.getRay1().length() >= right.getRay1().length() &&
				left.getRay2().length() >= right.getRay2().length()) return 1;
		
		// both left rays <= right rays-> -1
		if (left.getRay1().length() <= right.getRay1().length() &&
				left.getRay2().length() <= right.getRay2().length()) return -1;
		
		return 0;
	}
}
