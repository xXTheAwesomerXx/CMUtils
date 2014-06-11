package org.CMUtils;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.Container.Methods;
import org.Container.Variables;
import org.w3c.dom.Document;

public class runSQL extends JFrame {
	private static final long serialVersionUID = 3107197191874268418L;

	public runSQL() {
		initComponents();
	}

	@SuppressWarnings("static-access")
	private void sqlFieldKeyPressed(KeyEvent p) {
		if (p.getKeyCode() == p.VK_ENTER) {
			sqlSelectThread sqlSelectThread = new sqlSelectThread("SQLSelect",
					sqlField.getText().toString());
			sqlSelectThread.start();
		}
	}

	private void excecuteButtonActionPerformed(ActionEvent e) {
		try {
			sqlSelectThread sqlSelectThread = new sqlSelectThread("SQLSelect",
					sqlField.getText().toString());
			sqlSelectThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void sqlComboBoxItemStateChanged(ItemEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		sqlField = new JTextField();
		excecuteButton = new JButton();
		resultsScrollPane = new JScrollPane();
		resultsTable = new JTable();
		sqlComboBox = new JComboBox<>();
		logScrollPane = new JScrollPane();
		logArea = new JTextArea();

		// ======== this ========
		setTitle("Run SQL Queries");
		Container contentPane = getContentPane();

		// ---- sqlField ----
		sqlField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				sqlFieldKeyPressed(e);
			}
		});

