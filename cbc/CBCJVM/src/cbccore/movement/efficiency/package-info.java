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


/**
 * This package is used to define motor efficiency (in the form used by
 * <code>cbccore.movement.DriveTrain</code>) as a function of velocity! Using
 * this package properly, you should be able to make your robot move
 * <b>really</b> accurately. This whole package is really just an OCD person's
 * wet dream.<p/>
 * 
 * I seriously doubt you'll be able to solve for all the calibration data most
 * of these classes need by hand. Our team built a completely automated
 * calibration rig that can find calibration values over the equivalent course
 * of a hundred meters or so! I'll probably make a write-up on it on the CBCJVM
 * wiki, so stay tuned.
 * 
 * @author Benjamin Woodruff
 * @see    cbccore.movement.DriveTrain
 */
package cbccore.movement.efficiency;
