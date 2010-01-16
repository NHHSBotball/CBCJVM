package cbccore.low.controllers;

public class MotorController {
	private int port;
	public MotorController(int port) throws InvalidPortException {
		if(port < 0 || port > 4)
		{
			throw new InvalidPortException();
		}
		this.port = port;
	}
	public void moveAtPercent(int percent) throws InvalidPercentageException
	{
		if(percent > 100 || percent < -100)
		{
			throw new InvalidPercentageException();
		}
		CBC.motor.motor(this.port, percent);
	}
	public void moveToPosition(int speed, int goal)
	{
		if(speed > 1000 || speed < -1000)
		{
			throw new InvalidValueException();
		}
	}
	public void moveAtVelocity(int velocity) throws InvalidValueException
	{
		if(velocity > 1000 || velocity < -1000)
		{
			throw new InvalidValueException();
		}
		CBC.motor.mav(this.port, velocity);
	}
	public void moveRelativePosition(int speed, int delta) throws InvalidValueException
	{
		if(speed > 1000 || speed < -1000)
		{
			throw new InvalidValueException();
		}
		CBC.motor.mrp(this.port, speed, delta);
	}
	public void blockMotorDone()
	{
		CBC.motor.bmd(this.port);
	}
	public int getPositionCounter()
	{
		return CBC.motor.get_motor_position_counter(this.port);
	}
	public void stop()
	{
		CBC.motor.off(this.port);
	}
	public void freeze()
	{
		CBC.motor.freeze(this.port);
	}
}