		// ---- excecuteButton ----
		excecuteButton.setText("execute");
		excecuteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				excecuteButtonActionPerformed(e);
			}
		});

		// ======== resultsScrollPane ========
		{
			resultsTable.setModel(new DefaultTableModel(Variables.sqlTableRows,
					Variables.sqlTableColumns) {
				private static final long serialVersionUID = -6396300478056746940L;
				boolean[] columnEditable = new boolean[6];

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return this.columnEditable[columnIndex];
				}
			});
			TableColumn tc = null;
			for (int i = 0; i < resultsTable.getColumnCount(); i++) {
				tc = resultsTable.getColumnModel().getColumn(i);
				if (i == 1) {
					tc.setWidth(1);
					;
				} else {
					tc.setWidth(80);
				}
			}
			resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			resultsScrollPane.setViewportView(resultsTable);
		}

		// ---- sqlComboBox ----
		sqlComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
				"SELECT", "UPDATE" }));
		sqlComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sqlComboBoxItemStateChanged(e);
			}
		});

		// ======== logScrollPane ========
		{
			logArea.setEditable(false);
			logScrollPane.setViewportView(logArea);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout
				.setHorizontalGroup(contentPaneLayout
						.createParallelGroup()
						.addGroup(
								contentPaneLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												contentPaneLayout
														.createParallelGroup()
														.addComponent(
																resultsScrollPane,
																GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE,
																780,
																Short.MAX_VALUE)
														.addGroup(
																GroupLayout.Alignment.TRAILING,
																contentPaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				sqlComboBox,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				sqlField,
																				GroupLayout.DEFAULT_SIZE,
																				645,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				excecuteButton))
														.addComponent(
																logScrollPane,
																GroupLayout.DEFAULT_SIZE,
																780,
																Short.MAX_VALUE))
										.addContainerGap()));
		contentPaneLayout
				.setVerticalGroup(contentPaneLayout
						.createParallelGroup()
						.addGroup(
								contentPaneLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												contentPaneLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																contentPaneLayout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				sqlComboBox,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				sqlField,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																excecuteButton,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(resultsScrollPane,
												GroupLayout.PREFERRED_SIZE,
												317, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(logScrollPane,
												GroupLayout.DEFAULT_SIZE, 40,
												Short.MAX_VALUE)
										.addContainerGap()));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Hamant Joucoo
	private JTextField sqlField;
	private JButton excecuteButton;
	private JScrollPane resultsScrollPane;
	private JTable resultsTable;
	private JComboBox<String> sqlComboBox;
	private JScrollPane logScrollPane;
	private JTextArea logArea;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	public class sqlSelectThread extends Thread {
		private Thread thread = null;
		private String threadName;
		private String queryString;

		sqlSelectThread(String name, String query) {
			threadName = name;
			queryString = query;
		}

		public void run() {
			try {
				progressBar querySelect = new progressBar();
				querySelect.setVisible(true);
				String xml = Methods.executeSQLSelect(queryString);
				querySelect.setVisible(false);
				String xString = null;
				if (xml.equalsIgnoreCase("<return>null</return>")
						|| xml == null) {
					JOptionPane
							.showMessageDialog(
									logArea,
									"Query returned no values \n Please Check query syntax!",
									"CMUtls - SQL Syntax Error",
									JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						Document doc = DocumentBuilderFactory
								.newInstance()
								.newDocumentBuilder()
								.parse(new ByteArrayInputStream(xml.getBytes()));
						TransformerFactory tf = TransformerFactory
								.newInstance();
						Transformer t = tf.newTransformer();
						t.setOutputProperty(OutputKeys.METHOD, "html");
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						t.transform(new DOMSource(doc), new StreamResult(os));
						xString = new String(os.toByteArray(), "UTF-8");
						xString = xString.replaceAll("(\\r|\\n)", "");
						Variables.sqlRows = Methods.substringsBetween(xString,
								"<row>", "</row>");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					Variables.sqlRowFields = Methods.substringsBetween(
							Variables.sqlRows[0], "<", ">", "/");
					Variables.sqlTableColumns = new String[Variables.sqlRowFields.length];
					for (int x = 0; x < Variables.sqlRowFields.length; x++) {
						Variables.sqlTableColumns[x] = Variables.sqlRowFields[x];
						Variables.sqlTableRows = new String[Variables.sqlRows.length][Variables.sqlRowFields.length + 1];
					}
					for (int y = 0; y < Variables.sqlRows.length; y++) {
						for (int g = 0; g < Variables.sqlRowFields.length; g++) {
							Variables.sqlTableRows[y][g] = Methods
									.substringBetween(Variables.sqlRows[y], "<"
											+ Variables.sqlRowFields[g] + ">",
											"</" + Variables.sqlRowFields[g]
													+ ">");
						}
					}
					resultsTable.setModel(new DefaultTableModel(
							Variables.sqlTableRows, Variables.sqlTableColumns) {
						private static final long serialVersionUID = -6396300478056746940L;
						boolean[] columnEditable = new boolean[6];

						public boolean isCellEditable(int rowIndex,
								int columnIndex) {
							return this.columnEditable[columnIndex];
						}
					});
					TableColumn tc = null;
					for (int i = 0; i < resultsTable.getColumnCount(); i++) {
						tc = resultsTable.getColumnModel().getColumn(i);
						if (i == 1) {
							tc.setWidth(1);
							;
						} else {
							tc.setWidth(80);
						}
					}
					TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
							resultsTable.getModel());
					resultsTable.setRowSorter(sorter);
					logArea.append("Retrieved " + Variables.sqlRows.length
							+ " records! \n");
				}
			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		public void start() {
			if (thread == null) {
				thread = new Thread(this, threadName);
				thread.start();
			}
		}
	}

	public final class progressBar extends JFrame {
		private static final long serialVersionUID = 6374162146237387106L;
		private JLabel a;
		private JProgressBar b;

		public progressBar() {
			progressBar localw = this;
			this.a = new JLabel();
			localw.b = new JProgressBar();
			localw.setTitle("CMUtils - Processing");
			localw.setResizable(false);
			localw.setAlwaysOnTop(true);
			localw.setDefaultCloseOperation(0);
			Container localContainer = localw.getContentPane();
			localw.a.setText("Please wait while your query is being executed!");
			localw.b.setMaximum(0);
			localw.b.setIndeterminate(true);
			GroupLayout localGroupLayout = new GroupLayout(localContainer);
			localContainer.setLayout(localGroupLayout);
			localGroupLayout
					.setHorizontalGroup(localGroupLayout
							.createParallelGroup()
							.addGroup(
									GroupLayout.Alignment.TRAILING,
									localGroupLayout
											.createSequentialGroup()
											.addContainerGap()
											.addGroup(
													localGroupLayout
															.createParallelGroup(
																	GroupLayout.Alignment.TRAILING)
															.addComponent(
																	localw.b,
																	GroupLayout.Alignment.LEADING,
																	-1, 232,
																	32767)
															.addComponent(
																	localw.a,
																	GroupLayout.Alignment.LEADING,
																	-1, 232,
																	32767))
											.addContainerGap()));
			localGroupLayout
					.setVerticalGroup(localGroupLayout
							.createParallelGroup()
							.addGroup(
									localGroupLayout
											.createSequentialGroup()
											.addContainerGap()
											.addComponent(localw.a)
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.RELATED,
													16, 32767)
											.addComponent(localw.b, -2, -1, -2)
											.addContainerGap()));
			localw.pack();
			localw.setLocationRelativeTo(localw.getOwner());
		}
	}
}
