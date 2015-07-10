package com.germany;

public class Modul {
	private final String name;
	private final int points;

	public Modul(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

}
