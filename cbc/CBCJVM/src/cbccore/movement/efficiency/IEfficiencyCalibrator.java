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
 * Efficiency values represent the proportion of the actual speed that a motor
 * moves when it is told to move a certain speed. Classes that implement this
 * interface can define efficiency as a function of speed for a motor/wheel.
 */
public interface IEfficiencyCalibrator {
	/**
	 * Efficiency values represent the proportion of the actual speed that a
	 * motor moves when it is told to move a certain speed. For example, if a
	 * wheel is moving at 5 cm/s when it is told to move at 4 cm/s, the
	 * efficiency value returned should be <code>5.0 / 4.0 = 1.25</code> for the
	 * rate of 4 cm/s. Therefore <code>getEfficiencyAt(4)</code> should return
	 * 1.25</code>.<p/>
	 * 
	 * The more efficient a motor, the higher the efficiency value, the less
	 * efficient the motor is, the lower the efficiency value should be.
	 */
	public double getEfficiencyAt(double cmps);
	
	/**
	 * Divides the desired cmps by the efficiency at that cmps. In other words,
	 * it returns the speed that the motor should be told to move at in order to
	 * actually get it to move at the desired speed.
	 */
	public double translateCmps(double cmps);
	
	/**
	 * returns the value of cmps such that <code>x * getEfficiencyAt(x)</code>
	 * is less than <code>oldMaxCmps</code> for all points from
	 * <code>x = 0</code> to <code>x = cmps</cmps>.
	 */
	public double getMaxCmps(double oldMaxCmps);
	
	/**
	 * returns the value of cmps such that <code>x * getEfficiencyAt(x)</code>
	 * is less than <code>oldMinCmps</code> for all points from
	 * <code>x = 0</code> to <code>x = -cmps</cmps>.
	 */
	public double getMinCmps(double oldMinCmps);
}
