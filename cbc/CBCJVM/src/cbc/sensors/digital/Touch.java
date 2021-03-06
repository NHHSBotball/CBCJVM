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

package cbc.sensors.digital;

import cbc.Device;
import cbc.InvalidPortException;

/**
 * 
 * @author Braden McDorman
 *
 */

public class Touch implements IBooleanSensor {
	private cbc.low.Sensor lowSensor = Device.getLowSensorController();
	private int port = 0;
	public Touch(int port) throws InvalidPortException {
		if(port < 8 || port > 15) { throw new InvalidPortException(); }
		this.port = port;
	}
	public boolean getValue() {
		return lowSensor.digital(port) != 0;
	}
	@Override
	public String toString() {
		return "Touch Sensor " + port;
	}
}
