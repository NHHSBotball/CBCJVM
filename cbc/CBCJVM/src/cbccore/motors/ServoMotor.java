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

package cbccore.motors;

import cbccore.motors.Servo;

/**
 * An easy way to control a servo.
 * 
 * 
 * @author Braden McDorman
 * @see cbccore.motors.Servo
 * 
 */
public class ServoMotor {
	private long begin = 0;
	private int ms = 0;
	private int delta = 0;
	private int curPos = 0;
	private boolean moving = false;
	private Servo servo = null;
	/**
	 * Constructs a new servo motor object taking in a servo to control.
	 * 
	 * @param  servo  the servo to control
	 */
	public ServoMotor(Servo servo) {
		this.servo = servo;
	}
	/**
	 * Returns a boolean that tells if the servo is moving or not.
	 * 
	 * @return  True is moving, false is not moving
	 */
	public boolean isMoving() {
		return moving;
	}
	/**
	 * Moves to a new servo position in a designated amount of time in milliseconds
	 * @param  ms      the allotted amount of time to move
	 * @param  newPos  the new servo position
	 */
	public void moveToTime(int ms, int newPos) {
		curPos = servo.getPosition();
		delta = newPos - curPos;
		if(delta == 0) return;
		begin = System.currentTimeMillis();
		moving = true;
		this.ms = ms;
		ServoMotorThread.get().addServoMotor(this);
	}
	
	/**
	 * Moves to a new servo position at a given speed (ticks per second)
	 * @param tps		the speed in servo ticks per second
	 * @param newPos	the new servo position
	 */
	public void moveToSpeed(int tps, int newPos) {
		int ms = Math.abs(((newPos - servo.getPosition()) / tps) * 1000);
		moveToTime(ms, newPos);
	}
	
	/**
	 * Do not use.
	 */
	public void update() {
		if (!moving)
			return;

		if (System.currentTimeMillis() > begin + ms) {
			servo.setPosition(curPos + delta);
			moving = false;
			return;
		}

		double frac = ((double) delta / (double) ms);
		int y = (int) (frac * (System.currentTimeMillis() - begin) + curPos);
		
		if ((delta > 0 && y > curPos + delta) || (delta < 0 && y < curPos + delta)) {
			y = curPos + delta;
			moving = false;
		}
		servo.setPosition(y);
	}
	/**
	 * Return The servo object that is being controlled.
	 * @return  The servo object being controlled
	 */
	public Servo getServo() {
		return servo;
	}
}
