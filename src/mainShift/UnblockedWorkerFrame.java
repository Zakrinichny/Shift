package mainShift;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnblockedWorkerFrame extends JFrame{
	
	private MainFrame mainFrame;
	private JPanel lbpanel, cbpanel, btpanel;
	private JLabel infolb, datelb;
	private JButton savebt, closebt;
	private JComboBox<String> workercb;
	private DefaultComboBoxModel<String> workerComboBoxModel;
	private ActionListener savebtAction, closebtAction, selectAction;
	private WindowAdapter windowAdapter;
	private Worker worker;
	
	public UnblockedWorkerFrame(MainFrame mainFrame) {	
		this.mainFrame = mainFrame;
		worker = mainFrame.getWorker();
		initializationComponents();
		initializationActions();
		addComponentsToPanel();
		setStyle(lbpanel);
		setStyle(cbpanel);
		setStyle(btpanel);
		add(lbpanel, BorderLayout.NORTH);
		add(cbpanel, BorderLayout.CENTER);
		add(btpanel, BorderLayout.SOUTH);
		setTitle("Розблокування співробітника");
		setSize(600, 300);
		setLocationRelativeTo(null);
		addWindowListener(windowAdapter);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private void initializationComponents() {
		lbpanel = new JPanel();
		cbpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btpanel = new JPanel(new FlowLayout());
		infolb = new JLabel("Оберіть зі списку співробітника, якого треба розблокувати");	
		workercb = new JComboBox<>();
		setComboBoxModel();
		datelb = new JLabel("Заблокований до " +
				worker.getDate(workercb.getSelectedItem().toString())+ " (включно)");
		savebt = new JButton("Розблокувати");
		closebt = new JButton("Закрити");
	}
	
	private void setComboBoxModel() {
		workerComboBoxModel = new DefaultComboBoxModel<>();
		addModelElements();
		workercb.setModel(workerComboBoxModel);
	}
	
	private void addModelElements() {
		for (String name : worker.getListBlockedWorkers()) {
			workerComboBoxModel.addElement(name);
		}
	}
	
	private void addComponentsToPanel() {
		
		lbpanel.add(infolb);
		cbpanel.add(workercb);
		cbpanel.add(datelb);
		btpanel.add(savebt);
		btpanel.add(closebt);
	}
	
	private void initializationActions() {
		
		savebtAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {		
				worker.deleteBlockedWorker(workercb.getSelectedItem().toString());
				mainFrame.updateWorker(new Worker());
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
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
		
		windowAdapter = new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				mainFrame.setEnabled(true);
				mainFrame.toFront();
			}
			
		};
		
		selectAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				datelb.setText("Заблокований до " +
					worker.getDate(workercb.getSelectedItem().toString()) + " (включно)");
			}
		};
		workercb.addActionListener(selectAction);
		
	}	
	
	private void setStyle(Container container) {
		for (Component comp : container.getComponents()) {
			comp.setFont(new Font("Verdana", 0, 16));
		}	
	}
	
	public void setWorker(Worker worker) { this.worker = worker; }
	public void updateWorker(Worker worker) {
		setWorker(worker);
		setComboBoxModel();
	}
}
