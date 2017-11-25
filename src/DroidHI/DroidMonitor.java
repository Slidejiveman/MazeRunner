package DroidHI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DroidPD.Droid;
import DroidPD.Droid.Status;
import mazeHI.MazeGridPanel;
import mazePD.Maze;
import mazePD.Maze.MazeMode;
import javax.swing.JButton;
import java.awt.SystemColor;

public class DroidMonitor extends JFrame {

	private JPanel contentPane, panel;
	private JTextField textField;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void altmain(String[] args) {
		Droid droid = new Droid("R9-D4");
		//Maze maze = new Maze(10, 2, MazeMode.TEST);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					DroidMonitor frame = new DroidMonitor(droid);
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
	public DroidMonitor(Droid droid) {
		getContentPane().setLayout(null);
		
		JTextField textField_1, textField_4;
		JLabel lblStatus, lbllevel, lblDim, lblLevels, lblName;
		JComboBox comboBox;
		//JPanel panel;
		MazeGridPanel mazeGridPanel = new MazeGridPanel();
		mazeGridPanel.setBackground(SystemColor.textHighlight);
		JButton btnRunMaze, btnCreateMaze;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setBounds(77, 208, 56, 22);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(77, 243, 56, 22);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblCoords = new JLabel("Coords:");
		lblCoords.setBounds(12, 211, 56, 16);
		contentPane.add(lblCoords);
		
		JLabel lblMoves = new JLabel("Moves:");
		lblMoves.setBounds(12, 246, 56, 16);
		contentPane.add(lblMoves);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int level = Integer.parseInt(comboBox.getSelectedItem().toString());
				
				mazeGridPanel.update(droid, level);
				revalidate();
				repaint();
			}
		});
		comboBox.setBounds(77, 285, 56, 22);
		contentPane.add(comboBox);
		
		mazeGridPanel.setBounds(221, 42, 332, 367);
		contentPane.add(mazeGridPanel);
		
		lblStatus = new JLabel("Status: ");
		lblStatus.setBounds(175, 13, 234, 16);
		contentPane.add(lblStatus);
		
		lbllevel = new JLabel("Level: ");
		lbllevel.setBounds(12, 288, 56, 16);
		contentPane.add(lbllevel);
		
		btnRunMaze = new JButton("Run Maze");
		btnRunMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Run is in a new thread
				new Thread(new Runnable() {
					public void run() {
						droid.enterMaze(droid.getMaze());
						mazeGridPanel.setLayoutSize(droid.getMazeSize());
						
						try{
							droid.searchLevel();
						} 
						catch(Exception e) 
						{
							System.err.println("I am Error.");
						}
						 }
					}).start();
			}
		});
		btnRunMaze.setBounds(58, 358, 97, 25);
		contentPane.add(btnRunMaze);
		
		btnCreateMaze = new JButton("Create Maze");
		btnCreateMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int levels = Integer.parseInt(textField_2.getText());
				Maze maze = new Maze(Integer.parseInt(textField.getText()), 
						levels, MazeMode.NORMAL);
				droid.setMaze(maze);
				
				comboBox.removeAllItems();
				for(int i = 0; i < levels; i++) {
					comboBox.addItem("" + i);
				}
			}
		});
		btnCreateMaze.setBounds(58, 146, 105, 25);
		contentPane.add(btnCreateMaze);
		
		lblDim = new JLabel("Dim:");
		lblDim.setBounds(12, 50, 56, 16);
		contentPane.add(lblDim);
		
		textField = new JTextField();
		textField.setBounds(77, 50, 56, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		lblLevels = new JLabel("Levels:");
		lblLevels.setBounds(12, 95, 56, 16);
		contentPane.add(lblLevels);
		
		textField_2 = new JTextField();
		textField_2.setBounds(76, 92, 56, 22);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		lblName = new JLabel("Name");
		lblName.setBounds(31, 13, 56, 16);
		contentPane.add(lblName);
		
		droid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Fine, JEEZ!");
				textField_1.setText(droid.getCurrentCoordinates().toString());
				textField_4.setText(new Integer(droid.getMoves()).toString());
				lblStatus.setText("Status : "+droid.getStatus());
				lblName.setText(droid.getName());
				if (droid.getStatus()== Status.LASTSEARCHSUCCESS)
					comboBox.setEditable(true);
				int displayLevel = droid.getCurrentCoordinates().getZ();
				//panel.removeAll();
				mazeGridPanel.update(droid, displayLevel);
				if(!comboBox.getSelectedItem().equals("" + displayLevel)) {
					comboBox.setSelectedItem("" + displayLevel);
				}
				//mazeGridPanel.setBounds(200, 50, 300, 300);
				//panel.add(mazeGridPanel);
				revalidate();
				repaint();
			}
		});
	}
}

