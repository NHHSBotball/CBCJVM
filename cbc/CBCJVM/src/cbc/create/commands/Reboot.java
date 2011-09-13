package cbc.create.commands;

import cbc.create.Create;

public class Reboot implements Command {

	@Override
	public void add(Create create) {
		create.writeByte(7);
	}

}
