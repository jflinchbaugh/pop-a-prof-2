import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The Well class represents the playing field.
 */
public class Well extends JPanel {
	private int width;
	private int height;
	private Ball[][] grid;
	private List falling;
	private List rebounding;
	private List rising;
	private List fallen;
	private Ball current;
	int ballSize;
	private Pointer pointer;

	/**
	 * Class constructor.
	 * @param w width of field in balls
	 * @param h height of field in balls
	 * @param ballSize diameter of a ball
	 */
	public Well(int w, int h, int ballSize, Pointer pointer) {
		width = w;
		height = h;
		this.ballSize = ballSize;
		setBackground(Color.black);

		falling = new Vector();
		rebounding = new Vector();
		rising = new Vector();
		fallen = new Vector();
		
		setPointer(pointer);

		adjustSizes();
	}
	
	/**
	 * Get the width in balls.
	 * @return width in balls
	 */
	public int getFieldWidth() {
		return width;
	}

	/**
	 * Set the width in balls.
	 * @param w width in balls
	 */
	public void setFieldWidth(int w) {
		width = w;
		adjustSizes();
	}

	/**
	 * Get the height in balls.
	 * @return height in balls
	 */
	public int getFieldHeight() {
		return height;
	}

	/**
	 * Set the height in balls.
	 * @param h height in balls
	 */
	public void setFieldHeight(int h) {
		height = h;
		adjustSizes();
	}

	/** 
	 * Get the ball size
	 * @return ball diameter
	 */
	public int getBallSize() {
		return ballSize;
	}

	/**
	 * Set the ball size.
	 * @param size ball diameter
	 */
	public void setBallSize(int size) {
		ballSize = size;
		adjustSizes();
	}

	/**
	 * Adjust all the dimensions for new size parameters when they
	 * change.
	 */
	private void adjustSizes() {
		setPreferredSize(new Dimension(ballSize * width,
			(int) (ballSize * (height - 1) * 0.5 * Math.sqrt(3)
			+ ballSize)));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		grid = new Ball[width][height];
	}

	/**
	 * Clear all balls.
	 */
	public void clear() {
		adjustSizes();
	}

	/**
	 * Set the ball at a location in the grid.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param ball the ball to place in that location
	 */
	public void setBall(int x, int y, Ball ball) 
		throws ArrayIndexOutOfBoundsException {
		if (y % 2 == 1 && x >= width - 1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		grid[x][y] = ball;
	}

	public void setBall(Ball ball)
		throws ArrayIndexOutOfBoundsException {
		GridPoint p = ball.getGridPoint();
		setBall((int) (p.getX()), (int) (p.getY()), ball);
	}

	/**
	 * Get the ball at the location.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return ball at that location
	 */
	public Ball getBall(int x, int y) {
		return grid[x][y];
	}

	/**
	 * Paint method.
	 */
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, (int) getWidth(), getHeight());

		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				try {
					paintBall(g, grid[x][y]);
				} catch (Exception ignored) {
				}
			}
		}

		synchronized (falling) {
			Iterator it = falling.iterator();
			while (it.hasNext()) {
				try {
					paintBall(g, (Ball) it.next());
				} catch (Exception ignored) {
					ignored.printStackTrace();
				}
			}
		}

		synchronized (rebounding) {
			Iterator it = rebounding.iterator();
			while (it.hasNext()) {
				try {
					paintBall(g, (Ball) it.next());
				} catch (Exception ignored) {
				}
			}
		}

		synchronized (rising) {
			Iterator it = rising.iterator();
			while (it.hasNext()) {
				try {
					paintBall(g, (Ball) it.next());
				} catch (Exception ignored) {
				}
			}
		}

		try {
			paintBall(g, current);
		} catch (Exception ignored) {
		}

		// draw the pointer;
		Point p = pointer.getCorner(
			new Point(getWidth() / 2, getHeight() - ballSize / 2));
		g.drawImage(pointer.getRotated(),
			(int) p.getX(), (int) p.getY(), this);
	}

	private void paintBall(Graphics g, Ball b) {
		ScreenPoint p = b.getScreenPoint();
		g.drawImage(b.getImage(), (int) p.getX(),
			(int) p.getY(), this);
	}

	public void setPointer(Pointer pointer) {
		this.pointer = pointer;
	}

	public Pointer getPointer() {
		return pointer;
	}

	public void fireDimensionChange() {
	}
	
	public List getFalling() {
		return falling;
	}

	public List getRebounding() {
		return rebounding;
	}

	public List getRising() {
		return rising;
	}

	public List getFallen() {
		return fallen;
	}

	public Ball getCurrentBall() {
		return current;
	}

	public void setCurrentBall(Ball b) {
		current = b;
	}

	public void positionCurrentBall() {
		ScreenPoint p = current.getScreenPoint();
		p.setX((getWidth() - ballSize) / 2);
		p.setY(getHeight() - ballSize);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int y = 0; y < getFieldHeight(); y ++) {
			for (int x = 0; x < getFieldWidth(); x ++) {
				buffer.append(getBall(x, y) == null?".":"X");
				buffer.append(" ");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
