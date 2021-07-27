package assignment2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class ExcelDemo extends JFrame implements ActionListener {
	//#########################FEILD####################
	private static int WIDTH = 610;
	private static int HEIGHT = 588;
	private JScrollPane scrollPane;
	private JTable table, headerTable;
	private JMenuBar menuBar;
	private JMenu fileMenu, formulasMenu, functionMenu;
	private JMenuItem newItem, open, save, exit, sum,
					  average, count, max, min;
	private String title;
	private int cardinality, degree;
	//#########################FEILD####################	
	
	//������
	public ExcelDemo () {	
		title = "�� Microsoft Excel ��ũ��Ʈ.xlsx - Excel"; //�⺻ ����
		
		setTitle(title); //���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close������ ����
		setSize(WIDTH, HEIGHT); //frame ũ�� ����
		setLocationRelativeTo(null);  //ȭ�� �߾ӿ� ��ġ�ϰ� �ϴ� �ڵ�
		
		
		setLayout( new BorderLayout() ); //���̾ƿ� ���� : borderlayout
		String data1[][] = new String[100][26]; //table ä��� 
		String head1[] = new String[26]; //table header
		
		for( int i = 0 ; i < 26 ; ++i) {
			head1[i] = new String( Character.toString(('A' + i)) ); //table col header A to Z
		}
		
		headerTable = new JTable(100,1); // table, headertable ����
		table = new JTable(data1, head1); 
		
		for( int i = 0 ; i < 100 ; ++i) {
			headerTable.setValueAt(Integer.toString(i), i, 0); // headertable���� rowheader�� �� ���� �ֱ�
		}
		
		DefaultTableCellRenderer rr = new DefaultTableCellRenderer();
		rr.setHorizontalAlignment(SwingConstants.CENTER);
		headerTable.getColumnModel().getColumn(0).setCellRenderer(rr); // rowheader ���� ��� ����
		
		rr = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int col) {
				JComponent com = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			
			if(isSelected)
				com.setFont(getFont().deriveFont(Font.BOLD)); // ����!
			return com;
			}
		}; // cell ���ý� �ش�Ǵ� rowheader�� ����
		
		headerTable.setDefaultRenderer(headerTable.getColumnClass(0), rr); //rowheader�� ����
		headerTable.setSelectionModel(table.getSelectionModel()); 
		
		headerTable.setBackground(new Color(243,243,243)); //rowheader ��ĥ
		headerTable.getColumnModel().getColumn(0).setPreferredWidth(60); //rowheader �ʺ� ����
		headerTable.setPreferredScrollableViewportSize(new Dimension(60,0));
		
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //headertable ũ�� ���� �Ұ�
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //talbe ũ�� ���� �Ұ�
		
		scrollPane = new JScrollPane(table); //table�� scrollpane�� ���̱�
		scrollPane.createHorizontalScrollBar(); //���� ��ũ�ѹ� ���̱�
		
		scrollPane.setColumnHeaderView(table.getTableHeader()); //columntable�� table�� header�� ����
		scrollPane.setRowHeaderView(headerTable); //rowheader headertalbe�� ����
		add(scrollPane); //scrollpane frame�� ���̱�
		
		createMenu();
		table.addMouseListener(new MouseAdapter() {	
			@Override
			public void mouseClicked(MouseEvent event) {
				cardinality = table.getSelectedRow();
				degree = table.getSelectedColumn();
			}
		}); //table �̺�Ʈ ó��
	}
	
	//createMenu �޽��
	public void createMenu () {
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File"); //�޴� �̸� ����
		formulasMenu = new JMenu("Formulas");
		functionMenu = new JMenu("Function");
		
		newItem = new JMenuItem("New"); //�޴������� �̸� ����
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		exit = new JMenuItem("Exit");
		
		sum = new JMenuItem("SUM");
		average = new JMenuItem("AVERAGE");
		count = new JMenuItem("COUNT");
		max = new JMenuItem("MAX");
		min = new JMenuItem("MIN");
		
		fileMenu.add(newItem); //filemenu�� filemenuItem ���̱�
		fileMenu.add(open);
		fileMenu.addSeparator(); //������
		fileMenu.add(save);
		fileMenu.addSeparator(); //������
		fileMenu.add(exit);
		
		functionMenu.add(sum); //functionMenu�� ���̱�
		functionMenu.add(average);
		functionMenu.add(count);
		functionMenu.add(max);
		functionMenu.add(min);
		
		formulasMenu.add(functionMenu); //formulas�� functionMenu ���̱�
		
		menuBar.add(fileMenu); //�޴��ٿ� file,formulas�޴� �ֱ�
		menuBar.add(formulasMenu);
		
		setJMenuBar(menuBar); //JMenubar�� menuBar ����
		
		sum.addActionListener(this); //MenuItem���� ActionListener�� �߰�
		average.addActionListener(this);
		count.addActionListener(this);
		max.addActionListener(this);
		min.addActionListener(this);
		
		newItem.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
	}

	//eventó�� �޽��
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc; //open, save�� ���� filechooser
		BufferedReader br; //open�� ���� BR
		BufferedWriter bw; //save�� ���� BW
		String str, from, to, line; 
		StringTokenizer st, st1; //open save , formulas�� ���� ST
		int idx1,idx2;
		int idx3,idx4;
		int colcount, rowcount;
		
		//###########################################FUNCTION####################################
		if(e.getSource() == sum) {
			double _sum = 0;
			str = JOptionPane.showInputDialog(null,"Function Arguments", 
													"SUM", JOptionPane.PLAIN_MESSAGE); //input dialog���
			st = new StringTokenizer(str,":"); //string�޾� �յ� ó��
			from = st.nextToken();
			to = st.nextToken();
			idx1 = Integer.parseInt(from.substring(1));
			idx2 = Integer.parseInt(to.substring(1));
			idx3 = from.charAt(0) - 'A';
			idx4 = to.charAt(0) - 'A';

			for( int i = idx1 < idx2 ? idx1 : idx2 ; i <= (idx1 < idx2 ? idx2 : idx1) ; ++i) { //�յ� ��ġ�� �ٲپ� �ᵵ ������ �˸°� �⵵�� ����
				for( int j = idx3 < idx4 ? idx3 : idx4 ; j <= (idx3 < idx4 ? idx4 : idx3) ; ++j ) {
					if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) //nullpointer exception ó��
						_sum += Double.parseDouble(table.getValueAt(i, j).toString()); //������ �ִ� ���ڵ� ��� ���ϱ�
					}
				}
			
			table.setValueAt(Double.toString(_sum), cardinality, degree ); //click�� cell�� �� ����
		}
		
		else if(e.getSource() == average) {
			double _average = 0;
			int count = 0;
			str = JOptionPane.showInputDialog(null,"Function Arguments", 
												   "AVERAGE", JOptionPane.PLAIN_MESSAGE);
			st = new StringTokenizer(str,":");
			from = st.nextToken();
			to = st.nextToken();
			idx1 = Integer.parseInt(from.substring(1));
			idx2 = Integer.parseInt(to.substring(1));
			idx3 = from.charAt(0) - 'A';
			idx4 = to.charAt(0) - 'A';

			for( int i = idx1 < idx2 ? idx1 : idx2 ; i <= (idx1 < idx2 ? idx2 : idx1) ; ++i) {
				for( int j = idx3 < idx4 ? idx3 : idx4 ; j <= (idx3 < idx4 ? idx4 : idx3) ; ++j ) {
					if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) {
						_average += Double.parseDouble(table.getValueAt(i, j).toString()); //���� �ȿ� ���ڸ� ��� ���ϰ�
						count++; //valid�� cell�� ���� ����
						}
					}
				}
			table.setValueAt(Double.toString(_average/count), cardinality, degree ); //�� ����
		}
		
		else if(e.getSource() == count) {
			int cnt = 0;
			str = JOptionPane.showInputDialog(null,"Function Arguments", 
													"COUNT", JOptionPane.PLAIN_MESSAGE);
			st = new StringTokenizer(str,":");
			from = st.nextToken();
			to = st.nextToken();
			idx1 = Integer.parseInt(from.substring(1));
			idx2 = Integer.parseInt(to.substring(1));
			idx3 = from.charAt(0) - 'A';
			idx4 = to.charAt(0) - 'A';

			for( int i = idx1 < idx2 ? idx1 : idx2 ; i <= (idx1 < idx2 ? idx2 : idx1) ; ++i) {
				for( int j = idx3 < idx4 ? idx3 : idx4 ; j <= (idx3 < idx4 ? idx4 : idx3) ; ++j ) {
					if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) 
						cnt++; //valid�� cell�� ���� ����
						
					}
				}
			table.setValueAt(Integer.toString(cnt), cardinality, degree ); //���������� �� ����
		}
		
		else if(e.getSource() == max) {
			double _max = Double.MIN_VALUE, temp; //max�� ���� ���� ������ ����
			str = JOptionPane.showInputDialog(null,"Function Arguments", 
													"MAX", JOptionPane.PLAIN_MESSAGE);
			st = new StringTokenizer(str,":");
			from = st.nextToken();
			to = st.nextToken();
			idx1 = Integer.parseInt(from.substring(1));
			idx2 = Integer.parseInt(to.substring(1));
			idx3 = from.charAt(0) - 'A';
			idx4 = to.charAt(0) - 'A';

			for( int i = idx1 < idx2 ? idx1 : idx2 ; i <= (idx1 < idx2 ? idx2 : idx1) ; ++i) {
				for( int j = idx3 < idx4 ? idx3 : idx4 ; j <= (idx3 < idx4 ? idx4 : idx3) ; ++j ) {
					if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) {
						temp = Double.parseDouble(table.getValueAt(i, j).toString());
						_max = _max > temp ? _max : temp; //�ִ밪�� ���Ѵ�
						}
					}
				}
			table.setValueAt(Double.toString(_max), cardinality, degree ); //�� ����
		}
		
		else if(e.getSource() == min) {
			double _min = Double.MAX_VALUE, temp; //min�� �ִ�� ����
			str = JOptionPane.showInputDialog(null,"Function Arguments", 
													"MIN", JOptionPane.PLAIN_MESSAGE);
			st = new StringTokenizer(str,":");
			from = st.nextToken();
			to = st.nextToken();
			idx1 = Integer.parseInt(from.substring(1));
			idx2 = Integer.parseInt(to.substring(1));
			idx3 = from.charAt(0) - 'A';
			idx4 = to.charAt(0) - 'A';

			for( int i = idx1 < idx2 ? idx1 : idx2 ; i <= (idx1 < idx2 ? idx2 : idx1) ; ++i) {
				for( int j = idx3 < idx4 ? idx3 : idx4 ; j <= (idx3 < idx4 ? idx4 : idx3) ; ++j ) {
					if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) {
						temp = Double.parseDouble(table.getValueAt(i, j).toString());
						_min = _min < temp ? _min : temp;
						}
					}
				}
			table.setValueAt(Double.toString(_min), cardinality, degree );
		}
		
		//###########################################FUNCTION####################################
				
		//###########################################FILE####################################
		
		else if(e.getSource() == newItem) {
			this.dispose();
			new ExcelDemo().setVisible(true);
		}
		
		else if(e.getSource() == open) {
			jfc = new JFileChooser();
			jfc.setAcceptAllFileFilterUsed(false); //�ٸ� ������ ���� ���°� ����
			jfc.setFileFilter(new FileNameExtensionFilter(".csv", "csv")); //csv���� ���
			jfc.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt")); //txt���� ���
			
			if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { //������ �� ���ȴٸ�
				title = jfc.getSelectedFile().getAbsolutePath(); //�����η� Ÿ��Ʋ ����
				setTitle(title); 	
			
				for( int i = 0 ; i < table.getRowCount() ; ++i) {
					for ( int j = 0 ; j < table.getColumnCount() ; ++j ) {
						table.setValueAt( null, i, j); //table�� ��� ���� null�� �ʱ�ȭ
					}
				}
			
				try {
					br = new BufferedReader(new FileReader(title)); //�����η� ������ ����
					line = br.readLine(); //�ٸ��� �ޱ�
					rowcount = 0;
					colcount = 0;
					while(line != null && rowcount <= 99) { //
						st1 = new StringTokenizer(line, ",");
						//int tem = st1.countTokens();
						while(st1.hasMoreElements() && colcount <= 25 ) { //col�������� ����, st�� element�� �� �ִ� ��Ȳ����
							String tmp = st1.nextToken();
							table.setValueAt(tmp, rowcount, colcount++); // table���� ����
						}
						colcount = 0; //������ ������ colcount 0���� �ʱ�ȭ
						line = br.readLine(); //������ �� �ް�
						rowcount++; //rowcount �Ѱ� �ø���
					}
					br.close(); //br�� �� ���� �ݱ�.
				} catch (IOException e1) { //���� ó��
					e1.printStackTrace(); 
				}
				for( int i = 0 ; i < table.getRowCount() ; ++i) {
					for ( int j = 0 ; j < table.getColumnCount() ; ++j ) {
						if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().equals(" ")) // ���⸦ ��� null�� �ٲ�
							table.setValueAt( null, i, j);
					}
				}
			}
		}
		
		else if(e.getSource() == save) {
			jfc = new JFileChooser();
			if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) { //���� ��θ� �� �����ߴٸ�
				try {
					bw = new BufferedWriter(new FileWriter(jfc.getSelectedFile())); //���õ� ��η� ���� ����
					for( int i = 0 ; i < 100 ; ++i ) {
						for( int j = 0 ; j < 26 ; ++j ) {
							if(table.getValueAt(i, j) != null && table.getValueAt(i, j).toString().trim().length() != 0) // ���� �ִٸ�
								bw.write(table.getValueAt(i, j).toString()); //table ������ ����
							else
								bw.write(" "); //null���� �����
							bw.write(","); // ������ ǥ��
						}
						bw.newLine(); // ���� ���;���
					}
					bw.close(); // �� ���� bw �ݱ�
				}
				catch ( Exception e1 ) {
					e1.printStackTrace();
				}	
			}
		}
		
		else if(e.getSource() == exit) {
			System.exit(0); // ���α׷� ����
		}
		
		//###########################################FILE####################################
		
	}
		
	//main
	public static void main(String[] args) {
		new ExcelDemo().setVisible(true); //��ü ����, frame ���̰� �ϱ�.
	}
}
