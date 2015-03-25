package com.kevinisabelle.dmxLive.ui.components;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author kisabelle
 */
public class AppButtonsPanel extends JPanel implements ActionListener {

	private final JButton helpButton = new JButton("Help...");
	private final JButton configButton = new JButton("DMX Live configuration...");
	
	public AppButtonsPanel(){
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(Box.createHorizontalStrut(5));
		this.add(new JLabel(new ImageIcon( Constants.LOGO_IMAGE )));
		this.add(Box.createHorizontalStrut(5));
		this.add(configButton);
		this.add(Box.createHorizontalStrut(5));
		this.add(helpButton);
		this.add(Box.createHorizontalStrut(5));
		
		configButton.addActionListener(this);
		helpButton.addActionListener(this);
		
		configButton.setPreferredSize(new Dimension(180, 25));
		helpButton.setPreferredSize(new Dimension(80, 25));
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == configButton){
			DmxLive.showConfiguration();
		} else if (e.getSource() == helpButton){
			DmxLive.showHelp();
		}
	}
	
}
