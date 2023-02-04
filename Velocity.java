/**
 * Velocity class to represent the velocity (angle and speed).
 */
public class Velocity {
	private double angle;
	private double speed;

	/**
	 * Constructor.
	 * @param angle angle (degrees) of movement
	 * @param speed rate of movement
	 */
	public Velocity(double angle, double speed) {
		this.angle = angle;
		this.speed = speed;
	}

	/**
	 * Constructor.
	 * @param v velocity
	 */
	public Velocity(Velocity v) {
		this.angle = v.angle;
		this.speed = v.speed;
	}

	/** 
	 * Get the angle of velocity.
	 * @return angle of velocity (degrees).
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Get the speed of the velocity.
	 * @return speed of velocity.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Set the angle of velocity.
	 * @param angle angle of velocity (degrees).
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * Set the speed of the velocity.
	 * @param speed speed of velocity.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Get the x differential.
	 * @return x differential
	 */
	public double getDX() {
		return Math.cos((angle - 90) * Math.PI / 180) * speed;
	}

	/**
	 * Get the y differential.
	 * @return y differential
	 */
	public double getDY() {
		return Math.sin((angle -90) * Math.PI / 180) * speed;
	}

	public String toString() {
		return "(Velocity) angle=" + angle + ", speed=" + speed;
	}
}
