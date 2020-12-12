package mainShift;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InformationFrame extends JFrame{

	private MainFrame mainFrame;
	private Worker worker;
	private JLabel label;
	private JScrollPane scrollPane;
	private File infoFile;
	private String pathAndName;
	private StringBuilder info;
	private JPanel btPanel, lbPanel;
	private JButton btOk;
	private Font font;
	
	public InformationFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		worker = mainFrame.getWorker();
		
		pathAndName = "src\\data\\information.txt";
		infoFile = new File(pathAndName);
		info = new StringBuilder();
		worker.loadFromFile(info, infoFile, "інформацію");
		font = new Font("Verdana",0, 16);
		label = new JLabel(info.toString());
		label.setFont(font);
		lbPanel = new JPanel();
		lbPanel.add(label);
		scrollPane = new JScrollPane(lbPanel);
	
		
		btPanel = new JPanel();
		btOk = new JButton("Ok");
		btOk.setFont(font);
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
			}
		});
		btPanel.add(btOk);
		
		add(scrollPane, BorderLayout.CENTER);
		add(btPanel, BorderLayout.SOUTH);
		setTitle("Інформація");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,600);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
			}
			
		});
		
	}

}
