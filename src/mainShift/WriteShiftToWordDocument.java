package mainShift;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class WriteShiftToWordDocument {
	
	private String filePath, fileName;
	private TablePanel[] tables;
	private XWPFDocument doc;
	private XWPFTable table;
	private ArrayList<ArrayList<String>> posts;
	private final int NUMBER_OF_WORKPLACES = 7;
	private String dateShift;
	
	public WriteShiftToWordDocument(String filePath, String fileName, TablePanel[] tablePanels) {
		this.filePath = filePath;
		this.fileName = fileName;
		tables = tablePanels;
		createWord();
	}
	
	private void createWord() {
		checkFileName();
		try {
			doc = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File(filePath+fileName));
			fillDocument();
			doc.write(out);
			out.close();
			doc.close();
		}
		catch(IOException exc) {
			JOptionPane.showMessageDialog(null, "������� ��������� �����");
		}
	}

	private void fillDocument() {
		for (int i=0; i<tables.length; i++) {
			writeHeading(i);
			fillPosts(i);
			writeTable();
			setEmptyRun(5); // ������ ������ �������, ����� ����� ����. ��� ������
		}
	}
	
	private void writeHeading(int count) {
		
		dateShift = tables[count].datecb.getSelectedItem().toString();
		XWPFParagraph paragraph = doc.createParagraph();	
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		paragraph.setSpacingAfter(0);
		XWPFRun run = paragraph.createRun();			
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);
		run.setText("���� ��������� ������ ����� ����- �� ������������� ������");
		run.addBreak();
		run.setText("������ ����- �� ������������� ������, ò�� �� ���");
		run.addBreak();
		run.setText("�� "+dateShift+" ����");
		run.addBreak();	
	}
	
	private void writeTable() {
		
		table = doc.createTable(15, 1);
		fillTableRow(0, 1, "����� ��Ĳ�- �� ������������� ��'����");
		fillTableRow(1, 1, "²�Ĳ� ��Ĳ�- �� ������������� ��'����");
		fillTableRow(2, 1, "2 ��-610");
		fillTableRow(3, 2, "��� 2-611");
		fillTableRow(4, 1, "��-620");
		fillTableRow(5, 2, "��� 621");
		fillTableRow(6, 1, "2 ��-630");
		fillTableRow(7, 2, "��� 2-631");
		fillTableRow(8, 2, "��� 2-632");
		fillTableRow(9, 2, "��� 2-633");
		fillTableRow(10, 2, "��� 2-634");
		fillTableRow(11, 1, "��-650");
		fillTableRow(12, 2, "��� 651");
		fillTableRow(13, 1, "��-690");
		fillTableRow(14, 2, "��� 691");
		
		// ������������ ������ ��� ������ ������, ����� �� ��� ����������� � ��������� ������
		CTTblWidth tableWidth = table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW();
		tableWidth.setType(STTblWidth.PCT);
		// � �� ���� ��� ��� �������� ������ (�������� �������� �� 20 �� 3000,
		// ������ �� ����������), �� ������� ����������� �� ������ �������� � ��� ���� ������. 
		// �������� STTblWidth.PCT ����� ����� �� ��, ����� ������� ���� �� ������ ��������
		tableWidth.setW(BigInteger.valueOf(20));	
		
		// ������������ ������������ � ������ ���� ������� ������� �� ������
		table.getRow(0).getCell(0).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
		table.getRow(1).getCell(0).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);	
	}
	
	private void fillTableRow(int indexOfRow, int numberOfCells, String text) {
		
		XWPFTableRow row = table.getRow(indexOfRow);
		createParagraph(row.getCell(0), text, false);
		if (numberOfCells == 1) {
			// ������������ ������ ������ � 2 ������
			row.getCell(0).getCTTc().addNewTcPr().addNewGridSpan().setVal(BigInteger.valueOf(2));
		}
		else {
			row.createCell();
			CTTblWidth cellWidth = row.getCell(1).getCTTc().addNewTcPr().addNewTcW();
			cellWidth.setType(STTblWidth.DXA);
			cellWidth.setW(BigInteger.valueOf(3000));
			int indexOfPosts = tables[0].getMainFrame().getWorker().getIndexOfPost(text);
			boolean addBreak = false;
			if (posts.get(indexOfPosts).size()>1) { 
				addBreak = true; 
				row.getCell(0).setVerticalAlignment(XWPFVertAlign.CENTER);
			}
			for (int i=0; i<posts.get(indexOfPosts).size(); i++) {
				if (i == posts.get(indexOfPosts).size()-1) { addBreak = false; }
				createParagraph(row.getCell(1), posts.get(indexOfPosts).get(i), addBreak);
			}
		}
	}
	
	private void createParagraph(XWPFTableCell cell, String text, Boolean addBreak) {
		XWPFParagraph paragraph = cell.getParagraphArray(0);
		paragraph.setSpacingAfter(0);
		paragraph.setIndentationLeft(100);
		XWPFRun run = paragraph.createRun();
		run.setText(text);
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);
		if(addBreak) { run.addBreak(); }
	}
	
	private void fillPosts(int count) {
				
		posts = new ArrayList<>(NUMBER_OF_WORKPLACES);
		
		for (int i=0;i<NUMBER_OF_WORKPLACES;i++) {
			 posts.add(new ArrayList<>());
		}	
		for (int i=0; i<tables[count].postcbes.size(); i++) {
			String name = tables[count].postcbes.get(i).getSelectedItem().toString();
			String nameOfPost = tables[count].namePostlbs.get(i).getText();
			int index = tables[count].getMainFrame().getWorker().getIndexOfPost(nameOfPost);
			posts.get(index).add(name);	
		}
	}
	
	private void setEmptyRun(int count) {
		XWPFParagraph paragraph = doc.createParagraph();
		XWPFRun run = paragraph.createRun();
		for (int i=0; i<count; i++) { run.setText(""); }
	}
	
	private void checkFileName() {
		if(!fileName.endsWith(".docx")) {
			fileName = fileName + ".docx";
		}
	}
}
