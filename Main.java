import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;

public class Main extends JFrame
	implements KeyListener, WindowListener {
	// game constants
	private static final int defaultRotateSpeed = 2;
	private static final int defaultShootSpeed = 20;
	private static final int defaultDropSpeed = defaultShootSpeed * 2;
	private static final int defaultReboundSpeed =
		defaultShootSpeed * 1;

	private int rotateSpeed;
	private Pointer pointer;
	private Well well;
	private Timer timer;
	private Image[] ballImage;

	public Main() {
		super("Pop-A-Prof 2");

		ballImage = new Image[7];

		for (int i = 0; i < 7; i++) {
			ballImage[i] = new ImageIcon(getClass().getResource(
				"mucs/" + i + ".gif")).getImage();
		}

		pointer = new Pointer(
			new ImageIcon(getClass().getResource(
			"mucs/arrow.gif")).getImage(), 28, 65, this);
		well = new Well(8, 11, 50, pointer);

		rotateSpeed = defaultRotateSpeed;

		addKeyListener(this);	
		addWindowListener(this);

		Painter painter = new Painter(getContentPane());
		BallMover ballMover = new BallMover(this);
		timer = new Timer();
		timer.scheduleAtFixedRate(ballMover, new Date(), 20);
		timer.scheduleAtFixedRate(painter, new Date(), 30);

		getContentPane().add(BorderLayout.CENTER, well);
		pack();
		setVisible(true);

		well.setCurrentBall(new Ball(ballImage[0], 
			ballImage[0].getWidth(this)));
		well.positionCurrentBall();
	}

	public static void main(String[] args) {
		new Main();
	}

	public void keyPressed(KeyEvent keyEv) {
		switch (keyEv.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				pointer.setRotateSpeed(- defaultRotateSpeed);
				break;
			case KeyEvent.VK_RIGHT:
				pointer.setRotateSpeed(defaultRotateSpeed);
				break;
			case KeyEvent.VK_SHIFT:
				rotateSpeed = defaultRotateSpeed * 3;
				break;
			case KeyEvent.VK_SPACE:
				if (well.getCurrentBall().getVelocity().getSpeed()
					== 0) {
					well.getCurrentBall().setVelocity(
						new Velocity(pointer.getAngle(),
							defaultShootSpeed));
				}
				break;
			case KeyEvent.VK_C:
				well.clear();
				break;
			case KeyEvent.VK_D:
				for (int y = 0; y < well.getFieldHeight(); y++) {
					System.out.print("[ ");
					for (int x = 0; x < well.getFieldWidth(); x++) {
						String text = ".";
						for (int b = 0; b < 7; b++) {
							try {
								if (well.getBall(x,y).getImage()
									== ballImage[b]) {
									//text = new Integer(b).toString();
									text = well.getBall(x,y)
										.getScreenPoint().toString();
								}
							} catch (Exception ignored) {
							}
						}
						System.out.print(text);
					}
					System.out.println(" ]");
				}
				break;
		}
	}

	public void keyReleased(KeyEvent keyEv) {
		switch(keyEv.getKeyCode()) {
			case KeyEvent.VK_SHIFT:
				rotateSpeed = defaultRotateSpeed;
				break;
			case KeyEvent.VK_LEFT:
				pointer.setRotateSpeed(0);
				break;
			case KeyEvent.VK_RIGHT:
				pointer.setRotateSpeed(0);
				break;
		}
	}

	public void keyTyped(KeyEvent keyEv) {
	}

	public boolean checkBounce(Ball ball) {
		return ball.getScreenPoint().getX() < 0
			|| ball.getScreenPoint().getX()
			> well.getWidth() - Math.abs(well.getBallSize());
	}

	public boolean checkBottom(Ball ball) {
		return ball.getScreenPoint().getY() > well.getHeight();
	}

	public void pop(Ball locus) {
		Collection balls = new Vector();
		if (countConnected((int) locus.getGridPoint().getX(),
			(int) locus.getGridPoint().getY(), balls) >= 3) {
			Iterator it = balls.iterator();
			while (it.hasNext()) {
				Ball b = (Ball) it.next();
				well.setBall((int) b.getGridPoint().getX(),
					(int) b.getGridPoint().getY(), null);
			}
		}
	}

	private int countConnected(int x, int y, Collection c) {
		int count = 0;
		Ball here = well.getBall(x, y);
		if (!c.contains(here)) {
			count = 1;
			c.add(here);
			try {
				if (well.getBall(x, y - 1).getImage()
					== here.getImage()) {
					count += countConnected(x, y - 1, c);
				}
			} catch (Exception ignored) {}
			try {
				if (well.getBall(x, y + 1).getImage()
					== here.getImage()) {
					count += countConnected(x, y + 1, c);
				}
			} catch (Exception ignored) {}
			try {
				if (well.getBall(x - 1, y).getImage()
					== here.getImage()) {
					count += countConnected(x - 1, y, c);
				}
			} catch (Exception ignored) {}
			try {
				if (well.getBall(x + 1, y).getImage()
					== here.getImage()) {
					count += countConnected(x + 1, y, c);
				}
			} catch (Exception ignored) {}
			if (y % 2 == 1) {
				try {
					if (well.getBall(x + 1, y - 1).getImage()
						== here.getImage()) {
						count += countConnected(x + 1, y - 1, c);
					}
				} catch (Exception ignored) {}
				try {
					if (well.getBall(x + 1, y + 1).getImage()
						== here.getImage()) {
						count += countConnected(x + 1, y + 1, c);
					}
				} catch (Exception ignored) {}
			} else {
				try {
					if (well.getBall(x - 1, y - 1).getImage()
						== here.getImage()) {
						count += countConnected(x - 1, y - 1, c);
					}
				} catch (Exception ignored) {}
				try {
					if (well.getBall(x - 1, y + 1).getImage()
						== here.getImage()) {
						count += countConnected(x - 1, y + 1, c);
					}
				} catch (Exception ignored) {}
			}
		}
		return count;
	}

	public boolean checkStick(Ball ball) {
		boolean ret = false;

		// test ball
		Ball check = new Ball(ball);

		// top
		ScreenPoint newScreen = check.getScreenPoint();
		ret = ret || (newScreen.getY() < 0);

		// direct grid
		check.move();
		GridPoint newGrid = check.getGridPoint();
		ret = ret || (well.getBall((int) newGrid.getX(),
			(int) newGrid.getY()) != null);

		// turn right and move half a ball for next check
		Velocity cv = check.getVelocity();
		cv.setSpeed(well.getBallSize() / 4);
		cv.setAngle(cv.getAngle() + 90);
		check.move();
		newGrid = check.getGridPoint();
		try {
			ret = ret || (well.getBall((int) newGrid.getX(),
				(int) newGrid.getY()) != null);
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}

		// turn back and go a full ball...check left side
		cv.setSpeed(well.getBallSize() / 2);
		cv.setAngle(cv.getAngle() - 180);
		check.move();
		newGrid = check.getGridPoint();
		try {
			ret = ret || (well.getBall((int) newGrid.getX(),
				(int) newGrid.getY()) != null);
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}

		return ret;
	}

	public Ball getNextBall() {
		Image bi = ballImage[(int) (Math.random() * 7.0)];
		Ball ball =new Ball(bi, well.getBallSize());
		return ball;
	}

	public Well getWell() {
		return well;
	}

	public void startDroppers() {
		Collection hanging = new Vector();
		for (int x = 0; x < well.getFieldWidth(); x ++) {
			gatherHanging(x, 0, hanging);
		}
		for (int y = 0; y < well.getFieldHeight(); y ++) {
			for (int x = 0; x < well.getFieldWidth(); x ++) {
				try {
					Ball b = well.getBall(x, y);
					if (! hanging.contains(b)) {
						b.setVelocity(new Velocity(180,
							defaultDropSpeed));
						synchronized (well.getFalling()) {
							well.getFalling().add(b);
						}
						well.setBall(x, y, null);
					}
				} catch (Exception ignored) { }
			}
		}
	}

	private void gatherHanging(int x, int y, Collection hanging) {
		try {
			Ball b = well.getBall(x, y);
			if (b != null) {
				if (! hanging.contains(b)) {
					hanging.add(b);
					gatherHanging(x, y - 1, hanging);
					gatherHanging(x, y + 1, hanging);
					gatherHanging(x - 1, y, hanging);
					gatherHanging(x + 1, y, hanging);
					if (y % 2 == 1) {
						gatherHanging(x + 1, y - 1, hanging);
						gatherHanging(x + 1, y + 1, hanging);
					} else {
						gatherHanging(x - 1, y - 1, hanging);
						gatherHanging(x - 1, y + 1, hanging);
					}
				}
			}
		} catch (Exception ignored) { }
	}

	public void reboundBalls() {
		long d = new Date().getTime();
		for (int y = 0; y < well.getFieldHeight(); y ++) {
			for (int x = 0; x < well.getFieldWidth(); x ++) {
				Iterator it = well.getFallen().iterator();
				while (it.hasNext()) {
					Ball b = (Ball) it.next();
					try {
						Ball here = well.getBall(x, y);
						if (here == null) {
							well.setBall(x, y, b);

							Collection balls = new Vector();
							int count = countConnected(x, y, balls);
							if (count >= 3) {
								b.setDestination(new GridPoint(x, y),
									defaultReboundSpeed);
								synchronized (well.getRebounding()) {
									well.getRebounding().add(b);
								}
								it.remove();
							}
							well.setBall(x, y, null);
						}
					} catch (ArrayIndexOutOfBoundsException ignored) {
					}
				}
			}
		}
		well.getFallen().clear();
		System.out.println("reboundBalls(): "
			+ (new Date().getTime() - d));
	}

	public void windowActivated(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
}
