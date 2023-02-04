import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class WellTest extends JFrame implements KeyListener {
	Pointer pointer;
	Well well;

	public WellTest() {
		super("Well Test");
		setResizable(false);
		pointer = new Pointer(new
			ImageIcon(getClass().getResource(
			"mucs/arrow.gif")).getImage(),
			28, 65, this);

		well = new Well(15, 11, 50, pointer);
		Image ballImage[] = new Image[7];

		addKeyListener(this);

		for (int i = 0; i < 7; i ++) {
			ballImage[i] = new ImageIcon(getClass().getResource(
				"mucs/" + i + ".gif")).getImage();
		}

		for (int x = 0; x < 15; x++) {
			for (int y = 0; y < 11; y++) {
				try {
				well.setBall(new Ball(ballImage[x % 7],
					ballImage[x % 7].getWidth(this),
					new GridPoint(x, y),
					new Velocity(0, 0)));
				} catch (ArrayIndexOutOfBoundsException aex) {
					aex.printStackTrace();
				}
			}
		}

		well.setBackground(Color.black);
		setContentPane(well);

		pack();
		setVisible(true);
		repaint();
	}

	public static void main(String[] args) {
		new WellTest();
	}

	public void keyPressed(KeyEvent keyEv) {
		switch (keyEv.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				pointer.decrement(5);
				break;
			case KeyEvent.VK_RIGHT:
				pointer.increment(5);
				break;
			case KeyEvent.VK_SHIFT:
				break;
		}

		repaint();
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void keyTyped(KeyEvent ke) {
	}
}
