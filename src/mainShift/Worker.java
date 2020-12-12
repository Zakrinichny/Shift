package mainShift;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

public class Worker {
	
	// name - �������� �����, ��� ������ � ���� ������, ������� �� �������
	// alphabet ������ ������ 5 ����, ���� �� name ������ ���������� � ������
	
	private static String path = "src\\data\\",
						  fileNameData = "data.txt",
						  fileNamePattern = "pattern.txt",
						  fileNameBlockedWorkers = "blocked.txt";
	private char[] alphabet = {'�','�','�','�','�'}; 
    private String[] rank;
	private File fileData, filePattern, fileBlockedWorkers;
	private int numberOfWorker, numberOfPattern;
    private StringBuilder dataString, patternString, blockedString;
    private StringBuilder[][] database, patternbase, blockedworkerbase;
    private SimpleDateFormat sdf;
	
	public Worker() {
		
		fileData = new File(path+fileNameData);
		filePattern = new File(path+fileNamePattern);
		fileBlockedWorkers = new File(path+fileNameBlockedWorkers);
		dataString = new StringBuilder();
		patternString = new StringBuilder();
		blockedString = new StringBuilder();
		sdf = new SimpleDateFormat("dd.MM.yyyy");
		initializationRank();
		loadFromFile(dataString, fileData, "�����������");
		loadFromFile(patternString, filePattern, "�������" );
		loadFromFile(blockedString, fileBlockedWorkers, "������������ �����������");
		if (!dataString.toString().equals("null")) { fillDatabase(); }
		if(!patternString.toString().equals("null")) { fillPatternbase(); }
		if (!blockedString.toString().equals("null")) { fillBlockedworkerbase(); }
	}
	
	public void loadFromFile (StringBuilder string, File file, String value) {	
		try (BufferedReader load = new BufferedReader(new FileReader(file))) {
			string.append(load.readLine());
		}
		catch(IOException exc) {
			JOptionPane.showMessageDialog(null, "������� ��� ����� ��������� "+value+".");
		}		
	}
	
	private void fillDatabase() {	
		initializationDatabase();
		int beginningOfNameWorker = 0;
		int endOfNameWorker = 0;
		int workerCount = 0;
		for(int i=0; i<dataString.length(); i++) {
			char elementOfDataString = dataString.charAt(i);			
			for (int j=0; j<alphabet.length; j++) {
				if (elementOfDataString == alphabet[j]) {
					beginningOfNameWorker = i;
					while(dataString.charAt(i)!=':') { i++; }
					endOfNameWorker = i;
					database[workerCount][0].append(
							dataString.substring(beginningOfNameWorker, endOfNameWorker));
					identifyPostsNumbers(workerCount, i);
					workerCount++;
				}
			}
		}		
	}
	
	private void fillPatternbase() {
		initializationPatternbase();
		int beginningOfNamePattern = 0;
		int endOfNamePattern = 0;
		int patternCount = 0;
		for (int i=0; i<patternString.length(); i++) {
			String elementOfPatternString = patternString.substring(i,i+1).toLowerCase();
			Alphabet[] alpha = Alphabet.values();
			for (Alphabet letter : alpha) {
				if (elementOfPatternString.equals(letter.toString())) {
					beginningOfNamePattern = i;
					while(patternString.charAt(i)!=':') { i++; }
					endOfNamePattern = i;
					patternbase[patternCount][0].append(
							patternString.substring(beginningOfNamePattern, endOfNamePattern));
					i = identifyNamesOfPattern(patternCount, i+1);
					patternCount++;
					break;
				}
			}	
		}
	}
	
	private void fillBlockedworkerbase() {
		// �������������� �������� ��������������� ���������� �� ����� �� ��������� ������ ����
		checkBlockedWorkers();
		initializationBlockedWorkerBase();
		int begin = 0;
		int blockedWorkerCount = 0;
		for (int i=0; i<blockedString.length(); i++) {
			if (blockedString.charAt(i) == ':') {
				blockedworkerbase[blockedWorkerCount][0].append(blockedString.substring(begin, i));
				i++;
				begin = i;		
				while(blockedString.charAt(i)!='/') { i++; }
				blockedworkerbase[blockedWorkerCount][1].append(blockedString.substring(begin, i));
				i++;
				begin = i;
				blockedWorkerCount++;
			}
		}
	}
	
