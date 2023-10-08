import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

public class PinList extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField label;
	private JTextArea description;
	public JButton button;

	public PinList() {
		label = new JTextField("Name");
		label.setBackground(Color.cyan);
		label.setForeground(Color.white);
		label.setPreferredSize(new Dimension(50, 30));

		description = new JTextArea("Enter Description");
		description.setLineWrap(true);
		description.setPreferredSize(new Dimension(180, 70));
		description.setBackground(Color.lightGray);
		description.setForeground(Color.white);
		
		button = new JButton("x"); 
		button.setPreferredSize(new Dimension(15, 15));


		add(label);
		add(description);
		add(button);
		setPreferredSize(new Dimension(300, 60));
		setBorder(new RoundedBorder(Color.white, 10));
		setBackground(Color.cyan);

	}
	
	//Methods used by Parents
	public void select() {
		setBorder(new RoundedBorder(Color.RED, 15));
		setMinimumSize(new Dimension(300, 180));

	}

	public void deselect() {
		setBorder(new RoundedBorder(Color.white, 10));
		setPreferredSize(new Dimension(300, 60));
	}
	
	public void setRemove(MouseListener ml) { 
		button.addMouseListener(ml);
	}

}
