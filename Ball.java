import java.io.Serializable;
import java.awt.*;
import java.awt.geom.*;

/**
 * Ball class to represent any ball in the game.
 */
public class Ball implements Serializable {
	private Image image;
	private ScreenPoint point;
	private GridPoint destination;
	private Velocity velocity;
	private int size;

	public String toString() {
		return "(Ball) image=" + image + ", point=" + point
			+ ", velocity=" + velocity + ", size=" + size;
	}

	/**
	 * Class constructor.
	 * @param image image for ball
	 * @param size the diameter of the ball
	 * @param p the position of the ball
	 * @param v the velocity of the ball
	 */
	public Ball(Image image, int size, ScreenPoint p, Velocity v) {
		point = p;
		velocity = v;

		this.image = image;
		this.size = size;
	}

	public Ball(Image image, int size, GridPoint p, Velocity v) {
		this.image = image;
		this.size = size;
		velocity = v;
		
		setGridPoint(p);
	}

	public Ball(Image image, int size, ScreenPoint p) {
		this(image, size, p, new Velocity(0, 0));
	}

	public Ball(Image image, int size, GridPoint p) {
		this(image, size, p, new Velocity(0, 0));
	}

	public Ball(Image image, int size) {
		this(image, size, new ScreenPoint(0, 0));
	}

	public Ball(Ball b) {
		this(b.getImage(), b.getSize(),
			new ScreenPoint(b.getScreenPoint()),
			new Velocity(b.getVelocity()));
	}

	/**
	 * Set the image.
	 * @param image the image for the ball
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Get the image for this ball.
	 * @return image for this ball
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Get a Point which represents the location.
	 * @return Point for location
	 */
	public ScreenPoint getScreenPoint() {
		return point;
	}

	/**
	 * Get the diameter of the ball image.
	 * @return the diameter of the ball.
	 */
	public int getSize() {
		return size;
	}

	public void setGridPoint(GridPoint p) {
		double x;
		double y = (p.getY() * size * 0.5 * Math.sqrt(3));

		if (p.getY() % 2 == 1) {
			x = ((p.getX() + 0.5) * size);
		} else {
			x = (p.getX() * size);
		}
		point = new ScreenPoint(x, y);
	}

	public GridPoint getGridPoint() {
		int x;
		int y = (int) (point.getY()
			/ (size * 0.5 * Math.sqrt(3)) + 0.5);

		if (y % 2 == 1) {
			x = (int) (point.getX() / size - 0.5 + 0.5);
		} else {
			x = (int) (point.getX() / size + 0.5);
		}

		return new GridPoint(x, y);
	}

	public void snapToGrid() {
		setGridPoint(getGridPoint());
	}

	public void setVelocity(Velocity v) {
		velocity = v;
	}

	public Velocity getVelocity() {
		return velocity;
	}

	public void move() {
		point.setX(point.getX() + velocity.getDX());	
		point.setY(point.getY() + velocity.getDY());	
	}

	public void bounce() {
		velocity.setAngle(- velocity.getAngle());
	}

	public ScreenPoint getNextScreenPoint() {
		return new ScreenPoint(point.getX() + velocity.getDX(),
			point.getY() + velocity.getDY());
	}
	
	public void setDestination(GridPoint p, double speed) {
		destination = p;
		Ball b = new Ball(this);
		b.setGridPoint(destination);
		ScreenPoint bp = b.getScreenPoint();
		double dx = bp.getX() - point.getX();
		double dy = bp.getY() - point.getY();
		double angle = 180 / Math.PI * Math.atan(dy / dx) + 90;
		if (angle > 90) angle -= 180;
		setVelocity(new Velocity(angle, speed));
	}

	public GridPoint getDestination() {
		return destination;
	}

	public boolean isAtDestination() {
		return getGridPoint().equals(destination);
	}
}
