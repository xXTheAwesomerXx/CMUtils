package org.Container.Threads;

import org.CMUtils.CMUtilsGUI;
import org.Container.Methods;
import org.Container.Variables;
import org.Container.progressBar;

public class updateenduserAssocThread extends Thread {
	/**
	 * 
	 */
	private final CMUtilsGUI cmUtilsGUI;
	private Thread thread = null;
	private String threadName;
	private int num;

	public updateenduserAssocThread(CMUtilsGUI cmUtilsGUI, String name, int number) {
		this.cmUtilsGUI = cmUtilsGUI;
		threadName = name;
		num = number;
	}

	public void run() {
		System.out.println("Running new Thread! " + threadName);
		progressBar updateDevAssoc = new progressBar();
		updateDevAssoc.setVisible(true);
		Methods.updateDeviceSetEnduser(
				Variables.newenduserAssocTableEnduserRows[num][0],
				this.cmUtilsGUI.enduserAssocTable.getModel().getValueAt(num, 1).toString());
		// Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[num][3]);
		Methods.addEnduserDeviceMap(
				Variables.newenduserAssocTableEnduserRows[num][0],
				this.cmUtilsGUI.enduserAssocTable.getModel().getValueAt(num, 1).toString());
		Methods.addNumplan(Variables.newenduserAssocTableEnduserRows[num][0]);
		Methods.updatePrimaryExtension(
				Variables.newenduserAssocTableEnduserRows[num][0],
				this.cmUtilsGUI.enduserAssocTable.getModel().getValueAt(num, 1).toString());
		Methods.addNumplanDevicemap(Variables.newenduserAssocTableEnduserRows[num][0]);
		Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newenduserAssocTableEnduserRows[num][0]);
		updateDevAssoc.setVisible(false);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
}