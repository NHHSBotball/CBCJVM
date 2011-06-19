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

import cbccore.movement.plugins.MovementPlugin;
import cbccore.config.ChoiceConfigurator;
import cbccore.sensors.digital.IBooleanSensor;
import cbccore.config.Choices;
import cbccore.sensors.buttons.AButton;
import cbccore.sensors.buttons.BButton;

/**
 * An extension of <code>DriveTrain</code>, allowing one to virtually flip the
 * motor movements, while keeping the individual wheel data in tact (such as
 * efficiency calibrations). It does this by overriding
 * <code>DriveTrain</code>'s <code>directDrive</code> method.<p/>
 * 
 * This class is designed for the 2011 Botball season, where the board layout is
 * flipped depending on what side you are on. Excluding information such as
 * sensor readings, simply using this class on a symmetrical robot should, in
 * theory, should allow you to use a program designed for one side of the board
 * on the other side of the board.<p/>
 * 
 * This class also contains a facilities for disabling or enabling the flipping
 * at runtime, and static tools for querying the user as to enable flipping or
 * not.
 * 
 * @author Benjamin Woodruff
 * @see	cbccore.movement.DriveTrain
 */

public class FlippedDriveTrain extends DriveTrain {
	
	private boolean flipped;
	
	/**
	 * Creates a <code>FlippedDriveTrain</code> instance with flipping enabled.
	 */
	public FlippedDriveTrain(MovementPlugin plugin) {
		this(plugin, true);
	}
	
	/**
	 * Creates a <code>FlippedDriveTrain</code> instance, allowing one to decide
	 * whether or not to actually initially flip the movements.
	 * 
	 * @param  mode  If <code>true</code>, the movements will be flipped, if
	 *               <code>false</code>, they won't.
	 */
	public FlippedDriveTrain(MovementPlugin plugin, boolean mode) {
		super(plugin);
		flipped = mode;
	}
	
	/** @inheritDoc */
	public void directDrive(double leftCmps, double rightCmps) {
		if(isFlipped()) {
			super.directDrive(rightCmps, leftCmps);
		} else {
			super.directDrive(leftCmps, rightCmps);
		}
	}
	
	/**
	 * Returns <code>true</code> if the movement commands are actively being
	 * flipped, <code>false</code> otherwise.
	 */
	public boolean isFlipped() {
		return flipped;
	}
	
	/**
	 * Returns <code>true</code> if the movement commands are actively being
	 * flipped, <code>false</code> otherwise.
	 */
	public boolean getIsFlipped() {
		return isFlipped();
	}
	
	/**
	 * Sets whether or not future movement commands should be flipped. You
	 * should avoid calling this while the robot is moving: it could have
	 * adverse side-effects with more complex movements.<p/>
	 * 
	 * <b>Example usage:</b><br/>
	 * <code>boolean flip = setIsFlipped(false);<br/>
	 * ... // some code that should run normally<br/>
	 * setIsFlipped(flip);</code>
	 * 
	 * @param   mode  If <code>true</code>, movement actions will be flipped, if
	 *                <code>false</code>, movement actions will not be flipped.
	 * @return  The old value of <code>isFlipped()</code>.
	 */
	public boolean setIsFlipped(boolean mode) {
		boolean old = isFlipped();
		flipped = mode;
		return old;
	}
	
	/**
	 * Uses a <code>ChoiceConfigurator</code> to query the user as to whether or
	 * not to flip the movements, and the returns a new corresponding
	 * <code>FlippedDriveTrain</code> using the specified
	 * <code>MovementPlugin</code>.
	 * 
	 * @param  plugin        The <code>MovementPlugin</code> to be used in the
	 *                       construction of the <code>FlippedDriveTrain</code>.
	 * @param  buttonOne     The first button to be used for an option in the
	 *                       <code>ChoiceConfigurator</code>.
	 * @param  buttonTwo     The second button to be used for an option in the
	 *                       <code>ChoiceConfigurator</code>.
	 * @param  flipString    The message to be displayed for the flipped option.
	 * @param  noFlipString  The message to be displayed for the no flipping
	 *                       option.
	 * @see    cbccore.config.ChoiceConfigurator
	 */
	public static FlippedDriveTrain userSelected(MovementPlugin plugin,
	                                             IBooleanSensor buttonOne,
	                                             IBooleanSensor buttonTwo,
	                                             String flipString,
	                                             String noFlipString) {
		Choices choices = new Choices();
		choices.put(0, noFlipString);
		choices.put(1, flipString);
		return new FlippedDriveTrain(plugin, new ChoiceConfigurator(
			new IBooleanSensor[] {buttonOne, buttonTwo}, choices
		).ask() == 1);
	}
	
	/**
	 * Uses a <code>ChoiceConfigurator</code> to query the user as to whether or
	 * not to flip the movements, and the returns a new corresponding
	 * <code>FlippedDriveTrain</code> using the specified
	 * <code>MovementPlugin</code>.<p/>
	 * 
	 * This method uses the A and B buttons in the
	 * <code>ChoiceConfigurator</code>.
	 * 
	 * @param  plugin        The <code>MovementPlugin</code> to be used in the
	 *                       construction of the <code>FlippedDriveTrain</code>.
	 * @param  flipString    The message to be displayed for the flipped option.
	 * @param  noFlipString  The message to be displayed for the no flipping
	 *                       option.
	 * @see    cbccore.config.ChoiceConfigurator
	 */
	public static FlippedDriveTrain userSelected(MovementPlugin plugin,
	                                             String flipString,
	                                             String noFlipString) {
		return userSelected(plugin, new AButton(), new BButton(), flipString,
		                    noFlipString);
	}
	
	/**
	 * Uses a <code>ChoiceConfigurator</code> to query the user as to whether or
	 * not to flip the movements, and the returns a new corresponding
	 * <code>FlippedDriveTrain</code> using the specified
	 * <code>MovementPlugin</code>.<p/>
	 * 
	 * This method uses the A and B buttons in the
	 * <code>ChoiceConfigurator</code>, and uses the option strings, "flip" and
	 * "not flip".
	 * 
	 * @param  plugin        The <code>MovementPlugin</code> to be used in the
	 *                       construction of the <code>FlippedDriveTrain</code>.
	 * @see    cbccore.config.ChoiceConfigurator
	 */
	public static FlippedDriveTrain userSelected(MovementPlugin plugin) {
		return userSelected(plugin, "flip", "not flip");
	}
}
