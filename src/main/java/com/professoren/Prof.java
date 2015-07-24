package com.professoren;

public enum Prof {

	BREUNIG("Breunig", getModulPath("Breunig")), 
	HINZ("Hinz", getModulPath("Hinz")), 
	HENNES("Hennes,", getModulPath("Hennes")), 
	HECK( "Heck", getModulPath("Heck"));

	private final String name, path;

	Prof(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	private static String getModulPath(String prof) {
		return "src/main/resources/" + prof + "_Wahl.txt";
	}
}
