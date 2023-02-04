import java.util.*;

public class BallMover extends TimerTask {
	Well well;
	Main main;
	Set ballSet;

	public BallMover(Main m) {
		main = m;
		well = main.getWell();
		ballSet = new HashSet();
	}

	public void run() {
		// move the current ball
		try {
			well.getPointer().move();

			Ball current = well.getCurrentBall();

			if (main.checkBounce(current)) {
				current.bounce();
			}

			if (main.checkStick(current)) {
				try {
					well.setBall(current);
				} catch (ArrayIndexOutOfBoundsException ex) {
					current.getScreenPoint().setX(
						current.getScreenPoint().getX()
						- well.getBallSize());
					well.setBall(current);
				}
				current.setVelocity(new Velocity(0,0));
				current.snapToGrid();

				main.pop(current);
				main.startDroppers();

				well.setCurrentBall(main.getNextBall());
				well.positionCurrentBall();
			} else {
				current.move();
			}
		} catch (NullPointerException ignored) {
		}

		// move the rebounding balls
		Iterator it = well.getRebounding().iterator();
		while (it.hasNext()) {
			try {
				Ball ball = (Ball) it.next();
				if (ball.isAtDestination()) {
					well.setBall(ball);
					ball.setVelocity(new Velocity(0,0));
					ball.snapToGrid();
					synchronized (well.getRebounding()) {
						it.remove();
					}

					main.pop(ball);
					main.startDroppers();
				} else {
					ball.move();
				}
			} catch (Exception ignored) {
			}
		}
		
		it = well.getFalling().iterator();
		while (it.hasNext()) {
			try {
				Ball ball = (Ball) it.next();
				if (main.checkBottom(ball)) {
					synchronized (well.getFalling()) {
						it.remove();
					}
					if (ballSet.add(ball.getImage())) {
						well.getFallen().add(ball);
					}
				} else {
					ball.move();
				}
			} catch (NullPointerException ignored) {
			}
		}

		if (well.getFallen().size() > 0 &&
			well.getFalling().size() == 0) {
			main.reboundBalls();
			ballSet.clear();
		}

		it = well.getRising().iterator();
		while (it.hasNext()) {
			try {
				Ball ball = (Ball) it.next();
				if (main.checkStick(ball)) {
					ball.setVelocity(new Velocity(0,0));
					ball.snapToGrid();
					well.setBall(ball);
					synchronized (well.getRising()) {
						well.getRising().remove(ball);
					}
				} else  {
					ball.move();
				}
			} catch (NullPointerException ignored) {
			}
		}
	}
}
