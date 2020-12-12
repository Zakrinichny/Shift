package mainShift;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddWorkerFrame extends JFrame {
	
	private MainFrame mainFrame;
	private JPanel mainpanel, btpanel;
	private JLabel ranklb, namelb, infolb;
	private JComboBox<String> rankcb;
	private DefaultComboBoxModel<String> rankComboBoxModel;
	private JTextField nametxfl;
	private JCheckBox[] postschb;
	private JButton savebt, closebt;
	private ActionListener savebtAction, closebtAction, checkBoxSelectedAction;
	private KeyAdapter nametxflAdapter;
	private WindowAdapter windowAdapter;	
	private final int NUMBER_OF_WORKPLACES = 7;
	private Worker worker;
	
	
	public AddWorkerFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame; 
		worker = mainFrame.getWorker();
		initializationComponents();
		initializationActions();
		addComponentsToPanel();
		setStyle(mainpanel);
		setStyle(btpanel);
		add(mainpanel, BorderLayout.CENTER);
		add(btpanel, BorderLayout.SOUTH);
		setTitle("Новий співробітник");
		pack();
		setLocationRelativeTo(null);
		addWindowListener(windowAdapter);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private void initializationComponents() {
		mainpanel = new JPanel(new GridBagLayout());
		ranklb = new JLabel("Оберіть звання зі списку:     ");
		namelb = new JLabel("Введіть прізвище та ініціали нового співробітника: ");
		infolb = new JLabel("Позначте на які пости допускається співробітник");
		rankcb = new JComboBox<>();
		rankComboBoxModel = new DefaultComboBoxModel<>();
		addModelElements();
		rankcb.setModel(rankComboBoxModel);
		nametxfl = new JTextField(20);
		postschb = new JCheckBox[NUMBER_OF_WORKPLACES];
		postschb[0] = new JCheckBox("НЧО 2-611");
		postschb[1] = new JCheckBox("НЧО 621");
		postschb[2] = new JCheckBox("НЧО 2-631");
		postschb[3] = new JCheckBox("НЧО 2-632");
		postschb[4] = new JCheckBox("НЧО 2-633");
		postschb[5] = new JCheckBox("НЧО 2-634");
		postschb[6] = new JCheckBox("НЧО 651");	
		btpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		savebt = new JButton("Зберегти");
		savebt.setEnabled(false);
		closebt = new JButton("Закрити");
		
	}
	
	private String createDataString(String name) {
		StringBuilder workerNameAndPosts = new StringBuilder();
		workerNameAndPosts.append(name + ":");
		for (int i=0; i<NUMBER_OF_WORKPLACES; i++) {
			if (postschb[i].isSelected()) {
				workerNameAndPosts.append(worker.getNumberOfPost(postschb[i].getText())+",");
			}
		}
		// видаляємо останню кому, вона нам не потрібна
		workerNameAndPosts.deleteCharAt(workerNameAndPosts.length()-1);
		workerNameAndPosts.append("/");
		return workerNameAndPosts.toString();
	}
	
	private void initializationActions() {
		
		savebtAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {	
				if (worker.checkName(nametxfl.getText())) {
					String data = createDataString(rankcb.getSelectedItem()+" "+nametxfl.getText());
					worker.addWorker(data, true);
		
					setVisible(false);
					rankcb.setSelectedIndex(0);
					nametxfl.setText("");
					for (int i=0; i<NUMBER_OF_WORKPLACES; i++) {
						postschb[i].setSelected(false);
					}
					mainFrame.updateWorker(new Worker());
					mainFrame.setEnabled(true);
					mainFrame.toFront();
				}
				else {
					JOptionPane.showMessageDialog(null, 
							"Невірний формат імені співробітника."
							+ "\nЗабороняється використовувати наступні символи: \n"
							+ ": , / |  ", 
							"Помилка в імені співробітника", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		};
		savebt.addActionListener(savebtAction);
	
		closebtAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
			}
		};
		closebt.addActionListener(closebtAction);
		
		checkBoxSelectedAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				savebt.setEnabled(false);
				if (isSelected(postschb) && !nametxfl.getText().equals("")) {
					savebt.setEnabled(true);
				}
				
			}
		}; 
		for (int i=0; i<postschb.length; i++) {
			postschb[i].addActionListener(checkBoxSelectedAction);
		}
		
		nametxflAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				savebt.setEnabled(false);
				if(!nametxfl.getText().equals("") && isSelected(postschb)) {
					savebt.setEnabled(true);
				}
			}
		};
		nametxfl.addKeyListener(nametxflAdapter);
		
		windowAdapter = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
			}
			
		};
		
	}
	
	private boolean isSelected(JCheckBox[] chb) { 
		boolean res = false;
		for (JCheckBox checkBox : chb) {
			if(checkBox.isSelected()) { res = true; }
		}
		return res;
	}
	
	private void addComponentsToPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5,5,5,5);
		mainpanel.add(ranklb, gbc);
		gbc.gridx = 1;
		mainpanel.add(namelb, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainpanel.add(rankcb, gbc);
		gbc.gridx = 1;
		mainpanel.add(nametxfl, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth =2;
		mainpanel.add(infolb, gbc);
		gbc.gridwidth = 1;
		for (int i=0; i<NUMBER_OF_WORKPLACES; i++) {
			gbc.gridy = i+3;
			mainpanel.add(postschb[i], gbc);
		}
		
		btpanel.add(savebt);
		btpanel.add(closebt);
		
	}
	
	private void addModelElements() {	
		for (String rank : worker.getListRank()) {
			rankComboBoxModel.addElement(rank);
		}
	}
	
	private void setStyle(Container container) {
		for (Component comp : container.getComponents()) {
			comp.setFont( new Font("Verdana", 0, 16));
		}
	}
}
