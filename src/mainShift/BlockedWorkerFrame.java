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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DateFormatter;

public class BlockedWorkerFrame extends JFrame {
	
	private MainFrame mainFrame;
	private JPanel mainPanel, btpanel;
	private WindowAdapter windowAdapter;
	private JLabel workerlb, datelb;
	private JComboBox<String> workercb;
	private DefaultComboBoxModel<String> workerComboBoxModel;
	private JFormattedTextField ftfDate;
	private JButton savebt, closebt;
	private ActionListener savebtAction, closebtAction;
	private KeyAdapter dateAdapter;
	private Worker worker;
	
	public BlockedWorkerFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.worker = mainFrame.getWorker();
		initializationComponents();
		initializationActions();
		addComponentsToPanel();
		setStyle(mainPanel);
		setStyle(btpanel);
		add(mainPanel, BorderLayout.CENTER);
		add(btpanel, BorderLayout.SOUTH);
		pack();
		setTitle("Блокування співробітника");
		setLocationRelativeTo(null);
		addWindowListener(windowAdapter);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initializationComponents() {
		mainPanel = new JPanel(new GridBagLayout());
		workerlb = new JLabel(
				"<html><br>Оберіть співробітника, якого треба тимчасово заблокувати</br>"
				+ "<br>(у зв'язку з відпусткою, відрадженням, лікарняним та ін.):</br></html>");
		
		datelb = new JLabel("Вкажіть дату, до якої треба заблокувати співробітника (включно):");
		workercb = new JComboBox<>();
		setComboBoxModel();
		createMaskDate();
		
		btpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		savebt = new JButton("Зберегти");
		savebt.setEnabled(false);
		closebt = new JButton("Закрити");	
	}
	
	private void setComboBoxModel() {
		workerComboBoxModel = new DefaultComboBoxModel<>();
		addModelElements();
		workercb.setModel(workerComboBoxModel);
	}
	
	private void initializationActions() {
		
		savebtAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {	
				Worker worker = mainFrame.getWorker();
				if(checkDate()) {
					String data = workercb.getSelectedItem()+":"+ftfDate.getText()+"/";
					worker.addBlockedWorker(data, true);
					workercb.setSelectedIndex(0);
					ftfDate.setValue(new Date());
					setVisible(false);
					mainFrame.updateWorker(new Worker());
					mainFrame.setEnabled(true);
					mainFrame.toFront();
				}
				else {
					JOptionPane.showMessageDialog(null, 
							"Невірний формат дати!\nНе вдалося заблокувати співробітника", 
							"Помилка", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		savebt.addActionListener(savebtAction);
	
		closebtAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {	
				workercb.setSelectedIndex(0);
				ftfDate.setValue(new Date());
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
		
		dateAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				savebt.setEnabled(false);
				if (!ftfDate.getText().equals("")) {
					savebt.setEnabled(true);
				}
			}
		};
		ftfDate.addKeyListener(dateAdapter);
	}
	
	private void createMaskDate() {
		// определение маски
		// DateFormat - класс для создания формат дат
		// формат даты создаём с помощью объекта SimpleDateFormat
		DateFormat date =new SimpleDateFormat("dd.MM.yyyy");
		DateFormatter dateFormatter = new DateFormatter(date);
		// DateFormatter по умолчанию разрешает ввод неверных значений
		// методом setAllowsInvalid() запрещем ему ввод таких значений
		dateFormatter.setAllowsInvalid(false);
		// включаем режим перезаписи значений для удобства изменений даты
		dateFormatter.setOverwriteMode(true);
		// создание форматированного текстого поля даты
		ftfDate = new JFormattedTextField(dateFormatter);
		ftfDate.setValue(new Date());
		ftfDate.setColumns(10);
		
	}
	
	private boolean checkDate() {
		String inputDate = ftfDate.getText();
		int day = Integer.parseInt(inputDate.substring(0,2));
		int month = Integer.parseInt(inputDate.substring(3,5));
		int year = Integer.parseInt(inputDate.substring(6,10));
		Calendar today = Calendar.getInstance();
		// отсчёт месяцев начинается с 0, поэтому month-1
		Calendar date = new GregorianCalendar(year, month-1, day);
		if (inputDate.length() > 10) return false;
		else if (date.after(today))  return true;  
		else return false;
	}
	
	private void addModelElements() {
		for (String worker : mainFrame.getWorker().getListWorkers(true)) {
			workerComboBoxModel.addElement(worker);
		}
	}
	
	private void addComponentsToPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5,5,5,5);
		mainPanel.add(workerlb, gbc);
		gbc.gridy = 1;
		mainPanel.add(workercb, gbc);
		gbc.gridy = 2;
		mainPanel.add(datelb, gbc);
		gbc.gridy = 3;
		mainPanel.add(ftfDate, gbc);
		
		btpanel.add(savebt);
		btpanel.add(closebt);
		
	}
	
	private void setStyle(Container container) {
		for (Component comp : container.getComponents()) {
			comp.setFont(new Font ("Verdana", 0, 16));
		}
	}
	public void setWorker(Worker worker) { this.worker = worker; }
	public void updateWorker(Worker worker) {
		setWorker(worker);
		setComboBoxModel();
	}

}
