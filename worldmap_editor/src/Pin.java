import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Pin extends JComponent {

	private static final long serialVersionUID = 1L;
	private volatile int screenX;
	private volatile int screenY;
	private volatile int myX = 50;
	private volatile int myY = 0;

	public Pin() {
	}

	public Pin(int x, int y) {
		setBorder(new LineBorder(Color.BLUE, 3));
		setBackground(Color.WHITE);
		int iY = y - 12;
		int iX = x - 12;
		setBounds(iX, iY, 25, 25);
		setOpaque(false);

		addCustomMouseListener();
		addCustomMouseMotionListener();


	}

	public String coords() {
		return String.valueOf(myX);
	}
	
	//methods used by parents

	public void select() {
		setBorder(new LineBorder(Color.RED, 3));
	}

	public void deselect() {
		setBorder(new LineBorder(Color.BLUE, 3));
	}
	
	public void addCustomMouseListener () { 
		addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				screenX = e.getXOnScreen();
				screenY = e.getYOnScreen();

				myX = getX();
				myY = getY();
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {				
			}

		});
	}
	
	public void addCustomMouseMotionListener () { 
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				int deltaX = e.getXOnScreen() - screenX;
				int deltaY = e.getYOnScreen() - screenY;

				setLocation(myX + deltaX, myY + deltaY);
			}

			public void mouseMoved(MouseEvent e) {
			}

		});
	}

}