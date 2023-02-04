import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/**
 * Pointer class to represent the pointer in the game.
 */
public class Pointer {
	// the base image
	private Image image;

	// the current angle
	private int angle;

	// axis of rotation 
	private int rotX;
	private int rotY;

	private BufferedImage src;
	private AffineTransformOp transform;

	private int imgWidth;
	private int imgHeight;
	private int newWidth;
	private int newHeight;

	private double dist;
	private ImageObserver observer;

	private int speed;
	/**
	 * Class constructor.
	 * @param img base image
	 * @param x x-coordinate of rotation point
	 * @param y y-coordinate of rotation point
	 */
	public Pointer(Image img, int x, int y, ImageObserver obs) {
		setRotX(x);
		setRotY(y);
		setAngle(0);
		setImage(img);
		setObserver(obs);
	}

	private void setNewWidth(int w) {
		newWidth = w;
	}

	private void setNewHeight(int h) {
		newHeight = h;
	}

	public int getNewWidth() {
		return newWidth;
	}

	public int getNewHeight() {
		return newHeight;
	}

	public void setObserver(ImageObserver obs) {
		observer = obs;
	}

	public ImageObserver getObserver() {
		return observer;
	}

	/**
	 * Set current angle
	 * @param angle current angle (degrees)
	 */
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	/**
	 * Recalculate transform object.
	 */
	private void transform() {
		transform = new AffineTransformOp(
			AffineTransform.getRotateInstance(
				angle * Math.PI / 180,
				(int) dist, (int) dist),
			AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}

	/**
	 * Get the current angle
	 * @return current angle (degrees)
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * Set the base image for the pointer.
	 * @param img image to transform
	 */
	public void setImage(Image img) { 
		image = img;

		imgWidth = image.getWidth(observer);
		imgHeight = image.getHeight(observer);

		double dw, dh;

		dw = (rotX > imgWidth / 2.0) ? (rotX) : (imgWidth - rotX);
		dh = (rotY > imgHeight / 2.0) ? (rotY) : (imgHeight - rotY);

		dist = Math.sqrt(dw * dw + dh * dh);

		setNewWidth((int) dist * 2);
		setNewHeight((int) dist * 2);

		src = new BufferedImage(getNewWidth(), getNewHeight(),
			BufferedImage.TYPE_INT_ARGB);

		src.createGraphics().drawImage(image, (int) (dist - rotX),
			(int) (dist - rotY), observer);
	}

	/**
	 * Get the base image for the pointer.
	 * @return the base image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Set the x-coordinate for rotation.
	 * @param x x-coordinate
	 */
	public void setRotX(int x) {
		rotX = x;
	}

	/**
	 * Get the x-coordinate for rotation.
	 * @return x-coordinate
	 */
	public int getRotX() {
		return rotX;
	}

	/**
	 * Set y-coordinate for rotation.
	 * @param y y-coordinate
	 */
	public void setRotY(int y) {
		rotY = y;

		double dw, dh;

		dw = (rotX > imgWidth / 2.0) ? (rotX) : (imgWidth - rotX);
		dh = (rotY > imgHeight / 2.0) ? (rotY) : (imgHeight - rotY);

		dist = Math.sqrt(dw * dw + dh * dh);

		transform = new AffineTransformOp(
			AffineTransform.getRotateInstance(
				angle * Math.PI / 180,
				(int) dist, (int) dist),
			AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}

	/**
	 * Get the y-coordinate for rotation.
	 * @return y-coordinate
	 */
	public int getRotY(int y) {
		return rotY;
	}

	/**
	 * adjust image dimensions
	 */
	public void adjustDimensions() {
		double dw, dh;

		dw = (rotX > imgWidth / 2.0) ? (rotX) : (imgWidth - rotX);
		dh = (rotY > imgHeight / 2.0) ? (rotY) : (imgHeight - rotY);

		dist = Math.sqrt(dw * dw + dh * dh);

		setNewWidth((int) dist * 2);
		setNewHeight((int) dist * 2);
	}

	/**
	 * Get the rotated instance of the pointer image.
	 * @param observer the ImageObserver using this image
	 * @return rotated image
	 */
	public Image getRotated() {
		BufferedImage dst = new BufferedImage(src.getWidth(),
			src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		transform.filter(src, dst);

		return dst;
	}

	/**
	 * Increment the current angle.
	 * @param ang angle by which to increment (degrees)
	 */
	public void increment(int ang) {
		if (ang != 0) {
			angle += ang;
			if (angle > 89) angle = 89;
			if (angle < -89) angle = -89;

			transform = new AffineTransformOp(
				AffineTransform.getRotateInstance(
					angle * Math.PI / 180,
					(int) dist, (int) dist),
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		}
	}

	/**
	 * Decrement the current angle.
	 * @param ang angle by which to decrement (degrees)
	 */
	public void decrement(int ang) {
		increment(- ang);
	}

	public Point getCorner(Point center) {
		return new Point((int) (center.getX() - dist),
			(int) (center.getY() - dist));
	}

	public void setRotateSpeed(int speed) {
		this.speed = speed;
	}

	public int getRotateSpeed() {
		return speed;
	}

	public void move() {
		increment(speed);
	}
}
