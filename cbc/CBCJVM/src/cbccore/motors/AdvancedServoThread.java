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

import java.util.ArrayList;

public class AdvancedServoThread extends Thread {
	public static AdvancedServoThread get() {
		if (instance == null) {
			instance = new AdvancedServoThread();
			instance.start();
		}
		return instance;
	}
	
	private ArrayList<AdvancedServo> advancedServos =
	                                             new ArrayList<AdvancedServo>();

	private boolean exit = false;

	private static AdvancedServoThread instance = null;

	public AdvancedServoThread() {
		setDaemon(true);
	}

	public void addAdvancedServo(AdvancedServo AdvancedServo) {
		synchronized(advancedServos) {
			//System.out.println("Added servo motor.");
			advancedServos.add(AdvancedServo);
		}
	}

	public void exit() {
		exit = true;
	}

	@Override
	public void run() {
		ArrayList<AdvancedServo> removes = new ArrayList<AdvancedServo>();
		while (!exit) {
			removes.clear();
			synchronized(advancedServos) {
				for(AdvancedServo advancedServo : advancedServos) {
					advancedServo.update();
					if (!advancedServo.isMoving()) {
						removes.add(advancedServo);
					}
				}
				advancedServos.removeAll(removes);
			}
		}
	}
}