	private void initializationDatabase() {
		numberOfWorker = getNumberOfWorkers();
		int[] numberOfPosts = getNumberOfPosts(numberOfWorker);
		database = new StringBuilder[numberOfWorker][];
		for (int i=0; i<database.length;i++) {
			database[i] = new StringBuilder[numberOfPosts[i]];
			for (int j=0; j<database[i].length; j++) {
				database[i][j] = new StringBuilder();
			}
		}
	}
	
	private void initializationPatternbase() {
		numberOfPattern = getNumberOfPatterns();
		int[] numberOfNames = getNumberOfNames(numberOfPattern);
		patternbase = new StringBuilder[numberOfPattern][];
		for (int i=0; i<patternbase.length;i++) {
			patternbase[i] = new StringBuilder[numberOfNames[i]];
			for (int j=0; j<patternbase[i].length; j++) {
				patternbase[i][j] = new StringBuilder();
			}
		}
	}
	
	private void initializationBlockedWorkerBase() {
		int numberOfBlockedWorker = getNumberOfBlockedWorkers();
		// 2, ������ ��� ����� ������ ��� ���������� � ����, �� ������� �� ������������
		blockedworkerbase = new StringBuilder[numberOfBlockedWorker][2];
	    for (int i=0; i<blockedworkerbase.length; i++) {
	    	for (int j=0; j<blockedworkerbase[i].length; j++) {
	    		blockedworkerbase[i][j] = new StringBuilder();
	    	}
	    }
	}
	
	private void identifyPostsNumbers(int numberOfWorker, int i) { 
		int step = 3; // ��� = 3, ������ ��� ������ ������ ����������
		// index = 1, ������ ��� ������� ������ ������� ���� ������ ��� name �����������,
		// � � 1-� ����� ���������� ����������� ������
		int index = 1; 
		while (dataString.charAt(i)!='/') {
			i++;		
			database[numberOfWorker][index].append(dataString.substring(i,i+step));
			index++;
			i = i+step;
		}
	}
	
	private int identifyNamesOfPattern(int patternCount, int i) {
		int amountOfWorkers = getNumberOfNamesForPattern(patternbase[patternCount][0].toString());
		for(int index=0; index<amountOfWorkers; index++) {
			int begginingOfName = i; 
			char symbol = index == amountOfWorkers-1 ? '/' : ',';
			while (patternString.charAt(i)!=symbol) { i++; 	}	
			// index+1, ������ ��� 0-� ������ ������ ������ �������
			patternbase[patternCount][index+1].append(patternString.substring(begginingOfName,i));
			i++;
		}
		int back = i-1; // ��������� �� 1, ������ ��� �� ������� ������ i ����� ���������� �� 1
		return back;
	}
		
	private void initializationRank() {
		rank = new String[15];
		rank[0] = "��������� ���";
		rank[1] = "������";
		rank[2] = "��. ������";
		rank[3] = "���. �������";
		rank[4] = "�������";
		rank[5] = "��. �������";
		rank[6] = "��������";
		rank[7] = "���������";
		rank[8] = "��. ���������";
		rank[9] = "���. ���������";
		rank[10] = "���������";
		rank[11] = "��. ���������";
		rank[12] = "������";
		rank[13] = "�����";
		rank[14] = "�����������";
	}
	// ���� ���� ���������������� ��������� �������, ������� ��� �� ����� � ������ ��������� ������
	private void checkBlockedWorkers() {
		int begin = 0;
		for (int i=0; i<blockedString.length(); i++) {
			if (blockedString.charAt(i) == ':') {
				String nameOfWorker = blockedString.substring(begin, i);
				i++;
				begin = i;		
				while(blockedString.charAt(i)!='/') { i++; }
			    Date today = new Date();
			    if ( !checkDate(sdf.format(today), blockedString.substring(begin,i)) ) {
			    	deleteBlockedWorker(nameOfWorker);
			    	// ������� ������
			    	blockedString.delete(0, blockedString.length());
			    	loadFromFile(blockedString, fileBlockedWorkers, "������������ �����������");
			    	checkBlockedWorkers();
			    	break;
			    }
			    i++;
			    begin = i;
			}
		}
	}
	
