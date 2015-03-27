/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.ui.components;

import com.kevinisabelle.dmxLive.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.objects.Script;
import com.kevinisabelle.dmxLive.objects.Song;
import com.kevinisabelle.dmxLive.processes.DmxRunnableObserver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author kevin
 */
public class SongDisplay extends JPanel {

	private Song currentSong = null;
	private final Label songTitle = new Label("Current Song Title");
	private final JTextField currentPosition = new JTextField("1:0:0");
	private final Label songInfo = new Label("Stopped: 4/4 @ 120 bpm");
	private final JPanel scenesButtons = new JPanel(new FlowLayout());
	
	private final DmxRunnableObserver observer;

	public SongDisplay(DmxRunnableObserver observer) {

		this.observer = observer;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.black);
		
		songTitle.setAlignment(Label.CENTER);
		songTitle.setMaximumSize(new Dimension(1000, 30));
		songTitle.setBackground(Color.YELLOW);
				
		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));//)BoxLayout(positionPanel, BoxLayout.X_AXIS));
		
		songInfo.setAlignment(Label.CENTER);
		songInfo.setMaximumSize(new Dimension(1000, 30));
		
		currentPosition.setPreferredSize(new Dimension(120, 40));
		currentPosition.setHorizontalAlignment(JTextField.CENTER);
		currentPosition.setFont(Constants.FONT_SUPER_BIG);
		
		this.add(songTitle);
		
		positionPanel.add(currentPosition);
		positionPanel.add(songInfo);
		positionPanel.setFont(Constants.FONT_SUPER_BIG);
		
		this.add(positionPanel);

		this.add(scenesButtons);
		scenesButtons.setPreferredSize(new Dimension(100, 280));
		scenesButtons.setBackground(Color.black);
		
		updateUIFromSong();
	}

	public void setPosition(String position) {
		currentPosition.setText(position);
	}

	public void setStatus(String status) {
		songInfo.setText(status + ": " + 
					currentSong.getSignature().toString() + 
					" @ "+ 
					currentSong.getBpm() + 
					" bpm");
	}

	public void setSong(Song song) {
		this.currentSong = song;
		updateUIFromSong();
	}

	private void updateUIFromSong() {
		
		scenesButtons.removeAll();

		if (currentSong == null) {

			songTitle.setText("No song loaded");
			songInfo.setText("Info: N/A @ N/A bpm");


		} else {

			songTitle.setText(currentSong.getTitle());
			songInfo.setText("Stopped: " + 
					currentSong.getSignature().toString() + 
					" @ "+ 
					currentSong.getBpm() + 
					" bpm");			

			for (String namedScriptName : currentSong.getScript().getNamedScripts().keySet()) {
				
				if (namedScriptName.startsWith("_")) {
					
					JButton sceneButton = new JButton(namedScriptName.substring(1));
					
					sceneButton.setFont(new Font("Helvetica", Font.BOLD, 20));
					sceneButton.setBackground(Constants.THEME_COLOR_3);
					sceneButton.setForeground(Constants.THEME_COLOR_2);
					sceneButton.setBorder(BorderFactory.createLineBorder(Color.white, 5));
					sceneButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String scriptName = "_" + ((JButton)e.getSource()).getLabel();
							Script script = currentSong.getScript().getNamedScripts().get(scriptName);
							
							DmxLive.startScene(currentSong.getSignature(), currentSong.getBpm(), script, observer);
						}
					});
					scenesButtons.add(sceneButton);
					sceneButton.setPreferredSize(new Dimension(200, 50));
				}
			}
		}
		
		scenesButtons.revalidate();
		scenesButtons.repaint();

	}
}
