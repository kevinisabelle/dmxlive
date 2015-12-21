package com.kevinisabelle.dmxLive.ui.components;


import com.kevinisabelle.dmxlive.api.output.dmx.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author kisabelle
 */
public class HelpDialog extends JDialog {

	private final TextArea instructions;

	public HelpDialog(JFrame owner) {

		super(owner, "Help - DMX Live", true);
		
		BufferedReader br;
		StringBuilder builder = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader("./conf/instructions.txt"));

			String line;

			builder.append("-------- Available color names ----------\n\n");
			
			for (Color.ColorEnum color : Color.ColorEnum.values()){
				builder.append(color).append("\n");
			}

			builder.append("\n");
			
			while ((line = br.readLine()) != null) {
				builder.append(line).append("\n");
			}
					
			
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Couln't open help file: " + ex.toString());
		}
		
		this.setLayout(new FlowLayout());
		
		instructions = new TextArea(builder.toString(), 20, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
		instructions.setFont(new Font("Helvetica", Font.PLAIN, 14));
		instructions.setEditable(false);
		instructions.setBackground(java.awt.Color.white);
		this.add(instructions);
	}
}
