package cbc.create.commands;

import cbc.create.Create;

public class Stop implements Command {
	public void add(Create create) {
		create.stop();
	}
}
