package com.kevinisabelle.dmxlive.core.scripting;

import com.kevinisabelle.dmxlive.api.output.dmx.TimedDmxEvent;
import com.kevinisabelle.dmxlive.music.TimeInfo;
import com.kevinisabelle.dmxlive.music.TimeSignature;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class Script {

	public static final String LINE_DELIMITER = "\n";

	public static final String VARIABLES_START = "$[";

	public static final String VARIABLES_SEPARATOR = "]=";

	public static final String SECTION_END = "end $";

	public static final String SECTION_END_V2 = "end$";

	public static final String SECTION_START = "$";

	public static final String COMMENTS_START = "//";

	public static final int MEASURE_FIELD = 0;

	public static final int FIXTURE_FIELD = 1;

	public static final int ACTION_FIELD = 2;

	private Map<String, Script> namedScripts;

	private List<ScriptCommand> commandsList;

	private Map<String, String> variables;

	private String scriptText = "";

	private Script masterScriptReference = null;

	public Script(String text) {
		this(text, null);
	}

	public Script(String text, Script masterScript) {

		scriptText = text;

		this.masterScriptReference = masterScript;

		if (this.masterScriptReference == null) {
			this.masterScriptReference = this;
		}

		parseScript();
	}

	public void setScriptText(String text) {
		this.scriptText = text;
		parseScript();
	}

	public String getScriptText() {
		return scriptText;
	}

	private void parseScript() {

		// Create all subscripts definitions
		namedScripts = new HashMap<>();

		//process current script (timed commands on fixture, subscript execution
		commandsList = new ArrayList<>();

		variables = new HashMap<>();

		// start parsing script
		scriptText = scriptText.replaceAll("\r", "");
		String[] lines = scriptText.split(LINE_DELIMITER);

		String currentSubScript = null;
		String currentSubScriptLines = "";

		for (String line : lines) {

			if (line.startsWith(COMMENTS_START)) {
				continue;
			}

			if (line.startsWith(VARIABLES_START)) {

				// this is a variable
				String[] elements = line.substring(2).split(VARIABLES_SEPARATOR);

				String name = elements[0];
				String value = elements[1];

				this.getMasterScriptReference().getVariables().put(name.trim(), value.trim());

			} else if (line.startsWith(SECTION_START)) {

				// this is a script start
				currentSubScript = line.replaceAll("\\$", "");

			} else if (line.startsWith(SECTION_END) || line.startsWith(SECTION_END_V2)) {

				// this is the end of a script
				Script script = new Script(currentSubScriptLines, this.masterScriptReference);
				namedScripts.put(currentSubScript.replaceAll(" ", ""), script);

				currentSubScriptLines = "";
				currentSubScript = null;

			} else if (currentSubScript != null) {

				currentSubScriptLines += LINE_DELIMITER + line;

			} else if (line.length() > 0 && Character.isDigit(line.charAt(0))) {
				// this is a command

				ScriptCommand command = new ScriptCommand(line, this);
				commandsList.add(command);
			}
		}
	}

	public List<TimedDmxEvent> getTimedDmxEvents(TimeInfo timeOffset, TimeSignature signature, int bpm, String[] parameters) {

		ArrayList list = new ArrayList();

		for (ScriptCommand command : commandsList) {
			list.addAll(command.getTimedDmxValues(timeOffset, signature, bpm, parameters));
		}

		// Need to sort the list by time before returning it.
		Collections.sort(list);

		return list;

	}

	/**
	 * @return the namedScripts
	 */
	public Map<String, Script> getNamedScripts() {
		return namedScripts;
	}

	public List<ScriptCommand> getCommands() {
		return commandsList;
	}

	/**
	 * @param namedScripts the namedScripts to set
	 */
	public void setNamedScripts(Map<String, Script> namedScripts) {
		this.namedScripts = namedScripts;
	}

	public Script getMasterScriptReference() {
		return this.masterScriptReference;
	}

	/**
	 * @return the variables
	 */
	public Map<String, String> getVariables() {
		return variables;
	}

}
