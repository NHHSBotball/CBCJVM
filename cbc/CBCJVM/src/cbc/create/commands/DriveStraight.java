package cbc.create.commands;

import cbc.create.Create;

public class DriveStraight implements Command {
	private int speed = 0;
	public DriveStraight(int speed) {
		this.speed = speed;
	}
	public void add(Create create) {
		create.driveStraight(speed);
	}
}
