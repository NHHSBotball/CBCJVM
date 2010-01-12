package cbccore.sensors.buttons;

import cbccore.Device;
import cbccore.sensors.IBooleanSensor;

public class DownButton implements IBooleanSensor {
	private cbccore.low.Input lowInput = Device.getLowInputController();
	@Override
	public boolean getValue() {
		return lowInput.down_button() == 1;
	}
}
