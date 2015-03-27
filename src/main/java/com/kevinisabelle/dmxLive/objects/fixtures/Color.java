package com.kevinisabelle.dmxLive.objects.fixtures;

public class Color {

	public enum ColorEnum {

		BLACK("black", 0, 0, 0),
		RED("red", 255, 0, 0),
		GREEN("green", 0, 255, 0),
		BLUE("blue", 0, 0, 255),
		YELLOW("yellow", 255, 255, 0),
		WHITE("white", 255, 255, 255),
		ORANGE("orange", 255, 128, 0),
		FIRE("fire", 255, 32, 0),
		MAGENTA("magenta", 255, 0, 255),
		AQUA("aqua", 0, 255, 255),
		BLUE_GREEN("bluegreen", 0, 128, 255),
		NAVY("navy", 0, 0, 128),
		PURPLE("purple", 128, 0, 128),
		OLIVE("olive", 128, 128, 0),
		MAROON("maroon", 128, 0, 0);
		
		private String txtName;
		private int r;
		private int g;
		private int b;

		ColorEnum(String name, int r, int g, int b) {
			this.txtName = name;
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public String toString() {
			return this.txtName;
		}
	}
	
	private ColorEnum colorEnum = null;
	private int r;
	private int g;
	private int b;

	public Color(ColorEnum color) {
		colorEnum = color;
	}

	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static Color getByName(String name) {
		for (ColorEnum color : ColorEnum.values()) {
			if (color.txtName.equals(name)) {
				return new Color(color);
			}
		}
		return null;
	}

	/**
	 * @return the r
	 */
	public int getR() {
		return colorEnum == null ? r : colorEnum.r;
	}

	/**
	 * @return the g
	 */
	public int getG() {
		return colorEnum == null ? g : colorEnum.g;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return colorEnum == null ? b : colorEnum.b;
	}
	
	@Override
	public boolean equals(Object c){
		
		if (c instanceof Color){
		
		} else {
			return false;
		}	
			
		Color color = (Color)c;
		
		if (color.getR() == getR() &&
				color.getG() == getG() &&
				color.getB() == getB()){
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 13 * hash + this.r;
		hash = 13 * hash + this.g;
		hash = 13 * hash + this.b;
		return hash;
	}
}
