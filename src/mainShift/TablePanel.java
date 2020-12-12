package mainShift;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class TablePanel  extends JPanel{
	
	// post - боєвий пост або НЧО (номер чергової обслуги)
	// если нужно создать TablePanel со следующей датой, то в конструктор передаём dateIndex
	
	private Font titleFont;
	private JLabel datelb;
	protected ArrayList <JLabel> namePostlbs;
	protected JComboBox<String> datecb;
	private DefaultComboBoxModel<String> datesComboBoxModel;
	protected ArrayList <JComboBox<String>> postcbes;
	private DefaultComboBoxModel <String> workerList;
	private ArrayList <JButton> humanbts;
	private final int NUMBER_OF_WORKPLACES = 7;
	private int dateIndex = 0;
	private GridBagConstraints gbc;
	private ActionListener dateAction;
	private Border lineBorder;
	private MainFrame mainFrame;
	
	public TablePanel(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5,5,5,5);
		titleFont = new Font("Arial", 0, 14);
		lineBorder = BorderFactory.createLineBorder(Color.black);
		
		initializationComponents();
		initializationAction();
		createDate();
		addDate();
		addWorkPlaces();
		setWorkerList(mainFrame.getWorker(), datecb.getSelectedItem().toString());
	}
	
	public TablePanel(MainFrame mainFrame, int dateIndex) {
		super();
		this.mainFrame = mainFrame;
		this.dateIndex = dateIndex;
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5,5,5,5);
		titleFont = new Font("Arial", 0, 14);
		lineBorder = BorderFactory.createLineBorder(Color.black);
		
		initializationComponents();
		initializationAction();
		createDate();
		addDate();
		addWorkPlaces();
		setWorkerList(mainFrame.getWorker(), datecb.getSelectedItem().toString());
	}
	
	private void createDate() {
		datelb = new JLabel("Вкажіть дату: ");
		datecb = new JComboBox<>();
		datesComboBoxModel = new DefaultComboBoxModel<>();
		addModelElementsForDate();
		datecb.setModel(datesComboBoxModel);
		datecb.setSelectedIndex(2+dateIndex);
		setBorderPanel(datecb.getSelectedItem().toString());
		datecb.addActionListener(dateAction);
		
		setStyle(datelb, 0);
		setStyle(datecb, 0);
	}
	
	private void addDate() {
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(datelb, gbc);
		gbc.gridx = 1;
		add(datecb, gbc);	
	}
	
	
	private void addModelElementsForDate() {
		Calendar cl =Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y");
		Date d = cl.getTime();
		datesComboBoxModel.addElement(sdf.format(d));
		for (int i=0; i<10; i=i+1) {
			cl.add(Calendar.DAY_OF_MONTH, 1);
			d = cl.getTime();
			datesComboBoxModel.addElement(sdf.format(d));
		}
	}
	
	private void initializationComponents() {
		
		namePostlbs = new ArrayList<>();
		namePostlbs.add(new JLabel("НЧО 2-611"));
		namePostlbs.add(new JLabel("НЧО 621"));
		namePostlbs.add(new JLabel("НЧО 2-631"));
		namePostlbs.add(new JLabel("НЧО 2-632"));
		namePostlbs.add(new JLabel("НЧО 2-633"));
		namePostlbs.add(new JLabel("НЧО 2-634"));
		namePostlbs.add(new JLabel("НЧО 651"));
		
		postcbes = new ArrayList<>();		
		humanbts = new ArrayList<>();
		for (int i=0; i<NUMBER_OF_WORKPLACES; i++) { 
			postcbes.add(i, new JComboBox<>());
			humanbts.add(i, new PlusButton());
		}		
		for (int i=0; i<NUMBER_OF_WORKPLACES; i++) { 
			setStyle(namePostlbs.get(i), Font.BOLD);
			postcbes.get(i).setPreferredSize(new Dimension(300, 30));
			setStyle(postcbes.get(i), 0);
		}
		
		
	}
	
	private void addWorkPlaces() {		
		for (int i=0; i<postcbes.size(); i++) {
			gbc.gridx = 0;
			gbc.gridy = i+1;
			add(namePostlbs.get(i), gbc);
			gbc.gridx = 1;
			add(postcbes.get(i), gbc);
			gbc.gridx = 2;
			add(humanbts.get(i), gbc);
		}
	}
	
	private void redrawingTablePanel() {		
		deleteComponents();
		addDate();
		addWorkPlaces();	
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		if (mainFrame.getExtendedState()!=JFrame.MAXIMIZED_BOTH) {
			mainFrame.pack();
			mainFrame.setLocationRelativeTo(null);
		}
		mainFrame.scrollPane.revalidate();
	}
	
	private void deleteComponents() {
		removeAll();
	}
	
	private void initializationAction() {
		dateAction = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setBorderPanel(datecb.getSelectedItem().toString());
				updateWorker();
			}
		};
		
		for (int i=0; i<humanbts.size(); i++) {
			humanbts.get(i).addActionListener(new AddHumanAction(i));
		}
		
	}
	
	private void reassignActionsForButtons(int index) {
		
		for (int i=index; i<humanbts.size(); i++) {
			// удаляем старые слушатели
			for (ActionListener listener: humanbts.get(i).getActionListeners()) {
				humanbts.get(i).removeActionListener(listener);
			}
			// устанавливаем новые
			if (humanbts.get(i) instanceof PlusButton) {
				humanbts.get(i).addActionListener(new AddHumanAction(i));
			}
			else {
				humanbts.get(i).addActionListener(new RemoveHumanAction(i));
			}
		}
	}
	
	// метод используется перед загрузкой нового шаблона, в случае, если в смене есть много
	// людей, т. е. после работы метода остаётся только по 1 человеку для каждого поста,
	// что является стандартной ситуацией
	private void clearPanel() {
		for (int i=0; i<humanbts.size(); i++) {
			if (humanbts.get(i) instanceof MinusButton) {
				removeHuman(i);
				i--;
			}
		}
	}
	
	private void setStyle(Component comp, int style) {
		int FONT_SIE =16;
		Font font = new Font("Verdana", style, FONT_SIE);
		comp.setFont(font);
	}
	
	private void setBorderPanel(String title) {
		setBorder(BorderFactory.createTitledBorder(lineBorder,
				"Зміна на "+title,TitledBorder.CENTER ,0,titleFont));
	}
	
	protected void setPattern(String nameOfPattern) {
		clearPanel();
		String[] names = mainFrame.getWorker().getListNames(nameOfPattern);
		for (int i=0, increase = 0, index=0; i<names.length; i++, index++) {
			int indexOfDelimiter = names[i].indexOf("|");
			if (names[i].indexOf("|") > 0 ) {
				int countOfDelimiter = names[i].length() - names[i].replace("|","").length();
				setNameForPost(names[i].substring(0, indexOfDelimiter), index);
				index++;
				int nextIndexOfDelimiter = indexOfDelimiter;
				String sub = names[i];
				for (int j=0; j<countOfDelimiter; j++) {
					sub = sub.substring(nextIndexOfDelimiter+1);
					nextIndexOfDelimiter = sub.indexOf("|");
					addHuman(i+increase, i+increase+1);
					increase++;
					if (nextIndexOfDelimiter < 0) {
						setNameForPost(sub.substring(0, sub.length()), index);
						break;
					}			
					setNameForPost(sub.substring(0, nextIndexOfDelimiter), index);
					index++;
				}
			}
			else {  setNameForPost(names[i], index); }
		}
	}
	
	
	// устанавливает имя на соответствующий пост по индексу, проверяет заблокирован ли данный
	// сотрудник, если заблокирован, выводит информационное сообщение
	private void setNameForPost(String nameOfWorker, int index) {
		if (mainFrame.getWorker().isBlocked()) {
			checkThePostForBlockedWorkers(nameOfWorker, index);
		}
		postcbes.get(index).setSelectedItem(nameOfWorker);	
	}
	// если сотрудник заблокирован, то вывести предупреждающее сообщение 
	// и установить пустую строку на данном посте
	protected void checkThePostForBlockedWorkers(String nameOfWorker, int index) {
		if ( mainFrame.getWorker().isBlocked(nameOfWorker) &&
				mainFrame.getWorker().checkDate(datecb.getSelectedItem().toString(),
						mainFrame.getWorker().getDate(nameOfWorker))) {
			String nameOfPost = namePostlbs.get(index).getText();
			JOptionPane.showMessageDialog(null, 
					nameOfWorker+" ("+nameOfPost+") "+" заблокований(на) до "
					+ mainFrame.getWorker().getDate(nameOfWorker)+" (включно)!\n"
					+ "Оберіть іншого співробітника на його(її) місце.",
					"Увага",JOptionPane.INFORMATION_MESSAGE);
			// если раннее не было добавлено пустого значения, то добавить его
			if(!postcbes.get(index).getItemAt(postcbes.get(index).getItemCount()-1).equals("")){
				postcbes.get(index).addItem("");
			}
			postcbes.get(index).setSelectedItem("");
		}	
	}
	
	// установить список сотрудников для всех постов
	public void setWorkerList(Worker worker, String date) {	
		for (int i=0; i<postcbes.size(); i++) {
			workerList = new DefaultComboBoxModel<>();
			int numberOfPost = worker.getNumberOfPost(namePostlbs.get(i).getText());
			for (String name : worker.getListWorkers(numberOfPost, datecb.getSelectedItem().toString())) {
				workerList.addElement(name);
			}
			postcbes.get(i).setModel(workerList);	
		}
	}
	
	// установить список сотрудников для конкретного поста
	public void setWorkerList(JComboBox<String> cb, int index) {	
		workerList = new DefaultComboBoxModel<>();
		Worker worker = getMainFrame().getWorker();
		int numberOfPost = worker.getNumberOfPost(namePostlbs.get(index).getText());
		for (String name : worker.getListWorkers(numberOfPost, datecb.getSelectedItem().toString())) {
			workerList.addElement(name);
		}
		cb.setModel(workerList);	
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public int getAllNumberOfPosts() {
		return postcbes.size();
	}
	
	public int getNumberOfPost(String nameOfPost) {
		int number = 0;
		for (int i=0; i<namePostlbs.size(); i++) {
			if (nameOfPost.equals(namePostlbs.get(i).getText())) { number++; }
		}		
		return number;
	}
	
	public void updateWorker() {
		setWorkerList(mainFrame.getWorker(), datecb.getSelectedItem().toString());
	}
	
	@Override
	public String toString() {
		
		StringBuilder tablePanel = new StringBuilder("TablePanel: ");	
		for (int i=0; i<postcbes.size(); i++) {
			tablePanel.append(System.lineSeparator());
			tablePanel.append(postcbes.get(i).getSelectedItem().toString());	
		}
		return tablePanel.toString();
	}
	
	private void addHuman(int index, int position) {
		namePostlbs.add(position, new JLabel(namePostlbs.get(index).getText()));
		setStyle(namePostlbs.get(position), Font.BOLD);
		postcbes.add(position, new JComboBox<>());
		postcbes.get(position).setPreferredSize(new Dimension(300, 30));
		setStyle(postcbes.get(position), 0);
		humanbts.add(position, new MinusButton());
		humanbts.get(position).addActionListener(new RemoveHumanAction(position));
		setWorkerList(postcbes.get(position), position);
		reassignActionsForButtons(position);
		redrawingTablePanel();
	}
	
	private void removeHuman(int index) {
		namePostlbs.remove(index);
		postcbes.remove(index);
		humanbts.remove(index);
		reassignActionsForButtons(index);
		redrawingTablePanel();
	}

	class AddHumanAction implements ActionListener {
		
		private int index;       // index -  it`s a position of current object in ArrayList
		protected int position;  // position - it`s a position for new object in ArrayList
		
		public AddHumanAction(int index) {
			this.index = index;
			position = index+1;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			addHuman(index, position);
		}
	}
	
	class RemoveHumanAction implements ActionListener {
		
		protected int index;  // index -  it`s a position of current object in ArrayList
		
		public RemoveHumanAction(int index) {  this.index = index; }
		@Override
		public void actionPerformed(ActionEvent e) {
			removeHuman(index);
		}			
	}
}