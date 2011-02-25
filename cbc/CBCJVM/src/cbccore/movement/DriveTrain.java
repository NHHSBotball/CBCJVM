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

package cbccore.movement;

import cbccore.easing.EasingEquation;
import cbccore.movement.plugins.MovementPlugin;

/**
 * A pluggable movement library. Fill in a few low-level fields by extending
 * the abstract MovementPlugin class, and this class will do the rest. You can
 * extend this class to more advanced things, and thanks to the pluggable
 * architecture, people will be able to use your version with their own plugins.
 * 
 * @author Benjamin Woodruff
 * @author Grady Bryson
 * @see	cbccore.movement.plugins
 * @see	cbccore.movement.plugins.MovementPlugin
 */

public class DriveTrain {
	
	public static final int DISABLED = 0;
	public static final int PERCENT_DISTANCE = 1;
	public static final int PERCENT_TIME = 2;
	public static final int CONSTANT_DISTANCE = 3;
	public static final int CONSTANT_TIME = 4;
	
	private MovementPlugin plugin;
	private double oldAngle;
	private double oldX;
	private double oldY;
	private double leftCmps;
	private double rightCmps;
	private long oldTime;
	
	public DriveTrain(MovementPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Rotates the device a specified number of degrees Counter-Clockwise
	 * 
	 * @param  degrees   The desired change in degrees
	 * @see              #rotateRadians
	 */
	public void rotateDegrees(double degrees, double degreesPerSecond) {
		rotateRadians(Math.toRadians(degrees), Math.toRadians(degreesPerSecond));
	}
	
	
	/**
	 * Rotates the device a specified number of radians Counter-Clockwise
	 * 
	 * @param  radians           The desired change in radians
	 * @param  radiansPerSecond  The number of radians to rotate every second
	 * @see                      #rotateDegrees
	 */
	public void rotateRadians(double radians, double radiansPerSecond) {
		double dist = plugin.getTrainWidth()*radians*.5;
		double speed = radiansPerSecond*plugin.getTrainWidth();
		moveWheelCm(-dist, dist, -speed, speed);
	}
	
	/**
	 * Moves the robot in a piece of a circle.
	 * 
	 * @param  degrees  The piece of the circle defined as a change in the
	 *                      robot's rotation in degrees
	 * @param  radius   The radius of the circle.
	 * @param  cmps     The Centimeters-Per-Second of the center of the robot.
	 *                      The maximum value for this is less than maxCmps
	 * @see             #moveCurveRadians
	 */
	public void moveCurveDegrees(double degrees, double radius, double cmps) {
		moveCurveRadians(Math.toRadians(degrees), radius, cmps);
	}
	
	
	/**
	 * Moves the robot in a piece of a circle.
	 * 
	 * @param  radians  The piece of the circle defined as a change in the
	 *                      robot's rotation in radians
	 * @param  radius   The radius of the circle.
	 * @param  cmps     The Centimeters-Per-Second of the center of the robot.
	 *                      The maximum value for this is less than maxCmps
	 * @see             #moveCurveDegrees
	 */
	public void moveCurveRadians(double radians, double radius, double cmps) {
		
		//method start
		double halfOffset = plugin.getTrainWidth()*radians*.5;
		double cm = radians*radius;
		double leftCm = cm - halfOffset;
		double rightCm = cm + halfOffset;
		double timeOfTrip = cm/cmps;
		double leftCmps = leftCm/timeOfTrip;
		double rightCmps = rightCm/timeOfTrip;
		moveWheelCm(leftCm, rightCm, leftCmps, rightCmps);
	}
	
	
	
	/**
	 * Moves robot forward a certain number of centimeters at a set speed. If
	 * you specify a negitive number for cm, the robot will move backwards that
	 * many centimeters.
	 * 
	 * @param  cm     Desired distance in centimeters
	 * @param  cmps   Desired speed in centimeters-per-second
	 */
	public void moveCm(double cm, double cmps) {
		moveWheelCm(cm, cm, cmps, cmps);
	}
	
	/**
	 * Utility to make cmps match the sign of cm.
	 */
	protected static double moveParser(double cm, double cmps) { //returns new cmps
		//cmps is made to match cm's sign
		return cm<0?0.-Math.abs(cmps):Math.abs(cmps);
	}
	
	
	/**
	 * Moves each wheel at a constant speed, and is used as the basis for
	 * moveWheelCm
	 */
	private void moveWheelCmConstant(double leftCm, double rightCm,
	                                 double leftCmps, double rightCmps,
	                                 long milliseconds)
	                                 throws InterruptedException {
		
		// do all calculations at front to prevent delays
		leftCmps = moveParser(leftCm, leftCmps);
		rightCmps = moveParser(rightCm, rightCmps);
		// actually call the support functions
		directDrive(leftCmps, rightCmps);
		Thread.sleep(milliseconds); // let our parent handle an
		                            // InterruptedException
	}
	
	//leftCm/leftCmps must be equal to rightCm/rightCmps
	protected void moveWheelCm(double leftCm, double rightCm, //positions
	                           double leftCmps, double rightCmps) { //speed
		
		leftCmps = moveParser(leftCm, leftCmps);
		rightCmps = moveParser(rightCm, rightCmps);
		long milliseconds = ((long)((leftCm/leftCmps)*1.0e3));
		try {
			moveWheelCmConstant(leftCm, rightCm, leftCmps, rightCmps,
			                    milliseconds);
		} catch(InterruptedException ex) {}
		stop();
	}
	
	/**
	 * Can be used to manually control the speed of each drive wheel. Every
	 * movement call eventually gets run through here, so if one wanted to make
	 * a fundamental change to the movement, this or moveWheelCm would be the
	 * methods to override.
	 * 
	 * 
	 * @param  leftCmps   The desired speed of the left wheel in
	 *                        centimeters-per-second
	 * @param  rightCmps  The desired speed of the right wheel in
	 *                        centimeters-per-second
	 */
	public void directDrive(double leftCmps, double rightCmps) {
		plugin.directDrive(leftCmps, rightCmps);
		updateOldPos();
		this.leftCmps = leftCmps; this.rightCmps = rightCmps;
	}
	
	
	// By putting this all in one function, we can minimize duplicate
	// calculations. Adapted from
	// http://rossum.sourceforge.net/papers/DiffSteer/DiffSteer.html
	// Thanks for directing me to it Jeremy!
	
	/**
	 * Solves for the specific position of the robot at this moment in time.
	 * Adapted from
	 * <a href="http://rossum.sourceforge.net/papers/DiffSteer/DiffSteer.html">
	 * here</a>.
	 */
	public DriveTrainPosition getPosition() {
		double newX, newY, newAngle;
		double seconds = (double)(System.currentTimeMillis()-oldTime)*1e-3;
		newAngle = seconds*(rightCmps-leftCmps)/plugin.getTrainWidth();
		double centerDist = seconds*(rightCmps+leftCmps)*.5;
		// minimizes duplicate calculations
		newX = oldX + centerDist*Math.cos(newAngle);
		newY = oldY + centerDist*Math.sin(newAngle);
		newAngle += oldAngle;
		
		return new DriveTrainPosition(newX, newY, newAngle);
	}
	
	/**
	 * Read <a href="https://github.com/CBCJVM/CBCJVM/wiki/Position-Tracking">
	 * this</a>.
	 */
	protected void updateOldPos() {
		DriveTrainPosition pos = getPosition();
		oldX = pos.getX();
		oldY = pos.getY();
		oldAngle = pos.getRawAngleRadians();
		oldTime = System.currentTimeMillis();
	}
	
	
	/**
	 * It's always handy to have a method like this. Just moves the robot at a
	 * set speed, so say you wanted to back into some PVC to line yourself up,
	 * this could help.
	 * 
	 * @param  cmps   The speed for the robot to move in centimeters-per-second
	 * @see           #directDrive
	 */
	public void moveAtCmps(double cmps) {
		directDrive(cmps, cmps);
	}
	
	
	public void stop() {
		plugin.stop();
		updateOldPos();
		leftCmps = 0; rightCmps = 0;
		//we don't need to change oldTime, cause speed is 0 :-)
	}
	
	
	public void freeze() {
		plugin.freeze();
		updateOldPos();
		leftCmps = 0; rightCmps = 0;
	}
	
	
	public void kill() {
		plugin.kill();
		updateOldPos();
		leftCmps = 0; rightCmps = 0;
	}
	
	
	/**
	 * Gets the maximum speed in centimeters-per-second for the robot.
	 * 
	 * @return      The maximum speed in centimeters-per-second for the robot.
	 * @see         #getMaxRadiansPerSec
	 * @see         cbccore.movement.plugins.MovementPlugin#getLeftMaxCmps
	 * @see         cbccore.movement.plugins.MovementPlugin#getRightMaxCmps
	 */
	public double getMaxCmps() {
		return Math.min(plugin.getLeftMaxCmps(), plugin.getRightMaxCmps());
	}
	
	
	/**
	 * Gets the maximum speed that the robot could turn in place
	 * 
	 * @return    The maximum speed in radians-per-second
	 * @see       #getMaxDegreesPerSec
	 * @see       #getMaxCmps
	 * @see       cbccore.movement.plugins.MovementPlugin#getLeftMaxCmps
	 * @see       cbccore.movement.plugins.MovementPlugin#getRightMaxCmps
	 */
	public double getMaxRadiansPerSec() {
		return Math.min(plugin.getLeftMaxCmps(), plugin.getRightMaxCmps())
		       / plugin.getTrainWidth();
	}
	
	
	/**
	 * Gets the maximum speed that the robot could turn in place
	 * 
	 * @return     The maximum speed in degrees-per-second
	 * @see        #getMaxRadiansPerSec
	 * @see        #getMaxCmps
	 * @see        cbccore.movement.plugins.MovementPlugin#getLeftMaxCmps
	 * @see        cbccore.movement.plugins.MovementPlugin#getRightMaxCmps
	 */
	public double getMaxDegreesPerSec() {
		return Math.toDegrees(getMaxRadiansPerSec());
	}
	
	
	/**
	 * Gets the maximum speed when moving in a curve
	 * DOES NOT YET WORK
	 * 
	 * @param   radius  paraminfo
	 * @return  The maximum speed of the center of the robot in
	 *              centimeters-per-second
	 * @see      #moveCurveRadians
	 * @see      #moveCurveDegrees
	 */
	public double getMaxCmps(double radius) {
		double outerMaxSpeed = (radius>0 ? plugin.getRightMaxCmps() : plugin.getLeftMaxCmps()) - (radius+plugin.getTrainWidth()*.5)/radius;
		double innerMaxSpeed = (radius>0 ? plugin.getLeftMaxCmps() : plugin.getRightMaxCmps()) + (radius+plugin.getTrainWidth()*.5)/radius;
		return Math.min(outerMaxSpeed, innerMaxSpeed);
	}
}
