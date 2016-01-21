package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.api.output.Fixture;
import com.kevinisabelle.dmxlive.core.factory.FixtureFactory;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;

/**
 *
 * @author kevin
 */
public class ScriptCommand {

  public static final String FIELD_DELIMITER = "\\|";

  public static final String SUBSCRIPT_CALL_START = "$";

  public static final String SUBSCRIPT_PARAM_START = "\\(";

  public static final String SUBSCRIPT_PARAM_END = "\\)";

  public static final String PARAMS_DELIMITER = ",";

  public static final String TIME_DELIMITER = ":";

  private TimeInfo time;

  private String scriptName;

  private String[] parameters;

  private List<Fixture> fixtures;

  private String actionParameters;

  private Script caller;

  public ScriptCommand(String commandTxt, Script caller) {

    String[] params = commandTxt.replaceAll("\t", "").replaceAll(" ", "").split(FIELD_DELIMITER);
    time = new TimeInfo(params[0]);
    time.setMeasure(time.getMeasure() - 1);
    this.caller = caller;

    if(params[1].startsWith(SUBSCRIPT_CALL_START)) {

      if(params[1].contains("(")) {

        String[] scriptNameAndParams = params[1].replaceAll("\\$", "").replaceAll(SUBSCRIPT_PARAM_END, "").split(SUBSCRIPT_PARAM_START);

        scriptName = scriptNameAndParams[0];
        parameters = scriptNameAndParams[1].split(PARAMS_DELIMITER);

      }
      else {

        scriptName = params[1].replaceAll("\\$", "");
      }

    }
    else {

      if(params[1].contains(TIME_DELIMITER)) {

        String[] fixtureNames = params[1].split(TIME_DELIMITER);
        fixtures = new ArrayList<Fixture>();

        for(String fixtureName : fixtureNames) {
          fixtures.add(getFixture(fixtureName));
        }

      }
      else {
        fixtures = Arrays.asList(getFixture(params[1]));
      }
    }

    if(params.length > 2) {
      actionParameters = params[2];
    }
    else {
      actionParameters = null;
    }
  }

  public ScriptCommand(String scriptName, List<Fixture> fixtures, String actionParameter) {
    this.scriptName = scriptName;
    this.fixtures = fixtures;
    this.actionParameters = actionParameter;
  }

  private Fixture getFixture(String fixtureParam) {

    String fixture = fixtureParam;

    if(caller.getMasterScriptReference().getVariables().containsKey(fixtureParam)) {
      fixture = caller.getMasterScriptReference().getVariables().get(fixtureParam);
    }

    return null;//FixtureFactory.getFixture(fixture);
  }

  public List<TimedDmxEvent> getTimedDmxValues(TimeInfo timeOffset, TimeSignature signature, int bpm, String[] parameters) {

    ArrayList list = new ArrayList();

    if(scriptName != null) {

      try {

        int numberOfTime = 1;
        TimeInfo duration = new TimeInfo("1:0:0");

        if(actionParameters != null) {
          String[] params = actionParameters.split(",");
          numberOfTime = Integer.parseInt(params[0]);
          duration = new TimeInfo(params[1]);
        }

        TimeInfo currentTime = time.add(timeOffset, signature);

        for(int j = 0; j < numberOfTime; j++) {

          scriptName = scriptName.replaceAll(" ", "");

          list.addAll(caller.getMasterScriptReference().getNamedScripts().get(scriptName).getTimedDmxEvents(currentTime, signature, bpm, this.parameters));

          currentTime = currentTime.add(duration, signature);
        }

      }
      catch(Exception e) {
        //JOptionPane.showMessageDialog(null, "Cannot find sub-script named: " + scriptName + ", " + e.getMessage());
      }

    }
    else if(fixtures != null && fixtures.size() > 0) {

      String scriptParametersReplaced = actionParameters;

      if(scriptParametersReplaced == null) {
        scriptParametersReplaced = "";
      }

      if(parameters != null) {
        // Replace parameters with actual params
        int i = 1;
        for(String param : parameters) {
          scriptParametersReplaced = scriptParametersReplaced.replaceAll("%" + i, param);
          i++;
        }
      }

      for(Fixture fixture : fixtures) {
        //list.addAll(fixture.convertToDmx(time.add(timeOffset, signature), signature, bpm, scriptParametersReplaced));
      }

    }

    return list;
  }

  /**
   * @return the script
   */
  public String getScriptName() {
    return scriptName;
  }

  /**
   * @param script the script to set
   */
  public void setScript(String script) {
    this.scriptName = script;
  }

  /**
   * @return the fixture
   */
  public List<Fixture> getFixtures() {
    return fixtures;
  }

  /**
   * @param fixture the fixture to set
   */
  public void setFixtures(List<Fixture> fixtures) {
    this.fixtures = fixtures;
  }

  /**
   * @return the actionParameters
   */
  public String getActionParameters() {
    return actionParameters;
  }

  /**
   * @param actionParameters the actionParameters to set
   */
  public void setActionParameters(String actionParameters) {
    this.actionParameters = actionParameters;
  }

  /**
   * @return the time
   */
  public TimeInfo getTime() {
    return time;
  }

  /**
   * @param time the time to set
   */
  public void setTime(TimeInfo time) {
    this.time = time;
  }
}
