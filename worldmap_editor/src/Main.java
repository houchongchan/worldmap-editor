import javax.swing.*;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.awt.Adjustable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {

	public static JPanel locationList;
	public static JFrame frame;
	public static JLayeredPane layeredPane;
	public static ButtonPanel buttonPanel;

	public static List<Pin> list = new ArrayList<Pin>();
	public static List<PinList> pinlist = new ArrayList<PinList>();

	private static volatile int myX = 50;
	private static volatile int myY = 0;
	public static volatile String mode = "pan";

	public static void main(String[] args) {
		setUpGUI();
		setUpFrame();
	}

	public static void setUpGUI() {
		frame = new JFrame();

		Icon ico = new ImageIcon(Main.class.getResource("world-map-2022.jpg"));
		JLabel map = new JLabel(ico);
		final JScrollPane scroll = new JScrollPane(map);

		// Listen for value changes in the scroll pane's scroll bars and move the pins
		// accordingly to keep them fixed relative to map location
		// http://www.java2s.com/Code/Java/Swing-JFC/ListeningforScrollbarValueChangesinaJScrollPaneContainer.htm
		AdjustmentListener scrollistener = ScrollListener();
		scroll.getHorizontalScrollBar().addAdjustmentListener(scrollistener);
		scroll.getVerticalScrollBar().addAdjustmentListener(scrollistener);

		MouseListener pinlistener = PinListener();
		scroll.addMouseListener(pinlistener);
		scroll.setBounds(0, 0, 1600, 1100);

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(400, 400));
		layeredPane.add(scroll, 51, 0);

		// Laying out the right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		buttonPanel = new ButtonPanel(list, pinlist);
		locationList = new JPanel();
		locationList.setLayout(new GridBagLayout());
		locationList.setSize(new Dimension(300, 1000));

		locationList.setBackground(new Color(0, 128, 128));
		JScrollPane Yscroll = new JScrollPane(locationList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Setting dimensions of the two panels on the right
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		rightPanel.add(buttonPanel, c);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.BOTH;
		c1.gridx = 0;
		c1.weighty = 1;
		c1.weightx = 1;
		rightPanel.add(Yscroll, c1);

		frame.add(layeredPane, BorderLayout.CENTER);
		frame.add(rightPanel, BorderLayout.EAST);

	}

	public static void setUpFrame() {
		frame.setVisible(true);
		frame.setSize(1600, 1400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("World Map location list");
	}

	public static MouseListener PinListener() {
		MouseListener listener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (buttonPanel.getMode() != "pin")
					return;

				final PinList item = new PinList();
			

				int eX = e.getXOnScreen();
				// subtract 64 for the offset of the tool bar to the map.
				int eY = e.getYOnScreen() - 64;
				final Pin newPin = new Pin(eX, eY);
				
				item.setRemove(RemoveListener(newPin, item));
				list.add(newPin);
				pinlist.add(item);
				MouseListener selectionListener = new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Pin selected = newPin;
						for (int i = 0; i < list.size(); i++) {
							Pin tmp = list.get(i);
							PinList tmp2 = pinlist.get(i);
							if (selected == tmp) {
								tmp.select();
								tmp2.select();
							} else {
								tmp.deselect();
								tmp2.deselect();
							}
						}

					};

				};

				newPin.addMouseListener(selectionListener);
				item.addMouseListener(selectionListener);
				layeredPane.add(newPin, 52, 0);
				layeredPane.revalidate();
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.VERTICAL;
				c.gridx = 0;
				c.ipady = 40;
				locationList.add(item, c);
//				final JPanel margin = new JPanel();
//				margin.setSize(new Dimension(20, 5));
//				margin.setBackground(new Color(0, 128, 128));
//				locationList.add(margin, c);
				locationList.revalidate();
			}
		};
		return listener;
	}

	public static AdjustmentListener ScrollListener() {
		AdjustmentListener listener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent evt) {
				Adjustable source = evt.getAdjustable();
				int value = evt.getValue();
				if (evt.getValueIsAdjusting()) {
					return;
				}
				int orient = source.getOrientation();
				if (orient == Adjustable.HORIZONTAL) {
					int change = (value > myX) ? -1 : 1;
					for (int i = 0; i < list.size(); i++) {
						Pin tmp = list.get(i);
						tmp.setLocation(tmp.getX() + change, tmp.getY());
					}
					myX = value;
				} else {
					int change = (value > myY) ? -1 : 1;
					for (int i = 0; i < list.size(); i++) {
						Pin tmp = list.get(i);
						tmp.setLocation(tmp.getX(), tmp.getY() + change);
					}
					myY = value;
				}

			}
		};
		return listener;

	}

	public static MouseListener RemoveListener(final Pin p, final PinList pl) {
		MouseListener listener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				list.remove(p);
				pinlist.remove(pl);
				locationList.remove(pl);
				layeredPane.remove(p);
				
				locationList.repaint();
				locationList.revalidate();
				
				layeredPane.repaint();
				layeredPane.revalidate();

			}
		};
		return listener;

	}

	@SuppressWarnings("unchecked")
	public static void Deserialize() {
//		List<PinList> e = null;
//		try {
//			FileInputStream fileIn = new FileInputStream("/tmp/locations.ser");
//			ObjectInputStream in = new ObjectInputStream(fileIn);
//			e = (List<PinList>) in.readObject();
//			System.out.println(e);
//			in.close();
//			fileIn.close();
//		} catch (IOException i) {
//			i.printStackTrace();
//			return;
//		} catch (ClassNotFoundException c) {
//			System.out.println("Employee class not found");
//			c.printStackTrace();
//			return;
//		}
//		
//		
//
//		final PinList item = new PinList();
//	
//		final Pin newPin = new Pin(eX, eY);
//		
//		item.setRemove(RemoveListener(newPin, item));
//		list.add(newPin);
//		pinlist.add(item);
//		MouseListener selectionListener = new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				Pin selected = newPin;
//				for (int i = 0; i < list.size(); i++) {
//					Pin tmp = list.get(i);
//					PinList tmp2 = pinlist.get(i);
//					if (selected == tmp) {
//						tmp.select();
//						tmp2.select();
//					} else {
//						tmp.deselect();
//						tmp2.deselect();
//					}
//				}
//
//			};
//
//		};
//
//		newPin.addMouseListener(selectionListener);
//		item.addMouseListener(selectionListener);
//		layeredPane.add(newPin, 52, 0);
//		layeredPane.revalidate();
//		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.VERTICAL;
//		c.gridx = 0;
//		c.ipady = 40;
//		locationList.add(item, c);
////		final JPanel margin = new JPanel();
////		margin.setSize(new Dimension(20, 5));
////		margin.setBackground(new Color(0, 128, 128));
////		locationList.add(margin, c);
//		locationList.revalidate();
//
	}

}
