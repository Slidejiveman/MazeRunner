package mazeHI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import DroidPD.Droid;
import mazePD.Maze.Content;

import java.awt.SystemColor;
import java.awt.GridLayout;

public class MazeGridPanel extends JPanel {

	/**
	 * Create the panel.
	 * @param displayLevel 
	 * @param droid 
	 */
	
	public void setLayoutSize(int mazeSize) {
		setLayout(new GridLayout(mazeSize, mazeSize, 0, 0));
	}
	public MazeGridPanel() {
		setLayoutSize(10);
	}
	public void update(Droid droid, int displayLevel) {
		removeAll();
		
		//System.out.println("Me be here");
		for (int y=0;y<droid.getMazeSize();y++)
		{
			for (int x=0;x<droid.getMazeSize();x++)
			{
				String tacos = droid.getPositionContent(x, y, displayLevel);
				
				JPanel box = new JPanel();
								
				if (droid.getCurrentCoordinates().getX() == x && droid.getCurrentCoordinates().getY() == y
						&&droid.getCurrentCoordinates().getZ() == displayLevel) {
					box.setBackground(Color.CYAN);
				}
				else if(tacos.equals("[*]")) {
					box.setBackground(Color.BLACK); 
				} else if (tacos.equals("[ ]")) {
					box.setBackground(Color.WHITE);
				} else if (tacos.equals("[P]")) {
					box.setBackground(Color.ORANGE);
				} else if (tacos.equals("[E]")) {
					box.setBackground(Color.GREEN);
				} else if (tacos.equals("[?]")) {
					box.setBackground(Color.GRAY);
				} 
				add(box);
			}
		}
		
		setPreferredSize(new Dimension(300, 300));
		repaint();
		
	}
	
}
