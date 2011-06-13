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

/**
 * Performs simple linear interpolation between calibration data-points, and
 * very basic linear extrapolation (which, like all extrapolation, should be
 * avoided whenever possible).<p/>
 * 
 * This doesn't form a smooth curve, but it makes fast approximations!
 * 
 * @author Benjamin Woodruff
 */
public class LinearCurveEfficiencyCalibrator
             extends AbstractCurveEfficiencyCalibrator {
	private double[] slopes;
	
	public LinearCurveEfficiencyCalibrator(double[] target, double[] actual) {
		super(target, actual);
		target = this.getTargetData(); actual = this.getActualData();
		slopes = new double[target.length];
		for(int i = 0; i < slopes.length - 1; ++i) {
			slopes[i] = (target[i+1] - target[i]) / (actual[i+1] - actual[i]);
		}
		slopes[slopes.length - 1] = slopes[slopes.length - 2]; // extrapolation
	}
	
	public double[] getSlopesData() {
		return slopes;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public double translateCmps(double cmps) {
		int i = bisectActualData(cmps);
		if(i < 0) {
			i = 0;
		} else if(i >= getActualData().length) {
			i = getActualData().length - 1;
		}
		return getTargetData()[i] + (cmps - getActualData()[i]) * slopes[i];
	}
	
	protected double inverseTranslateCmps(double cmps) {
		int i = bisectTargetData(cmps);
		if(i < 0) {
			i = 0;
		} else if(i >= getTargetData().length) {
			i = getTargetData().length - 1;
		}
		return getActualData()[i] + (cmps - getTargetData()[i]) / slopes[i];
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public double getMinCmps(double oldCmps) {
		return inverseTranslateCmps(oldCmps);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public double getMaxCmps(double oldCmps) {
		return inverseTranslateCmps(oldCmps);
	}
}
