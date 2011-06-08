/*
 * This file is part of CBCJVM.
 * CBCJVM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CBCJVM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CBCJVM.  If not, see <http://www.gnu.org/licenses/>.
 */

package cbccore.movement.efficiency.fitcurve;

import java.util.Arrays;
import cbccore.movement.efficiency.IEfficiencyCalibrator;

public abstract class AbstractCurveEfficiencyCalibrator
                      implements IEfficiencyCalibrator {
	private double[] target, actual;
	
	protected AbstractCurveEfficiencyCalibrator(double[] target,
	                                            double[] actual) {
		if(target.length != actual.length) {
			throw new IllegalArgumentException(
				"The actual and target arrays don't have matching lengths."
			);
		}
		
		if(target.length < 2) {
			throw new IllegalArgumentException(
				"You need at least two points of data to fit a curve."
			);
		}
		
		double[][] sorted = sort(target, actual);
		target = sorted[0]; actual = sorted[1];
		if(!checkReversible(target, actual)) {
			throw new IllegalArgumentException(
				"The actual values are not constantly increasing or " +
				"decreasing relative to the target value."
			);
		}
		
		this.target = target; this.actual = actual;
	}
	
	public double[] getTargetData() {
		return target;
	}
	
	public double[] getActualData() {
		return actual;
	}
	
	/**
	 * Finds where in an array, <code>a</code>, a <code>key</code> should be
	 * located. In other words, it gives us a clue where to look when
	 * interpolating.
	 * 
	 * @param   a    The array to look in.
	 * @param   key  The key to look for in the array.
	 * @return  The index of the key, if the key is in the array, otherwise the
	 *          index of the first value in the array below the key
	 *          <code>-1</code> if there are none). This value is always between
	 *          <code>-1</code> and <code>a.length - 1</code>, inclusive.
	 */
	protected static int bisect(double[] a, double key) {
		int i = Arrays.binarySearch(a, key);
		if(i >= 0) {
			return i;
		}
		
		// from the javadocs, binarySearch returns the:
		
		// index of the search key, if it is contained in the list; otherwise,
		// (-(insertion point) - 1). The insertion point is defined as the point
		// at which the key would be inserted into the list: the index of the
		// first element greater than the key, or list.size(), if all elements
		// in the list are less than the specified key. Note that this
		// guarantees that the return value will be >= 0 if and only if the key
		// is found.
		return -i - 2;
	}
	
	/**
	 * Performs a <code>bisect</code> operation on the array returned by
	 * <code>getTargetData()</code>.
	 * 
	 * @see  #bisect
	 * @see  #bisectActualData
	 */
	protected int bisectTargetData(double key) {
		return bisect(getTargetData(), key);
	}
	
	/**
	 * Performs a <code>bisect</code> operation on the array returned by
	 * <code>getActualData()</code>.
	 * 
	 * @see  #bisect
	 * @see  #bisectTargetData
	 */
	protected int bisectActualData(double key) {
		return bisect(getActualData(), key);
	}
	
	/**
	 * Sorts parallel arrays, such that the first, <code>x</code>, is in
	 * increasing numerical order. It does not mutate the arrays like
	 * <code>java.util.Arrays.sort</code> does, but rather it returns the sorted
	 * versions.
	 * 
	 * @param   x  The array to sort in increasing numerical order.
	 * @param   y  The array parallel to <code>x</code>.
	 * @return  An array of <code>{x, y}</code> in their sorted forms
	 * @see     java.util.Arrays#sort
	 */
	protected static double[][] sort(double[] x, double[] y) {
		// Ideally we'd implement our own sorting algorithm, but a bunch of
		// post-processing like we do works okay too, it's just a little bit
		// slower
		
		// sort the x array
		double[] sx = new double[x.length];
		System.arraycopy(x, 0, sx, 0, x.length); // copy first
		Arrays.sort(sx);                         // because Arrays.copy mutates
		
		// sort the y array matching it to the x array's order
		double[] sy = new double[y.length];
		for(int i = 0; i < x.length; ++i) {
			sy[Arrays.binarySearch(sx, x[i])] = y[i];
		}
		
		// bundle the parallel arrays for returning
		double[][] r = new double[2][];
		r[0] = sx; r[1] = sy;
		return r;
	}
	
	private static boolean checkReversible(double[] x, double[] y) {
		if(x.length != y.length) { return false; }
		
		// sort and unpack x and y
		double[][] sorted = sort(x, y);
		x = sorted[0]; y = sorted[1];
		
		// figure out what order y is going in relative to x, if this changes,
		// the system is not reversible
		boolean increasing = y[0] < y[1];
		
		double prev = y[1]; // we could also use y[i - 1]
		for(int i = 2; i < y.length; ++i) {
			if(increasing && y[i] < prev) {
				return false;
			} if(!increasing && y[i] > prev) {
				return false;
			}
			prev = y[i];
		}
		return true;
	}
}
