package org.Container.Threads;

import org.CMUtils.CMUtilsGUI;
import org.Container.Methods;
import org.Container.Variables;
import org.Container.progressBar;

public class updateThread extends Thread {
	/**
	 * 
	 */
	private final CMUtilsGUI cmUtilsGUI;
	private Thread thread = null;
	private String threadName;
	private int num;

	public updateThread(CMUtilsGUI cmUtilsGUI, String name, int number) {
		this.cmUtilsGUI = cmUtilsGUI;
		threadName = name;
		num = number;
	}

	public void run() {
		System.out.println("Running new Thread! " + threadName);
		progressBar updateDevices = new progressBar();
		updateDevices.setVisible(true);
		Methods.updateDeviceSetEnduser(
				Variables.newDeviceTableEnduserRows[num][0], this.cmUtilsGUI.deviceTable
						.getModel().getValueAt(num, 1).toString());
		Methods.removeEnduserDeviceMap(Variables.oldDeviceTableEnduserRows[num][3]);
		Methods.addEnduserDeviceMap(
				Variables.newDeviceTableEnduserRows[num][0], this.cmUtilsGUI.deviceTable
						.getModel().getValueAt(num, 1).toString());
		Methods.addNumplan(Variables.newDeviceTableEnduserRows[num][0]);
		Methods.updatePrimaryExtension(
				Variables.newDeviceTableEnduserRows[num][0], this.cmUtilsGUI.deviceTable
						.getModel().getValueAt(num, 1).toString());
		Methods.addNumplanDevicemap(Variables.newDeviceTableEnduserRows[num][0]);
		Methods.addDevicenumplanEnduserNumplanAssoc(Variables.newDeviceTableEnduserRows[num][0]);
		updateDevices.setVisible(false);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
}