/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinisabelle.dmxLive.ui.components.fixtures;

import com.kevinisabelle.dmxLive.DmxLive;
import com.kevinisabelle.dmxLive.objects.Script;
import com.kevinisabelle.dmxLive.objects.TimeSignature;
import com.kevinisabelle.dmxLive.objects.fixtures.Color;
import com.kevinisabelle.dmxLive.objects.fixtures.LumiMKII;
import com.kevinisabelle.dmxLive.ui.DmxLiveFunctionsTesterWindow;
import java.awt.Button;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author kisabelle
 */
public class LumiMKIITester extends JPanel implements ActionListener {
	
	private DmxLiveFunctionsTesterWindow parent = null;
	private LumiMKII fixture = new LumiMKII();
	
	private Label dimLabel = new Label("Dim Function");
	private Label strobeLabel = new Label("Strobe Function");
	private Label modeLabel = new Label("Mode Function");
	private Label fadeLabel = new Label("Fade Function");
		
	private JComboBox dimColorCombo = new JComboBox(Color.ColorEnum.values());
	private TextField dimDimmerValue = new TextField("100%", 5);
	private Button dimExecuteButton = new Button("Execute Dim");
	
	private JComboBox strobeColorCombo = new JComboBox(Color.ColorEnum.values());
	private TextField strobeDimmerValue = new TextField("100%", 5);
	private TextField strobeSpeedValue = new TextField("98%", 5);
	private Button strobeExecuteButton = new Button("Execute Strobe");
	
	private JComboBox modeColorCombo = new JComboBox(Color.ColorEnum.values());
	private TextField modeDimmerValue = new TextField("100%", 5);
	private TextField modeSpeedValue = new TextField("98%", 5);
	private JComboBox modeNameCombo = new JComboBox(new String[]{"grad", "mixed", "mode1", "mode2", "mode3", "mode4", "mode5", "mode6", "mode7"});
	private Button modeExecuteButton = new Button("Execute Mode");
			
	private JComboBox fadeColor1Combo = new JComboBox(Color.ColorEnum.values());
	private JComboBox fadeColor2Combo = new JComboBox(Color.ColorEnum.values());
	private TextField fadeDimmerValue1 = new TextField("0%", 5);
	private TextField fadeDimmerValue2 = new TextField("100%", 5);
	private TextField fadeExecutionTime = new TextField("1:0:0", 5);
	private Button fadeExecuteButton = new Button("Execute Fade");
	
	public LumiMKIITester(int channel, DmxLiveFunctionsTesterWindow parent){
		
		super();
		
		this.parent = parent;
		fixture.setChannel(channel);

		Font functionNameFont = new Font("Verdana", Font.BOLD, 14);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		dimLabel.setFont(functionNameFont);		
		JPanel dimFunctionPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
		dimFunctionPanel.add(dimLabel);
		dimFunctionPanel.add(dimColorCombo);
		dimFunctionPanel.add(new Label("Dimmer %: "));
		dimFunctionPanel.add(dimDimmerValue);
		dimFunctionPanel.add(dimExecuteButton);
		this.add(dimFunctionPanel);
		
		
		strobeLabel.setFont(functionNameFont);
		JPanel strobeFunctionPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
		strobeFunctionPanel.add(strobeLabel);
		strobeFunctionPanel.add(strobeColorCombo);
		strobeFunctionPanel.add(new Label("Dimmer %: "));
		strobeFunctionPanel.add(strobeDimmerValue);
		strobeFunctionPanel.add(new Label("Speed %: "));
		strobeFunctionPanel.add(strobeSpeedValue);
		strobeFunctionPanel.add(strobeExecuteButton);
		this.add(strobeFunctionPanel);
		
		
		modeLabel.setFont(functionNameFont);
		JPanel modeFunctionPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
		modeFunctionPanel.add(modeLabel);
		modeFunctionPanel.add(modeColorCombo);
		modeFunctionPanel.add(new Label("Dimmer %: "));
		modeFunctionPanel.add(modeDimmerValue);
		modeFunctionPanel.add(new Label("Speed %: "));
		modeFunctionPanel.add(modeSpeedValue);
		modeFunctionPanel.add(modeNameCombo);
		modeFunctionPanel.add(modeExecuteButton);
		this.add(modeFunctionPanel);
		
		
		fadeLabel.setFont(functionNameFont);
		JPanel fadeFunctionPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
		fadeFunctionPanel.add(fadeLabel);
		fadeFunctionPanel.add(new Label("Color from: "));
		fadeFunctionPanel.add(fadeColor1Combo);
		fadeFunctionPanel.add(new Label("to: "));
		fadeFunctionPanel.add(fadeColor2Combo);
		fadeFunctionPanel.add(new Label("Dimmer % from: "));
		fadeFunctionPanel.add(fadeDimmerValue1);
		fadeFunctionPanel.add(new Label("to: "));
		fadeFunctionPanel.add(fadeDimmerValue2);
		fadeFunctionPanel.add(new Label("Execution time: "));
		fadeFunctionPanel.add(fadeExecutionTime);
		fadeFunctionPanel.add(fadeExecuteButton);
		this.add(fadeFunctionPanel);
		
		dimExecuteButton.addActionListener(this);
		strobeExecuteButton.addActionListener(this);
		modeExecuteButton.addActionListener(this);
		fadeExecuteButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dimExecuteButton){
			executeDim();
		} else if (e.getSource() == strobeExecuteButton){
			executeStrobe();
		} else if (e.getSource() == modeExecuteButton){
			executeMode();
		} else if (e.getSource() == fadeExecuteButton){
			executeFade();
		}
	}
		
	private void executeDim(){
		
		String scriptStr = "1:0:0|LEDBar_" + this.fixture.getChannel() + "|Dim:" + dimColorCombo.getSelectedItem() + "," + dimDimmerValue.getText();
		Script script = new Script(scriptStr);
		
		executeScript(script);
	}
	
	private void executeStrobe(){
		
		String scriptStr = "1:0:0|LEDBar_" + this.fixture.getChannel() + "|Strobe:" + strobeColorCombo.getSelectedItem() + "," + strobeDimmerValue.getText() + "," + strobeSpeedValue.getText();
		Script script = new Script(scriptStr);
		
		executeScript(script);
	}
	
	private void executeMode(){
		
		String scriptStr = "1:0:0|LEDBar_" + this.fixture.getChannel() + "|Mode:" + modeColorCombo.getSelectedItem() + "," + modeDimmerValue.getText() + "," + modeSpeedValue.getText() + "," + modeNameCombo.getSelectedItem();
		Script script = new Script(scriptStr);
		
		executeScript(script);
	}
	
	private void executeFade(){
		
		String scriptStr = "1:0:0|LEDBar_" + this.fixture.getChannel() + "|Fade:" + 
				fadeColor1Combo.getSelectedItem() + "_" + fadeColor2Combo.getSelectedItem() + "," + 
				fadeDimmerValue1.getText() + "_" + fadeDimmerValue2.getText() + "," + 
				fadeExecutionTime.getText().replaceAll("\\:", "_");
		Script script = new Script(scriptStr);
		
		executeScript(script);
	}
	
	private void executeScript(Script script){
		
		this.parent.logMessage("Executing: " + script.getScriptText(), 0);
		DmxLive.startScene(new TimeSignature("4/4"), 120, script, this.parent);
	}
}