	private int getNumberOfValue(StringBuilder string) {
		int numberOfValue = 0;
		for (int i=0;i<string.length();i++) {
			char symbol = string.charAt(i);
			if (symbol=='/') { numberOfValue++; }
		}
		return numberOfValue;
	}
	
	public int getNumberOfWorkers() {
		return getNumberOfValue(dataString);
	}
	
	public int getNumberOfPatterns() {
		return getNumberOfValue(patternString);
	}
	
	public int getNumberOfActiveWorkers() {
		return getNumberOfWorkers() - getNumberOfBlockedWorkers();
	}
	
	public int getNumberOfBlockedWorkers() {
		return getNumberOfValue(blockedString);
	}
	
	private int[] getNumberOfValue(int numberOfValue, StringBuilder str) {
		int[] numberOfValues = new int[numberOfValue];
		int index = 0;
		// number = 2, ������ ��� ���� ����������� �������, �� ��� ������� ���� ���� ����(���)
		// (������ ������� ���������� ��� ����� 2-�� ������� (�������)) + 0-� ������ ������� ����
		// ��������� ��� name ����������(�������), ����� ����������� ����� ���� ��� ������� 
		// ����������(�������) ��� 2 (nameOfWorker � ����� �����(nameOfPattern � ����� ����������)). 
		// ���������� ��� ����� � ������ ����� �� ����� ����!
		int number = 2;
		for (int i=0;i<str.length();i++) {		
			if (str.charAt(i)==',') { number++; }
			if (str.charAt(i)=='/') {
				numberOfValues[index] = number;
				index++;
				number = 2;
			}
		}		
		return numberOfValues;
	}
	
	// ����� ���������� ������ ���������� ������ ��� ������� ���������� +1 
	// (������ ��� ����� ����������),
	// ������ ��� ������ ��� ���� ����� ��� ���������� � ������������ ��� ������
	private int[] getNumberOfPosts(int numberOfWorker) {
		return getNumberOfValue(numberOfWorker, dataString);
	}
	
	private int[] getNumberOfNames(int numberOfPattern) {
		return getNumberOfValue(numberOfPattern, patternString);
	}
	
	private int getNumberOfNamesForPattern(String nameOfPattern) {
		int number = 0;
		int index = patternString.indexOf(nameOfPattern);
		while(patternString.charAt(index)!=':') { index++; }
		for (int i=index+1; i<patternString.length(); i++) {
			if (patternString.charAt(i)==',') { number++; }
			if (patternString.charAt(i)=='/') { number++; break; }
		}
		return number;
	}
	
	
	// ��������� ������ ��� �������� �������, ��������� � �������� ��������� ������ ���
	// ���������� ������ ����� ���������� ������ � ������� rank
	private String[] sortedList(String[] names) {		
		String sortedNames[] = new String[names.length];
		int index = 0;
		for (int i=rank.length-1; i>=0; i--) {
			for (int j=0; j<names.length; j++) {
				if(names[j].indexOf(rank[i]) == 0) {
					sortedNames[index] = names[j];
					index++;
				}
			}
		}
		return sortedNames;	
	}
	
	private void writeToFile(String str, Boolean addANote, File file) {
		try (PrintWriter write = new PrintWriter(new FileWriter(file, addANote))) {
			write.write(str); 
		}
		catch (FileNotFoundException exc) {
			JOptionPane.showMessageDialog(null, "���� ��� ������ ����������� �� ��������.");
		}
		catch(IOException exc) {
			JOptionPane.showMessageDialog(null, "������� ������ ����� � ����.");
		}
	}
	
