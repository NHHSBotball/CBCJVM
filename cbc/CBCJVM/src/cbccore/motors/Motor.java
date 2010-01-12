package cbccore.motors;

import cbccore.Device;
import cbccore.InvalidPortException;

public class Motor {
	private int port = 0;
	private cbccore.low.Motor lowMotor = Device.getLowMotorController();
	
	public Motor(int port) throws InvalidPortException {
		if(port < 0 || port > 4) throw new InvalidPortException();
		this.port = port;
	}
	public void motor(int percent) {
		lowMotor.motor(port, percent);
	}

	public int clearPositionCounter() {
		return lowMotor.clear_motor_position_counter(port);
	}

	public int moveAtVelocity(int velocity) {
		return lowMotor.mav(port, velocity);
	}

	public int moveToPosition(int speed, int goal_pos) {
		return lowMotor.mtp(port, speed, goal_pos);
	}

	public int moveRelativePosition(int speed, int delta_pos) {
		return lowMotor.mrp(port, speed, delta_pos);
	}

	public void setPidGains(int p, int i, int d, int pd, int id, int dd) {
	}

	public int freeze() {
		return lowMotor.freeze(port);
	}

	public int getDone() {
		return lowMotor.get_motor_done(port);
	}

	public int getPositionCounter() {
		return lowMotor.get_motor_position_counter(port);
	}

	public void blockMotorDone() {
		lowMotor.bmd(port);
	}

	public int setPwm(int pwm) {
		return lowMotor.setpwm(port, pwm);
	}

	public int getPwm() {
		return lowMotor.getpwm(port);
	}

	public void forward() {
		lowMotor.fd(port);
	}

	public void backward() {
		lowMotor.bk(port);
	}

	public void off() {
		lowMotor.off(port);
	}
}
