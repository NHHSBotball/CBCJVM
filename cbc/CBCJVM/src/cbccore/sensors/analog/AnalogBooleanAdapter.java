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

package cbccore.sensors.analog;

import cbccore.sensors.digital.IBooleanSensor;
import cbccore.low.Input;

/**
 * Allows you to use an Analog sensor in a boolean (digital) fashion. If a
 * sensor value goes over or under a specified value, an event will be emitted.
 */
public class AnalogBooleanAdapter implements IBooleanSensor {
	private Analog sensor = null;
	private int pivot = 0;
	private boolean reverseCondition = false;
	/**
	 * @param  sensor  The analog sensor to watch
	 * @param  pivot   The point where the boolean value "pivot"s around, if the
	 *                 value goes below this, false is returned otherwise true
	 *                 is returned<p/>
	 *                 If you want to do <code>x >= 50</code> then your pivot
	 *                 value would be 50. You can use boolean algebra to form
	 *                 pivot values for other equations.				
	 * @see cbccore.sensors.analog.Analog
	 */
	public AnalogBooleanAdapter(Analog sensor, int pivot) {
		this.sensor = sensor;
		this.pivot = pivot;
	}
	
	/**
	 * @param  sensor  The analog sensor to watch
	 * @param  pivot   The point where the boolean value "pivot"s around, if the
	 *                 value goes below this, false is returned otherwise true
	 *                 is returned<p/>
	 *                 If you want to do <code>x >= 50</code> then your pivot
	 *                 value would be 50. You can use boolean algebra to form
	 *                 pivot values for other equations.	
	 * @param reverseCondition	if this is set true, than when the sensor input 
	 *				is greater than the pivot, it will return false, but
	 *				when it is less than the pivot, it will return true		
	 * @see cbccore.sensors.analog.Analog
	 */
	public AnalogBooleanAdapter(Analog sensor,int pivot,boolean reverseCondition) {
		this.sensor = sensor;
		this.pivot = pivot;
		this.reverseCondition = reverseCondition;	
	}
	public Analog getAnalog() {
		return sensor;
	}
	
	public boolean getReverseCondition() {
		return reverseCondition	;
	}

	/**
	 * @param reverseCondition	if this is set true, than when the sensor input 
	 *				is greater than the pivot, it will return false, but
	 *				when it is less than the pivot, it will return true
	 */				
	public void setReverseCondition(boolean reverseCondition) {
		this.reverseCondition = reverseCondition;
	}

	/**
	 * The point where the boolean value "pivot"s around, if the value goes
	 * below this, false is returned otherwise true is returned.
	 * 
	 * @return The "pivot" value
	 */
	public int getPivot() {
		return pivot;
	}
	
	/**
	 * The point where the boolean value "pivot"s around, if the value goes
	 * below this, false is returned otherwise true is returned.
	 * 
	 * @param  pivot  The point where the boolean value "pivot"s around, if the
	 *                value goes below this, false is returned otherwise true is
	 *                returned<p/>
	 *                If you want to do <code>x >= 50</code> then your pivot
	 *                value would be 50. You can use boolean algebra to form
	 *                pivot values for other equations.
	 */
	public void setPivot(int pivot) {
		this.pivot = pivot;
	}
	
	@Override
	public boolean getValue() {
		if(sensor.getValueHigh() < pivot) return reverseCondition;
		return !reverseCondition;
	}
	
	/**
	 * Preforms automatic calibration of sensor based on user input.
	 */
	public void calibrateSensor() throws Exception {
		Input in = new cbccore.low.Input();	//setup input from black_button
		int threshold = 0;

		System.out.println("Place sensor in true condition and press button");
		while(in.black_button()==0){java.lang.Thread.yield();}
		while(in.black_button()==1){java.lang.Thread.yield();}	//wait for button to be pressed and released
		int trueval = sensor.getValueHigh();
	
		System.out.println("Place sensor in false condition and press button");
		while(in.black_button()==0){java.lang.Thread.yield();}	//wait for button to be pressed and released
		while(in.black_button()==1){java.lang.Thread.yield();}
		int falseval = sensor.getValueHigh();

		if(trueval>falseval)
		{
			threshold=trueval+(2/(falseval-trueval)); //set set threshold to halfway inbetween high
								// and low ranges
			reverseCondition=false;			//don't reverse
		}	
		if(falseval>trueval)
		{
			threshold=falseval+(2/(trueval-falseval));
			reverseCondition=true;			//reverse true and false
		}
		if(falseval==trueval)				//they shouldn't be equal
			throw new Exception("Bad Calibration! High and low range values are both at "+trueval);
		pivot=threshold;				//set pivot
		System.out.println("The high value/range is "+trueval);
		System.out.println("The low value/range is "+falseval);
		System.out.println("The threshold value is "+threshold);
	}
}
