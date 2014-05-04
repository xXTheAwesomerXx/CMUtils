package org.CMUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.Container.ExcelExporter;
import org.Container.Methods;
import org.Container.Variables;

public class CMUtilsGUI extends JFrame {
	private static final long serialVersionUID = 290926775182408833L;

	public CMUtilsGUI() {
		initComponents();
	}

	private void allPhonesItemActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() != 0) {
			tabbedPane.setSelectedIndex(0);
			logArea.append("Set selected view to Devices \n");
		}
		allPhonesThread phoneThread = new allPhonesThread("GAPT", 1, 1, "");
		phoneThread.start();
	}

	private void lineAssocItemActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() != 1) {
			tabbedPane.setSelectedIndex(1);
			logArea.append("Set selected view to Line Association \n");
		}
		Methods.getAllEndusers();
		Methods.getNumplanAssoc();
		// TODO
		DefaultTableModel model = new DefaultTableModel(
				Variables.lineAssocTableRows, Variables.lineAssocTableColumns) {

			boolean[] canEdit = new boolean[] { false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}

		};
		lineAssocTable.setModel(model);
	}

	private void deviceAssocItemActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() != 2) {
			tabbedPane.setSelectedIndex(2);
			logArea.append("Set selected view to Device Association \n");
		}
		try {
			Methods.getDeviceAssoc();
			Methods.getDeviceAssocDevices();
			// lookupDeviceType(1, 1, "");
			// lookupDevicepool(1, 1, "");
			// TODO
			DefaultTableModel model = new DefaultTableModel(
					Variables.deviceAssocTableRows,
					Variables.deviceAssocTableColumns) {

				boolean[] canEdit = new boolean[] { false, true, false, false,
						false, true };

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}

			};
			deviceAssocTable.setModel(model);
			TableColumn NC = deviceAssocTable.getColumnModel().getColumn(0);
			TableColumn UpdateColumn = deviceAssocTable.getColumnModel()
					.getColumn(5);
			TableColumn DNC = deviceAssocTable.getColumnModel().getColumn(1);
			final JComboBox deviceNameCombo = new JComboBox(
					Variables.devAssocDevicenames);
			deviceNameCombo.setEditable(true);
			final JCheckBox updateRow = new JCheckBox();
			new S15WorkingBackspace(deviceNameCombo);
			updateRow.setSelected(false);
			updateRow.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (updateRow.isSelected()) {
						System.out.println("Update: TRUE");
					}

				}

			});
			DNC.setCellEditor(new ComboBoxCellEditor(deviceNameCombo));
			NC.setPreferredWidth(5);
			UpdateColumn.setPreferredWidth(5);
			UpdateColumn.setCellEditor(new DefaultCellEditor(updateRow));
		} catch (Exception error) {
			error.printStackTrace();
		}
		Variables.oldDeviceAssocTableEnduserRows = new String[deviceAssocTable
				.getRowCount()][1];
		// for (int i = 0; i < deviceAssocTable.getRowCount(); i++) {
		// if (deviceAssocTable.getModel().getValueAt(i, 1).toString() != null)
		// {
		// Variables.oldDeviceAssocTableEnduserRows[i][0] = deviceAssocTable
		// .getModel().getValueAt(i, 1).toString();
		// }
		// }
