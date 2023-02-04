public class ScreenPoint {
	private double x;
	private double y;

	public ScreenPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public ScreenPoint(ScreenPoint p) {
		x = p.getX();
		y = p.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean equals(Object o) {
		boolean ret = false;

		try {
			if (((ScreenPoint) o).getX() == x
				&& ((ScreenPoint) o).getY() == y) {
				ret = true;
			}
		} catch (Exception ignored) {
		}

		return ret;
	}

	public String toString() {
		return "(ScreenPoint)(" + x + "," + y + ")";
	}
}
