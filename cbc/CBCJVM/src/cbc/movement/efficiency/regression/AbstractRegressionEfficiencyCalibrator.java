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

package cbc.movement.efficiency.regression;

/**
 * Makes creating an <code>IRegressionEfficiencyCalibrator</code> just a little
 * bit easier.
 */
public abstract class AbstractRegressionEfficiencyCalibrator
                      implements IRegressionEfficiencyCalibrator {
	/**
	 * @inheritDoc
	 */
	@Override
	public double getMaxCmps(double oldMaxCmps) {
		return inverseTranslateCmps(oldMaxCmps);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public double getMinCmps(double oldMinCmps) {
		return inverseTranslateCmps(oldMinCmps);
	}
}