//		for (int i = 0; i < deviceAssocTable.getRowCount(); i++) {
//			Variables.deviceAssocTableRows[i][4] = "false";
//			Variables.deviceAssocTableRows[i][1] = "NONE";
//		}
		TableRowSorter<TableModel> devAssocSort = new TableRowSorter<TableModel>(
				deviceAssocTable.getModel());
		deviceAssocTable.setRowSorter(devAssocSort);
		/*
		 * if (tabbedPane.getSelectedIndex() != 2) {
		 * tabbedPane.setSelectedIndex(2);
		 * logArea.append("Set selected view to Device Association \n"); } try {
		 * Methods.getDeviceAssoc(); Methods.getAllEndusers(); //
		 * lookupDeviceType(1, 1, ""); // lookupDevicepool(1, 1, ""); for (int i
		 * = 0; i < deviceAssocTable.getRowCount(); i++) {
		 * Variables.deviceAssocTableRows[i][5] = "false"; } // TODO
		 * DefaultTableModel model = new DefaultTableModel(
		 * Variables.deviceAssocTableRows, Variables.deviceAssocTableColumns) {
		 * 
		 * boolean[] canEdit = new boolean[] { false, false, true, true, true,
		 * true };
		 * 
		 * public boolean isCellEditable(int rowIndex, int columnIndex) { return
		 * canEdit[columnIndex]; }
		 * 
		 * }; deviceAssocTable.setModel(model); TableColumn UIDC =
		 * deviceAssocTable.getColumnModel().getColumn(2); TableColumn FNC =
		 * deviceAssocTable.getColumnModel().getColumn(3); TableColumn LNC =
		 * deviceAssocTable.getColumnModel().getColumn(4); TableColumn NC =
		 * deviceAssocTable.getColumnModel().getColumn(0); TableColumn
		 * UpdateColumn = deviceAssocTable.getColumnModel() .getColumn(5); final
		 * JComboBox firstnameCombo = new JComboBox(
		 * Variables.phoneAllEndusersFirstnames); final JComboBox lastnameCombo
		 * = new JComboBox( Variables.phoneAllEndusersLastnames); final
		 * JComboBox useridCombo = new JComboBox(
		 * Variables.phoneAllEndusersIDs); final JCheckBox updateRow = new
		 * JCheckBox(); firstnameCombo.setEditable(true);
		 * lastnameCombo.setEditable(true); useridCombo.setEditable(true);
		 * updateRow.setSelected(false); new
		 * S15WorkingBackspace(firstnameCombo); new
		 * S15WorkingBackspace(lastnameCombo); new
		 * S15WorkingBackspace(useridCombo); final int firstnameCol = 3,
		 * lastnameCol = 4, useridCol = 2; updateRow.addActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { if
		 * (updateRow.isSelected()) { System.out.println("Update: TRUE"); }
		 * 
		 * }
		 * 
		 * }); firstnameCombo.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) { if (e.getStateChange() ==
		 * ItemEvent.SELECTED) { deviceAssocTable.getModel().setValueAt(
		 * lastnameCombo.getItemAt(firstnameCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), lastnameCol);
		 * deviceAssocTable.getModel().setValueAt(
		 * useridCombo.getItemAt(firstnameCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), useridCol); }
		 * 
		 * } }); lastnameCombo.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) { if (e.getStateChange() ==
		 * ItemEvent.SELECTED) { deviceAssocTable.getModel() .setValueAt(
		 * firstnameCombo.getItemAt(lastnameCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), firstnameCol);
		 * deviceAssocTable.getModel().setValueAt(
		 * useridCombo.getItemAt(lastnameCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), useridCol); } } });
		 * useridCombo.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) { if (e.getStateChange() ==
		 * ItemEvent.SELECTED) { deviceAssocTable.getModel() .setValueAt(
		 * firstnameCombo.getItemAt(useridCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), firstnameCol);
		 * deviceAssocTable.getModel().setValueAt(
		 * lastnameCombo.getItemAt(useridCombo .getSelectedIndex()),
		 * deviceAssocTable.getSelectedRow(), lastnameCol); } } });
		 * UIDC.setCellEditor(new ComboBoxCellEditor(useridCombo));
		 * LNC.setCellEditor(new ComboBoxCellEditor(lastnameCombo));
		 * FNC.setCellEditor(new ComboBoxCellEditor(firstnameCombo));
		 * NC.setPreferredWidth(5); UpdateColumn.setPreferredWidth(5);
		 * UpdateColumn.setCellEditor(new DefaultCellEditor(updateRow)); } catch
		 * (Exception error) { error.printStackTrace(); }
		 * Variables.oldDeviceAssocTableEnduserRows = new
		 * String[deviceAssocTable .getRowCount()][4]; for (int i = 0; i <
		 * deviceAssocTable.getRowCount(); i++) {
		 * Variables.oldDeviceAssocTableEnduserRows[i][0] = deviceAssocTable
		 * .getModel().getValueAt(i, 1).toString();
		 * Variables.oldDeviceAssocTableEnduserRows[i][1] = deviceAssocTable
		 * .getModel().getValueAt(i, 2).toString();
		 * Variables.oldDeviceAssocTableEnduserRows[i][2] = deviceAssocTable
		 * .getModel().getValueAt(i, 3).toString();
		 * Variables.oldDeviceAssocTableEnduserRows[i][3] = deviceAssocTable
		 * .getModel().getValueAt(i, 4).toString(); } for (int i = 0; i <
		 * deviceAssocTable.getRowCount(); i++) {
		 * Variables.deviceAssocTableRows[i][5] = "false"; }
		 * TableRowSorter<TableModel> devAssocSort = new
		 * TableRowSorter<TableModel>( deviceAssocTable.getModel());
		 * deviceAssocTable.setRowSorter(devAssocSort);
		 */
	}

	private void exportItemActionPerformed(ActionEvent e) {
		if (deviceTable.getRowCount() <= 0) {
			logArea.append("Table is empty, cannot export table to CSV!");
		} else {
			ExcelExporter ee = new ExcelExporter(deviceTable, "", false);
			ee.storeTableAsCSV(new File("database.csv"), deviceTable);
			logArea.append("Successfully exported table!");
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
		}
		int arg1 = fieldComboBox.getSelectedIndex() + 1;
		int arg2 = condComboBox.getSelectedIndex() + 1;
		allPhonesThread phoneThread = new allPhonesThread("GCPT", arg1, arg2,
				condTextField.getText());
		phoneThread.start();
	}

	private void executeButtonActionPerformed(ActionEvent e) {
		if (tabbedPane.getSelectedIndex() == 0) {
			int n = JOptionPane.showConfirmDialog(deviceTable,
					"Are you sure you'd like to update these device(s)?",
					"Update device", JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				// updateDeviceSetEnduser(arg1, arg2);
				// addEnduserDeviceMap(arg1, arg2);
				Variables.newDeviceTableEnduserRows = new String[deviceTable
						.getRowCount()][4];
				for (int i = 0; i < deviceTable.getRowCount(); i++) {
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
						updateThread newUpdateThread = new updateThread(
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
						newUpdateThread.start();
						// Methods.updateDeviceSetEnduser(
						// Variables.newDeviceTableEnduserRows[i][0],
						// deviceTable.getModel().getValueAt(i, 1)
						// .toString());
						// System.out.println("Update1 " +
						// Variables.newDeviceTableEnduserRows[i][0] + ", " +
						// deviceTable.getModel().getValueAt(i, 1)
						// .toString());
						// Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[i][3]);
						// System.out.println("Update2 " +
						// Variables.oldDeviceTableEnduserRows[i][3]);
						// Methods.addEnduserDeviceMap(
						// Variables.newDeviceTableEnduserRows[i][0],
						// deviceTable.getModel().getValueAt(i, 1)
						// .toString());
						// System.out.println("Update3 " +
						// Variables.newDeviceTableEnduserRows[i][0]);
						// Methods.addNumplan(Variables.newDeviceTableEnduserRows[i][0]);
						// System.out.println("Update4 " +
						// Variables.newDeviceTableEnduserRows[i][0]);
						// Methods.updatePrimaryExtension(
						// Variables.newDeviceTableEnduserRows[i][0],
						// deviceTable.getModel().getValueAt(i, 1)
						// .toString());
						// System.out.println("Update5 " +
						// Variables.newDeviceTableEnduserRows[i][0]);
						// Methods.addNumplanDevicemap(Variables.newDeviceTableEnduserRows[i][0]);
						// System.out.println("Update6 " +
						// Variables.newDeviceTableEnduserRows[i][0]);
						// Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newDeviceTableEnduserRows[i][0]);
						// System.out.println("Update7 " +
						// Variables.newDeviceTableEnduserRows[i][0]);
					}
				}
			} else if (n == JOptionPane.NO_OPTION) {
				Variables.newDeviceTableEnduserRows = new String[deviceTable
						.getRowCount()][3];
				for (int i = 0; i < deviceTable.getRowCount(); i++) {
					Variables.newDeviceTableEnduserRows[i][0] = deviceTable
							.getModel().getValueAt(i, 4).toString();
					Variables.newDeviceTableEnduserRows[i][1] = deviceTable
							.getModel().getValueAt(i, 3).toString();
					Variables.newDeviceTableEnduserRows[i][2] = deviceTable
							.getModel().getValueAt(i, 2).toString();
					System.out
							.println(Variables.newDeviceTableEnduserRows[i][0]
									+ ", "
									+ Variables.newDeviceTableEnduserRows[i][1]
									+ ", "
									+ Variables.newDeviceTableEnduserRows[i][2]);
					if (!Variables.oldDeviceTableEnduserRows[i][0]
							.equals(Variables.newDeviceTableEnduserRows[i][0])) {
						System.out.println("Update where userid = "
								+ Variables.newDeviceTableEnduserRows[i][0]
								+ ", "
								+ Variables.oldDeviceTableEnduserRows[i][0]
								+ " and Device = "
								+ deviceTable.getModel().getValueAt(i, 1)
										.toString());
					}
				}
			}
		} else if (tabbedPane.getSelectedIndex() == 2) { // TODO: Finish Device
															// Assoc Update
			int n = JOptionPane.showConfirmDialog(deviceAssocTable,
					"Are you sure you'd like to update these user(s)?",
					"Update user", JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				// updateDeviceSetEnduser(arg1, arg2);
				// addEnduserDeviceMap(arg1, arg2);
				try {
					Variables.newDeviceAssocTableEnduserRows = new String[deviceAssocTable
							.getRowCount()][1];
					for (int i = 0; i < deviceAssocTable.getRowCount(); i++) {
						if (deviceAssocTable.getModel().getValueAt(i, 2)
								.toString() != null) {
							Variables.newDeviceAssocTableEnduserRows[i][0] = deviceAssocTable
									.getModel().getValueAt(i, 2).toString();
						} else {
							Variables.newDeviceAssocTableEnduserRows[i][0] = "NULL";
						}
						if (deviceAssocTable.getModel().getValueAt(i, 5) != null) {
							if (deviceAssocTable.getModel().getValueAt(i, 5)
									.toString().equalsIgnoreCase("true")) {
								updateDeviceAssocThread newUpdateThread = new updateDeviceAssocThread(
										deviceAssocTable.getModel()
												.getValueAt(i, 0).toString(), i);
								newUpdateThread.start();
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (n == JOptionPane.NO_OPTION) {

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
						for (int i = 0; i < deviceTable.getRowCount(); i++) {
							Variables.deviceTableRows[i][8] = "false";
						}
						try {
							int arg1 = fieldComboBox.getSelectedIndex() + 1;
							int arg2 = condComboBox.getSelectedIndex() + 1;
							System.out.println("Arg1, Arg2: " + arg1 + arg2);
							Methods.getDevices(arg1, arg2,
									condTextField.getText());
							Methods.getAllEndusers();
							// TODO
							DefaultTableModel model = new DefaultTableModel(
									Variables.deviceTableRows,
									Variables.deviceTableColumns) {

								boolean[] canEdit = new boolean[] { false,
										true, true, true, true, false, false,
										true, true /* Checkbox */};

								public boolean isCellEditable(int rowIndex,
										int columnIndex) {
									return canEdit[columnIndex];
								}

								Class[] columnTypes = { String.class,
										String.class, JComboBox.class,
										JComboBox.class, JComboBox.class,
										String.class, String.class,
										String.class, JCheckBox.class };

								public Class<?> getColumnClass(int columnIndex) {
									return this.columnTypes[columnIndex];
								}

							};
							deviceTable.setModel(model);
							TableColumn UIDC = deviceTable.getColumnModel()
									.getColumn(4);
							TableColumn LNC = deviceTable.getColumnModel()
									.getColumn(3);
							TableColumn FNC = deviceTable.getColumnModel()
									.getColumn(2);
							TableColumn NC = deviceTable.getColumnModel()
									.getColumn(0);
							TableColumn UpdateColumn = deviceTable
									.getColumnModel().getColumn(8);
							final JComboBox firstnameCombo = new JComboBox(
									Variables.phoneAllEndusersFirstnames);
							final JComboBox lastnameCombo = new JComboBox(
									Variables.phoneAllEndusersLastnames);
							final JComboBox useridCombo = new JComboBox(
									Variables.phoneAllEndusersIDs);
							final JCheckBox updateRow = new JCheckBox();
							firstnameCombo.setEditable(true);
							lastnameCombo.setEditable(true);
							useridCombo.setEditable(true);
							updateRow.setSelected(false);
							new S15WorkingBackspace(firstnameCombo);
							new S15WorkingBackspace(lastnameCombo);
							new S15WorkingBackspace(useridCombo);
							final int firstnameCol = 2, lastnameCol = 3, useridCol = 4;
							firstnameCombo.addItemListener(new ItemListener() {
								public void itemStateChanged(ItemEvent e) {
									if (e.getStateChange() == ItemEvent.SELECTED) {
										try {
											deviceTable
													.getModel()
													.setValueAt(
															lastnameCombo
																	.getItemAt(firstnameCombo
																			.getSelectedIndex()),
															deviceTable
																	.getSelectedRow(),
															lastnameCol);
											deviceTable
													.getModel()
													.setValueAt(
															useridCombo
																	.getItemAt(firstnameCombo
																			.getSelectedIndex()),
															deviceTable
																	.getSelectedRow(),
															useridCol);
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}
								}
							});
							lastnameCombo.addItemListener(new ItemListener() {
								public void itemStateChanged(ItemEvent e) {
									if (e.getStateChange() == ItemEvent.SELECTED) {
										deviceTable
												.getModel()
												.setValueAt(
														firstnameCombo
																.getItemAt(lastnameCombo
																		.getSelectedIndex()),
														deviceTable
																.getSelectedRow(),
														firstnameCol);
										deviceTable
												.getModel()
												.setValueAt(
														useridCombo
																.getItemAt(lastnameCombo
																		.getSelectedIndex()),
														deviceTable
																.getSelectedRow(),
														useridCol);
									}
								}
							});
							useridCombo.addItemListener(new ItemListener() {
								public void itemStateChanged(ItemEvent e) {
									if (e.getStateChange() == ItemEvent.SELECTED) {
										deviceTable
												.getModel()
												.setValueAt(
														firstnameCombo
																.getItemAt(useridCombo
																		.getSelectedIndex()),
														deviceTable
																.getSelectedRow(),
														firstnameCol);
										deviceTable
												.getModel()
												.setValueAt(
														lastnameCombo
																.getItemAt(useridCombo
																		.getSelectedIndex()),
														deviceTable
																.getSelectedRow(),
														lastnameCol);
									}
								}
							});
							UIDC.setCellEditor(new ComboBoxCellEditor(
									useridCombo));
							LNC.setCellEditor(new ComboBoxCellEditor(
									lastnameCombo));
							FNC.setCellEditor(new ComboBoxCellEditor(
									firstnameCombo));
							NC.setPreferredWidth(5);
							UpdateColumn.setPreferredWidth(5);
							UpdateColumn.setCellEditor(new DefaultCellEditor(
									updateRow));
						} catch (Exception error) {
							error.printStackTrace();
						}
						Variables.oldDeviceTableEnduserRows = new String[deviceTable
								.getRowCount()][4];
						for (int i = 0; i < deviceTable.getRowCount(); i++) {
							Variables.oldDeviceTableEnduserRows[i][0] = deviceTable
									.getModel().getValueAt(i, 4).toString();
							Variables.oldDeviceTableEnduserRows[i][1] = deviceTable
									.getModel().getValueAt(i, 3).toString();
							Variables.oldDeviceTableEnduserRows[i][2] = deviceTable
									.getModel().getValueAt(i, 2).toString();
							Variables.oldDeviceTableEnduserRows[i][3] = deviceTable
									.getModel().getValueAt(i, 1).toString();
						}
					}

				}

				@Override
				public void keyReleased(KeyEvent r) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyTyped(KeyEvent t) {
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
				TableColumnModel cm = this.deviceTable.getColumnModel();
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
					Class[] columnTypes = { String.class, String.class };
					boolean[] columnEditable = new boolean[2];

					public Class<?> getColumnClass(int columnIndex) {
						return this.columnTypes[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.columnEditable[columnIndex];
					}
				});
				TableColumnModel cm = this.lineAssocTable.getColumnModel();
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

			// ======== deviceAssocPane ========
			{
				// ---- deviceAssocTable ----
				deviceAssocTable.setModel(new DefaultTableModel(
						Variables.deviceAssocTableRows,
						Variables.deviceAssocTableColumns) {
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
				TableColumnModel cm = this.deviceAssocTable.getColumnModel();
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
	private JScrollPane deviceTableScrollPane;
	private JTable deviceTable;
	private JScrollPane lineAssocPane;
	private JTable lineAssocTable;
	private JScrollPane deviceAssocPane;
	private JTable deviceAssocTable;
	private JPanel logPanel;
	private JScrollPane logScrollPane;
	public static JTextArea logArea;
	private JFrame aboutFrame;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	// JFormDesigner - End of variables declaration //GEN-END:variables

	// CustomComboBoxStuff
	public class S15WorkingBackspace extends PlainDocument {
		JComboBox comboBox;
		ComboBoxModel model;
		JTextComponent editor;
		// flag to indicate if setSelectedItem has been called
		// subsequent calls to remove/insertString should be ignored
		boolean selecting = false;
		boolean hidePopupOnFocusLoss;
		boolean hitBackspace = false;
		boolean hitBackspaceOnSelection;

		public S15WorkingBackspace(final JComboBox comboBox) {
			this.comboBox = comboBox;
			model = comboBox.getModel();
			editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
			editor.setDocument(this);
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!selecting)
						highlightCompletedText(0);
				}
			});
			editor.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (comboBox.isDisplayable())
						comboBox.setPopupVisible(true);
					hitBackspace = false;
					switch (e.getKeyCode()) {
					// determine if the pressed key is backspace (needed by the
					// remove method)
					case KeyEvent.VK_BACK_SPACE:
						hitBackspace = true;
						hitBackspaceOnSelection = editor.getSelectionStart() != editor
								.getSelectionEnd();
						break;
					// ignore delete key
					case KeyEvent.VK_DELETE:
						e.consume();
						comboBox.getToolkit().beep();
						break;
					}
				}
			});
			// Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when
			// tabbing out
			hidePopupOnFocusLoss = System.getProperty("java.version")
					.startsWith("1.5");
			// Highlight whole text when gaining focus
			editor.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					highlightCompletedText(0);
				}

				public void focusLost(FocusEvent e) {
					// Workaround for Bug 5100422 - Hide Popup on focus loss
					if (hidePopupOnFocusLoss)
						comboBox.setPopupVisible(false);
				}
			});
			// Handle initially selected object
			Object selected = comboBox.getSelectedItem();
			if (selected != null)
				setText(selected.toString());
			highlightCompletedText(0);
		}

		public void remove(int offs, int len) throws BadLocationException {
			// return immediately when selecting an item
			if (selecting)
				return;
			if (hitBackspace) {
				// user hit backspace => move the selection backwards
				// old item keeps being selected
				if (offs > 0) {
					if (hitBackspaceOnSelection)
						offs--;
				} else {
					// User hit backspace with the cursor positioned on the
					// start => beep
					comboBox.getToolkit().beep(); // when available use:
													// UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
				}
				highlightCompletedText(offs);
			} else {
				super.remove(offs, len);
			}
		}

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			// return immediately when selecting an item
			if (selecting)
				return;
			// insert the string into the document
			super.insertString(offs, str, a);
			// lookup and select a matching item
			Object item = lookupItem(getText(0, getLength()));
			if (item != null) {
				setSelectedItem(item);
			} else {
				// keep old item selected if there is no match
				item = comboBox.getSelectedItem();
				// imitate no insert (later on offs will be incremented by
				// str.length(): selection won't move forward)
				offs = offs - str.length();
				// provide feedback to the user that his input has been received
				// but can not be accepted
				comboBox.getToolkit().beep(); // when available use:
												// UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
			}
			setText(item.toString());
			// select the completed part
			highlightCompletedText(offs + str.length());
		}

		private void setText(String text) {
			try {
				// remove all text and insert the completed string
				super.remove(0, getLength());
				super.insertString(0, text, null);
			} catch (BadLocationException e) {
				throw new RuntimeException(e.toString());
			}
		}

		private void highlightCompletedText(int start) {
			editor.setCaretPosition(getLength());
			editor.moveCaretPosition(start);
		}

		private void setSelectedItem(Object item) {
			selecting = true;
			model.setSelectedItem(item);
			selecting = false;
		}

		private Object lookupItem(String pattern) {
			Object selectedItem = model.getSelectedItem();
			// only search for a different item if the currently selected does
			// not match
			if (selectedItem != null
					&& startsWithIgnoreCase(selectedItem.toString(), pattern)) {
				return selectedItem;
			} else {
				// iterate over all items
				for (int i = 0, n = model.getSize(); i < n; i++) {
					Object currentItem = model.getElementAt(i);
					// current item starts with the pattern?
					if (startsWithIgnoreCase(currentItem.toString(), pattern))
						return currentItem;
				}
			}
			// no item starts with the pattern => return null
			return null;
		}

		// checks if str1 starts with str2 - ignores case
		private boolean startsWithIgnoreCase(String str1, String str2) {
			return str1.toUpperCase().startsWith(str2.toUpperCase());
		}

		private void createAndShowGUI() {
			// the combo box (add/modify items if you like to)
			JComboBox comboBox = new JComboBox(new Object[] { "Ester", "Jordi",
					"Jordina", "Jorge", "Sergi" });
			// has to be editable
			comboBox.setEditable(true);
			// change the editor's document
			new S15WorkingBackspace(comboBox);

			// create and show a window containing the combo box
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(3);
			frame.getContentPane().add(comboBox);
			frame.pack();
			frame.setVisible(true);
		}

		public void main(String[] args) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		}
	}

	public class ComboBoxCellEditor extends AbstractCellEditor implements
			ActionListener, TableCellEditor, Serializable {

		private JComboBox comboBox;

		public ComboBoxCellEditor(JComboBox comboBox) {
			this.comboBox = comboBox;
			this.comboBox.putClientProperty("JComboBox.isTableCellEditor",
					Boolean.TRUE);
			// hitting enter in the combo box should stop cellediting (see
			// below)
			this.comboBox.addActionListener(this);
			// remove the editor's border - the cell itself already has one
			((JComponent) comboBox.getEditor().getEditorComponent())
					.setBorder(null);
		}

		private void setValue(Object value) {
			comboBox.setSelectedItem(value);
		}

		// Implementing ActionListener
		public void actionPerformed(java.awt.event.ActionEvent e) {
			// Selecting an item results in an actioncommand "comboBoxChanged".
			// We should ignore these ones.

			// Hitting enter results in an actioncommand "comboBoxEdited"
			if (e.getActionCommand().equals("comboBoxEdited")) {
				stopCellEditing();
			}
		}

		// Implementing CellEditor
		public Object getCellEditorValue() {
			return comboBox.getSelectedItem();
		}

		public boolean stopCellEditing() {
			if (comboBox.isEditable()) {
				// Notify the combo box that editing has stopped (e.g. User
				// pressed F2)
				comboBox.actionPerformed(new ActionEvent(this, 0, ""));
			}
			fireEditingStopped();
			return true;
		}

		// Implementing TableCellEditor
		public java.awt.Component getTableCellEditorComponent(
				javax.swing.JTable table, Object value, boolean isSelected,
				int row, int column) {
			setValue(value);
			return comboBox;
		}

		// Implementing TreeCellEditor
		// public java.awt.Component
		// getTreeCellEditorComponent(javax.swing.JTree tree, Object value,
		// boolean isSelected, boolean expanded, boolean leaf, int row) {
		// String stringValue = tree.convertValueToText(value, isSelected,
		// expanded, leaf, row, false);
		// setValue(stringValue);
		// return comboBox;
		// }
	}

	public class updateThreadContainer extends Thread {
		private Thread thread = null;
		private String threadName;

		updateThreadContainer(String name) {
			threadName = name;
		}

		public void run() {
			System.out.println("New update container");
			Variables.newDeviceTableEnduserRows = new String[deviceTable
					.getRowCount()][4];
			for (int i = 0; i < deviceTable.getRowCount(); i++) {
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
					updateThread newUpdateThread = new updateThread(deviceTable
							.getModel().getValueAt(i, 0).toString(), i);
					logArea.append("Update device: "
							+ deviceTable.getModel().getValueAt(i, 1)
									.toString() + ", User: "
							+ Variables.oldDeviceTableEnduserRows[i][2] + ", "
							+ Variables.oldDeviceTableEnduserRows[i][1]
							+ " >> "
							+ Variables.newDeviceTableEnduserRows[i][2] + ", "
							+ Variables.newDeviceTableEnduserRows[i][1] + "\n");
					newUpdateThread.start();
					// Methods.updateDeviceSetEnduser(
					// Variables.newDeviceTableEnduserRows[i][0],
					// deviceTable.getModel().getValueAt(i, 1).toString());
					// Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[i][3]);
					// Methods.addEnduserDeviceMap(
					// Variables.newDeviceTableEnduserRows[i][0],
					// deviceTable.getModel().getValueAt(i, 1).toString());
					// Methods.addNumplan(Variables.newDeviceTableEnduserRows[i][0]);
					// Methods.updatePrimaryExtension(
					// Variables.newDeviceTableEnduserRows[i][0],
					// deviceTable.getModel().getValueAt(i, 1).toString());
					// Methods.addNumplanDevicemap(Variables.newDeviceTableEnduserRows[i][0]);
					// Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newDeviceTableEnduserRows[i][0]);
				}
			}
		}

		public void start() {
			if (thread == null) {
				thread = new Thread(this, threadName);
				thread.start();
			}
		}
	}

	public class allPhonesThread extends Thread {
		private Thread thread = null;
		private String threadName;
		private String sqlQuery;
		private int argument1;
		private int argument2;

		allPhonesThread(String name, int arg1, int arg2, String query) {
			threadName = name;
			argument1 = arg1;
			argument2 = arg2;
			sqlQuery = query;
		}

		public void run() {
			try {
				progressBar allDevices = new progressBar();
				allDevices.setVisible(true);
				Methods.getDevices(1, 1, "");
				Methods.getAllEndusers();
				// lookupDeviceType(1, 1, "");
				// lookupDevicepool(1, 1, "");
				// TODO
				allDevices.setVisible(false);
				for (int i = 0; i < deviceTable.getRowCount(); i++) {
					Variables.deviceTableRows[i][8] = "false";
				}
				DefaultTableModel model = new DefaultTableModel(
						Variables.deviceTableRows, Variables.deviceTableColumns) {
					Class[] columnTypes = { String.class, String.class,
							JComboBox.class, JComboBox.class, JComboBox.class,
							String.class, String.class, String.class,
							JCheckBox.class };

					public Class<?> getColumnClass(int columnIndex) {
						return this.columnTypes[columnIndex];
					}

					boolean[] canEdit = new boolean[] { false, true, true,
							true, true, false, false, true, true /* Checkbox */};

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];
					}

				};
				deviceTable.setModel(model);
				TableColumn UIDC = deviceTable.getColumnModel().getColumn(4);
				TableColumn LNC = deviceTable.getColumnModel().getColumn(3);
				TableColumn FNC = deviceTable.getColumnModel().getColumn(2);
				TableColumn NC = deviceTable.getColumnModel().getColumn(0);
				TableColumn UpdateColumn = deviceTable.getColumnModel()
						.getColumn(8);
				final JComboBox firstnameCombo = new JComboBox(
						Variables.phoneAllEndusersFirstnames);
				final JComboBox lastnameCombo = new JComboBox(
						Variables.phoneAllEndusersLastnames);
				final JComboBox useridCombo = new JComboBox(
						Variables.phoneAllEndusersIDs);
				final JCheckBox updateRow = new JCheckBox();
				firstnameCombo.setEditable(true);
				lastnameCombo.setEditable(true);
				useridCombo.setEditable(true);
				updateRow.setSelected(false);
				new S15WorkingBackspace(firstnameCombo);
				new S15WorkingBackspace(lastnameCombo);
				new S15WorkingBackspace(useridCombo);
				final int firstnameCol = 2, lastnameCol = 3, useridCol = 4;
				updateRow.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (updateRow.isSelected()) {
							System.out.println("Update: TRUE");
						}

					}

				});
				firstnameCombo.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							deviceTable.getModel().setValueAt(
									lastnameCombo.getItemAt(firstnameCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), lastnameCol);
							deviceTable.getModel().setValueAt(
									useridCombo.getItemAt(firstnameCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), useridCol);
						}

					}
				});
				lastnameCombo.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							deviceTable.getModel().setValueAt(
									firstnameCombo.getItemAt(lastnameCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), firstnameCol);
							deviceTable.getModel().setValueAt(
									useridCombo.getItemAt(lastnameCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), useridCol);
						}
					}
				});
				useridCombo.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							deviceTable.getModel().setValueAt(
									firstnameCombo.getItemAt(useridCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), firstnameCol);
							deviceTable.getModel().setValueAt(
									lastnameCombo.getItemAt(useridCombo
											.getSelectedIndex()),
									deviceTable.getSelectedRow(), lastnameCol);
						}
					}
				});
				UIDC.setCellEditor(new ComboBoxCellEditor(useridCombo));
				LNC.setCellEditor(new ComboBoxCellEditor(lastnameCombo));
				FNC.setCellEditor(new ComboBoxCellEditor(firstnameCombo));
				NC.setPreferredWidth(5);
				UpdateColumn.setPreferredWidth(5);
				UpdateColumn.setCellEditor(new DefaultCellEditor(updateRow));
			} catch (Exception error) {
				error.printStackTrace();
			}
			Variables.oldDeviceTableEnduserRows = new String[deviceTable
					.getRowCount()][4];
			for (int i = 0; i < deviceTable.getRowCount(); i++) {
				Variables.oldDeviceTableEnduserRows[i][0] = deviceTable
						.getModel().getValueAt(i, 4).toString();
				Variables.oldDeviceTableEnduserRows[i][1] = deviceTable
						.getModel().getValueAt(i, 3).toString();
				Variables.oldDeviceTableEnduserRows[i][2] = deviceTable
						.getModel().getValueAt(i, 2).toString();
				Variables.oldDeviceTableEnduserRows[i][3] = deviceTable
						.getModel().getValueAt(i, 1).toString();
			}
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
					deviceTable.getModel());
			deviceTable.setRowSorter(sorter);
		}

		public void start() {
			if (thread == null) {
				thread = new Thread(this, threadName);
				thread.start();
			}
		}
	}

	public class updateThread extends Thread {
		private Thread thread = null;
		private String threadName;
		private String[] args;
		private int num;

		updateThread(String name, int number) {
			threadName = name;
			num = number;
			// for (int i = 0; i < args.length; i++) {
			// args[i] = arguments[i];
			// }
		}

		public void run() {
			System.out.println("Running new Thread! " + threadName);
			Methods.updateDeviceSetEnduser(
					Variables.newDeviceTableEnduserRows[num][0], deviceTable
							.getModel().getValueAt(num, 1).toString());
			Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[num][3]);
			Methods.addEnduserDeviceMap(
					Variables.newDeviceTableEnduserRows[num][0], deviceTable
							.getModel().getValueAt(num, 1).toString());
			Methods.addNumplan(Variables.newDeviceTableEnduserRows[num][0]);
			Methods.updatePrimaryExtension(
					Variables.newDeviceTableEnduserRows[num][0], deviceTable
							.getModel().getValueAt(num, 1).toString());
			Methods.addNumplanDevicemap(Variables.newDeviceTableEnduserRows[num][0]);
			Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newDeviceTableEnduserRows[num][0]);
			System.out.println("Done: " + num);
		}

		public void start() {
			if (thread == null) {
				thread = new Thread(this, threadName);
				thread.start();
			}
		}
	}

	public class updateDeviceAssocThread extends Thread {
		private Thread thread = null;
		private String threadName;
		private String[] args;
		private int num;

		updateDeviceAssocThread(String name, int number) {
			threadName = name;
			num = number;
		}

		public void run() {
			System.out.println("Running new Thread! " + threadName);
			Methods.updateDeviceSetEnduser(
					Variables.newDeviceAssocTableEnduserRows[num][0],
					deviceAssocTable.getModel().getValueAt(num, 1).toString());
			// Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[num][3]);
			Methods.addEnduserDeviceMap(
					Variables.newDeviceAssocTableEnduserRows[num][0],
					deviceAssocTable.getModel().getValueAt(num, 1).toString());
			Methods.addNumplan(Variables.newDeviceAssocTableEnduserRows[num][0]);
			Methods.updatePrimaryExtension(
					Variables.newDeviceAssocTableEnduserRows[num][0],
					deviceAssocTable.getModel().getValueAt(num, 1).toString());
			Methods.addNumplanDevicemap(Variables.newDeviceAssocTableEnduserRows[num][0]);
			Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newDeviceAssocTableEnduserRows[num][0]);
			System.out.println("Done: " + num);
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
			localw.setTitle("Processing");
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
