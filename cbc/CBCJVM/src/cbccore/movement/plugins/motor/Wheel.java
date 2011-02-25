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

package cbccore.movement.plugins.motor;

import cbccore.motors.Motor;

/**
 * A wheel class used by MotorDriveTrain
 * 
 * @author Benjamin Woodruff
 * @author Jonathan Frias
 */

public class Wheel extends Motor {
	
	//public final double WHEEL = 0; must find the actual circumferences
	
	protected double efficiency;
	protected double circumference;
	private double maxRps;
	private double maxCmps;
	private int currentTps;
	
	public Wheel(int port, double circumference, double efficiency) {
		super(port);
		this.circumference = circumference;
		this.efficiency = efficiency;
		this.maxRps = 1000./MotorMovementPlugin.ticksPerRotation*efficiency;
		this.maxCmps = maxRps*getCircumference();
	}
	
	public double getMaxRps() {
		return maxRps;
	}
	
	public double getMaxCmps() {
		return maxCmps;
	}
	
	public int currentTps() {
	    return currentTps;
	}
	
	public double currentRps() {
		return currentTps/MotorMovementPlugin.ticksPerRotation;
	}
	
	public double getCircumference() {
		return circumference;
	}
	
	protected void checkTpsRange(int tps) throws IllegalArgumentException {
		if(Math.abs(tps) > (maxRps*MotorMovementPlugin.ticksPerRotation)) {
			throw new IllegalArgumentException(tps + "tps is out of range");
		}
	}
	
	public void moveAtTps(int tps) throws IllegalArgumentException {
		checkTpsRange(tps);
		currentTps = tps;
		clearPositionCounter(); //work-around for CBOBv2 motor bug
		super.moveAtVelocity((int)(tps/efficiency));
	}
	
	public void moveAtRps(double rps) throws IllegalArgumentException {
		moveAtTps((int)(rps*MotorMovementPlugin.ticksPerRotation));
	}
	
	public void moveAtCmps(double cmps) throws IllegalArgumentException {
		moveAtRps(cmps/getCircumference());
	}
	
	public int getTickCounter() {
		return getPositionCounter();
	}
	
	public double getWheelRotationCounter() {
		return ((double)getTickCounter())/MotorMovementPlugin.ticksPerRotation;
	}
	
	public double getCmCounter() {
		return getWheelRotationCounter()*getCircumference();
	}
	
	/**
	 * Depreciated
	 */
	public int moveAtVelocity(int tps) {
		moveAtTps(tps);
		return 0;
	}
}
