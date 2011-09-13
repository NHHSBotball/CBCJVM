package cbc.create.commands;

import cbc.create.Create;

public class Drive implements Command {
	private int speed = 0;
	private int radius = 0;
	public Drive(int speed, int radius) {
		this.speed = speed;
		this.radius = radius;
	}
	public void add(Create create) {
		create.drive(speed, radius);
	}
}

