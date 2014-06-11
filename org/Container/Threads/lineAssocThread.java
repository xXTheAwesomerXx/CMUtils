package org.Container.Threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

public class lineAssocThread extends Thread {
	/**
	 * 
	 */
	private final CMUtilsGUI cmUtilsGUI;
	private Thread thread = null;
	private String threadName;

	public lineAssocThread(CMUtilsGUI cmUtilsGUI, String name) {
		this.cmUtilsGUI = cmUtilsGUI;
		threadName = name;
	}

	@SuppressWarnings("serial")
	public void run() {
		try {
			progressBar allDevices = new progressBar();
			allDevices.setVisible(true);
			Methods.getLineAssocEndusers();
			Methods.getNumplanAssoc();
			allDevices.setVisible(false);
			DefaultTableModel model = new DefaultTableModel(
					Variables.lineAssocTableRows,
					Variables.lineAssocTableColumns) {

				boolean[] canEdit = new boolean[] { false, false, true,
						true, true, true };

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}

			};
			this.cmUtilsGUI.lineAssocTable.setModel(model);
			TableColumn NC = this.cmUtilsGUI.lineAssocTable.getColumnModel().getColumn(0);
			TableColumn UpdateColumn = this.cmUtilsGUI.lineAssocTable.getColumnModel()
					.getColumn(5);
			TableColumn FNC = this.cmUtilsGUI.lineAssocTable.getColumnModel().getColumn(2);
			this.cmUtilsGUI.lineAssocTable.getColumnModel().getColumn(3);
			this.cmUtilsGUI.lineAssocTable.getColumnModel().getColumn(4);
			final JComboBox<?> firstnameCombo = new JComboBox<Object>(
					Variables.lineAssocFirstnames);
			final JComboBox<?> lastnameCombo = new JComboBox<Object>(
					Variables.lineAssocLastnames);
			final JComboBox<?> useridCombo = new JComboBox<Object>(
					Variables.lineAssocUserids);
			firstnameCombo.setEditable(true);
			lastnameCombo.setEditable(true);
			useridCombo.setEditable(true);
			final JCheckBox updateRow = new JCheckBox();
			new S15WorkingBackspace(firstnameCombo);
			new S15WorkingBackspace(lastnameCombo);
			new S15WorkingBackspace(useridCombo);
			updateRow.setSelected(false);
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
							lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
									lastnameCombo.getItemAt(firstnameCombo
											.getSelectedIndex()),
									lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(),
									lastnameCol);
							lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
									useridCombo.getItemAt(firstnameCombo
											.getSelectedIndex()),
									lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(),
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
						lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
								firstnameCombo.getItemAt(lastnameCombo
										.getSelectedIndex()),
								lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(),
								firstnameCol);
						lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
								useridCombo.getItemAt(lastnameCombo
										.getSelectedIndex()),
								lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(), useridCol);
					}
				}
			});
			useridCombo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
								firstnameCombo.getItemAt(useridCombo
										.getSelectedIndex()),
								lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(),
								firstnameCol);
						lineAssocThread.this.cmUtilsGUI.lineAssocTable.getModel().setValueAt(
								lastnameCombo.getItemAt(useridCombo
										.getSelectedIndex()),
								lineAssocThread.this.cmUtilsGUI.lineAssocTable.getSelectedRow(),
								lastnameCol);
					}
				}
			});
			FNC.setCellEditor(new ComboBoxCellEditor(firstnameCombo));
			NC.setPreferredWidth(5);
			UpdateColumn.setPreferredWidth(5);
			UpdateColumn.setCellEditor(new DefaultCellEditor(updateRow));
		} catch (Exception error) {
			error.printStackTrace();
		}
		Variables.oldLineAssocTableEnduserRows = new String[this.cmUtilsGUI.lineAssocTable
				.getRowCount()][4];
		TableRowSorter<TableModel> devAssocSort = new TableRowSorter<TableModel>(
				this.cmUtilsGUI.lineAssocTable.getModel());
		this.cmUtilsGUI.lineAssocTable.setRowSorter(devAssocSort);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
}