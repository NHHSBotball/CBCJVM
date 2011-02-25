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

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A special motor controller that can remember certain set positions, and allow
 * you to move to them. (Like open and close positions for a simple claw)
 * 
 * @author Benjamin Woodruff
 * @see    cbccore.motors.Motor
 */

public class RestorePointMotor<K> extends HashMap<K, Integer> {
	
	protected IAdvancedStateMotor controlMotor;
	protected int speed;
	
	public RestorePointMotor(IAdvancedStateMotor controlMotor) {
		this(controlMotor, -1);
	}
	
	public RestorePointMotor(IAdvancedStateMotor controlMotor, int speed) {
		super();
		this.controlMotor = controlMotor;
		this.speed = speed;
	}
	
	
	/**
	 * @param  positions  The positions to store in measured as tick positions
	 */
	public RestorePointMotor(IAdvancedStateMotor controlMotor,
	                         K[] keys, int[] positions) {
		this(controlMotor, -1, keys, positions);
	}
	
	/**
	 * @param  positions  The positions to store in measured as tick positions
	 */
	public RestorePointMotor(IAdvancedStateMotor controlMotor, int speed,
	                         K[] keys, int[] positions) {
		super(keys.length);
		this.controlMotor = controlMotor;
		this.speed = speed;
		addPositions(keys, positions);
	}
	
	
	/**
	 * @param  positions  The positions to store in measured as tick positions
	 */
	public RestorePointMotor(IAdvancedStateMotor controlMotor,
	                         Map<K, Integer> positions) {
		this(controlMotor, -1, positions);
	}
	
	/**
	 * @param  positions  The positions to store in measured as tick positions
	 */
	public RestorePointMotor(IAdvancedStateMotor controlMotor, int speed,
	                         Map<K, Integer> positions) {
		super(positions);
		this.speed = speed;
		this.controlMotor = controlMotor;
	}
	
	
	
	
	// contructor helper method, Arrays.asList would not work here because we
	// need the wrapper class
	private void addPositions(K[] keys, int[] positions) {
		for(int i = 0; i < keys.length; ++i) {
			put(keys[i], Integer.valueOf(positions[i]));
		}
	}
	
	
	
	/**
	 * Gets the stored motor position at a specific location, and move to it,
	 * "restores" the motor position to that archived point.
	 * 
	 * @param  speed     Speed to move to position in tick per second
	 * @param  blocking  If true, the function doesn't return until done
	 * @see    #get
	 */
	public void setToPosition(K position, int speed, boolean blocking) {
		((IBlockingAdvancedStateMotor)controlMotor).setPositionSpeed(
			get(position).intValue(), speed, blocking
		);
	}
	
	/**
	 * Gets the stored motor position at a specific location, and move to it,
	 * "restores" the motor position to that archived point. This is
	 * nonblocking, it returns before it is done moving.
	 * 
	 * @param  speed     Speed to move to position in tick per second
	 * @see    #get
	 */
	public void setToPosition(K position, int speed) {
		controlMotor.setPositionSpeed(get(position).intValue(), speed);
	}
	
	/**
	 * Gets the stored motor position at a specific location, and move to it,
	 * "restores" the motor position to that archived point.
	 * 
	 * @param  blocking  If true, the function doesn't return until done
	 * @see    #get
	 */
	public void setToPosition(K position, boolean blocking) {
		if(speed < 0) {
			((IBlockingAdvancedStateMotor)controlMotor).setPosition(
				get(position).intValue(), blocking
			);
		}
		this.setToPosition(position, speed, blocking);
	}
	
	/**
	 * Gets the stored motor position at a specific location, and move to it,
	 * "restores" the motor position to that archived point. This is
	 * nonblocking, it returns before it is done moving.
	 * 
	 * @see    #get
	 */
	public void setToPosition(K position) {
		this.setToPosition(position, false);
	}
	
	public int getPosition() {
		return controlMotor.getPosition();
	}
	
	/**
	 * Gets the controller motor, as was passed into the constructor
	 */
	public IAdvancedStateMotor getMotor() {
		return controlMotor;
	}
}
