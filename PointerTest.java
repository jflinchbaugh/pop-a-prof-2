import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PointerTest extends JPanel implements KeyListener {
	Pointer pointer;
	Image current;
	Image img;

	public PointerTest() {
		img = new ImageIcon(
			getClass().getResource("mucs/arrow.gif")).getImage();
		pointer = new Pointer(img, 28, 65, this);
		current = pointer.getRotated();
		addKeyListener(this);
		requestFocus();
		setBackground(Color.black);
		setPreferredSize(new Dimension(
			pointer.getNewWidth(),
			pointer.getNewHeight()));
	}
	
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, pointer.getRotated().getWidth(this),
			pointer.getRotated().getHeight(this));
		g.drawImage(current, 0, 0, this);
	}

	public void keyPressed(KeyEvent keyEv) {
		switch (keyEv.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				System.out.println("left");
				pointer.decrement(5);
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("right");
				pointer.increment(5);
				break;
			case KeyEvent.VK_SHIFT:
				break;
		}
		current = pointer.getRotated();
		repaint();
	}

	public void keyReleased(KeyEvent keyEv) {
	}

	public void keyTyped(KeyEvent keyEv) {
	}

	public static void main(String[] args) {
		PointerTest pointerTest = new PointerTest();
		JFrame pointerFrame = new JFrame("test");
		pointerFrame.setContentPane(pointerTest);
		pointerFrame.addKeyListener(pointerTest);
		pointerFrame.pack();
		pointerFrame.setVisible(true);
	}
}
