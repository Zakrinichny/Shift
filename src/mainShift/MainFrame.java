package mainShift;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class MainFrame extends JFrame {
	
	private static final int FONTSIZE = 16;
	private static Font fontbt, fontmenu, fontmenuitem;
	private ActionListener exitAction, addWorkerAction, delWorkerAction, delShiftAction,
		orientationAction, savePatternAction, downloadPatternAction, delPatternAction,
		blockedWorkerAction, unblockedWorkerAction,  infoAction;
	private FrameAction addShiftAction, saveAction;
	private JButton addshiftbt, delshiftbt, addworkerbt, delworkerbt, savebt, exitbt;
	private JPanel mainPanel, shiftPanel;	
	private AddWorkerFrame addWorkerFrame;
	private DelWorkerFrame delWorkerFrame;
	private InformationFrame infoFrame;
	private UnblockedWorkerFrame unblockedWorkerFrame;
	private BlockedWorkerFrame blockedWorkerFrame;
	private Worker worker;
	private Stack <TablePanel> tablePanels; 
	private int orientation;
	private JMenuItem newItem, delItem, saveItem, unblockedWorker;
	private JRadioButtonMenuItem horizont, vertical;
	private JLabel info;
	private JPanel lbFlowPanel, southPanel;
	
	protected JScrollPane scrollPane;
	
	public MainFrame() {
		
		fontbt = new Font("Arial", Font.BOLD, FONTSIZE);
		fontmenu = new Font("Calibri", Font.BOLD, FONTSIZE);
		fontmenuitem = new Font("Calibri", 0, FONTSIZE);
		worker = new Worker();
		orientation = BoxLayout.Y_AXIS;
		addWorkerFrame = new AddWorkerFrame(this);
		delWorkerFrame = new DelWorkerFrame(this);
		blockedWorkerFrame = new BlockedWorkerFrame(this);
		infoFrame = new InformationFrame(this);
		if (worker.isBlocked()) { unblockedWorkerFrame = new UnblockedWorkerFrame(this); }	
		initializationAction();
		info = new JLabel();
		initializationInfo();
		addMenu();
		addMainPanel();
		addButton();
		setTitle("Зміна");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addMenu() {
				
		newItem = new JMenuItem("Додати зміну", new ImageIcon("src\\icon\\addshift.png"));
		delItem = new JMenuItem("Видалити зміну",  new ImageIcon("src\\icon\\delshift.png"));
		saveItem = new JMenuItem("Зберегти", new ImageIcon("src\\icon\\save.png"));
		JMenuItem exitItem = new JMenuItem("Вихід", new ImageIcon("src\\icon\\exit.png"));

		setPreferencesForMenuItem(newItem, fontmenuitem, addShiftAction, "ctrl N");
		setPreferencesForMenuItem(delItem, fontmenuitem, delShiftAction, "ctrl D");
		delItem.setEnabled(false);
		setPreferencesForMenuItem(saveItem, fontmenuitem, saveAction, "ctrl S");
		setPreferencesForMenuItem(exitItem, fontmenuitem, exitAction, "ctrl E");
		
		JMenu fileMenu = new JMenu("Файл");
		fileMenu.setFont(fontmenu);
		fileMenu.add(newItem);
		fileMenu.add(delItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		
		JMenu editMenu = new JMenu("Правка");
		editMenu.setFont(fontmenu);
		
		JMenuItem addWorkerItem = new JMenuItem("Додати співробітника",
				new ImageIcon("src\\icon\\pl.png"));
		JMenuItem delWorkerItem = new JMenuItem("Видалити співробітника",
				new ImageIcon("src\\icon\\min.png"));
		JMenuItem blockedWorker = new JMenuItem("Заблокувати співробітника",
				new ImageIcon("src\\icon\\lock.png"));
		unblockedWorker = new JMenuItem("Розблокувати співробітника",
				new ImageIcon("src\\icon\\unlock.png")); 
		
		setPreferencesForMenuItem(addWorkerItem, fontmenuitem, addWorkerAction, "shift N");
		setPreferencesForMenuItem(delWorkerItem, fontmenuitem, delWorkerAction, "shift D");
		setPreferencesForMenuItem(blockedWorker, fontmenuitem, blockedWorkerAction, "shift B");
		setPreferencesForMenuItem(unblockedWorker, fontmenuitem, unblockedWorkerAction, "shift U");

		if (!worker.isBlocked()) { unblockedWorker.setEnabled(false); }
		editMenu.add(addWorkerItem);
		editMenu.add(delWorkerItem);
		editMenu.add(blockedWorker);
		editMenu.add(unblockedWorker);
		
		JMenu patternMenu = new JMenu("Шаблон");
		patternMenu.setFont(fontmenu);
		
		JMenuItem downloadPatternItem = new JMenuItem("Загрузити");
		JMenuItem savePatternItem = new JMenuItem("Зберегти");
		JMenuItem delPatternItem = new JMenuItem("Видалити");
		
		setPreferencesForMenuItem(downloadPatternItem, fontmenuitem, downloadPatternAction, "alt D");
		setPreferencesForMenuItem(savePatternItem, fontmenuitem, savePatternAction, "alt S");
		setPreferencesForMenuItem(delPatternItem, fontmenuitem, delPatternAction, "alt DELETE");
		
		patternMenu.add(downloadPatternItem);
		patternMenu.add(savePatternItem);
		patternMenu.add(delPatternItem);
		
		JMenu viewMenu = new JMenu("Вид");
		viewMenu.setFont(fontmenu);
		
		ButtonGroup btViewGroup = new ButtonGroup();
		horizont = new JRadioButtonMenuItem("Горизонтальний");
		vertical = new JRadioButtonMenuItem("Вертикальний");
		
		setPreferencesForMenuItem(horizont, fontmenuitem, orientationAction, "ctrl H");
		vertical.setSelected(true);
		setPreferencesForMenuItem(vertical, fontmenuitem, orientationAction, "ctrl V");
		
		btViewGroup.add(horizont);
		btViewGroup.add(vertical);
		viewMenu.add(horizont);
		viewMenu.add(vertical);
		
		JMenu infoMenu = new JMenu("Інфо");
		infoMenu.setFont(fontmenu);
		JMenuItem infoItem = new JMenuItem("Інформація", new ImageIcon("src\\icon\\info.png"));
		setPreferencesForMenuItem(infoItem, fontmenuitem, infoAction, "ctrl I");
		
		infoMenu.add(infoItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(patternMenu);
		menuBar.add(viewMenu);
		menuBar.add(infoMenu);
		menuBar.setFont(fontmenu);
		setJMenuBar(menuBar);
	}
	
	private void setPreferencesForMenuItem(JMenuItem item, Font font, 
			ActionListener action, String keystroke) {
		item.setFont(font);
		item.addActionListener(action);
		item.setAccelerator(KeyStroke.getKeyStroke(keystroke));
	}

	private void addMainPanel() {
		mainPanel = new JPanel();
		shiftPanel = new JPanel();
		shiftPanel.setLayout(new BoxLayout(shiftPanel, orientation));
		tablePanels = new Stack<>();
		tablePanels.add(new TablePanel(this));	
		shiftPanel.add(tablePanels.get(0));
		mainPanel.add(shiftPanel);
		scrollPane = new JScrollPane(mainPanel);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void addButton() {
		addshiftbt = new JButton("Додати зміну", new ImageIcon("src\\icon\\addshift.png"));
		delshiftbt = new JButton("Видалити зміну", new ImageIcon("src\\icon\\delshift.png"));
		addworkerbt = new JButton("Додати", new ImageIcon("src\\icon\\pl.png"));
		delworkerbt = new JButton("Видалити", new ImageIcon("src\\icon\\min.png"));
		savebt = new JButton("Зберегти", new ImageIcon("src\\icon\\save.png"));
		exitbt = new JButton("Вихід", new ImageIcon("src\\icon\\exit.png"));
		
		delshiftbt.setEnabled(false);
		setPreferencesForButton(addshiftbt, addShiftAction);
		setPreferencesForButton(delshiftbt, delShiftAction);
		setPreferencesForButton(addworkerbt, addWorkerAction);
		setPreferencesForButton(delworkerbt, delWorkerAction);
		setPreferencesForButton(savebt, saveAction);		
		setPreferencesForButton(exitbt, exitAction);

		JPanel[] flowPanels = new JPanel[2];
		for(int i=0; i<flowPanels.length; i++) {
			flowPanels[i] = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		}
		flowPanels[0].add(addworkerbt);
		flowPanels[0].add(delworkerbt);
		flowPanels[0].add(addshiftbt);
		flowPanels[0].add(delshiftbt);
		flowPanels[1].add(savebt);
		flowPanels[1].add(exitbt);
				
		JPanel boxPanel =  new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		for(int i=0; i<flowPanels.length; i++) {
			boxPanel.add(flowPanels[i]);
		}	
		JPanel btFlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btFlowPanel.add(boxPanel);
		
		lbFlowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		lbFlowPanel.add(info);
		
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));	
		southPanel.add(btFlowPanel);
		southPanel.add(lbFlowPanel);
			
		add(southPanel, BorderLayout.SOUTH);
	}
	
	private void setPreferencesForButton(JButton button, ActionListener action) {
		button.setFont(fontbt);
		button.addActionListener(action);
	}
	
	private void initializationInfo() {		
		
		info.setText("Всього робітників: " + worker.getNumberOfWorkers()+
				"  |  З них активних: " + worker.getNumberOfActiveWorkers()+
				"  |  Кількість шаблонів: " + worker.getNumberOfPatterns());
	}
	
	private void initializationAction() {
		exitAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		};
		
		addWorkerAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {		
				addWorkerFrame.setVisible(true);
				setEnabled(false);	
			}
		};
		
		delWorkerAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				delWorkerFrame.setVisible(true);
				setEnabled(false);
			}
		};
		
		unblockedWorkerAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				unblockedWorkerFrame.setVisible(true);
				setEnabled(false);
			}
		};
		
		addShiftAction = new FrameAction(this) {
			public void actionPerformed(ActionEvent event) {
				tablePanels.push(new TablePanel(mainFrame, tablePanels.size()));		
				shiftPanel.add(Box.createRigidArea(new Dimension(15, 15)));
				// добавляем на панель последний добавленный tablePanel
				shiftPanel.add(tablePanels.peek());
				if (tablePanels.size()>1) { 
					delshiftbt.setEnabled(true); 
					delItem.setEnabled(true);
				}
				scrollPane.revalidate();
				savebt.setEnabled(true);
				saveItem.setEnabled(true);
			}
		};
		
		delShiftAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (tablePanels.size()>1) {
					shiftPanel.remove(tablePanels.pop());
					shiftPanel.remove(shiftPanel.getComponentCount()-1);
					scrollPane.revalidate();
				}
				if (tablePanels.size()==1) {
					delshiftbt.setEnabled(false);
					delItem.setEnabled(false);
				}
			}
		};
		
		saveAction = new FrameAction(this) {
			public void actionPerformed(ActionEvent event) {
				FileDialog saveDlg = new FileDialog(mainFrame,"Збереження файлу",FileDialog.SAVE);
				saveDlg.setDirectory(".");
				saveDlg.setFile(getFileName()+".docx");
				saveDlg.setVisible(true);
				// проверка нажал ли пользователь кнопку "сохранить", если нет, то
				// методы getFile() и getDirectory() вернут null
				if (saveDlg.getFile()!=null) { 
					new WriteShiftToWordDocument(
							saveDlg.getDirectory(), saveDlg.getFile(), mainFrame.getTablePanels());
					tablePanels.removeAllElements();
					shiftPanel.removeAll();
					shiftPanel.repaint();
					scrollPane.revalidate();
					delshiftbt.setEnabled(false);
					delItem.setEnabled(false);
					savebt.setEnabled(false);
					saveItem.setEnabled(false);
				}
			}
		};	
		
		orientationAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (horizont.isSelected()) { setOrientation(BoxLayout.X_AXIS); }
				else if (vertical.isSelected() ) { setOrientation(BoxLayout.Y_AXIS); }
				shiftPanel.setLayout(new BoxLayout(shiftPanel, orientation));
				shiftPanel.removeAll();
				for (int i=0; i<tablePanels.size(); i++) {
					shiftPanel.add(tablePanels.get(i));
					shiftPanel.add(Box.createRigidArea(new Dimension(15,15)));
				}
				shiftPanel.repaint();
				scrollPane.revalidate();	
			}
		};
		
		savePatternAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Object nameOfPattern = JOptionPane.showInputDialog(null, 
						"Введіть назву шаблону:", "Збереження шаблону", 
						JOptionPane.PLAIN_MESSAGE, null, null, "Назва шаблону");					
				if (nameOfPattern != null) {
					if (worker.checkName((String)nameOfPattern)) {
						worker.addPattern(createPatternString((String)nameOfPattern), true);
						JOptionPane.showMessageDialog(null, "Шаблон успішно збережено");
						updateWorker(new Worker());
					}
					else {
						JOptionPane.showMessageDialog(null, 
								"Невірний формат назви шаблону."
								+ "\nЗабороняється використовувати наступні символи: \n"
								+ ": , / |  "
								+"\nШаблон не збережено.", 
								"Помилка в назві шаблону", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		
		downloadPatternAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String[] namesOfPatterns = worker.getListPatterns();			
				Object res = JOptionPane.showInputDialog(null, "Оберіть потрібний шаблон:", 
						"Загрузка шаблону", JOptionPane.DEFAULT_OPTION, 
						null, namesOfPatterns, namesOfPatterns[0]);
				if (res != null) { setPattern((String)res); }
			}
		};
		
		delPatternAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String[] namesOfPatterns = worker.getListPatterns();
				Object res = JOptionPane.showInputDialog(null, "Оберіть потрібний шаблон:", 
						"Видалення шаблону", JOptionPane.DEFAULT_OPTION, 
						null, namesOfPatterns, namesOfPatterns[0]);
				if (res != null) {
					worker.deletePattern((String)res);
					JOptionPane.showMessageDialog(null, "Шаблон видалено.");
					updateWorker(new Worker());
				}
			}
		};
		
		blockedWorkerAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				blockedWorkerFrame.setVisible(true);
				setEnabled(false);
			}
		};
		
		infoAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				infoFrame.setVisible(true);
				setEnabled(false);
			}
		};
	}
	
	
	
	private String createPatternString(String nameOfPattern) {
		StringBuilder namePatternAndNames = new StringBuilder();
		namePatternAndNames.append(nameOfPattern + ":");	
		for (int i=0; i<tablePanels.peek().postcbes.size(); i++) {
			StringBuilder name = new StringBuilder(
					tablePanels.peek().postcbes.get(i).getSelectedItem().toString());
			String nameOfPost = tablePanels.peek().namePostlbs.get(i).getText();
			int count = 0; // count - количество дополнительных людей для текущего поста
			// проверка на выход за границы списка
			for (;i+1+count<tablePanels.peek().postcbes.size() && 
					nameOfPost.equals(tablePanels.peek().namePostlbs.get(i+1+count).getText());
					count++) {}
			for (int j=0; j<count;j++) {
				name.append("|"+tablePanels.peek().postcbes.get(i+1+j).getSelectedItem().toString());
			}
			i= i+count;
			namePatternAndNames.append(name+",");
		}
		// видаляємо останню кому, вона нам не потрібна
		namePatternAndNames.deleteCharAt(namePatternAndNames.length()-1);
		namePatternAndNames.append("/");
		return namePatternAndNames.toString();
	}
	
	private void setPattern (String nameOfPattern) {
		tablePanels.peek().setPattern(nameOfPattern);
	}
	
	private String getFileName() {
		StringBuilder date = new StringBuilder("Зміна Гайда на ");
		if (tablePanels.size() > 1) {		
			date.append(tablePanels.get(0).datecb.getSelectedItem().toString());
			date.delete(date.length()-5, date.length());
			date.append(" - ");
			date.append(tablePanels.peek().datecb.getSelectedItem().toString());	
		}
		else {
			date.append(tablePanels.peek().datecb.getSelectedItem().toString());
		}
		return date.toString();
	}
	
	private void setOrientation(int axis) {
		orientation = axis;
	}
	
	public TablePanel[] getTablePanels() {
		TablePanel[] tableP = new TablePanel[tablePanels.size()];
		for (int i=0; i< tableP.length; i++) {
			tableP[i] = tablePanels.get(i);
		}
		return tableP;		
	}
	
	public Worker getWorker() { return worker; }
	public void setWorker(Worker worker) { this.worker = worker; }
	public void updateWorker(Worker worker) {
		setWorker(worker);
		initializationInfo();
		if (worker.isBlocked()) { 
			unblockedWorker.setEnabled(true);
			unblockedWorkerFrame = new UnblockedWorkerFrame(this);
		}
		else { unblockedWorker.setEnabled(false); }
		for (int i=0; i<tablePanels.size(); i++) {
			tablePanels.get(i).updateWorker();
		}
		delWorkerFrame.updateWorker(worker);
		blockedWorkerFrame.updateWorker(worker);
		if (worker.isBlocked()) {
			unblockedWorkerFrame.updateWorker(worker);
		}
	}	
	
	class FrameAction implements ActionListener {
		protected MainFrame mainFrame;
		public FrameAction(MainFrame mainFrame) {
			this.mainFrame = mainFrame;
		}
		public void actionPerformed(ActionEvent e) {}
	}
	
}