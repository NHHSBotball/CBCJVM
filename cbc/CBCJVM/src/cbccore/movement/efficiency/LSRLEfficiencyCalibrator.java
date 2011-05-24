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

package cbccore.movement.efficiency;

/**
 * Uses a Least-Squares Regression Line to solve for efficiency. (A simple
 * linear regression model)
 */
public class LSRLEfficiencyCalibrator implements IEfficiencyCalibrator {
	
	private double slope;
	private double invSlope;
	private double intercept;
	private double invIntercept;
	
	public LSRLEfficiencyCalibrator(double[] target, double[] actual) {
		this(findSlope(target, actual), findIntercept(target, actual));
	}
	
	public LSRLEfficiencyCalibrator(double slope, double intercept) {
		this.slope = slope;
		this.intercept = intercept;
		invSlope = -1./slope;
		invIntercept = intercept * invSlope;
	}
	
	private static double findMean(double ... data) {
		double sum = 0.;
		for(double i: data) {
			sum += i;
		}
		return sum / data.length;
	}
	
	private static double findStdDev(double ... data) {
		double mean = findMean(data);
		double sum = 0;
		for(double i: data) {
			sum += Math.abs(mean - i);
		}
		return sum / (data.length - 1);
	}
	
	private static double findSlope(double[] x, double[] y) {
		return findStdDev(y) / findStdDev(x);
	}
	
	private static double findIntercept(double[] x, double[] y) {
		return findMean(y) - findMean(x) * findSlope(x, y);
	}
	
	public double getSlope() {
		return slope;
	}
	
	public double getIntercept() {
		return intercept;
	}
	
	public double translateCmps(double oldCmps) {
		return oldCmps * slope + intercept;
	}
	
	public double getMaxCmps(double oldMaxCmps) {
		return oldMaxCmps * invSlope + invIntercept;
	}
	
	public double getMinCmps(double oldMinCmps) {
		return oldMinCmps * invSlope + invIntercept;
	}
}
