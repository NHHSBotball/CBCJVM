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

package cbccore.motors.statemotors.composed;

import cbccore.motors.statemotors.IBlockingAdvancedStateMotor;
import cbccore.motors.statemotors.offset.OffsetBlockingAdvancedStateMotor;

/**
 * 
 * @author Benjamin Woodruff
 *
 */

public class ComposedBlockingAdvancedStateMotor
             <E extends IBlockingAdvancedStateMotor>
             extends ComposedAdvancedStateMotor<E>
             implements IBlockingAdvancedStateMotor {
	
	public ComposedBlockingAdvancedStateMotor(E ... motors) {
		super(motors);
	}
	
	public ComposedBlockingAdvancedStateMotor(E[] motors, int ... offsets) {
		super(motors, offsets);
	}
	
	protected E offsetMotorFactory(E baseMotor, int offset) {
		return (E)(new OffsetBlockingAdvancedStateMotor(baseMotor, offset));
	}
	
	public void setPositionTime(int pos, int ms, boolean blocking) {
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			m.setPositionTime(pos, ms);
		}
		waitForMotors(blocking);
	}
	
	public void setPositionTime(int pos, double sec, boolean blocking) {
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			m.setPositionTime(pos, sec);
		}
		waitForMotors(blocking);
	}
	
	public void setPositionSpeed(int pos, int speed, boolean blocking) {
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			m.setPositionSpeed(pos, speed);
		}
		waitForMotors(blocking);
	}
	
	public void setPosition(int pos, boolean blocking) {
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			m.setPosition(pos);
		}
		waitForMotors(blocking);
	}
	
	/**
	 * Doesn't return until all the motors have moved, if <code>blocking</code>
	 * is <code>true</code>. If <code>blocking</code> is <code>false</code>,
	 * this function returns immediately.
	 * 
	 * @param  blocking  If <code>false</code>, the function immediately
	 *                   returns, if <code>true</code>, the function waits for
	 *                   all motors to finish moving before returning.
	 */
	protected void waitForMotors(boolean blocking) {
		if(!blocking) {
			return;
		}
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			while(m.isMoving()) {
				Thread.yield();
			}
		}
	}
	
	/**
	 * Returns <code>true</code> if at least one of the child motors is moving,
	 * <code>false</code> if none of them are moving.
	 */
	public boolean isMoving() {
		for(IBlockingAdvancedStateMotor m: getMotors()) {
			if(m.isMoving()) {
				return true;
			}
		}
		return false;
	}
}
