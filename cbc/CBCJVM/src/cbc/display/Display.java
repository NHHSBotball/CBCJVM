package cbc.display;

import java.io.File;

import cbc.Device;

public class Display {
	private static Framebuffer fb0 = Device.isOnCBC() ? new Framebuffer(new File("/dev/fb0")) : new SimulatedFramebuffer("fb0");
	private static Touchscreen ts0 = Device.isOnCBC() ? new Touchscreen(new File("/dev/input/ts0")) : null;
	public static Framebuffer getFramebuffer() {
		if(!Device.isOnCBC()) Device.getSimulatorController().addFramebuffers();
		return fb0;
	}
	public static Touchscreen getTouchscreen() {
		return ts0;
	}
}
