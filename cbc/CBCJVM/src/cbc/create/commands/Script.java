package cbc.create.commands;

import cbc.create.Create;

public class Script implements Command {

	@Override
	public void add(Create create) {
		create.writeByte(152);
	    create.writeByte(5);
	}

}