	public void addWorker(String nameAndPosts, Boolean addANote) {
		writeToFile(nameAndPosts, addANote, fileData);
	}
	
	public void addBlockedWorker(String nameAndDate, Boolean addANote) {
		writeToFile(nameAndDate, addANote, fileBlockedWorkers);
	}
	
	public void addPattern(String patternAndNames, Boolean addANote) {
		writeToFile(patternAndNames, addANote, filePattern);
	}
	
	private void deleteValue(StringBuilder str, String value, File file) {
		int begin = str.indexOf(value);
		//  � end ���������� 1, ������ ��� ����� delete() ������� �� ���������� ������� 
		//  �� ������������
		int end = str.indexOf("/", begin)+1;
		str.delete(begin, end);
		writeToFile(str.toString(), false, file);
	}
	
	public void deleteWorker(String name) {
		deleteValue(dataString, name, fileData);
	}
	
	public void deleteBlockedWorker(String name) {
		deleteValue(blockedString, name, fileBlockedWorkers );
	}
	
	public void deletePattern(String nameOfPattern) {
		deleteValue(patternString, nameOfPattern, filePattern);
	}
	
	public String[] getListValues(StringBuilder[][] base, int quantity) {
		String[] listValues = new String[quantity];
		for (int i=0; i<listValues.length; i++) {
			listValues[i] = base[i][0].toString();
		}
		return listValues;
	}
	
	
	public String[] getListWorkers() {
		return sortedList(getListValues(database, numberOfWorker));
	}
	
	public String[] getListWorkers(boolean withoutBlockedWorkers) {
		ArrayList<String> listWorkers = new ArrayList<>(numberOfWorker);
		for (String name : getListWorkers()) { listWorkers.add(name); }
		Date date = new Date();
		if (blockedworkerbase!=null) {  removingBlockedWorkers(listWorkers, sdf.format(date)); }
		String[] list = new String[listWorkers.size()];
		for (int i=0; i<list.length;i++) { list[i] = listWorkers.get(i);  }
		return sortedList(list);
	}
	
	public String[] getListBlockedWorkers() {
		return sortedList(getListValues(blockedworkerbase, getNumberOfBlockedWorkers()));
	}
	
	public String[] getListPatterns() {
		return getListValues(patternbase, numberOfPattern);
	}
	
	
	public String[] getListWorkers(int post, String dateShift) {
		String postStr = Integer.toString(post);
		int size = identifyTheNumberOfWorkersForAGivenPost(postStr);
		ArrayList<String> listWorkers = new ArrayList<>(size); 
		for (int i=0; i<numberOfWorker; i++) {
			for (int j=0; j<database[i].length;j++) {
				if (database[i][j].toString().equals(postStr)) {
					listWorkers.add(database[i][0].toString());  
				}
			}
		}
		if (blockedworkerbase!=null) {  removingBlockedWorkers(listWorkers, dateShift); }
		String[] list = new String[listWorkers.size()];
		for (int i=0; i<list.length;i++) { list[i] = listWorkers.get(i);  }
		return sortedList(list);
	}
	
