package com.kevinisabelle.dmxLive.objects;

import com.kevinisabelle.dmxLive.objects.fixtures.Color;

/**
 * Parameters class for parsing command parameters
 *
 * @author kisabelle
 */
public class CommandParameters {

	public static final String COLOR = "c=";
	public static final String DIM = "d=";
	public static final String SPEED = "s=";
	public static final String DURATION = "t=";
	public static final String NOTE_UNIT = "u=";
	public static final String ON_TIME = "o=";
	public static final String RIFF_NOTES = "r=";
	public static final String MODE = "m=";
	public static final String ANIMATION = "a=";
	public static final String CURVE = "cu=";
	public static final String ANIMATION_TRIGGER = "at=";
	
	private Color[] colors = null;
	private Integer[] dimmers = null;
	private Integer speed = -1;
	private TimeInfo duration = null;
	private Double noteUnit = null;
	private Double onTime = null;
	private char[] riffNotes = null;
	private String mode = null;
	private Integer curve = -1;
	private Integer animationTrigger = -1;
	private Integer animation = -1;
	
	public CommandParameters(String parameterString) throws IllegalArgumentException {

		String[] params = parameterString.split(",");

		for (String param : params) {

			if (param.startsWith(COLOR)) {
				
				colors = getColors(param.replace(COLOR, ""));
				
			} else if (param.startsWith(DIM)) {
				
				dimmers = getDimmers(param.replace(DIM, ""));
				
			} else if (param.startsWith(SPEED)) {
				
				speed = Integer.parseInt(param.replace(SPEED, "").replaceAll("%", ""));
			
			} else if (param.startsWith(DURATION)) {
				
				duration = new TimeInfo(param.replace(DURATION, "").replaceAll("_", ":"));
			
			} else if (param.startsWith(NOTE_UNIT)) {
				
				noteUnit = Double.parseDouble(param.replace(NOTE_UNIT, ""));
			
			} else if (param.startsWith(ON_TIME)) {
				
				onTime = Double.parseDouble(param.replace(ON_TIME, ""));
			
			} else if (param.startsWith(RIFF_NOTES)) {
				
				riffNotes = param.replace(RIFF_NOTES, "").replaceAll(" ", "").toCharArray();
			
			} else if (param.startsWith(MODE)) {
				
				mode = param.replace(MODE, "");
				
			} else if (param.startsWith(ANIMATION)) {
				
				animation = Integer.parseInt(param.replace(ANIMATION, ""));
				
			} else if (param.startsWith(ANIMATION_TRIGGER)) {
				
				animationTrigger = Integer.parseInt(param.replace(ANIMATION_TRIGGER, ""));
				
			} else if (param.startsWith(CURVE)) {
				
				curve = Integer.parseInt(param.replace(CURVE, ""));
				
			} else {
				
				throw new IllegalArgumentException("Unsupported parameter: " + param);
			}
		}
		
		if (colors == null){
			colors = new Color[]{
				Color.getByName("red")
			};
		}
		
		if (dimmers == null){
			dimmers = new Integer[]{100, 100, 100, 100, 100, 100};
		}
		
		if (speed == null){
			speed = 100;
		}
		
		if (duration == null){
			duration = new TimeInfo(1, 0, 0);
		}
		
		if (noteUnit == null){
			noteUnit = Integer.valueOf(TimeInfo.BEAT_UNIT_QUARTER).doubleValue();
		}
		
		if (onTime == null){
			onTime = 0.5;
		}
		
		if (riffNotes == null){
			riffNotes = "0".toCharArray();
		}
		
		if (mode == null){
			mode = "Mode1";
		}

	}

	/**
	 * Parses dimmer values from the string of parameters.
	 *
	 * @param param
	 * @return
	 */
	protected static Integer[] getDimmers(String param) {

		Integer[] dimmers;

		if (param.isEmpty()) {
			dimmers = new Integer[]{100, 100, 100, 100, 100, 100};
		} else {

			String[] dimmersStr = param.split("_");

			dimmers = new Integer[dimmersStr.length];
			int i = 0;

			for (String dimmerStr : dimmersStr) {
				dimmers[i++] = Integer.parseInt(dimmerStr.replaceAll("%", ""));
			}
		}

		return dimmers;
	}

	/**
	 * Parses the colors from the input string parameters. Can be empty.
	 *
	 * @param param
	 * @return
	 */
	protected static Color[] getColors(String param) {

		if (param == null || param.isEmpty()) {
			Color[] colors = new Color[]{
				Color.getByName("red")
			};

			return colors;
		}

		String[] colorsStrs = param.split("_");
		Color[] colors = new Color[colorsStrs.length];
		int i = 0;

		for (String colorStr : colorsStrs) {
			
			if (Color.getByName(colorStr) != null){
				colors[i++] = Color.getByName(colorStr);
			} else {
				colors[i++] = new Color(Color.ColorEnum.values()[Integer.parseInt(colorStr)]);
			}
		}

		return colors;
	}

	/**
	 * @return the colors
	 */
	public Color[] getColors() {
		return colors;
	}

	/**
	 * @return the dimmers
	 */
	public Integer[] getDimmers() {
		return dimmers;
	}

	/**
	 * @return the speed
	 */
	public Integer getSpeed() {
		return speed;
	}

	/**
	 * @return the duration
	 */
	public TimeInfo getDuration() {
		return duration;
	}

	/**
	 * @return the noteUnit
	 */
	public Double getNoteUnit() {
		return noteUnit;
	}

	/**
	 * @return the onTime
	 */
	public Double getOnTime() {
		return onTime;
	}

	/**
	 * @return the riffNotes
	 */
	public char[] getRiffNotes() {
		return riffNotes;
	}

	public String getMode() {
		return mode;
	}

	/**
	 * @return the curve
	 */
	public Integer getCurve() {
		return curve;
	}

	/**
	 * @return the animationTrigger
	 */
	public Integer getAnimationTrigger() {
		return animationTrigger;
	}

	/**
	 * @return the animation
	 */
	public Integer getAnimation() {
		return animation;
	}
	
	
}
