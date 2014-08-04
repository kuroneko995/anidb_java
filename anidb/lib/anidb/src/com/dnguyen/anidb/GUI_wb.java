package com.dnguyen.anidb;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.JTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.JXTreeTable;

public class GUI_wb extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_wb frame = new GUI_wb();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_wb() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnAddFiles = new JButton("Add files");
		menuBar.add(btnAddFiles);
		
		JButton btnConnect = new JButton("Connect");
		menuBar.add(btnConnect);
		
		JButton btnOpenFolder = new JButton("Open folder");
		menuBar.add(btnOpenFolder);
		
		JButton btnRehash = new JButton("Rehash");
		menuBar.add(btnRehash);
		
		JRadioButton rdbtnShowUnavailableFiles = new JRadioButton("Show unavailable files");
		menuBar.add(rdbtnShowUnavailableFiles);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea txtrLog = new JTextArea();
		contentPane.add(txtrLog, BorderLayout.SOUTH);
		
		
		table = new JXTreeTable();
		contentPane.add(table, BorderLayout.CENTER);
	}

}
