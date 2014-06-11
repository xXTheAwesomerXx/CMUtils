package org.Container.Threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.CMUtils.CMUtilsGUI;
import org.Container.ComboBoxCellEditor;
import org.Container.Methods;
import org.Container.S15WorkingBackspace;
import org.Container.Variables;
import org.Container.progressBar;

public class allPhonesThread extends Thread {
	/**
	 * 
	 */
	private final CMUtilsGUI cmUtilsGUI;
	private Thread thread = null;
	private String threadName;
	private String sqlQuery;
	private int argument1;
	private int argument2;

	public allPhonesThread(CMUtilsGUI cmUtilsGUI, String name, int arg1, int arg2, String query) {
		this.cmUtilsGUI = cmUtilsGUI;
		threadName = name;
		argument1 = arg1;
		argument2 = arg2;
		sqlQuery = query;
	}

	@SuppressWarnings("serial")
	public void run() {
		try {
			progressBar allDevices = new progressBar();
			allDevices.setVisible(true);
			Methods.getDevices(argument1, argument2, sqlQuery);
			Methods.getAllEndusers();
			allDevices.setVisible(false);
			JScrollBar scrollbar = CMUtilsGUI.deviceTableScrollPane
					.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMinimum());
			DefaultTableModel model = new DefaultTableModel(
					Variables.deviceTableRows, Variables.deviceTableColumns) {
				boolean[] canEdit = new boolean[] { false, true, true,
						true, true, false, false, true, true };

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}

			};
			this.cmUtilsGUI.deviceTable.setModel(model);
			TableColumn UIDC = this.cmUtilsGUI.deviceTable.getColumnModel().getColumn(4);
			TableColumn LNC = this.cmUtilsGUI.deviceTable.getColumnModel().getColumn(3);
			TableColumn FNC = this.cmUtilsGUI.deviceTable.getColumnModel().getColumn(2);
			TableColumn NC = this.cmUtilsGUI.deviceTable.getColumnModel().getColumn(0);
			TableColumn UpdateColumn = this.cmUtilsGUI.deviceTable.getColumnModel()
					.getColumn(8);
			List<String> firstnameList = new ArrayList<String>();
			List<String> lastnameList = new ArrayList<String>();
			List<String> useridList = new ArrayList<String>();
			firstnameList.add(0, "Default");
			lastnameList.add(0, "Default");
			useridList.add(0, "Default");
			for (int i = 0; i < Variables.phoneAllEndusersFirstnames.length; i++) {
				firstnameList.add(Variables.phoneAllEndusersFirstnames[i]);
				lastnameList.add(Variables.phoneAllEndusersLastnames[i]);
				useridList.add(Variables.phoneAllEndusersIDs[i]);
			}
			final JComboBox<?> firstnameCombo = new JComboBox<Object>(
					firstnameList.toArray());
			final JComboBox<?> lastnameCombo = new JComboBox<Object>(
					lastnameList.toArray());
			final JComboBox<?> useridCombo = new JComboBox<Object>(
					useridList.toArray());
			final JCheckBox updateRow = new JCheckBox();
			firstnameCombo.setEditable(true);
			lastnameCombo.setEditable(true);
			useridCombo.setEditable(true);
			updateRow.setSelected(false);
			new S15WorkingBackspace(firstnameCombo);
			new S15WorkingBackspace(lastnameCombo);
			new S15WorkingBackspace(useridCombo);
			final int firstnameCol = 2, lastnameCol = 3, useridCol = 4;
			if (firstnameCombo.getSelectedIndex() == -1) {
				firstnameCombo.setSelectedIndex(0);
			}
			if (lastnameCombo.getSelectedIndex() == -1) {
				lastnameCombo.setSelectedIndex(0);
			}
			if (useridCombo.getSelectedIndex() == -1) {
				useridCombo.setSelectedIndex(0);
			}
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
						try {
							allPhonesThread.this.cmUtilsGUI.deviceTable.getModel().setValueAt(
									lastnameCombo.getItemAt(firstnameCombo
											.getSelectedIndex()),
									allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
									lastnameCol);
							allPhonesThread.this.cmUtilsGUI.deviceTable
									.getModel()
									.setValueAt(
											useridCombo
													.getItemAt(firstnameCombo
															.getSelectedIndex()),
											allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
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
						try {
							allPhonesThread.this.cmUtilsGUI.deviceTable.getModel().setValueAt(
									firstnameCombo.getItemAt(lastnameCombo
											.getSelectedIndex()),
									allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
									firstnameCol);
							allPhonesThread.this.cmUtilsGUI.deviceTable
									.getModel()
									.setValueAt(
											useridCombo
													.getItemAt(lastnameCombo
															.getSelectedIndex()),
											allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
											useridCol);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			useridCombo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						try {
							allPhonesThread.this.cmUtilsGUI.deviceTable.getModel().setValueAt(
									firstnameCombo.getItemAt(useridCombo
											.getSelectedIndex()),
									allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
									firstnameCol);
							allPhonesThread.this.cmUtilsGUI.deviceTable.getModel().setValueAt(
									lastnameCombo.getItemAt(useridCombo
											.getSelectedIndex()),
									allPhonesThread.this.cmUtilsGUI.deviceTable.getSelectedRow(),
									lastnameCol);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
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
		Variables.oldDeviceTableEnduserRows = new String[Variables.phoneNames.length + 1][4];
		for (int i = 0; i < Variables.phoneNames.length; i++) {
			Variables.oldDeviceTableEnduserRows[i][0] = this.cmUtilsGUI.deviceTable
					.getModel().getValueAt(i, 4).toString();
			Variables.oldDeviceTableEnduserRows[i][1] = this.cmUtilsGUI.deviceTable
					.getModel().getValueAt(i, 3).toString();
			Variables.oldDeviceTableEnduserRows[i][2] = this.cmUtilsGUI.deviceTable
					.getModel().getValueAt(i, 2).toString();
			Variables.oldDeviceTableEnduserRows[i][3] = this.cmUtilsGUI.deviceTable
					.getModel().getValueAt(i, 1).toString();
		}
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				this.cmUtilsGUI.deviceTable.getModel());
		this.cmUtilsGUI.deviceTable.setRowSorter(sorter);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
}