	private void removingBlockedWorkers(ArrayList<String> list, String dateShift) {	
		for (int i=0; i<list.size(); i++) {
			for (int j=0; j<blockedworkerbase.length; j++) {
				if ( list.get(i).toString().equals(blockedworkerbase[j][0].toString()) &&
						checkDate(dateShift, blockedworkerbase[j][1].toString()) ) {
					list.remove(i);
					i--;
					break;
				}
			}
		}
	}
	// ��������� ��� ������ (����) � ������� �������: 00.00.0000
	// ���������� true, ���� date1 ������ ��� date2+day
	public boolean checkDate(String dateShift, String dateBlockedWorker) {
		int dayShift = Integer.parseInt(dateShift.substring(0,2));
		int dayBlockedWorker = Integer.parseInt(dateBlockedWorker.substring(0,2));
		int monthShift = Integer.parseInt(dateShift.substring(3,5));
		int monthBlockedWorker = Integer.parseInt(dateBlockedWorker.substring(3,5));
		int yearShift = Integer.parseInt(dateShift.substring(6,10));
		int yearBlockedWorker = Integer.parseInt(dateBlockedWorker.substring(6,10));	
		Calendar shiftDate = new GregorianCalendar(yearShift, monthShift-1, dayShift);
		// �������� �� ��� 1, ������ ��� ���������� ��������� �� ������������ ��� ������������,
		// ������� ���������� ����� �� ��������� ��� �� ����������
		Calendar blockedWorkerDate = new GregorianCalendar(yearBlockedWorker, 
				monthBlockedWorker-1, dayBlockedWorker+1);
		if (shiftDate.before(blockedWorkerDate)) {  return true; }
		else return false;
	}
	
	public String[] getListNames(String nameOfPattern) {
		int size = 0;
		String[] listNames = null;
		for (int i=0; i<patternbase.length; i++) {
			if (patternbase[i][0].toString().equals(nameOfPattern)) {
// ������ � ���� �������� ��� ������� � ������ ���, ��� ����� ���������� ���, ������� length-1
				size = patternbase[i].length-1;
				listNames = new String[size];
				for (int j=0; j<size; j++) {
// patternbase[i][0] - ��� ��� ������� (��� ��� �� �����), ������� ���������� 1 � ������� �������
					listNames[j] = patternbase[i][j+1].toString();
				}
				break;
			}
		}
		return listNames;
	}

	public String[] getListRank() {
		return rank;
	}

	private int identifyTheNumberOfWorkersForAGivenPost(String post) {
		int number = 0;
		for (int i=0; i<numberOfWorker; i++) {
			for (int j=0; j<database[i].length;j++) {
				if (database[i][j].toString().equals(post)) {
					number++;
					break;
				}
			}
		}	
		return number;
	}
	
	public int getNumberOfPost(String nameOfPost) {	
		StringBuilder res = new StringBuilder(nameOfPost);		
		return Integer.parseInt(res.substring(res.length()-3, res.length()));
	}
	
	public int getIndexOfPost(String nameOfPost) {
		int index = -1;
		if (nameOfPost.equals("��� 2-611") || nameOfPost.equals("��� 691")) {
			index = 0;
		}
		else if(nameOfPost.equals("��� 621")) {
			index = 1;
		}
		else if(nameOfPost.equals("��� 2-631")) {
			index = 2;
		}
		else if(nameOfPost.equals("��� 2-632")) {
			index = 3;
		}
		else if(nameOfPost.equals("��� 2-633")) {
			index = 4;
		}
		else if(nameOfPost.equals("��� 2-634")) {
			index = 5;
		}
		else if(nameOfPost.equals("��� 651")) {
			index = 6;
		}
		return index;
	}
	
	public boolean isBlocked() {
		return !blockedString.toString().equals("null");
	}
	
	public boolean isBlocked(String nameOfBlockedWorker) {
		if (isBlocked()) {
			for (int i=0; i<blockedworkerbase.length; i++) {
				if (blockedworkerbase[i][0].toString().equals(nameOfBlockedWorker)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public Boolean checkName(String name) {
		boolean result = true;
		String forbiddenSymbols = ":,/|";
		for (int i=0; i<forbiddenSymbols.length(); i++) {
			for (int j=0; j<name.length();j++) {
				if ( name.substring(j,j+1).equals(forbiddenSymbols.substring(i,i+1)) ) { 
					result = false; 
					break;
					}
			}
		}
		return result;
	}
	
	// �������� ����, �� ������� ������������ ���������
	public String getDate(String nameOfWorker) {
		String date = null;
		for (int i=0; i<blockedworkerbase.length; i++) {
			if (nameOfWorker.equals(blockedworkerbase[i][0].toString())) {
				date = blockedworkerbase[i][1].toString();
			}
		}
		return date;
	}

}
