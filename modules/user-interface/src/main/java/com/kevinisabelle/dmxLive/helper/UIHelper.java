package com.kevinisabelle.dmxLive.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 * Helper class to create UI stuff.
 * @author kisabelle
 */
public class UIHelper {
	
	protected UIHelper(){
		
	}
	
	public static JPanel createColoredPanel(Color color, int height){
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(color);
		panel.setSize(200, height);
		panel.setMaximumSize(new Dimension(1300, height));
		panel.setMinimumSize(new Dimension(200, height));
		return panel;
	}
}
