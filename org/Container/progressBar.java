package org.Container;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle;

public final class progressBar extends JFrame {
	private static final long serialVersionUID = 6374162146237387106L;
	private JLabel a;
	private JProgressBar b;

	public progressBar() {
		progressBar localw = this;
		this.a = new JLabel();
		localw.b = new JProgressBar();
		localw.setTitle("CMUtils - Processing");
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