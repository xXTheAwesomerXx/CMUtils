package org.CMUtils;

import org.Container.Variables;

public class CMUtils {
	public static void main(String[] args) {
		ConnectGUI conGUI = new ConnectGUI();
		conGUI.setVisible(true);
		while (!Variables.connected) {
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
