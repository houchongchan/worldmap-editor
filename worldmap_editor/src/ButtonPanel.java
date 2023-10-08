import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.*;

public class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton button, pin, save;
	public String mode;
	public static List<PinList> pinlist;
	public static List<Pin> list;

	public ButtonPanel(List<Pin> p, List<PinList> pl) {
		pinlist = pl;
		list = p;

		button = new JButton("disable");
		button.setVerticalTextPosition(AbstractButton.CENTER);
		button.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setDisable();
			}
		};

		button.addActionListener(actionListener);

		pin = new JButton("Pin");
		pin.setVerticalTextPosition(AbstractButton.CENTER);
		pin.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales

		ActionListener actionListener1 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setPin();
			}
		};

		pin.addActionListener(actionListener1);

		save = new JButton("Save");
		save.setVerticalTextPosition(AbstractButton.CENTER);
		save.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for left-to-right locales

		ActionListener actionListener2 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				setSave();
			}
		};

		save.addActionListener(actionListener2);
		add(button);
		add(pin);
		add(save);
		setSize(new Dimension(300, 60));
		setBackground(new Color(0, 128, 128));
	}

	public void setDisable() {
		mode = "disable";
		button.setForeground(Color.blue);
		pin.setForeground(Color.black);
		save.setForeground(Color.black);

	}

	public void setPin() {
		mode = "pin";
		button.setForeground(Color.black);
		pin.setForeground(Color.blue);
		save.setForeground(Color.black);
	}

	public void setSave() {
		mode = "save";
		button.setForeground(Color.black);
		pin.setForeground(Color.black);
		save.setForeground(Color.blue);
//		serialize();

	}

	public String getMode() {
		return mode;
	}

	public void serialize() {
		try {
			FileOutputStream fileOut = new FileOutputStream("/tmp/locations.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(pinlist);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /locations.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}

	}
}
