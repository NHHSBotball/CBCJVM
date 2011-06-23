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

package cbccore.motors.statemotors;

import cbccore.Device;
import cbccore.InvalidPortException;

/**
 * A blocking function is one that doesn't return until its action is done. This
 * interface defines blocking versions of the functions available in
 * <code>IStateMotor</code> and <code>IAdvancedStateMotor</code>.<p/>
 * 
 * <b>Implementation note:</b> When using versions of the functions defined in
 * <code>IAdvancedStateMotor</code>, the default behavior should be
 * non-blocking.
 * 
 * @see    cbccore.motors.statemotors.IStateMotor
 * @see    cbccore.motors.statemotors.IAdvancedStateMotor
 * @author Benjamin Woodruff
 */

public interface IBlockingAdvancedStateMotor extends IAdvancedStateMotor {
	public void setPositionTime(int pos, int ms, boolean blocking);
	public void setPositionTime(int pos, double sec, boolean blocking);
	
	public void setPositionSpeed(int pos, int speed, boolean blocking);
	public void setPosition(int pos, boolean blocking);
	
	/**
	 * Determines if the motor is still moving or not. If it's moving, this
	 * returns true, false otherwise. A motor is classified of moving regardless
	 * of how it is started, be it through an <code>IAdvancedStateMotor</code>
	 * method or even an <code>IStateMotor</code> method. If the motor is
	 * physically moving, this returns true. If it's not, this returns false.
	 * 
	 * @return  <code>true</code> if the motor is physically moving,
	 *          <code>false</code> otherwise.
	 */
	public boolean isMoving();
}
