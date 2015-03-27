package com.kevinisabelle.dmxLive.ui;

import com.kevinisabelle.dmxlive.core.Constants;
import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.helper.UIHelper;
import com.kevinisabelle.dmxlive.core.scripting.Script;
import com.kevinisabelle.dmxlive.core.music.TimeSignature;
import com.kevinisabelle.dmxlive.dmxengine.processes.DmxRunnableObserver;
import com.kevinisabelle.dmxLive.ui.components.AppButtonsPanel;
import com.kevinisabelle.dmxLive.ui.components.fixtures.LumiLEDPar64Tester;
import com.kevinisabelle.dmxLive.ui.components.fixtures.LumiMKIITester;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author kevin
 */
public class DmxLiveFunctionsTesterWindow extends JFrame implements DmxRunnableObserver, ActionListener {
	
	private LumiLEDPar64Tester par64Panel;
	private LumiMKIITester mk2Panel;
	
	private TextArea miniScript = new TextArea("", 4, 80, TextArea.SCROLLBARS_BOTH);
	private JButton executeSriptButton = new JButton("Execute");
	private JCheckBox useLogCheckBox = new JCheckBox("Show DMX log");
	
	private TextArea log = new TextArea(10, 30);
	
	public DmxLiveFunctionsTesterWindow(){
		
		super("DMX Functions Tester - " + Constants.VERSION);
		
		DmxLive.enableDMXLogging(true);
		
		Font fixtureNameFont = new Font("Verdana", Font.BOLD, 20);
				
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 10));
		
		this.add(new AppButtonsPanel());
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_3, 10));
		
		par64Panel = new LumiLEDPar64Tester(3, this);		
		Label fixtureLabel = new Label("Fixture: Lumi LED Par 64");
		this.add(fixtureLabel);
		fixtureLabel.setFont(fixtureNameFont);
		this.add(par64Panel);
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));
		
		mk2Panel = new LumiMKIITester(11, this);		
		fixtureLabel = new Label("Fixture: Lumi MK-II LED Bar");
		this.add(fixtureLabel);
		fixtureLabel.setFont(fixtureNameFont);
		this.add(mk2Panel);
				
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));
		
		JPanel miniScriptPanel = new JPanel();
		miniScriptPanel.add(new Label("Mini Script:"));
		miniScriptPanel.add(miniScript);
		miniScriptPanel.add(executeSriptButton);
		miniScriptPanel.add(useLogCheckBox);
		useLogCheckBox.setPreferredSize(new Dimension(120, 20));
		this.add(miniScriptPanel);
		
		this.add(UIHelper.createColoredPanel(Constants.THEME_COLOR_1, 10));
				
		this.add(log);
		log.setForeground(Constants.THEME_COLOR_2);
		log.setBackground(Constants.THEME_COLOR_3);
		log.setFont(Constants.FONT_LOG);
		
		executeSriptButton.addActionListener(this);
		useLogCheckBox.addActionListener(this);
		
		useLogCheckBox.setSelected(DmxLive.isDMXLoggingEnabled());
				
	}

	@Override
	public void logMessage(String message, long millis) {
		log.setText(millis + "ms: " + message + "\n" + (log.getText().length() > 5000 ? log.getText().substring(0, 5000) : log.getText()));
	}

	@Override
	public void updatePosition(long millis) {
		
	}
	
		
	@Override
	public void stopNotify(boolean isComplete) {
		
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == executeSriptButton){
			executeMiniscript(miniScript.getText());
		} else if (e.getSource() == useLogCheckBox){
			DmxLive.enableDMXLogging(useLogCheckBox.isSelected());
		}
	}
	
	private void executeMiniscript(String script){
		DmxLive.startScene(new TimeSignature("4/4"), 120, new Script(script), this);
	}

	@Override
	public void openFile(String filename) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
