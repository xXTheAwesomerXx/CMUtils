package org.CMUtils;

import java.awt.*;
import java.awt.event.*;
import java.io.StringReader;

import javax.swing.*;

import org.Container.Methods;
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
				SAXBuilder sax = new SAXBuilder();
				String xml = Methods.executeSQLSelect(sqlField.getText().toString());
				String test = "<test><message1>Message</message1><message2>message2</message2></test>";
				System.out.println("SQLSelect: " + xml);
				Document doc = sax.build(new StringReader(test));
				String message = doc.getRootElement().getText();
				System.out.println("Message: " + message.toString());
				outputArea.setText(message.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void excecuteButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void sqlComboBoxItemStateChanged(ItemEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Hamant Joucoo
		sqlField = new JTextField();
		excecuteButton = new JButton();
		scrollPane1 = new JScrollPane();
		outputArea = new JTextArea();
		sqlComboBox = new JComboBox<>();

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

		// ======== scrollPane1 ========
		{
			scrollPane1.setViewportView(outputArea);
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
																scrollPane1,
																GroupLayout.DEFAULT_SIZE,
																780,
																Short.MAX_VALUE)
														.addGroup(
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
																				excecuteButton)))
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
										.addComponent(scrollPane1,
												GroupLayout.DEFAULT_SIZE, 364,
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
	private JScrollPane scrollPane1;
	private JTextArea outputArea;
	private JComboBox<String> sqlComboBox;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}