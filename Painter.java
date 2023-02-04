import java.util.*;
import java.awt.Container;

public class Painter extends TimerTask {
	Container c;

	public Painter(Container target) {
		c = target;
	}

	public void run() {
		c.repaint();
	}
}
