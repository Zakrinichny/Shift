package mainShift;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PlusButton extends JButton {
	
	public PlusButton() {
		super(new ImageIcon("src\\icon\\add.png"));
		setPreferredSize(new Dimension(20,20));
	}
}
