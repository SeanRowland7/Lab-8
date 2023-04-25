package geometry_objects.angle.comparators;

import java.util.Comparator;

import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import utilities.math.MathUtilities;

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
		
		Double lengthOfLeftRay1;
		Double lengthOfLeftRay2;
		Double lengthOfRightRay1;
		Double lengthOfRightRay2;
		
		// Ensure that left ray 1 corresponds to right ray 1.
		if(Segment.overlaysAsRay(left.getRay1(), right.getRay1()))
		{
			lengthOfLeftRay1 = left.getRay1().length();
			lengthOfLeftRay2 = left.getRay2().length();
			lengthOfRightRay1 = right.getRay1().length();
			lengthOfRightRay2 = right.getRay2().length();
		}
		else
		{
			lengthOfLeftRay1 = left.getRay1().length();
			lengthOfLeftRay2 = left.getRay2().length();
			lengthOfRightRay1 = right.getRay2().length();
			lengthOfRightRay2 = right.getRay1().length();
		}
		
		// both left rays >= right rights -> 1
		if (isGreaterOrEqual(lengthOfLeftRay1, lengthOfRightRay1) && isGreaterOrEqual(lengthOfLeftRay2, lengthOfRightRay2)) return 1;
		
		// both left rays <= right rays-> -1
		if (isGreaterOrEqual(lengthOfRightRay1, lengthOfLeftRay1) && isGreaterOrEqual(lengthOfRightRay2, lengthOfLeftRay2)) return -1;
		
		return 0;
	}
	
	private boolean isGreaterOrEqual(double num1, double num2)
	{
		if(MathUtilities.doubleEquals(num1, num2)) return true;
		
		return num1 > num2;
	}
}
