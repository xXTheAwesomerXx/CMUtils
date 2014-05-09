package org.CMUtils;

import java.awt.*;
import java.awt.event.*;
import java.io.StringReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.Container.Methods;
import org.Container.Variables;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

/*
 * Created by JFormDesigner on Mon Apr 28 00:45:29 EDT 2014
 */

/**
 * @author Hamant Joucoo
 */
public class runSQL extends JFrame {
	public runSQL() {
		initComponents();
	}

	private void sqlFieldKeyPressed(KeyEvent p) {
		if (p.getKeyCode() == p.VK_ENTER) {
			try {
				String xml = Methods.executeSQLSelect(sqlField.getText()
						.toString());
				Variables.sqlRows = Methods.substringsBetween(xml, "<row>",
						"</row>");
				for (int i = 0; i < Variables.sqlRows.length; i++) {
					Variables.sqlRowFields = Methods.substringsBetween(
							Variables.sqlRows[0], "<", ">", "/");
				}
				Variables.sqlTableColumns = new String[Variables.sqlRowFields.length];
				for (int x = 0; x < Variables.sqlRowFields.length; x++) {
					Variables.sqlTableColumns[x] = Variables.sqlRowFields[x];
					Variables.sqlTableRows = new String[Variables.sqlRows.length][Variables.sqlRowFields.length];
				}
				for (int y = 0; y < Variables.sqlRows.length; y++) {
					for (int g = 0; g < Variables.sqlRowFields.length; g++) {
						Variables.sqlTableRows[y][g] = Methods
								.substringBetween(Variables.sqlRows[y], "<"
										+ Variables.sqlRowFields[g] + ">", "</"
										+ Variables.sqlRowFields[g] + ">");
					}
				}
				resultsTable.setModel(new DefaultTableModel(
						Variables.sqlTableRows, Variables.sqlTableColumns) {
					private static final long serialVersionUID = -6396300478056746940L;
					boolean[] columnEditable = new boolean[6];

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.columnEditable[columnIndex];
					}
				});
				TableColumnModel cm = this.resultsTable.getColumnModel();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void excecuteButtonActionPerformed(ActionEvent e) {
		try {
			String xml = Methods
					.executeSQLSelect(sqlField.getText().toString());
			Variables.sqlRows = Methods.substringsBetween(xml, "<row>",
					"</row>");
			for (int i = 0; i < Variables.sqlRows.length; i++) {
				Variables.sqlRowFields = Methods.substringsBetween(
						Variables.sqlRows[0], "<", ">", "/");
			}
			Variables.sqlTableColumns = new String[Variables.sqlRowFields.length];
			for (int x = 0; x < Variables.sqlRowFields.length; x++) {
				Variables.sqlTableColumns[x] = Variables.sqlRowFields[x];
				Variables.sqlTableRows = new String[Variables.sqlRows.length][Variables.sqlRowFields.length];
			}
			for (int y = 0; y < Variables.sqlRows.length; y++) {
				for (int g = 0; g < Variables.sqlRowFields.length; g++) {
					Variables.sqlTableRows[y][g] = Methods.substringBetween(
							Variables.sqlRows[y], "<"
									+ Variables.sqlRowFields[g] + ">", "</"
									+ Variables.sqlRowFields[g] + ">");
				}
			}
			resultsTable.setModel(new DefaultTableModel(Variables.sqlTableRows,
					Variables.sqlTableColumns) {
				private static final long serialVersionUID = -6396300478056746940L;
				boolean[] columnEditable = new boolean[6];

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return this.columnEditable[columnIndex];
				}
			});
			TableColumnModel cm = this.resultsTable.getColumnModel();
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
		logPane = new JTextPane();

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
			// String xml =
			// "<return><row><pkid>35270061-eaab-424c-9489-39da75a944a4</pkid><fkenduser>5494065b-5814-8f5c-c897-84c2d3f0ae61</fkenduser><fknumplan>6f49ac3d-6771-4b1b-8ca3-718b782de888</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>18004dc2-7bec-4a82-b347-07994eb39df5</pkid><fkenduser>576d4d7b-d445-b7ac-c187-5e2ec7ac9b7c</fkenduser><fknumplan>6f49ac3d-6771-4b1b-8ca3-718b782de888</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>9491d3f8-517f-4118-887a-e638f1cf96c4</pkid><fkenduser>80a1f643-8bb4-cc4c-01a9-bff7879f78b9</fkenduser><fknumplan>247b1b72-c95e-49b6-94fc-7e358da5ac4a</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>41134638-0384-4f91-9acd-180448ba5e9f</pkid><fkenduser>80a1f643-8bb4-cc4c-01a9-bff7879f78b9</fkenduser><fknumplan>87df0075-75f2-4a58-b469-2e915ff83a1c</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>05e5ed78-1608-44f8-84a7-437904a0f7d4</pkid><fkenduser>819181ee-74e0-1ed0-0001-72558746479d</fkenduser><fknumplan>247b1b72-c95e-49b6-94fc-7e358da5ac4a</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>da482b45-7bc8-44d9-9324-d371c3e06205</pkid><fkenduser>5494065b-5814-8f5c-c897-84c2d3f0ae61</fkenduser><fknumplan>a3ebf53d-c386-de09-53d6-01ed18914976</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>2b7cfbfc-ec64-4a1d-aeea-5452f1e478c9</pkid><fkenduser>819181ee-74e0-1ed0-0001-72558746479d</fkenduser><fknumplan>76d74c2d-f6f3-664e-8063-181a37938514</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>2262cc33-3d3f-4535-ae7f-882c5c190bb3</pkid><fkenduser>d83b2f83-4098-5339-e0f8-b2a9175e4229</fkenduser><fknumplan>247b1b72-c95e-49b6-94fc-7e358da5ac4a</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>08792770-507d-49a2-aea6-6ec226ab1e62</pkid><fkenduser>d83b2f83-4098-5339-e0f8-b2a9175e4229</fkenduser><fknumplan>76d74c2d-f6f3-664e-8063-181a37938514</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>831841a7-ab17-4e7c-8ebb-5f0c1e1c9694</pkid><fkenduser>655bf510-24f6-7d29-50a6-7e069156c4d5</fkenduser><fknumplan>a3ebf53d-c386-de09-53d6-01ed18914976</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>d59787bf-33a5-493b-ba85-c5e027b8cd73</pkid><fkenduser>576d4d7b-d445-b7ac-c187-5e2ec7ac9b7c</fkenduser><fknumplan>a3ebf53d-c386-de09-53d6-01ed18914976</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>63663ba6-35e5-42fc-ac88-f8147fa3d3db</pkid><fkenduser>655bf510-24f6-7d29-50a6-7e069156c4d5</fkenduser><fknumplan>6f49ac3d-6771-4b1b-8ca3-718b782de888</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>905a0058-83ba-4f7f-9d41-9db26bc5a35f</pkid><fkenduser>80a1f643-8bb4-cc4c-01a9-bff7879f78b9</fkenduser><fknumplan>6f49ac3d-6771-4b1b-8ca3-718b782de888</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>6dd2674c-5155-4fe7-96e1-e5f8386b159c</pkid><fkenduser>5494065b-5814-8f5c-c897-84c2d3f0ae61</fkenduser><fknumplan>247b1b72-c95e-49b6-94fc-7e358da5ac4a</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>c9d0c1e6-23d6-4234-aa23-d83fd45c4b3e</pkid><fkenduser>80a1f643-8bb4-cc4c-01a9-bff7879f78b9</fkenduser><fknumplan>a3ebf53d-c386-de09-53d6-01ed18914976</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>2992c4e1-b4e4-4990-867a-b5ee8618bbcd</pkid><fkenduser>80a1f643-8bb4-cc4c-01a9-bff7879f78b9</fkenduser><fknumplan>76d74c2d-f6f3-664e-8063-181a37938514</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>28fab8de-abf4-49e2-9c9d-6153a85ebe43</pkid><fkenduser>945a7675-7ee0-4971-b2ea-8d49d331086f</fkenduser><fknumplan>2eaa33d3-7c32-a401-f9df-2c99061d99e9</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>ba132b87-b8e5-4441-94a3-1ef134d5c9e6</pkid><fkenduser>b6c319be-b2e5-4eb3-b412-13b221ee4b9c</fkenduser><fknumplan>52c616b3-f62e-b8ec-7b1b-f83aa59421d4</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>581429f8-83bc-48a0-94e3-b83c6c67538a</pkid><fkenduser>5fc3bf89-141f-436f-86fc-ed877e5771ba</fkenduser><fknumplan>6e94220a-015b-7ce9-53f6-ca6cbad18f04</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>36b97520-1502-4842-b7ca-4cbcef3468ab</pkid><fkenduser>bd5c5d06-f807-41f5-9a75-3291da097e39</fkenduser><fknumplan>03204d57-97b8-d4b6-0ff6-3f3c6d3388f3</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>33ae13ba-3d03-4e46-b9e5-9e014f44d504</pkid><fkenduser>32307e88-89be-434e-a80f-8b9ec97e20f9</fkenduser><fknumplan>a3ce233f-da4e-20f8-6b6b-b4f7051f1343</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>077eb559-4686-4ffb-9d32-2f5641faf500</pkid><fkenduser>cdcc03e2-73cf-420a-8c97-a957b8a85fd2</fkenduser><fknumplan>fc0bfb4c-0cce-0c59-1acf-83ccae37d91c</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>d9a0b190-07fa-4e02-bbff-f9bc8caa3c95</pkid><fkenduser>cb3f60da-6efc-47e2-a3c4-8b49fa1219a3</fkenduser><fknumplan>cbf0a8f4-68dd-f9a1-f940-849f920983c3</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>49d66b02-1786-4cee-880f-65d3d307e9a5</pkid><fkenduser>19aafb2f-f08f-4182-a38b-9ba251059bbc</fkenduser><fknumplan>76cc1a16-db74-3652-5da4-595358a27d8b</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>5d2a32be-da7b-494c-b0e9-3eb2e7998e11</pkid><fkenduser>2cce0656-9c76-46d2-bf76-9987e87c568a</fkenduser><fknumplan>f1ab818f-c9b6-62ff-69c6-a2b3c882b463</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>59a0f8c2-f8c8-4d9c-bc13-114d6735d46a</pkid><fkenduser>30912ff3-5f51-4cfb-b71d-c814440fb18a</fkenduser><fknumplan>f75fc44f-f0a9-b40c-723f-0c81651abe2f</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>4b217dc8-f7d3-4746-8f6c-fd2ac96d5447</pkid><fkenduser>c2dff1a5-ee4b-4ada-ae42-2c64fb03acda</fkenduser><fknumplan>6d4f8e04-9765-5ce0-aad0-a89020cb0b48</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>27d339b5-6aa0-48ee-a1cf-512b415c0f19</pkid><fkenduser>963c059f-e3eb-4ce1-827d-cf5a4bd2bf9c</fkenduser><fknumplan>c79b1242-4939-6ab9-af3b-d1b0883be9e4</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>9a5fd75d-01d4-4088-b203-1e63a786fc08</pkid><fkenduser>425df240-804a-4688-ab27-24eae58a407c</fkenduser><fknumplan>bd0016ac-9d2c-5dff-b832-b152700abc6c</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>4fb3d478-467b-4387-9a5a-c88a08addf78</pkid><fkenduser>c6f5bb40-8724-45b3-a7f3-3ca02de61a5d</fkenduser><fknumplan>f3998ce3-1f3e-5b1a-67f2-624a84d83cd8</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>e1b2439b-f276-465a-8026-39302bae4c3e</pkid><fkenduser>58291d6a-172d-9a78-1332-bac0b4a93140</fkenduser><fknumplan>fa79af45-c313-c192-3894-0c2aa496b6e7</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>40982d2f-15aa-4655-ad6f-f2fc50b3cf1b</pkid><fkenduser>4c20805e-f116-4d54-adc6-068a57f7a695</fkenduser><fknumplan>fa79af45-c313-c192-3894-0c2aa496b6e7</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>1dcae51f-e596-4f8e-97ea-97086446a3f1</pkid><fkenduser>19aafb2f-f08f-4182-a38b-9ba251059bbc</fkenduser><fknumplan>a1cc0bbe-5cc1-3561-78e2-1697cf8a1dc8</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>c6fa0a77-6070-47c7-a7cf-0b52cc96f4a8</pkid><fkenduser>2ae597a0-21d7-4769-b6c0-3372e76c567d</fkenduser><fknumplan>53d9af00-f52b-09a1-e1a4-db73cb726df0</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>8aa3d687-e7bd-4fc0-abdf-b3bfadffc8a0</pkid><fkenduser>5350b57e-e0e3-4337-815a-80059b09c2d9</fkenduser><fknumplan>318b9caf-0738-f386-3dd1-1042aed886e4</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>c3bd7f80-1ab1-41e3-9ac6-e3c5a8860b1a</pkid><fkenduser>f0183f70-7c05-4eaa-9ff2-7a7cf09aa8f7</fkenduser><fknumplan>db930376-3091-6af4-3016-623dcc2132c7</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>6bbb6609-219b-482b-aa2c-6ccacfbbe465</pkid><fkenduser>ab26f12a-b5e2-49b9-b962-111d459cd831</fkenduser><fknumplan>ef2893d5-d4cd-464f-a133-c1d0765ba1c0</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>3eab6182-16ea-437f-be5a-f479e303172a</pkid><fkenduser>ab26f12a-b5e2-49b9-b962-111d459cd831</fkenduser><fknumplan>e69416dd-b948-270b-ae64-4cf5418fc492</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>e62054e2-95e1-44a0-b91c-00a728f6c6bb</pkid><fkenduser>f0183f70-7c05-4eaa-9ff2-7a7cf09aa8f7</fkenduser><fknumplan>5e152360-e7f1-0101-b0bd-349c1fcb2f1d</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>04e72a1c-3846-4337-910b-064d43a40ff2</pkid><fkenduser>0a8c15f8-829a-4953-9eb1-e057a13fde1f</fkenduser><fknumplan>398f32a7-7871-1e21-05f6-8970bd9340f5</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>044c7c77-9545-44e1-a004-ad6d6ae616f0</pkid><fkenduser>d4426bf9-f6be-4e8e-93ee-3ea926f12572</fkenduser><fknumplan>6d4f8e04-9765-5ce0-aad0-a89020cb0b48</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>acb01ac7-b3a1-4f9a-b0eb-1cde967b0782</pkid><fkenduser>5350b57e-e0e3-4337-815a-80059b09c2d9</fkenduser><fknumplan>ed285c3e-58f7-5a8b-5b9d-91bf6521165a</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>0206cc68-3fc8-42cc-a42c-f87da53a26b0</pkid><fkenduser>6524b4d4-4b79-47b1-8bc2-ec79fd73fa2e</fkenduser><fknumplan>76d74c2d-f6f3-664e-8063-181a37938514</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>72c7e8c6-b3d3-495c-8d0e-246ad726ee84</pkid><fkenduser>819181ee-74e0-1ed0-0001-72558746479d</fkenduser><fknumplan>a3ebf53d-c386-de09-53d6-01ed18914976</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>9da4c051-9948-4f9e-beda-20bb839ee314</pkid><fkenduser>e4082517-f7d5-4929-8d1f-a1d2e6f53fe2</fkenduser><fknumplan>92cde8c1-d65c-101d-b24f-1300f20ca660</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>2940a9bc-07be-4320-91df-55d3192f961b</pkid><fkenduser>fdbfad62-11ac-4a50-9ea9-5df794cc69ba</fkenduser><fknumplan>f3998ce3-1f3e-5b1a-67f2-624a84d83cd8</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>6e59b489-0fa3-42e0-9cae-f2840ee8f0b9</pkid><fkenduser>31c70d3b-3612-421e-b646-b2ebe4e9b2b2</fkenduser><fknumplan>775df186-38af-330b-e8e3-f38b800dc2b8</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>ddd6800c-cb51-40bc-849e-4d49d4ee95f7</pkid><fkenduser>b061f7cf-5356-436d-bc06-28ca535b9e00</fkenduser><fknumplan>cc1c5ed9-bd22-0735-af97-4a9c2755fb08</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>d550e634-1daf-42b5-953a-8ddc66adba61</pkid><fkenduser>3e2bcb19-eee5-4686-b94a-82d440fcfdc6</fkenduser><fknumplan>168b736c-b3af-7454-a0db-98012c0ba7a9</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>12d7c9c6-0a2f-4fa7-8345-291f74dc46e6</pkid><fkenduser>6524b4d4-4b79-47b1-8bc2-ec79fd73fa2e</fkenduser><fknumplan>0a999ae4-71f1-9ca6-4257-403bcc4bfd45</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>a6eb0327-27ce-4843-ab34-35768065f2ef</pkid><fkenduser>252bd44f-94b7-46ef-a6c9-495fe5a89396</fkenduser><fknumplan>bd9f7bf7-6fb7-0cd2-2488-88cde48f5db0</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>517d3209-f02a-4283-8e2e-23251707a8fb</pkid><fkenduser>65b25230-2638-4ace-a508-c6d498a9c0d8</fkenduser><fknumplan>4fabdf90-dbc8-6e05-685a-303fd64ba7c4</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>b75b11d7-d49a-406e-a94f-a654bd0350cf</pkid><fkenduser>116fb407-b088-4e47-bd61-6aba4d5b9ef6</fkenduser><fknumplan>e5713060-f1d7-e1a7-c261-3e7598e97b54</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>dacc5cf8-6c04-43f4-8833-0d61c4cd873d</pkid><fkenduser>5350b57e-e0e3-4337-815a-80059b09c2d9</fkenduser><fknumplan>97020f4b-d90e-cd3d-3bc9-2c399aaa8484</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>69b4eba0-c8f1-49b2-8ce5-de4d0b3de216</pkid><fkenduser>d04dea38-2989-4b39-ada2-78093b6749fa</fkenduser><fknumplan>312eee64-6813-c8d2-6599-63fc4c6520ba</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>0d265021-16be-48bd-a270-d7d493d8e2a3</pkid><fkenduser>383cb1d8-e599-9c8a-6c1d-f0ee309623e0</fkenduser><fknumplan>9a7c8144-c051-d2c2-6bbf-f4e44559613c</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>a1feb303-8035-479e-9253-6cce2ad8ed7d</pkid><fkenduser>d796571e-a63a-4808-9c95-b583e9b58fe7</fkenduser><fknumplan>8fc40266-0129-be8c-d68b-f3c5fe9ce8ad</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>b67caf04-5721-4033-9908-6e7d6742332b</pkid><fkenduser>f518c116-ad78-41ff-8a2c-35646443633b</fkenduser><fknumplan>d7ef6b69-b5d5-a008-3513-948edfbefde2</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>e69a5f27-7685-4783-bc5c-12fc5a960012</pkid><fkenduser>5907a869-c307-4134-8ff6-6911c6cee578</fkenduser><fknumplan>b19fb343-b1a1-2ca8-c95b-f178fc6c8666</fknumplan><tkdnusage>1</tkdnusage></row><row><pkid>2c5f6d8b-d0e9-4e3a-a23a-e113881ba45e</pkid><fkenduser>6d65ff5c-bfca-4973-8210-38d1066d8694</fkenduser><fknumplan>f214dece-e863-682d-8b3b-a5148277b25c</fknumplan><tkdnusage>1</tkdnusage></row></return>";
			// Variables.sqlRows = Methods.substringsBetween(xml, "<row>",
			// "</row>");
			// for (int i = 0; i < Variables.sqlRows.length; i++) {
			// System.out.println("Row" + i + ": " + Variables.sqlRows[i]);
			// Variables.sqlRowFields =
			// Methods.substringsBetween(Variables.sqlRows[0], "<", ">", "/");
			// }
			// Variables.sqlTableColumns = new
			// String[Variables.sqlRowFields.length];
			// for (int x = 0; x < Variables.sqlRowFields.length; x++) {
			// Variables.sqlTableColumns[x] = Variables.sqlRowFields[x];
			// System.out.print(Variables.sqlRowFields[x] + " ");
			// Variables.sqlTableRows = new
			// String[Variables.sqlRows.length][Variables.sqlRowFields.length];
			// }
			// for (int y = 0; y < Variables.sqlRows.length; y++) {
			// for (int g = 0; g < Variables.sqlRowFields.length; g++) {
			// Variables.sqlTableRows[y][g] =
			// Methods.substringBetween(Variables.sqlRows[y], "<" +
			// Variables.sqlRowFields[g] + ">", "</" + Variables.sqlRowFields[g]
			// + ">");
			// System.out.println(Methods.substringBetween(Variables.sqlRows[y],
			// "<" + Variables.sqlRowFields[g] + ">", "</" +
			// Variables.sqlRowFields[g] + ">"));
			// }
			// }
			resultsTable.setModel(new DefaultTableModel(Variables.sqlTableRows,
					Variables.sqlTableColumns) {
				private static final long serialVersionUID = -6396300478056746940L;
				// Class[] columnTypes = { String.class, String.class,
				// JComboBox.class, JComboBox.class, JComboBox.class,
				// JCheckBox.class };
				boolean[] columnEditable = new boolean[6];

				// public Class<?> getColumnClass(int columnIndex) {
				// return this.columnTypes[columnIndex];
				// }

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return this.columnEditable[columnIndex];
				}
			});
			TableColumnModel cm = this.resultsTable.getColumnModel();
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
			logPane.setEditable(false);
			logScrollPane.setViewportView(logPane);
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
	private JTextPane logPane;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
