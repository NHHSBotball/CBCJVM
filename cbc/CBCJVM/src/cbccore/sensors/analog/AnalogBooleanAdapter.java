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

package cbccore.sensors.analog;

import cbccore.sensors.digital.IBooleanSensor;

/**
 * Allows you to use an Analog sensor in a boolean (digital) fashion. If a
 * sensor value goes over or under a specified value, an event will be emitted.
 */
public class AnalogBooleanAdapter implements IBooleanSensor {
	private Analog sensor = null;
	private int pivot = 0;
	
	/**
	 * @param  sensor  The analog sensor to watch
	 * @param  pivot   The point where the boolean value "pivot"s around, if the
	 *                 value goes below this, false is returned otherwise true
	 *                 is returned<p/>
	 *                 If you want to do <code>x >= 50</code> then your pivot
	 *                 value would be 50. You can use boolean algebra to form
	 *                 pivot values for other equations.
	 * @see cbccore.sensors.analog.Analog
	 */
	public AnalogBooleanAdapter(Analog sensor, int pivot) {
		this.sensor = sensor;
		this.pivot = pivot;
	}
	
	public Analog getAnalog() {
		return sensor;
	}
	
	/**
	 * The point where the boolean value "pivot"s around, if the value goes
	 * below this, false is returned otherwise true is returned.
	 * 
	 * @return The "pivot" value
	 */
	public int getPivot() {
		return pivot;
	}
	
	/**
	 * The point where the boolean value "pivot"s around, if the value goes
	 * below this, false is returned otherwise true is returned.
	 * 
	 * @param  pivot  The point where the boolean value "pivot"s around, if the
	 *                value goes below this, false is returned otherwise true is
	 *                returned<p/>
	 *                If you want to do <code>x >= 50</code> then your pivot
	 *                value would be 50. You can use boolean algebra to form
	 *                pivot values for other equations.
	 */
	public void setPivot(int pivot) {
		this.pivot = pivot;
	}
	
	@Override
	public boolean getValue() {
		if(sensor.getValueHigh() < pivot) return false;
		return true;
	}
}
