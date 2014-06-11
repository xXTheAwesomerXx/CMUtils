package org.Container.Threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class enduserAssocThread extends Thread {
	/**
	 * 
	 */
	private final CMUtilsGUI cmUtilsGUI;
	private Thread thread = null;
	private String threadName;

	public enduserAssocThread(CMUtilsGUI cmUtilsGUI, String name) {
		this.cmUtilsGUI = cmUtilsGUI;
		threadName = name;
	}

	@SuppressWarnings("serial")
	public void run() {
		try {
			progressBar allDevices = new progressBar();
			allDevices.setVisible(true);
			Methods.getEnduserAssoc();
			Methods.getEnduserAssocDevices();
			allDevices.setVisible(false);
			DefaultTableModel model = new DefaultTableModel(
					Variables.enduserAssocTableRows,
					Variables.enduserAssocTableColumns) {

				boolean[] canEdit = new boolean[] { false, true, false,
						false, false, true };

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}

			};
			this.cmUtilsGUI.enduserAssocTable.setModel(model);
			TableColumn NC = this.cmUtilsGUI.enduserAssocTable.getColumnModel()
					.getColumn(0);
			TableColumn UpdateColumn = this.cmUtilsGUI.enduserAssocTable.getColumnModel()
					.getColumn(5);
			TableColumn DNC = this.cmUtilsGUI.enduserAssocTable.getColumnModel().getColumn(
					1);
			final JComboBox<?> deviceNameCombo = new JComboBox<Object>(
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
		Variables.oldenduserAssocTableEnduserRows = new String[this.cmUtilsGUI.enduserAssocTable
				.getRowCount()][1];
		TableRowSorter<TableModel> devAssocSort = new TableRowSorter<TableModel>(
				this.cmUtilsGUI.enduserAssocTable.getModel());
		this.cmUtilsGUI.enduserAssocTable.setRowSorter(devAssocSort);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
}