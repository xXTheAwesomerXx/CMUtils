package org.CMUtils;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.Container.ExcelExporter;
import org.Container.Variables;
import org.Container.Threads.allPhonesThread;
import org.Container.Threads.enduserAssocThread;
import org.Container.Threads.lineAssocThread;
import org.Container.Threads.updateThread;
import org.Container.Threads.updateenduserAssocThread;

@SuppressWarnings("all")
public class CMUtilsGUI extends JFrame {
	private static final long serialVersionUID = 290926775182408833L;

	public CMUtilsGUI() {
		initComponents();
	}

	private void allPhonesItemActionPerformed(ActionEvent e) {
		allPhonesThread phoneThread = new allPhonesThread(this, "GAPT", 1, 1,
				"");
		if (tabbedPane.getSelectedIndex() != 0) {
			tabbedPane.setSelectedIndex(0);
			logArea.append("Set selected view to Devices \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
			phoneThread.start();
		} else {
			phoneThread.start();
		}
	}

	private void lineAssocItemActionPerformed(ActionEvent e) {
		lineAssocThread lineThread = new lineAssocThread(this, "GALT");
		if (tabbedPane.getSelectedIndex() != 1) {
			tabbedPane.setSelectedIndex(1);
			logArea.append("Set selected view to Associated Lines \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
			lineThread.start();
		} else {
			lineThread.start();
		}
	}

	private void enduserAssocItemActionPerformed(ActionEvent e) {
		enduserAssocThread enduserThread = new enduserAssocThread(this, "GAEUT");
		if (tabbedPane.getSelectedIndex() != 2) {
			tabbedPane.setSelectedIndex(2);
			logArea.append("Set selected view to Associated Endusers \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
			enduserThread.start();
		} else {
			enduserThread.start();
		}
	}
	
	private void deviceAssocItemActionPerformed(ActionEvent e) {
		enduserAssocThread enduserThread = new enduserAssocThread(this, "GADT");
		if (tabbedPane.getSelectedIndex() != 3) {
			tabbedPane.setSelectedIndex(3);
			logArea.append("Set selected view to Associated Devices \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
			enduserThread.start();
		} else {
			enduserThread.start();
		}
	}

	private void exportItemActionPerformed(ActionEvent e) {
		int currentTab = tabbedPane.getSelectedIndex();
		if (currentTab == 0) {
			if (deviceTable.getRowCount() <= 0) {
				logArea.append("Table is empty, cannot export table to CSV! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			} else {
				ExcelExporter ee = new ExcelExporter(deviceTable, "", false);
				ee.storeTableAsCSV(new File("device_database.csv"), deviceTable);
				logArea.append("Successfully exported table! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			}
		} else if (currentTab == 1) {
			if (lineAssocTable.getRowCount() <= 0) {
				logArea.append("Table is empty, cannot export table to CSV! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			} else {
				ExcelExporter ee = new ExcelExporter(lineAssocTable, "", false);
				ee.storeTableAsCSV(new File("lineassoc_database.csv"),
						lineAssocTable);
				logArea.append("Successfully exported table! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			}
		} else if (currentTab == 2) {
			if (enduserAssocTable.getRowCount() <= 0) {
				logArea.append("Table is empty, cannot export table to CSV! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			} else {
				ExcelExporter ee = new ExcelExporter(enduserAssocTable, "",
						false);
				ee.storeTableAsCSV(new File("enduserAssoc_database.csv"),
						enduserAssocTable);
				logArea.append("Successfully exported table! \n");
				JScrollBar scrollbar = CMUtilsGUI.logScrollPane
						.getVerticalScrollBar();
				scrollbar.setValue(scrollbar.getMaximum());
			}
		}
	}

	private void exitItemActionPerformed(ActionEvent e) {
		this.dispose();
		ConnectGUI connect = new ConnectGUI();
		Variables.pass = "";
		connect.setVisible(true);
		Variables.connected = false;
	}

	private void aboutItemActionPerformed(ActionEvent e) {
		aboutFrame.setVisible(true);
	}

	private void findButtonActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() != 0) {
			tabbedPane.setSelectedIndex(0);
			logArea.append("Set selected view to Devices \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
		}
		int arg1 = fieldComboBox.getSelectedIndex() + 1;
		int arg2 = condComboBox.getSelectedIndex() + 1;
		allPhonesThread phoneThread = new allPhonesThread(this, "GCPT", arg1,
				arg2, condTextField.getText());
		phoneThread.start();
	}

	private void executeButtonActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() == 0) {
			int n = JOptionPane.showConfirmDialog(deviceTable,
					"Are you sure you'd like to update these device(s)?",
					"Update device", JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				Variables.newDeviceTableEnduserRows = new String[Variables.phoneNames.length + 1][4];
				for (int i = 0; i < Variables.phoneNames.length; i++) {
					Variables.newDeviceTableEnduserRows[i][0] = deviceTable
							.getModel().getValueAt(i, 4).toString();
					Variables.newDeviceTableEnduserRows[i][1] = deviceTable
							.getModel().getValueAt(i, 3).toString();
					Variables.newDeviceTableEnduserRows[i][2] = deviceTable
							.getModel().getValueAt(i, 2).toString();
					Variables.newDeviceTableEnduserRows[i][3] = deviceTable
							.getModel().getValueAt(i, 1).toString();
					if (deviceTable.getModel().getValueAt(i, 8).toString()
							.equalsIgnoreCase("true")) {
						updateThread newUpdateThread = new updateThread(this,
								deviceTable.getModel().getValueAt(i, 0)
										.toString(), i);
						logArea.append("Update device: "
								+ deviceTable.getModel().getValueAt(i, 1)
										.toString() + ", User: "
								+ Variables.oldDeviceTableEnduserRows[i][2]
								+ ", "
								+ Variables.oldDeviceTableEnduserRows[i][1]
								+ " >> "
								+ Variables.newDeviceTableEnduserRows[i][2]
								+ ", "
								+ Variables.newDeviceTableEnduserRows[i][1]
								+ "\n");
						JScrollBar scrollbar = CMUtilsGUI.logScrollPane
								.getVerticalScrollBar();
						scrollbar.setAutoscrolls(true);
						scrollbar.setValue(scrollbar.getMaximum());
						newUpdateThread.start();
					}
				}
			}
		} else if (tabbedPane.getSelectedIndex() == 2) { // TODO: Finish Device
															// Assoc Update
			int n = JOptionPane.showConfirmDialog(enduserAssocTable,
					"Are you sure you'd like to update these user(s)?",
					"Update user", JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				try {
					Variables.newenduserAssocTableEnduserRows = new String[enduserAssocTable
							.getRowCount()][1];
					for (int i = 0; i < enduserAssocTable.getRowCount(); i++) {
						if (enduserAssocTable.getModel().getValueAt(i, 2)
								.toString() != null) {
							Variables.newenduserAssocTableEnduserRows[i][0] = enduserAssocTable
									.getModel().getValueAt(i, 2).toString();
						} else {
							Variables.newenduserAssocTableEnduserRows[i][0] = "NULL";
						}
						if (enduserAssocTable.getModel().getValueAt(i, 5) != null) {
							if (enduserAssocTable.getModel().getValueAt(i, 5)
									.toString().equalsIgnoreCase("true")) {
								updateenduserAssocThread newUpdateThread = new updateenduserAssocThread(
										this, enduserAssocTable.getModel()
												.getValueAt(i, 0).toString(), i);
								newUpdateThread.start();
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private void sqlMenuItemActionPerformed(ActionEvent e) {
		runSQL sqlGUI = new runSQL();
		sqlGUI.setVisible(true);
	}

	private void initComponents() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		allPhonesItem = new JMenuItem();
		findPhonesMenu = new JMenu();
		lineAssocItem = new JMenuItem();
		enduserAssocItem = new JMenuItem();
		deviceAssocItem = new JMenuItem();
		exportItem = new JMenuItem();
		exitItem = new JMenuItem();
		helpMenu = new JMenu();
		aboutItem = new JMenuItem();
		sqlMenuItem = new JMenuItem();
		argsPanel = new JPanel();
		findLabel = new JLabel();
		fieldComboBox = new JComboBox<>();
		condComboBox = new JComboBox<>();
		condTextField = new JTextField();
		findButton = new JButton();
		executeButton = new JButton();
		tabbedPane = new JTabbedPane();
		deviceTableScrollPane = new JScrollPane();
		deviceTable = new JTable();
		lineAssocPane = new JScrollPane();
		lineAssocTable = new JTable();
		enduserAssocPane = new JScrollPane();
		enduserAssocTable = new JTable();
		deviceAssocPane = new JScrollPane();
		deviceAssocTable = new JTable();
		logPanel = new JPanel();
		logScrollPane = new JScrollPane();
		logArea = new JTextArea();
		aboutFrame = new JFrame();

		// ======== this ========
		setTitle("CMUtils");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();

		// ======== menuBar ========
		{

			// ======== fileMenu ========
			{
				fileMenu.setText("File");

				// ---- allPhonesItem ----
				allPhonesItem.setText("Find all phones");
				allPhonesItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						allPhonesItemActionPerformed(e);
					}
				});
				fileMenu.add(allPhonesItem);

				// ======== findPhonesMenu ========
				{
					findPhonesMenu.setText("Find phones by");

					// ---- lineAssocItem ----
					lineAssocItem.setText("Line Association");
					lineAssocItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							lineAssocItemActionPerformed(e);
						}
					});
					findPhonesMenu.add(lineAssocItem);

					// ---- enduserAssocItem ----
					enduserAssocItem.setText("Enduser Association");
					enduserAssocItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							enduserAssocItemActionPerformed(e);
						}
					});
					findPhonesMenu.add(enduserAssocItem);

					// ---- deviceAssocItem ----
					deviceAssocItem.setText("Device Association");
					deviceAssocItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							deviceAssocItemActionPerformed(e);
						}
					});
					findPhonesMenu.add(deviceAssocItem);
				}
				fileMenu.add(findPhonesMenu);
				fileMenu.addSeparator();

				// ---- exportItem ----
				exportItem.setText("Export Table");
				exportItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						exportItemActionPerformed(e);
					}
				});
				fileMenu.add(exportItem);
				fileMenu.addSeparator();

				// ---- sqlMenuItem ----
				sqlMenuItem.setText("Run SQL");
				sqlMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sqlMenuItemActionPerformed(e);
					}
				});
				fileMenu.add(sqlMenuItem);
				fileMenu.addSeparator();

				// ---- exitItem ----
				exitItem.setText("Exit");
				exitItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						exitItemActionPerformed(e);
					}
				});
				fileMenu.add(exitItem);
			}
			menuBar.add(fileMenu);

			// ======== helpMenu ========
			{
				helpMenu.setText("Help");

				// ---- aboutItem ----
				aboutItem.setText("About");
				aboutItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						aboutItemActionPerformed(e);
					}
				});
				helpMenu.add(aboutItem);
			}
			menuBar.add(helpMenu);
		}
		setJMenuBar(menuBar);

		// ======== argsPanel ========
		{

			// ---- findLabel ----
			findLabel.setText("Find Phone where");

			// ---- fieldComboBox ----
			fieldComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"Device Name", "Description", "Device Pool", "Device Type",
					"Owner ID" }));

			// ---- condComboBox ----
			condComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"begins with", "contains", "ends with", "is exactly",
					"is empty", "is not empty" }));

			// ---- condTextField ----
			condTextField.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent p) {
					if (p.getKeyCode() == p.VK_ENTER) {
						int arg1 = fieldComboBox.getSelectedIndex() + 1;
						int arg2 = condComboBox.getSelectedIndex() + 1;
						String sqlQuery = condTextField.getText();
						if (tabbedPane.getSelectedIndex() != 0) {
							tabbedPane.setSelectedIndex(0);
							logArea.append("Set selected view to Devices \n");
							JScrollBar scrollbar = CMUtilsGUI.logScrollPane
									.getVerticalScrollBar();
							scrollbar.setValue(scrollbar.getMaximum());
						}
						allPhonesThread phoneThread = new allPhonesThread(
								CMUtilsGUI.this, "GAPT", arg1, arg2, sqlQuery);
						phoneThread.start();
					}

				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub

				}

			});

			// ---- findButton ----
			findButton.setText("Go!");
			findButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					findButtonActionPerformed(e);
				}
			});

			// ---- executeButton ----
			executeButton.setText("Update");
			executeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					executeButtonActionPerformed(e);
				}
			});

			GroupLayout argsPanelLayout = new GroupLayout(argsPanel);
			argsPanel.setLayout(argsPanelLayout);
			argsPanelLayout
					.setHorizontalGroup(argsPanelLayout
							.createParallelGroup()
							.addGroup(
									argsPanelLayout
											.createSequentialGroup()
											.addContainerGap()
											.addComponent(findLabel)
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(fieldComboBox,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(condComboBox,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(condTextField,
													GroupLayout.PREFERRED_SIZE,
													129,
													GroupLayout.PREFERRED_SIZE)
											.addGap(18, 18, 18)
											.addComponent(findButton,
													GroupLayout.PREFERRED_SIZE,
													69,
													GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.RELATED,
													GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addComponent(executeButton,
													GroupLayout.PREFERRED_SIZE,
													145,
													GroupLayout.PREFERRED_SIZE)
											.addContainerGap()));
			argsPanelLayout
					.setVerticalGroup(argsPanelLayout
							.createParallelGroup()
							.addGroup(
									argsPanelLayout
											.createSequentialGroup()
											.addGroup(
													argsPanelLayout
															.createParallelGroup(
																	GroupLayout.Alignment.BASELINE)
															.addComponent(
																	executeButton)
															.addComponent(
																	findLabel)
															.addComponent(
																	fieldComboBox,
																	GroupLayout.PREFERRED_SIZE,
																	GroupLayout.DEFAULT_SIZE,
																	GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	condComboBox,
																	GroupLayout.PREFERRED_SIZE,
																	GroupLayout.DEFAULT_SIZE,
																	GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	condTextField,
																	GroupLayout.PREFERRED_SIZE,
																	GroupLayout.DEFAULT_SIZE,
																	GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	findButton))
											.addGap(0, 16, Short.MAX_VALUE)));
		}

		// ======== tabbedPane ========
		{

			// ======== deviceTableScrollPane ========
			{

				// ---- deviceTable ----
				deviceTable
						.setModel(new DefaultTableModel(
								Variables.deviceTableRows,
								Variables.deviceTableColumns) {
							private static final long serialVersionUID = -6396300478056746940L;

							boolean[] columnEditable = new boolean[9];
							@SuppressWarnings("rawtypes")
							Class[] columnTypes = { String.class, String.class,
									JComboBox.class, JComboBox.class,
									JComboBox.class, String.class,
									String.class, String.class, JCheckBox.class };

							public Class<?> getColumnClass(int columnIndex) {
								return this.columnTypes[columnIndex];
							}

							public boolean isCellEditable(int rowIndex,
									int columnIndex) {
								return this.columnEditable[columnIndex];
							}
						});
				this.deviceTable.getColumnModel();
				TableColumn tc = null;
				for (int i = 0; i < deviceTable.getColumnCount(); i++) {
					tc = deviceTable.getColumnModel().getColumn(i);
					if (i == 1) {
						tc.setWidth(1);
						;
					} else {
						tc.setWidth(80);
					}
				}
				deviceTable
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				deviceTableScrollPane.setViewportView(deviceTable);
			}
			tabbedPane.addTab("Devices", deviceTableScrollPane);

			// ======== lineAssocPane ========
			{
				// ---- lineAssocTable ----
				lineAssocTable.setModel(new DefaultTableModel(
						Variables.lineAssocTableRows,
						Variables.lineAssocTableColumns) {
					private static final long serialVersionUID = -6396300478056746940L;
					@SuppressWarnings("rawtypes")
					Class[] columnTypes = { String.class, String.class,
							JComboBox.class, JComboBox.class, JComboBox.class,
							JCheckBox.class };
					boolean[] columnEditable = new boolean[6];

					public Class<?> getColumnClass(int columnIndex) {
						return this.columnTypes[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.columnEditable[columnIndex];
					}
				});
				this.lineAssocTable.getColumnModel();
				TableColumn tc = null;
				for (int i = 0; i < lineAssocTable.getColumnCount(); i++) {
					tc = lineAssocTable.getColumnModel().getColumn(i);
					if (i == 1) {
						tc.setWidth(1);
						;
					} else {
						tc.setWidth(80);
					}
				}
				lineAssocTable
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lineAssocPane.setViewportView(lineAssocTable);
			}
			tabbedPane.addTab("Line Association", lineAssocPane);

			// ======== enduserAssocPane ========
			{
				// ---- enduserAssocTable ----
				enduserAssocTable.setModel(new DefaultTableModel(
						Variables.enduserAssocTableRows,
						Variables.enduserAssocTableColumns) {
					private static final long serialVersionUID = -6396300478056746940L;
					Class[] columnTypes = { String.class, String.class,
							JComboBox.class, JComboBox.class, JComboBox.class,
							JCheckBox.class };
					boolean[] columnEditable = new boolean[6];

					public Class<?> getColumnClass(int columnIndex) {
						return this.columnTypes[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.columnEditable[columnIndex];
					}
				});
				this.enduserAssocTable.getColumnModel();
				TableColumn tc = null;
				for (int i = 0; i < enduserAssocTable.getColumnCount(); i++) {
					tc = enduserAssocTable.getColumnModel().getColumn(i);
					if (i == 1) {
						tc.setWidth(1);
						;
					} else {
						tc.setWidth(80);
					}
				}
				enduserAssocTable
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				enduserAssocPane.setViewportView(enduserAssocTable);
			}
			tabbedPane.addTab("Enduser Association", enduserAssocPane);

			// ======== deviceAssocPane ========
			{
				// ---- deviceAssocTable ----
				deviceAssocTable.setModel(new DefaultTableModel(
						Variables.enduserAssocTableRows,
						Variables.enduserAssocTableColumns) {
					private static final long serialVersionUID = -6396300478056746940L;
					Class[] columnTypes = { String.class, String.class,
							JComboBox.class, JComboBox.class, JComboBox.class,
							JCheckBox.class };
					boolean[] columnEditable = new boolean[6];

					public Class<?> getColumnClass(int columnIndex) {
						return this.columnTypes[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.columnEditable[columnIndex];
					}
				});
				this.deviceAssocTable.getColumnModel();
				TableColumn tc = null;
				for (int i = 0; i < deviceAssocTable.getColumnCount(); i++) {
					tc = deviceAssocTable.getColumnModel().getColumn(i);
					if (i == 1) {
						tc.setWidth(1);
						;
					} else {
						tc.setWidth(80);
					}
				}
				deviceAssocTable
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				deviceAssocPane.setViewportView(deviceAssocTable);
			}
			tabbedPane.addTab("Device Association", deviceAssocPane);
		}

		// ======== logPanel ========
		{

			// ======== logScrollPane ========
			{

				// ---- logArea ----
				logArea.setEditable(false);
				logArea.setLineWrap(true);
				logArea.setWrapStyleWord(true);
				logScrollPane.setViewportView(logArea);
			}

			GroupLayout logPanelLayout = new GroupLayout(logPanel);
			logPanel.setLayout(logPanelLayout);
			logPanelLayout.setHorizontalGroup(logPanelLayout
					.createParallelGroup().addComponent(logScrollPane,
							GroupLayout.Alignment.TRAILING));
			logPanelLayout.setVerticalGroup(logPanelLayout
					.createParallelGroup().addGroup(
							GroupLayout.Alignment.TRAILING,
							logPanelLayout
									.createSequentialGroup()
									.addGap(0, 16, Short.MAX_VALUE)
									.addComponent(logScrollPane,
											GroupLayout.PREFERRED_SIZE, 73,
											GroupLayout.PREFERRED_SIZE)
									.addGap(0, 0, 0)));
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
																argsPanel,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																logPanel,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																tabbedPane,
																GroupLayout.Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE,
																0,
																Short.MAX_VALUE))
										.addContainerGap()));
		contentPaneLayout.setVerticalGroup(contentPaneLayout
				.createParallelGroup().addGroup(
						GroupLayout.Alignment.TRAILING,
						contentPaneLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(argsPanel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(1, 1, 1)
								.addComponent(tabbedPane,
										GroupLayout.DEFAULT_SIZE, 285,
										Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(logPanel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pack();
		setLocationRelativeTo(getOwner());

		// ======== aboutFrame ========
		{
			aboutFrame.setAlwaysOnTop(true);
			aboutFrame.setTitle("About");
			Container aboutFrameContentPane = aboutFrame.getContentPane();

			GroupLayout aboutFrameContentPaneLayout = new GroupLayout(
					aboutFrameContentPane);
			aboutFrameContentPane.setLayout(aboutFrameContentPaneLayout);
			aboutFrameContentPaneLayout
					.setHorizontalGroup(aboutFrameContentPaneLayout
							.createParallelGroup().addGap(0, 394,
									Short.MAX_VALUE));
			aboutFrameContentPaneLayout
					.setVerticalGroup(aboutFrameContentPaneLayout
							.createParallelGroup().addGap(0, 169,
									Short.MAX_VALUE));
			aboutFrame.pack();
			aboutFrame.setLocationRelativeTo(aboutFrame.getOwner());
		}
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Hamant Joucoo
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem allPhonesItem;
	private JMenu findPhonesMenu;
	private JMenuItem lineAssocItem;
	private JMenuItem enduserAssocItem;
	private JMenuItem deviceAssocItem;
	private JMenuItem exportItem;
	private JMenuItem sqlMenuItem;
	private JMenuItem exitItem;
	private JMenu helpMenu;
	private JMenuItem aboutItem;
	private JPanel argsPanel;
	private JLabel findLabel;
	private JComboBox<String> fieldComboBox;
	private JComboBox<String> condComboBox;
	private JTextField condTextField;
	private JButton findButton;
	private JButton executeButton;
	private JTabbedPane tabbedPane;
	public static JScrollPane deviceTableScrollPane;
	public JTable deviceTable;
	public static JScrollPane lineAssocPane;
	public JTable lineAssocTable;
	public static JScrollPane enduserAssocPane;
	public JTable enduserAssocTable;
	public static JScrollPane deviceAssocPane;
	public JTable deviceAssocTable;
	private JPanel logPanel;
	public static JScrollPane logScrollPane;
	public static JTextArea logArea;
	private JFrame aboutFrame;

}
