package com.kevinisabelle.dmxLive.ui.components;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.objects.Configuration;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;

/**
 * Configuration window.
 *
 * @author kisabelle
 */
public class ConfigurationWindow extends JDialog implements ActionListener {

	private final JButton okButton = new JButton("OK");
	private final JButton cancelButton = new JButton("Cancel");
	private final JComboBox drummerAudioChannel = new JComboBox(DmxLive.getConfiguration().getMixers().toArray());
	private final JComboBox samplesAudioChannel = new JComboBox(DmxLive.getConfiguration().getMixers().toArray());
	private final TextField dmxRate = new TextField();
	private final TextField dmxRunRefreshRate = new TextField();
	private final TextField dmxRunOffset = new TextField();
	
	private static Logger logger = Logger.getLogger(ConfigurationWindow.class);

	public ConfigurationWindow(JFrame owner) {
		
		super(owner, "DMX Live configuration", true);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel optionsPanel = new JPanel(new GridLayout(5, 2));
		optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );
		
		optionsPanel.add(new Label("Drummer audio channel: "));
		optionsPanel.add(drummerAudioChannel);	
		drummerAudioChannel.setMinimumSize(new Dimension(300, 20));
				
		optionsPanel.add(new Label("Samples audio channel: "));
		optionsPanel.add(samplesAudioChannel);
				
		optionsPanel.add(new Label("DMX rate (in frames per second): "));
		optionsPanel.add(dmxRate);
				
		optionsPanel.add(new Label("DMX offset (in milliseconds): "));
		optionsPanel.add(dmxRunOffset);
				
		optionsPanel.add(new Label("DMX refresh rate (in numbers per beat): "));
		optionsPanel.add(dmxRunRefreshRate);
		
		this.add(optionsPanel);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		okButton.setPreferredSize(new Dimension(100, 30));
		cancelButton.setPreferredSize(new Dimension(100, 30));
		buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );
		this.add(buttonsPanel);

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		this.setPreferredSize(new Dimension(550, 250));
		
		updateInterfaceFromConfig();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			this.setVisible(false);
		} else if (e.getSource() == okButton) {
			updateConfigFromInterface();
			this.setVisible(false);
		}
	}
	
	private void updateInterfaceFromConfig(){
		
		dmxRate.setText(Integer.toString(DmxLive.getConfiguration().getDmxRate()));
		dmxRunOffset.setText(Integer.toString(DmxLive.getConfiguration().getDmxRunnableOffset()));
		dmxRunRefreshRate.setText(Integer.toString(DmxLive.getConfiguration().getDmxRunnableCheckResolution()));
		
		drummerAudioChannel.setSelectedItem(DmxLive.getConfiguration().getDrummerMixer().getMixerInfo());
		samplesAudioChannel.setSelectedItem(DmxLive.getConfiguration().getSamplesMixer().getMixerInfo());
	}
	
	private void updateConfigFromInterface(){
		
		Configuration config = DmxLive.getConfiguration();
		config.setDmxRate(Integer.parseInt(dmxRate.getText()));
		config.setDmxRunnableOffset(Integer.parseInt(dmxRunOffset.getText()));
		config.setDmxRunnableCheckResolution(Integer.parseInt(dmxRunRefreshRate.getText()));
		config.setDrummerMixer(AudioSystem.getMixer((Mixer.Info)drummerAudioChannel.getSelectedItem()));
		config.setSamplesMixer(AudioSystem.getMixer((Mixer.Info)samplesAudioChannel.getSelectedItem()));
		
		try {
			config.saveConfig();
		} catch (IOException e){
			logger.error("Cannot save configuration: " + e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Could not save configuration: " + e.toString());
		}
	}
}
