package mainShift;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Start {
	
	public static void main (String[] args) {		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Font joptionpanefont = new Font("Verdana",0,16);
				UIManager.put("OptionPane.messageFont", joptionpanefont);
				UIManager.put("OptionPane.buttonFont", joptionpanefont);
				UIManager.put("OptionPane.cancelButtonText", "Вихід");
				MainFrame main = new MainFrame();
			}
		});
	}
}
