package cbc.create.commands;

import cbc.create.Create;

public class WaitCWAngle implements Command {
	private int deg = 0;
	public WaitCWAngle(int deg) {
		this.deg = -deg;
	}
	public void add(Create create) {
		create.writeByte(157);
		create.writeByte((deg & 0x0000FF00) >> 8);
		create.writeByte((deg & 0x000000FF) >> 0);
	}
}
