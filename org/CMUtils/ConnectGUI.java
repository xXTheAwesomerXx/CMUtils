package org.CMUtils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.Container.Methods;
import org.Container.Variables;

@SuppressWarnings("all")
public class ConnectGUI extends JFrame {
	private static final long serialVersionUID = 6892535037828518281L;

	public ConnectGUI() {
		initComponents();
	}

	private void savedLoadButtonActionPerformed(ActionEvent e) {
		if (savedConnectionsList.getSelectedIndex() != -1) {
			connectionHostTextField
					.setText(Variables.connectionHostnames[savedConnectionsList
							.getSelectedIndex()]);
			connectionUsernameTextField
					.setText(Variables.connectionUsernames[savedConnectionsList
							.getSelectedIndex()]);
			connectionPasswordField
					.setText(Variables.connectionPasswords[savedConnectionsList
							.getSelectedIndex()]);
		} else {
			JOptionPane.showMessageDialog(ConnectGUI.this.contentPanel,
					"Please select a cluster!", "CMUtls - Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void savedSaveButtonActionPerformed(ActionEvent e) {
		String path = System.getProperty("user.home") + File.separator
				+ "Documents";
		path += File.separator + "CMUtils";
		File customDir = new File(path);
		if (customDir.exists()) {
			String path2 = System.getProperty("user.home") + File.separator
					+ "Documents";
			path += File.separator + "CMUtils";
			File customDir2 = new File(path + "Connections.txt");
			if (customDir2.exists()) {
				try {
					// Create object of
					// FileReader
					String connectionName = JOptionPane.showInputDialog(this,
							"Please select a name for this Connection",
							"CMUtils - New Connection",
							JOptionPane.PLAIN_MESSAGE);
					if (connectionName != null) {
						FileWriter inputFile = new FileWriter(customDir2, true);
						BufferedWriter bufferWriter = new BufferedWriter(
								inputFile);
						bufferWriter.write("<Connection>"
								+ "<name>"
								+ connectionName
								+ "</name>"
								+ "<host>"
								+ connectionHostTextField.getText()
								+ "</host>"
								+ "<username>"
								+ connectionUsernameTextField.getText()
								+ "</username>"
								+ "<password>"
								+ new String(connectionPasswordField
										.getPassword()) + "</password>"
								+ "</Connection>\n");
						bufferWriter.close();
						if (setConnectionsModel()) {
							System.out.println("True");
						} else {
							System.out.println("False");
						}
					} else {
						JOptionPane.showMessageDialog(
								ConnectGUI.this.contentPanel,
								"Please select a name for this connection!",
								"CMUtils - Connection Name", 0);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				System.out.println("Connections File doesn't exist!");
			}
		}
	}

	private void savedRemoveButtonActionPerformed(ActionEvent e) {
		try {
			String path = System.getProperty("user.home") + File.separator
					+ "Documents";
			path += File.separator + "CMUtils";
			File customDir = new File(path);
			if (savedConnectionsList.getSelectedIndex() != 0) {
			if (customDir.exists()) {
				String path2 = System.getProperty("user.home") + File.separator
						+ "Documents";
				path += File.separator + "CMUtils";
				File customDir2 = new File(path + "Connections.txt");
				if (customDir2.exists()) {
					String line = "<Connection><name>"
							+ Variables.connectionNames[savedConnectionsList
									.getSelectedIndex()]
							+ "</name><host>"
							+ Variables.connectionHostnames[savedConnectionsList
									.getSelectedIndex()]
							+ "</host><username>"
							+ Variables.connectionUsernames[savedConnectionsList
									.getSelectedIndex()]
							+ "</username><password>"
							+ Variables.connectionPasswords[savedConnectionsList
									.getSelectedIndex()]
							+ "</password></Connection>";
					if (removeLineFromFile(customDir2.getAbsolutePath(), line)) {
						int index = savedConnectionsList.getSelectedIndex();
						System.out.println(index);
						DefaultListModel theModel = getModel();
						//System.out.println("Removed: " + theModel.remove(index));
						savedConnectionsList.setModel(theModel);
					}
					
//					if (setConnectionsModel()) {
//						System.out.println("True");
//					} else {
//						System.out.println("False");
//					}
				}
			}
			} else {
				System.out.println("Can't delete default settings!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent e) {
		try {
			try {
				Variables.ip = ConnectGUI.this.connectionHostTextField
						.getText();
				Variables.user = ConnectGUI.this.connectionUsernameTextField
						.getText();
				Variables.pass = String
						.valueOf(ConnectGUI.this.connectionPasswordField
								.getPassword());
				if (Variables.connected == false) {
					testConnectionThread connectThread = new testConnectionThread(
							"CONNECTION");
					connectThread.start();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void connectionPasswordFieldKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == e.VK_ENTER) {
			try {
				try {
					Variables.ip = ConnectGUI.this.connectionHostTextField
							.getText();
					Variables.user = ConnectGUI.this.connectionUsernameTextField
							.getText();
					Variables.pass = String
							.valueOf(ConnectGUI.this.connectionPasswordField
									.getPassword());
					if (Variables.connected == false) {
						testConnectionThread connectThread = new testConnectionThread(
								"CONNECTION");
						connectThread.start();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Hamant Joucoo
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		connectionPanel = new JPanel();
		connectionHostLabel = new JLabel();
		connectionHostTextField = new JTextField();
		connectionPasswordLabel = new JLabel();
		connectionUsernameTextField = new JTextField();
		connectionUsernameLabel = new JLabel();
		connectionPasswordField = new JPasswordField();
		savedConnectionsPanel = new JPanel();
		scrollPane1 = new JScrollPane();
		savedConnectionsList = new JList<>();
		savedLoadButton = new JButton();
		savedSaveButton = new JButton();
		savedRemoveButton = new JButton();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		connectionsListModel = new DefaultListModel<String>() {
			private static final long serialVersionUID = -4256579372683350582L;

			public String[] getValues() {
				List<String> lines = new ArrayList<String>();
				if (Variables.connectionNames.length <= 1) {
					try {
						String path = System.getProperty("user.home")
								+ File.separator + "Documents";
						path += File.separator + "CMUtils";
						File customDir = new File(path);
						if (customDir.exists()) {
							String path2 = System.getProperty("user.home")
									+ File.separator + "Documents";
							path += File.separator + "CMUtils";
							File customDir2 = new File(path + "Connections.txt");
							if (customDir2.exists()) {
								try {
									FileReader inputFile = new FileReader(
											customDir2);
									BufferedReader bufferReader = new BufferedReader(
											inputFile);
									String line;
									while ((line = bufferReader.readLine()) != null) {
										Variables.connectionsString = line
												.toString();
										lines.add(line.toString());
									}
									bufferReader.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (customDir2.createNewFile()) {
								try {
									FileWriter inputFile = new FileWriter(
											customDir2, true);
									BufferedWriter bufferWriter = new BufferedWriter(
											inputFile);
									bufferWriter
											.write("<Connection>"
													+ "<name>"
													+ "Default"
													+ "</name>"
													+ "<host>"
													+ connectionHostTextField
															.getText()
													+ "</host>"
													+ "<username>"
													+ connectionUsernameTextField
															.getText()
													+ "</username>"
													+ "<password>"
													+ new String(
															connectionPasswordField
																	.getPassword())
													+ "</password>"
													+ "</Connection>\n");
									bufferWriter.close();
									try {
										FileReader inputFile2 = new FileReader(
												customDir2);
										BufferedReader bufferReader = new BufferedReader(
												inputFile2);
										String line;
										while ((line = bufferReader.readLine()) != null) {
											Variables.connectionsString = line
													.toString();
											lines.add(line.toString());
										}
										bufferReader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}

								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								System.out.println("Failed to create file");
							}
						} else if (customDir.mkdirs()) {
							String path2 = System.getProperty("user.home")
									+ File.separator + "Documents";
							path += File.separator + "CMUtils";
							File customDir2 = new File(path + "Connections.txt");
							if (customDir2.exists()) {
								try {
									FileReader inputFile = new FileReader(
											customDir2);
									BufferedReader bufferReader = new BufferedReader(
											inputFile);
									String line;
									while ((line = bufferReader.readLine()) != null) {
										Variables.connectionsString = line
												.toString();
										lines.add(line.toString());
									}
									bufferReader.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (customDir2.createNewFile()) {
								try {
									FileWriter inputFile = new FileWriter(
											customDir2, true);
									BufferedWriter bufferWriter = new BufferedWriter(
											inputFile);
									bufferWriter
											.write("<Connection>"
													+ "<name>"
													+ "Default"
													+ "</name>"
													+ "<host>"
													+ connectionHostTextField
															.getText()
													+ "</host>"
													+ "<username>"
													+ connectionUsernameTextField
															.getText()
													+ "</username>"
													+ "<password>"
													+ new String(
															connectionPasswordField
																	.getPassword())
													+ "</password>"
													+ "</Connection>\n");
									bufferWriter.close();
									try {
										FileReader inputFile2 = new FileReader(
												customDir2);
										BufferedReader bufferReader = new BufferedReader(
												inputFile2);
										String line;
										while ((line = bufferReader.readLine()) != null) {
											Variables.connectionsString = line
													.toString();
											lines.add(line.toString());
										}
										bufferReader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								System.out.println("Failed to create file");
							}
						} else {
							System.out.println(customDir + " was not created");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					String details = lines.toString();
					Variables.connections = Methods.substringsBetween(details,
							"<Connection>", "</Connection>");
					Variables.connectionDetails = new String[Variables.connections.length][4];
					for (int i = 0; i < Variables.connections.length; i++) {
						Variables.connectionDetails[i][0] = Methods
								.substringBetween(Variables.connections[i],
										"<host>", "</host>");
						Variables.connectionDetails[i][1] = Methods
								.substringBetween(Variables.connections[i],
										"<username>", "</username>");
						Variables.connectionDetails[i][2] = Methods
								.substringBetween(Variables.connections[i],
										"<password>", "</password>");
						Variables.connectionDetails[i][3] = Methods
								.substringBetween(Variables.connections[i],
										"<name>", "</name>");
					}
					Variables.connectionHostnames = new String[Variables.connections.length];
					Variables.connectionUsernames = new String[Variables.connections.length];
					Variables.connectionPasswords = new String[Variables.connections.length];
					Variables.connectionNames = new String[Variables.connections.length];
					for (int i = 0; i < Variables.connections.length; i++) {
						Variables.connectionHostnames[i] = Variables.connectionDetails[i][0];
						Variables.connectionUsernames[i] = Variables.connectionDetails[i][1];
						Variables.connectionPasswords[i] = Variables.connectionDetails[i][2];
						Variables.connectionNames[i] = Variables.connectionDetails[i][3];
					}
				}
				return Variables.connectionNames;
			}

			@Override
			public int getSize() {
				return getValues().length;
			}

			@Override
			public String getElementAt(int i) {
				return getValues()[i];
			}
		};

		// ======== this ========
		setTitle("CMUtils - Connection Credentials");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// ======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane
					.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							if ("border".equals(e.getPropertyName()))
								throw new RuntimeException();
						}
					});

			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{

				// ======== connectionPanel ========
				{
					connectionPanel.setBorder(new TitledBorder(
							"Host Connection"));

					// ---- connectionHostLabel ----
					connectionHostLabel.setText("Host Name (IP Address)");

					// ---- connectionPasswordLabel ----
					connectionPasswordLabel.setText("Password");

					// ---- connectionUsernameLabel ----
					connectionUsernameLabel.setText("Username");

					// ---- connectionPasswordField ----
					connectionPasswordField.addKeyListener(new KeyAdapter() {
						@Override
						public void keyPressed(KeyEvent e) {
							connectionPasswordFieldKeyPressed(e);
						}
					});

					GroupLayout connectionPanelLayout = new GroupLayout(
							connectionPanel);
					connectionPanel.setLayout(connectionPanelLayout);
					connectionPanelLayout
							.setHorizontalGroup(connectionPanelLayout
									.createParallelGroup()
									.addGroup(
											connectionPanelLayout
													.createSequentialGroup()
													.addContainerGap()
													.addGroup(
															connectionPanelLayout
																	.createParallelGroup(
																			GroupLayout.Alignment.LEADING,
																			false)
																	.addComponent(
																			connectionHostLabel,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			Short.MAX_VALUE)
																	.addComponent(
																			connectionHostTextField))
													.addPreferredGap(
															LayoutStyle.ComponentPlacement.UNRELATED)
													.addGroup(
															connectionPanelLayout
																	.createParallelGroup()
																	.addComponent(
																			connectionUsernameLabel)
																	.addComponent(
																			connectionUsernameTextField,
																			GroupLayout.PREFERRED_SIZE,
																			126,
																			GroupLayout.PREFERRED_SIZE))
													.addGap(18, 18, 18)
													.addGroup(
															connectionPanelLayout
																	.createParallelGroup()
																	.addComponent(
																			connectionPasswordField,
																			GroupLayout.DEFAULT_SIZE,
																			124,
																			Short.MAX_VALUE)
																	.addGroup(
																			connectionPanelLayout
																					.createSequentialGroup()
																					.addComponent(
																							connectionPasswordLabel)
																					.addGap(0,
																							74,
																							Short.MAX_VALUE)))
													.addContainerGap()));
					connectionPanelLayout
							.setVerticalGroup(connectionPanelLayout
									.createParallelGroup()
									.addGroup(
											connectionPanelLayout
													.createSequentialGroup()
													.addGroup(
															connectionPanelLayout
																	.createParallelGroup(
																			GroupLayout.Alignment.BASELINE)
																	.addComponent(
																			connectionHostLabel)
																	.addComponent(
																			connectionUsernameLabel)
																	.addComponent(
																			connectionPasswordLabel))
													.addPreferredGap(
															LayoutStyle.ComponentPlacement.RELATED)
													.addGroup(
															connectionPanelLayout
																	.createParallelGroup(
																			GroupLayout.Alignment.BASELINE)
																	.addComponent(
																			connectionHostTextField,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE)
																	.addComponent(
																			connectionUsernameTextField,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE)
																	.addComponent(
																			connectionPasswordField,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE))
													.addContainerGap(
															GroupLayout.DEFAULT_SIZE,
															Short.MAX_VALUE)));
				}

				// ======== savedConnectionsPanel ========
				{
					savedConnectionsPanel.setBorder(new TitledBorder(
							"Saved Connections"));

					// ======== scrollPane1 ========
					{

						// ---- savedConnectionsList ----
						savedConnectionsList.setModel(getModel());
						scrollPane1.setViewportView(savedConnectionsList);
					}

					// ---- savedLoadButton ----
					savedLoadButton.setText("Load");
					savedLoadButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							savedLoadButtonActionPerformed(e);
						}
					});

					// ---- savedSaveButton ----
					savedSaveButton.setText("Save");
					savedSaveButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							savedSaveButtonActionPerformed(e);
							savedConnectionsList.setModel(connectionsListModel);
						}
					});

					// ---- savedRemoveButton ----
					savedRemoveButton.setText("Remove");
					savedRemoveButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							savedRemoveButtonActionPerformed(e);
						}
					});

					GroupLayout savedConnectionsPanelLayout = new GroupLayout(
							savedConnectionsPanel);
					savedConnectionsPanel
							.setLayout(savedConnectionsPanelLayout);
					savedConnectionsPanelLayout
							.setHorizontalGroup(savedConnectionsPanelLayout
									.createParallelGroup()
									.addGroup(
											savedConnectionsPanelLayout
													.createSequentialGroup()
													.addContainerGap()
													.addComponent(
															scrollPane1,
															GroupLayout.DEFAULT_SIZE,
															146,
															Short.MAX_VALUE)
													.addGroup(
															savedConnectionsPanelLayout
																	.createParallelGroup()
																	.addGroup(
																			savedConnectionsPanelLayout
																					.createParallelGroup()
																					.addGroup(
																							savedConnectionsPanelLayout
																									.createSequentialGroup()
																									.addGap(160,
																											160,
																											160)
																									.addComponent(
																											savedLoadButton,
																											GroupLayout.PREFERRED_SIZE,
																											100,
																											GroupLayout.PREFERRED_SIZE))
																					.addGroup(
																							GroupLayout.Alignment.TRAILING,
																							savedConnectionsPanelLayout
																									.createSequentialGroup()
																									.addPreferredGap(
																											LayoutStyle.ComponentPlacement.RELATED)
																									.addComponent(
																											savedSaveButton,
																											GroupLayout.PREFERRED_SIZE,
																											100,
																											GroupLayout.PREFERRED_SIZE)))
																	.addGroup(
																			GroupLayout.Alignment.TRAILING,
																			savedConnectionsPanelLayout
																					.createSequentialGroup()
																					.addPreferredGap(
																							LayoutStyle.ComponentPlacement.RELATED)
																					.addComponent(
																							savedRemoveButton,
																							GroupLayout.PREFERRED_SIZE,
																							100,
																							GroupLayout.PREFERRED_SIZE)))
													.addContainerGap()));
					savedConnectionsPanelLayout
							.setVerticalGroup(savedConnectionsPanelLayout
									.createParallelGroup()
									.addGroup(
											savedConnectionsPanelLayout
													.createSequentialGroup()
													.addContainerGap()
													.addGroup(
															savedConnectionsPanelLayout
																	.createParallelGroup()
																	.addComponent(
																			scrollPane1,
																			GroupLayout.DEFAULT_SIZE,
																			168,
																			Short.MAX_VALUE)
																	.addGroup(
																			savedConnectionsPanelLayout
																					.createSequentialGroup()
																					.addComponent(
																							savedLoadButton)
																					.addGap(18,
																							18,
																							18)
																					.addComponent(
																							savedSaveButton)
																					.addGap(18,
																							18,
																							18)
																					.addComponent(
																							savedRemoveButton)
																					.addGap(0,
																							66,
																							Short.MAX_VALUE)))
													.addContainerGap()));
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout
						.createParallelGroup()
						.addComponent(connectionPanel,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(savedConnectionsPanel,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
				contentPanelLayout.setVerticalGroup(contentPanelLayout
						.createParallelGroup().addGroup(
								contentPanelLayout
										.createSequentialGroup()
										.addComponent(connectionPanel,
												GroupLayout.PREFERRED_SIZE, 76,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(savedConnectionsPanel,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] {
						0, 85, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {
						1.0, 0.0, 0.0 };

				// ---- okButton ----
				okButton.setText("Connect");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0,
						0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Hamant Joucoo
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel connectionPanel;
	private JLabel connectionHostLabel;
	private static JTextField connectionHostTextField;
	private JLabel connectionPasswordLabel;
	private static JTextField connectionUsernameTextField;
	private JLabel connectionUsernameLabel;
	private static JPasswordField connectionPasswordField;
	private JPanel savedConnectionsPanel;
	private JScrollPane scrollPane1;
	private static JList<String> savedConnectionsList;
	private JButton savedLoadButton;
	private JButton savedSaveButton;
	private JButton savedRemoveButton;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private static DefaultListModel<String> connectionsListModel;

	// Method: getModel
	public static DefaultListModel getModel() {
		DefaultListModel model = new DefaultListModel();
		List<String> lines = new ArrayList<String>();
		if (Variables.connectionNames.length <= 1) {
			try {
				String path = System.getProperty("user.home")
						+ File.separator + "Documents";
				path += File.separator + "CMUtils";
				File customDir = new File(path);
				if (customDir.exists()) {
					String path2 = System.getProperty("user.home")
							+ File.separator + "Documents";
					path += File.separator + "CMUtils";
					File customDir2 = new File(path + "Connections.txt");
					if (customDir2.exists()) {
						try {
							FileReader inputFile = new FileReader(
									customDir2);
							BufferedReader bufferReader = new BufferedReader(
									inputFile);
							String line;
							while ((line = bufferReader.readLine()) != null) {
								Variables.connectionsString = line
										.toString();
								lines.add(line.toString());
							}
							bufferReader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (customDir2.createNewFile()) {
						try {
							FileWriter inputFile = new FileWriter(
									customDir2, true);
							BufferedWriter bufferWriter = new BufferedWriter(
									inputFile);
							bufferWriter
									.write("<Connection>"
											+ "<name>"
											+ "Default"
											+ "</name>"
											+ "<host>"
											+ connectionHostTextField
													.getText()
											+ "</host>"
											+ "<username>"
											+ connectionUsernameTextField
													.getText()
											+ "</username>"
											+ "<password>"
											+ new String(
													connectionPasswordField
															.getPassword())
											+ "</password>"
											+ "</Connection>\n");
							bufferWriter.close();
							try {
								FileReader inputFile2 = new FileReader(
										customDir2);
								BufferedReader bufferReader = new BufferedReader(
										inputFile2);
								String line;
								while ((line = bufferReader.readLine()) != null) {
									Variables.connectionsString = line
											.toString();
									lines.add(line.toString());
								}
								bufferReader.close();
							} catch (Exception e) {
								e.printStackTrace();
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						System.out.println("Failed to create file");
					}
				} else if (customDir.mkdirs()) {
					String path2 = System.getProperty("user.home")
							+ File.separator + "Documents";
					path += File.separator + "CMUtils";
					File customDir2 = new File(path + "Connections.txt");
					if (customDir2.exists()) {
						try {
							FileReader inputFile = new FileReader(
									customDir2);
							BufferedReader bufferReader = new BufferedReader(
									inputFile);
							String line;
							while ((line = bufferReader.readLine()) != null) {
								Variables.connectionsString = line
										.toString();
								lines.add(line.toString());
							}
							bufferReader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (customDir2.createNewFile()) {
						try {
							FileWriter inputFile = new FileWriter(
									customDir2, true);
							BufferedWriter bufferWriter = new BufferedWriter(
									inputFile);
							bufferWriter
									.write("<Connection>"
											+ "<name>"
											+ "Default"
											+ "</name>"
											+ "<host>"
											+ connectionHostTextField
													.getText()
											+ "</host>"
											+ "<username>"
											+ connectionUsernameTextField
													.getText()
											+ "</username>"
											+ "<password>"
											+ new String(
													connectionPasswordField
															.getPassword()) + "</password>"
									+ "</Connection>\n");
							bufferWriter.close();
							try {
								FileReader inputFile2 = new FileReader(
										customDir2);
								BufferedReader bufferReader = new BufferedReader(
										inputFile2);
								String line;
								while ((line = bufferReader.readLine()) != null) {
									Variables.connectionsString = line
											.toString();
									lines.add(line.toString());
								}
								bufferReader.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						System.out.println("Failed to create file");
					}
				} else {
					System.out.println(customDir + " was not created");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String details = lines.toString();
			Variables.connections = Methods.substringsBetween(details,
					"<Connection>", "</Connection>");
			Variables.connectionDetails = new String[Variables.connections.length][4];
			for (int i = 0; i < Variables.connections.length; i++) {
				Variables.connectionDetails[i][0] = Methods.substringBetween(
						Variables.connections[i], "<host>", "</host>");
				Variables.connectionDetails[i][1] = Methods.substringBetween(
						Variables.connections[i], "<username>", "</username>");
				Variables.connectionDetails[i][2] = Methods.substringBetween(
						Variables.connections[i], "<password>", "</password>");
				Variables.connectionDetails[i][3] = Methods.substringBetween(
						Variables.connections[i], "<name>", "</name>");
				model.addElement(Variables.connectionDetails[i][3]);
			}
			Variables.connectionHostnames = new String[Variables.connections.length];
			Variables.connectionUsernames = new String[Variables.connections.length];
			Variables.connectionPasswords = new String[Variables.connections.length];
			Variables.connectionNames = new String[Variables.connections.length];
			for (int i = 0; i < Variables.connections.length; i++) {
				Variables.connectionHostnames[i] = Variables.connectionDetails[i][0];
				Variables.connectionUsernames[i] = Variables.connectionDetails[i][1];
				Variables.connectionPasswords[i] = Variables.connectionDetails[i][2];
				Variables.connectionNames[i] = Variables.connectionDetails[i][3];
			}
		}
		for (int i = 0; i < model.getSize(); i++) {
			System.out.println(model.getElementAt(i));
		}
		return model;
	}

	public static boolean setConnectionsModel() {
		DefaultListModel model = new DefaultListModel<String>() {
			private static final long serialVersionUID = -4256579372683350582L;

			public String[] getValues() {
				List<String> lines = new ArrayList<String>();
				if (Variables.connectionNames.length <= 1) {
					try {
						String path = System.getProperty("user.home")
								+ File.separator + "Documents";
						path += File.separator + "CMUtils";
						File customDir = new File(path);
						if (customDir.exists()) {
							String path2 = System.getProperty("user.home")
									+ File.separator + "Documents";
							path += File.separator + "CMUtils";
							File customDir2 = new File(path + "Connections.txt");
							if (customDir2.exists()) {
								try {
									FileReader inputFile = new FileReader(
											customDir2);
									BufferedReader bufferReader = new BufferedReader(
											inputFile);
									String line;
									while ((line = bufferReader.readLine()) != null) {
										Variables.connectionsString = line
												.toString();
										lines.add(line.toString());
									}
									bufferReader.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (customDir2.createNewFile()) {
								try {
									FileWriter inputFile = new FileWriter(
											customDir2, true);
									BufferedWriter bufferWriter = new BufferedWriter(
											inputFile);
									bufferWriter
											.write("<Connection>"
													+ "<name>"
													+ "Default"
													+ "</name>"
													+ "<host>"
													+ connectionHostTextField
															.getText()
													+ "</host>"
													+ "<username>"
													+ connectionUsernameTextField
															.getText()
													+ "</username>"
													+ "<password>"
													+ new String(
															connectionPasswordField
																	.getPassword())
													+ "</password>"
													+ "</Connection>\n");
									bufferWriter.close();
									try {
										FileReader inputFile2 = new FileReader(
												customDir2);
										BufferedReader bufferReader = new BufferedReader(
												inputFile2);
										String line;
										while ((line = bufferReader.readLine()) != null) {
											Variables.connectionsString = line
													.toString();
											lines.add(line.toString());
										}
										bufferReader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}

								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								System.out.println("Failed to create file");
							}
						} else if (customDir.mkdirs()) {
							String path2 = System.getProperty("user.home")
									+ File.separator + "Documents";
							path += File.separator + "CMUtils";
							File customDir2 = new File(path + "Connections.txt");
							if (customDir2.exists()) {
								try {
									FileReader inputFile = new FileReader(
											customDir2);
									BufferedReader bufferReader = new BufferedReader(
											inputFile);
									String line;
									while ((line = bufferReader.readLine()) != null) {
										Variables.connectionsString = line
												.toString();
										lines.add(line.toString());
									}
									bufferReader.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (customDir2.createNewFile()) {
								try {
									FileWriter inputFile = new FileWriter(
											customDir2, true);
									BufferedWriter bufferWriter = new BufferedWriter(
											inputFile);
									bufferWriter
											.write("<Connection>"
													+ "<name>"
													+ "Default"
													+ "</name>"
													+ "<host>"
													+ connectionHostTextField
															.getText()
													+ "</host>"
													+ "<username>"
													+ connectionUsernameTextField
															.getText()
													+ "</username>"
													+ "<password>"
													+ new String(
															connectionPasswordField
																	.getPassword())
													+ "</password>"
													+ "</Connection>\n");
									bufferWriter.close();
									try {
										FileReader inputFile2 = new FileReader(
												customDir2);
										BufferedReader bufferReader = new BufferedReader(
												inputFile2);
										String line;
										while ((line = bufferReader.readLine()) != null) {
											Variables.connectionsString = line
													.toString();
											lines.add(line.toString());
										}
										bufferReader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								System.out.println("Failed to create file");
							}
						} else {
							System.out.println(customDir + " was not created");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					String details = lines.toString();
					Variables.connections = Methods.substringsBetween(details,
							"<Connection>", "</Connection>");
					Variables.connectionDetails = new String[Variables.connections.length][4];
					for (int i = 0; i < Variables.connections.length; i++) {
						Variables.connectionDetails[i][0] = Methods
								.substringBetween(Variables.connections[i],
										"<host>", "</host>");
						Variables.connectionDetails[i][1] = Methods
								.substringBetween(Variables.connections[i],
										"<username>", "</username>");
						Variables.connectionDetails[i][2] = Methods
								.substringBetween(Variables.connections[i],
										"<password>", "</password>");
						Variables.connectionDetails[i][3] = Methods
								.substringBetween(Variables.connections[i],
										"<name>", "</name>");
					}
					Variables.connectionHostnames = new String[Variables.connections.length];
					Variables.connectionUsernames = new String[Variables.connections.length];
					Variables.connectionPasswords = new String[Variables.connections.length];
					Variables.connectionNames = new String[Variables.connections.length];
					for (int i = 0; i < Variables.connections.length; i++) {
						Variables.connectionHostnames[i] = Variables.connectionDetails[i][0];
						Variables.connectionUsernames[i] = Variables.connectionDetails[i][1];
						Variables.connectionPasswords[i] = Variables.connectionDetails[i][2];
						Variables.connectionNames[i] = Variables.connectionDetails[i][3];
					}
				}
				return Variables.connectionNames;
			}

			@Override
			public int getSize() {
				return getValues().length;
			}

			@Override
			public String getElementAt(int i) {
				return getValues()[i];
			}
		};
		try {
			savedConnectionsList.setModel(connectionsListModel);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public boolean removeLineFromFile(String file, String lineToRemove) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return false;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.trim().equals(lineToRemove)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return false;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile)) {
				System.out.println("Could not rename file");
				return false;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public class testConnectionThread extends Thread {
		private String threadName;
		private boolean connected;
		private Thread thread;

		testConnectionThread(String name) {
			threadName = name;
			connected = Variables.connected;
		}

		public void run() {
			System.out.println("Running Connection!");
			CMUtilsGUI gui = new CMUtilsGUI();
			progressBarConnect newBar = new progressBarConnect(
					"Please while an AXL Connection to " + Variables.ip
							+ " is established");
			newBar.setVisible(true);
			if (Methods.testConnection()) {
				Variables.connected = true;
				Variables.connectionFailed = false;
				if (ConnectGUI.this.isVisible()) {
					ConnectGUI.this.setVisible(false);
				}
				if (!gui.isVisible()) {
					gui.setVisible(true);
				}
			} else {
				Variables.connected = false;
				Variables.connectionFailed = true;
				newBar.setVisible(false);
				JOptionPane
						.showMessageDialog(
								ConnectGUI.this.contentPanel,
								"Connection failed, check your connection and try again!",
								"Connection Error", 0);
			}
			newBar.setVisible(false);
		}

		public void start() {
			if (thread == null) {
				thread = new Thread(this, threadName);
				System.out.println("Started Connection Establishment");
				thread.start();
			}
		}
	}

	public final class progressBarConnect extends JFrame {
		private static final long serialVersionUID = 6374162146237387106L;
		private JLabel a;
		private JProgressBar b;

		public progressBarConnect(String text) {
			progressBarConnect localw = this;
			this.a = new JLabel();
			localw.b = new JProgressBar();
			localw.setTitle("CMUtils - New Query");
			localw.setResizable(false);
			localw.setAlwaysOnTop(true);
			localw.setDefaultCloseOperation(0);
			Container localContainer = localw.getContentPane();
			localw.a.setText(text);
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
